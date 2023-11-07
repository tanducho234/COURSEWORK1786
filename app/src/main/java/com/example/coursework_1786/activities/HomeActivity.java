package com.example.coursework_1786.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.coursework_1786.HikeAdapter;
import com.example.coursework_1786.R;
import com.example.coursework_1786.database.AppDatabase;
import com.example.coursework_1786.models.Hike;
import com.example.coursework_1786.models.Observation;

import java.util.List;

public class HomeActivity extends AppCompatActivity {
    private AppDatabase appDatabase;
    HikeAdapter adapter;
    List<Hike> hikes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        appDatabase = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "hiking")
                .allowMainThreadQueries() // For simplicity, don't use this in production
                .build();
        hikes = appDatabase.hikeDao().getAllHikes();
        List<Observation> observations = appDatabase.observationDao().getAllObservation();

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        HikeAdapter.OnItemClickListener listener = new HikeAdapter.OnItemClickListener() {
            @Override
            public void onMoreButtonClick(Hike hike) {
                // Handle More button click for the hike
                Intent intent = new Intent(getApplicationContext(), ObservationActivity.class);
                intent.putExtra("hikeId", hike.id);
                startActivity(intent);
            }


            @Override
            public void onDeleteButtonClick(Hike hike) {
                appDatabase.hikeDao().deleteHike(hike);
                // Update the list
                hikes.remove(hike);
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onHikeNameClick(Hike hike) {
                Intent intent = new Intent(getApplicationContext(), EditHikeActivity.class);
                intent.putExtra("id", hike.id);
                intent.putExtra("name", hike.name);
                intent.putExtra("location", hike.location);
                intent.putExtra("date", hike.date);
                intent.putExtra("desc", hike.desc);
                intent.putExtra("length", hike.length);
                intent.putExtra("isParkingAvailable", hike.isParkingAvailable);
                intent.putExtra("level", hike.level);
                finish();
                startActivity(intent);
//                // Log a message with different log levels
//                Log.d("MyTag", String.valueOf(hike.id)); // Debug log
//                Log.i("MyTag", hike.date);   // Information log
//                Log.w("MyTag", hike.level); // Warning log
//                Log.e("MyTag", "Error message: Hello");   // Error log

            }
        };

        adapter = new HikeAdapter(hikes, listener,true);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        Button btnAdd = findViewById(R.id.btnAdd);
        Button btnHome = findViewById(R.id.btnHome);
        Button btnSearch = findViewById(R.id.btnSearch);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddHikeActivity.class);
                finish();
                startActivity(intent);

            }
        });
        Typeface boldTypeface = Typeface.defaultFromStyle(Typeface.BOLD);
        btnHome.setTypeface(boldTypeface);
        btnHome.setTextColor(Color.parseColor("#7e57c2"));
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                startActivity(intent);
            }
        });

        Button btnDeleteAllHike = findViewById(R.id.deleteAllHikeButton);
        btnDeleteAllHike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appDatabase.hikeDao().deleteAllHikes();
                // Update the list
//                hikes.remove(hikes);
//                adapter.notifyDataSetChanged();
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                finish();
                startActivity(intent);
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        adapter.notifyDataSetChanged();

//        if (requestCode == YOUR_REQUEST_CODE && resultCode == RESULT_OK) {
//            if (data != null) {
//                String receivedData = data.getStringExtra("key"); // Replace "key" with the same unique identifier used in the current intent
//                // Use the received data as needed
//            }
//        }
    }
}