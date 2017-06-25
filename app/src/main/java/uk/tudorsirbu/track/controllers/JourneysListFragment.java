package uk.tudorsirbu.track.controllers;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import uk.tudorsirbu.track.R;
import uk.tudorsirbu.track.models.Journey;
import uk.tudorsirbu.track.models.JourneyDao;
import uk.tudorsirbu.track.util.JourneysAdapter;

/**
 * Created by tudorsirbu on 25/06/2017.
 */

public class JourneysListFragment extends Fragment implements ValueEventListener {

    private List<Journey> journeys;
    private RecyclerView journeysList;
    private JourneyDao dao;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_journeys_list, container, false);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());

        journeysList = (RecyclerView) view.findViewById(R.id.journeys_list);
        journeysList.setLayoutManager(linearLayoutManager);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        dao = new JourneyDao(getActivity());
        dao.queryAll(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        dao.removeQuery(this);
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        Log.d("Tudor", "received some journeys");
        journeys = new ArrayList<Journey>();
        for(DataSnapshot ds : dataSnapshot.getChildren()){
            Journey journey = ds.getValue(Journey.class);
            journeys.add(journey);
        }
        showJourneys();
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    private void showJourneys(){
        JourneysAdapter adapter = new JourneysAdapter(journeys);
        journeysList.setAdapter(adapter);
    }
}
