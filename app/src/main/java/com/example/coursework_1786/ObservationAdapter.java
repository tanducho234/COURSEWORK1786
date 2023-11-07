package com.example.coursework_1786;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coursework_1786.models.Observation;

import java.util.List;

public class ObservationAdapter  extends RecyclerView.Adapter<ObservationAdapter.ViewHolder>{
    private List<Observation> observations;
    private ObservationAdapter.OnItemClickListener listener;

    public ObservationAdapter(List<Observation> observations, ObservationAdapter.OnItemClickListener listener) {
        this.observations = observations;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ObservationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_observation, parent, false);
        return new ObservationAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ObservationAdapter.ViewHolder holder, int position) {
        Observation observation = observations.get(position);
        holder.observationName.setText(observation.name);
        holder.observationEditButton.setOnClickListener(v -> listener.onObservationEditButtonClick(observation));
        holder.observationDeleteButton.setOnClickListener(v -> listener.onObservationDeleteButtonClick(observation));
//        holder.observationName.setOnClickListener(v -> listener.onObservationNameClick(observation));

    }

    @Override
    public int getItemCount() {
        return observations.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView observationName;
        Button observationEditButton;
        Button observationDeleteButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            observationName = itemView.findViewById(R.id.observationName);
            observationEditButton = itemView.findViewById(R.id.observationEditButton);
            observationDeleteButton = itemView.findViewById(R.id.observationDeleteButton);
        }
    }

    public interface OnItemClickListener {
        void onObservationEditButtonClick(Observation observation);
        void onObservationDeleteButtonClick(Observation observation);
//        void onObservationNameClick(Observation observation); // Add this method for  click

    }

}