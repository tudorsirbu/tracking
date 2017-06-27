package uk.tudorsirbu.track.util;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import uk.tudorsirbu.track.controllers.JourneyDetailsActivity;
import uk.tudorsirbu.track.models.Journey;

import static uk.tudorsirbu.track.util.Constants.JOURNEY_INTENT_KEY;

/**
 * Created by tudorsirbu on 25/06/2017.
 */

public class JourneysAdapter extends RecyclerView.Adapter<JourneysAdapter.JourneyViewHolder>{

    List<Journey> mJourneys;

    public JourneysAdapter(List<Journey> mJourneys) {
        Collections.sort(mJourneys);
        this.mJourneys = mJourneys;
    }

    @Override
    public JourneyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(android.R.layout.simple_list_item_1, null);
        return new JourneyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(JourneyViewHolder holder, int position) {
        holder.bind(mJourneys.get(position));
    }

    @Override
    public int getItemCount() {
        return mJourneys.size();
    }

    public class JourneyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mTitle;
        private Journey mJourney;

        public JourneyViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mTitle = (TextView) itemView;
        }

        public void bind(Journey journey){
            mJourney = journey;
            mTitle.setText(TimeHelper.millisToString(journey.getStart()));
        }

        @Override
        public void onClick(View v) {
            Context context = v.getContext();
            Intent intent = new Intent(context, JourneyDetailsActivity.class);
            intent.putExtra(JOURNEY_INTENT_KEY, mJourney);
            context.startActivity(intent);
        }
    }
}
