package uk.tudorsirbu.track.controllers;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import uk.tudorsirbu.track.R;
import uk.tudorsirbu.track.models.Journey;

import static uk.tudorsirbu.track.util.Constants.JOURNEY_INTENT_KEY;

public class JourneyDetailsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Journey mJourney;

    private TextView journeyStart;
    private TextView journeyEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journey_details);

        journeyStart = (TextView) findViewById(R.id.journey_start);
        journeyEnd = (TextView) findViewById(R.id.journey_end);

        loadJourney();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    /**
     * Gets the journey passed through the starting intent
     */
    private void loadJourney(){
        Intent intent = getIntent();
        if(intent != null) {
            mJourney = intent.getParcelableExtra(JOURNEY_INTENT_KEY);
            showJourneyDetails();
        } else
            finish();
    }

    private void showJourneyDetails(){
        journeyStart.setText(getString(R.string.journey_start, millisToString(mJourney.getStart())));
        journeyEnd.setText(getString(R.string.journey_end, millisToString(mJourney.getEnd())));
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    private String millisToString(long millis){
        if(millis <= 0)
            return getString(R.string.ongoing);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);

        SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
        return format.format(calendar.getTime());
    }
}
