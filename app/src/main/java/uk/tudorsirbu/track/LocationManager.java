package uk.tudorsirbu.track;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.location.Location;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by tudorsirbu on 23/06/2017.
 */

public class LocationManager extends LocationCallback implements PermissionListener {

    private final LocationRequest mLocationRequest;
    private final FusedLocationProviderClient mFusedLocationClient;

    private final Activity mActivity;
    private boolean started = false;

    /**
     *
     * @param inBackground @link{Boolean} true if app is in background false otherwise
     */
    public LocationManager(boolean inBackground, Activity activity) {
        mActivity = activity;

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(activity);
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);

        if(inBackground)
            mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        else
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    /**
     * To be used when app is in foreground
     */
    public LocationManager(Activity activity) {
        this(false, activity);
    }

    public void start(){
        started = true;
        Dexter.withActivity(mActivity)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(this)
                .check();
    }

    public void stop(){
        if(started) {
            mFusedLocationClient.removeLocationUpdates(this);
            started = false;
        }
    }

    @Override
    public void onLocationResult(LocationResult locationResult) {
        super.onLocationResult(locationResult);

        Log.d("Tudor", locationResult.getLastLocation().toString());

        LocationEvent event = new LocationEvent(locationResult.getLastLocation());
        EventBus.getDefault().post(event);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onPermissionGranted(PermissionGrantedResponse response) {
        Log.d("Tudor", "granted");
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, this, null);
    }

    @Override
    public void onPermissionDenied(PermissionDeniedResponse response) {
        Log.d("Tudor", "denied");

    }

    @Override
    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
        Log.d("Tudor", "rationale");

    }

    public class LocationEvent {
        private LatLng coordionates;
        public LocationEvent(Location location) {
            coordionates = new LatLng(location.getLatitude(), location.getLongitude());
        }

        public LatLng getLocation(){
            return coordionates;
        }
    }
}
