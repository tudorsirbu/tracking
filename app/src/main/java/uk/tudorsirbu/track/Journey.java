package uk.tudorsirbu.track;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by tudorsirbu on 23/06/2017.
 */

public class Journey {

    private String mId;
    private long mStart;
    private long mEnd;

    private ArrayList<LatLng> mLocations;

    public Journey() {
        mId = UUID.randomUUID().toString();
        mStart = System.currentTimeMillis();
        mLocations = new ArrayList<LatLng>();
    }

    public void addLocation(LatLng latLng){
        mLocations.add(latLng);
    }

    public void end(){
        mEnd = System.currentTimeMillis();
    }


}
