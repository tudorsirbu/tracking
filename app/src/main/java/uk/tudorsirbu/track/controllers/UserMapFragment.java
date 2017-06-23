package uk.tudorsirbu.track.controllers;


import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class UserMapFragment extends Fragment implements OnMapReadyCallback, OnSuccessListener<Location> {

    private LocationManager manager;
    private GoogleMap mMap;
    private Marker userLocation;

    private LatLng lastLocation;
    private Journey journey;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        MapFragment mapFragment = (MapFragment) getActivity().getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        manager = new LocationManager(getActivity());

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
        manager.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
        manager.stop();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        getLocation();
    }

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

        // create journey and set the start
        journey = new Journey();
        journey.addLocation(lastLocation);

        moveUser(lastLocation);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNewLocation(LocationManager.LocationEvent event) {
        moveUser(event.getLocation());
        Log.d("Tudor", "Received one more location!");
    };

    private void moveUser(LatLng location){
        if(userLocation == null){
            userLocation = mMap.addMarker(new MarkerOptions().position(location).title("You are here"));
        } else {
            userLocation.setPosition(location);
        }

        drawPath(location);

        // add a new location to the journey
        journey.addLocation(location);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 16f));
    }

    private void drawPath(LatLng newLocation) {
        PolylineOptions options = new PolylineOptions();
        options.add(lastLocation);
        options.add(newLocation);
        options.color(R.color.black);
        lastLocation = newLocation;
        mMap.addPolyline(options);
    }
}
