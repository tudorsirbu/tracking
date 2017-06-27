package uk.tudorsirbu.track;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import uk.tudorsirbu.track.models.Journey;
import uk.tudorsirbu.track.models.JourneyDao;

import static uk.tudorsirbu.track.util.Constants.TRACKING_SEND_JOURNEY_KEY;

/**
 * Created by tudorsirbu on 23/06/2017.
 */

public class LocationManager extends Service  {

    private FusedLocationProviderClient mFusedLocationClient;
    private LocationCallback mLocationCallback;

    private Journey mJourney;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onCreate() {
        super.onCreate();

        EventBus.getDefault().register(this);

        mJourney = new Journey();
        mJourney.start();

        mLocationCallback = new NewLocationCallback();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(5000); // todo make these constants
        mLocationRequest.setFastestInterval(3000);

        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback,null);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    public void onDestroy() {
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);

        mJourney.end();
        JourneyDao dao = new JourneyDao(this);
        dao.save(mJourney);

        EventBus.getDefault().unregister(this);

        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onJourneyRequest(JourneyRequestEvent event){
        Log.d("Tudor", "received request for journey");
        EventBus.getDefault().post(new LocationEvent(null, mJourney));
    }

    public static boolean isRunning(Context context){
        String serviceClassName = LocationManager.class.getName();
        final ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        final List<ActivityManager.RunningServiceInfo> services = activityManager.getRunningServices(Integer.MAX_VALUE);

        for (ActivityManager.RunningServiceInfo runningServiceInfo : services) {
            if (runningServiceInfo.service.getClassName().equals(serviceClassName)){
                return true;
            }
        }
        return false;
    }

    /**
     * Toggles the location services
     * @param start true = start / false = stop
     */
    public static void toggleLocation(Context context, boolean start){
        Intent intent = new Intent(context, LocationManager.class);
        if(start) {
            context.startService(intent);
        } else
            context.stopService(intent);
    }

    /**
     * Helper class used by the service to send new locations
     * to activities through EventBus
     */
    public class LocationEvent {
        private LatLng mCoordinates;
        private Journey mJourney;
        public LocationEvent(Location location, Journey journey) {
            mJourney = journey;
            if(location != null)
                mCoordinates = new LatLng(location.getLatitude(), location.getLongitude());
        }

        public Journey getJourney() {
            return mJourney;
        }

        public LatLng getLocation(){
            return mCoordinates;
        }
    }


    public static class JourneyRequestEvent{}

    private class NewLocationCallback extends LocationCallback{
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);

            Log.d("Tudor", locationResult.getLastLocation().toString());

            mJourney.addLocation(locationResult.getLastLocation());

            LocationEvent event = new LocationEvent(locationResult.getLastLocation(), mJourney);
            EventBus.getDefault().post(event);
        }
    }
}
