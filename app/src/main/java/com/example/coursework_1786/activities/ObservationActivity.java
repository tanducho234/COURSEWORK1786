package com.example.coursework_1786.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.coursework_1786.ObservationAdapter;
import com.example.coursework_1786.R;
import com.example.coursework_1786.database.AppDatabase;
import com.example.coursework_1786.models.Observation;

import java.util.List;

public class ObservationActivity extends AppCompatActivity {
    private AppDatabase appDatabase;
    ObservationAdapter adapter;
    List<Observation> observations;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_observation);

        appDatabase = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "hiking")
                .allowMainThreadQueries() // For simplicity, don't use this in production
                .build();

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        long hikeId = extras.getLong("hikeId");
        observations = appDatabase.observationDao().getAllObservationsForHikeId(hikeId);

        RecyclerView recyclerView = findViewById(R.id.observationRecyclerView);
        ObservationAdapter.OnItemClickListener listener = new ObservationAdapter.OnItemClickListener() {
            @Override
            public void onObservationEditButtonClick(Observation observation) {
                Intent intent = new Intent(getApplicationContext(), EditObservationActivity.class);
                intent.putExtra("observation_id", observation.observation_id);
                intent.putExtra("hikeId", observation.hike_id);
                finish();
                startActivity(intent);

            }

            @Override
            public void onObservationDeleteButtonClick(Observation observation) {
                appDatabase.observationDao().deleteObservation(observation);
                // Update the list
                observations.remove(observation);
                adapter.notifyDataSetChanged();
            }

        };
        adapter = new ObservationAdapter(observations, listener);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Button addObservationButton = findViewById(R.id.addObservationButton);
        addObservationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddObservationActivity.class);
                intent.putExtra("hikeId", hikeId);
                finish();
                startActivity(intent);
            }
        });
    }

}