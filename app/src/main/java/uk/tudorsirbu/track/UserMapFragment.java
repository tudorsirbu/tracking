package uk.tudorsirbu.track;


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
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class UserMapFragment extends Fragment implements OnMapReadyCallback, OnSuccessListener<Location> {

    private LocationManager manager;
    private GoogleMap mMap;

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
//        manager.start();
    }

    @Override
    public void onPause() {
        super.onPause();
//        manager.stop();
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
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.addMarker(new MarkerOptions().position(latLng).title("You are here"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16f));
    }
}
