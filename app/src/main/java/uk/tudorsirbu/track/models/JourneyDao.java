package uk.tudorsirbu.track.models;

import android.content.Context;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import uk.tudorsirbu.track.util.UserManager;

/**
 * Created by tudorsirbu on 25/06/2017.
 */

public class JourneyDao {

    private FirebaseDatabase mDatabase;
    private DatabaseReference mJourneysRef;
    private String mUserId;

    public JourneyDao(Context context) {
        mDatabase = FirebaseDatabase.getInstance();
//        mDatabase.setPersistenceEnabled(true);  // cache in case no internet is available

        // get the UUID for the session
        UserManager manager = new UserManager(context);
        mUserId = manager.getUserId();

        // set the path to match the uuid
        mJourneysRef = mDatabase.getReference("journeys/" + mUserId );
    }

    /**
     * Adds the journey to the database. Each journey is stored at the following path
     * /journeys/{userId}/{journeyId}
     * @param journey
     */
    public void save(Journey journey){
        mJourneysRef.child(journey.getId()).setValue(journey);
    }

    /**
     * Adds a listener to be notified of all journeys currently in the db
     * for the current user and of any changes.
     * @param listener
     */
    public void queryAll(ValueEventListener listener){
        mJourneysRef.addValueEventListener(listener);
    }

    /**
     * Removes the listener so no more updates are sent.
     * @param listener
     */
    public void removeQuery(ValueEventListener listener){
        mJourneysRef.removeEventListener(listener);
    }
}
