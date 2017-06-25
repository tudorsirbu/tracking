package uk.tudorsirbu.track.controllers;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import uk.tudorsirbu.track.LocationManager;
import uk.tudorsirbu.track.R;
import uk.tudorsirbu.track.models.Journey;
import uk.tudorsirbu.track.models.JourneyDao;



/**
 * A simple {@link Fragment} subclass.
 */
public class UserMapFragment extends Fragment implements OnMapReadyCallback, OnSuccessListener<Location>, View.OnClickListener {

    private GoogleMap mMap;
    private Marker userLocation;

    private LatLng lastLocation;
    private Journey journey;

    private JourneyDao mJourneyDao;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        MapFragment mapFragment = (MapFragment) getActivity().getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(this);

        mJourneyDao = new JourneyDao(getContext());

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        getLocation();
        requestBackgroundJourney();
    }

    /**
     * Requests the current tracked journey from the Location service
     */
    private void requestBackgroundJourney(){
        if(LocationManager.isRunning(getActivity()))
            EventBus.getDefault().post(new LocationManager.JourneyRequestEvent());
    }

    /**
     * Gets the user's current location as a one off
     */
    private void getLocation() {
        FusedLocationProviderClient client =
                LocationServices.getFusedLocationProviderClient(getActivity());

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            client.getLastLocation().addOnSuccessListener(this);
        }
    }

    @Override
    public void onSuccess(Location location) {
        lastLocation = new LatLng(location.getLatitude(), location.getLongitude());
        moveUser(lastLocation, true);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNewLocation(LocationManager.LocationEvent event) {
        if(journey == null) {
            // restore journey
            journey = event.getJourney();
            for(uk.tudorsirbu.track.models.Location location : journey.getLocations()){
                LatLng latLng = location.getLatLng();
                drawPath(latLng);
                moveUser(latLng, false);
            }
        } else {
            moveUser(event.getLocation(), false);
            drawPath(event.getLocation());
        }
    };

    private void moveUser(LatLng location, boolean zoom){
        if(userLocation == null){
            userLocation = mMap.addMarker(new MarkerOptions().position(location).title("You are here"));
        } else {
            userLocation.setPosition(location);
        }

        if(zoom)
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location,17.5f));
        else
            mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
    }

    private void drawPath(LatLng newLocation) {
        PolylineOptions options = new PolylineOptions();
        options.add(lastLocation);
        options.add(newLocation);
        options.color(R.color.black);
        lastLocation = newLocation;
        mMap.addPolyline(options);
    }

    @Override
    public void onClick(View v) {
        Drawable drawable;
        String action = (String) v.getTag();
        switch(action){
            case "start":
                drawable = ContextCompat.getDrawable(getActivity(), R.drawable.ic_stop_black_24dp);
                ((FloatingActionButton) v).setImageDrawable(drawable);

                Snackbar.make(v, getString(R.string.started_tracking), Snackbar.LENGTH_LONG).show();
                journey = new Journey();
                journey.start();
                toggleLocation(true);
                v.setTag("stop");
                break;
            case "stop":
                drawable = ContextCompat.getDrawable(getActivity(), R.drawable.ic_add_location_black_24dp);
                ((FloatingActionButton) v).setImageDrawable(drawable);

                Snackbar.make(v, getString(R.string.stopped_tracking), Snackbar.LENGTH_LONG).show();
                toggleLocation(false);
                v.setTag("start");

                journey.end();
                mJourneyDao.save(journey);
                break;
        }
    }

    /**
     * Toggles the location services
     * @param start true = start / false = stop
     */
    private void toggleLocation(boolean start){
        Intent intent = new Intent(getActivity(), LocationManager.class);
        if(start) {
            getActivity().startService(intent);
        } else
            getActivity().stopService(intent);
    }
}
