package uk.tudorsirbu.track.models;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by tudorsirbu on 25/06/2017.
 */

public class JourneyDao {

    private FirebaseDatabase mDatabase;
    private DatabaseReference mJourneysRef;
    private String mUserId;

    public JourneyDao(String userId) {
        mDatabase = FirebaseDatabase.getInstance();
//        mDatabase.setPersistenceEnabled(true);  // cache in case no internet is available

        mUserId = userId;
        mJourneysRef = mDatabase.getReference("journeys/" + mUserId );
    }

    public void save(Journey journey){
        mJourneysRef.child(journey.getId()).setValue(journey);
    }
}
