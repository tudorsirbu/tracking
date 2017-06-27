package uk.tudorsirbu.track.controllers;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
    private TextView noJourneysDescription;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_journeys_list, container, false);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());

        noJourneysDescription = (TextView) view.findViewById(R.id.no_journeys_desc);

        journeysList = (RecyclerView) view.findViewById(R.id.journeys_list);
        journeysList.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
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
        if(journeys.size()  <= 0){
            journeysList.setVisibility(View.INVISIBLE);
            noJourneysDescription.setVisibility(View.VISIBLE);
        } else {
            JourneysAdapter adapter = new JourneysAdapter(journeys);
            journeysList.setAdapter(adapter);
            journeysList.setVisibility(View.VISIBLE);
            noJourneysDescription.setVisibility(View.INVISIBLE);
        }
    }
}
