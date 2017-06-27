package uk.tudorsirbu.track.controllers;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.CalendarView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import uk.tudorsirbu.track.R;
import uk.tudorsirbu.track.models.Journey;
import uk.tudorsirbu.track.models.Location;

import static uk.tudorsirbu.track.util.Constants.JOURNEY_INTENT_KEY;
import static uk.tudorsirbu.track.util.TimeHelper.millisToString;

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

    private void showPath(){
        PolylineOptions options = new PolylineOptions();
        List<Location> locations = mJourney.getLocations();
        for(Location location : locations){
            Log.d("Tudor", "adding point on map...");
            options.add(location.getLatLng());
        }
        options.color(R.color.black);

        LatLng start = locations.get(0).getLatLng();
        LatLng end = locations.get(locations.size() - 1).getLatLng();
        mMap.addMarker(new MarkerOptions().position(start).title("Start"));
        mMap.addMarker(new MarkerOptions().position(end).title("End"));

        mMap.addPolyline(options);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(start, 17.5f));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        showPath();
    }
}
