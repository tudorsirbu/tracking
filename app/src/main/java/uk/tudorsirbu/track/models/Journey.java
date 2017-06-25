package uk.tudorsirbu.track.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by tudorsirbu on 23/06/2017.
 */

public class Journey implements Parcelable {

    private String mId;
    private long mStart;
    private long mEnd;

    private ArrayList<Location> mLocations;

    public Journey() {
    }

    public String getId() {
        return mId;
    }

    public long getStart() {
        return mStart;
    }

    public long getEnd() {
        return mEnd;
    }

    public void setId(String mId) {
        this.mId = mId;
    }

    public void setStart(long mStart) {
        this.mStart = mStart;
    }

    public void setEnd(long mEnd) {
        this.mEnd = mEnd;
    }

    public void setLocations(ArrayList<Location> mLocations) {
        this.mLocations = mLocations;
    }

    public ArrayList<Location> getLocations() {
        return mLocations;
    }

    public void addLocation(LatLng latLng){
        if(mLocations == null){
            mLocations = new ArrayList<Location>();
        }
        mLocations.add(new Location(latLng.latitude, latLng.longitude));
    }

    public void addLocation(android.location.Location location){
        addLocation(new LatLng(location.getLatitude(), location.getLongitude()));
    }

    public void start(){
        mId = UUID.randomUUID().toString();
        mStart = System.currentTimeMillis();
    }

    public void end(){
        mEnd = System.currentTimeMillis();
    }



    protected Journey(Parcel in) {
        mId = in.readString();
        mStart = in.readLong();
        mEnd = in.readLong();
        if (in.readByte() == 0x01) {
            mLocations = new ArrayList<Location>();
            in.readList(mLocations, LatLng.class.getClassLoader());
        } else {
            mLocations = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mId);
        dest.writeLong(mStart);
        dest.writeLong(mEnd);
        if (mLocations == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(mLocations);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Journey> CREATOR = new Parcelable.Creator<Journey>() {
        @Override
        public Journey createFromParcel(Parcel in) {
            return new Journey(in);
        }

        @Override
        public Journey[] newArray(int size) {
            return new Journey[size];
        }
    };
}
