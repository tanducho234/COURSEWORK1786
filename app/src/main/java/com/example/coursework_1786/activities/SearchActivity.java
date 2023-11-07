package com.example.coursework_1786.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.coursework_1786.HikeAdapter;
import com.example.coursework_1786.R;
import com.example.coursework_1786.database.AppDatabase;
import com.example.coursework_1786.models.Hike;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class SearchActivity extends AppCompatActivity {
    private AppDatabase appDatabase;
    HikeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        appDatabase = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "hiking")
                .allowMainThreadQueries() // For simplicity, don't use this in production
                .build();
//        hikes = appDatabase.hikeDao().getAllHikes();

        Button searchHikeButton = findViewById(R.id.searchButton);
        TextView searchbar = findViewById(R.id.searchHikeBar);
        searchHikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displaySearchResult(searchbar.getText().toString());
            }
        });
        Button btnAdd = findViewById(R.id.btnAdd);
        Button btnHome = findViewById(R.id.btnHome);
        Button btnSearch = findViewById(R.id.btnSearch);

        Typeface boldTypeface = Typeface.defaultFromStyle(Typeface.BOLD);
        btnSearch.setTypeface(boldTypeface);
        btnSearch.setTextColor(Color.parseColor("#7e57c2"));


        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a new intent to navigate to the SearchActivity
                Intent intent = new Intent(getApplicationContext(), AddHikeActivity.class);
                finish();
                // Start the SearchActivity
                startActivity(intent);
            }
        });


    }




    public void displaySearchResult(String keyword) {

        List<Hike> hikes = appDatabase.hikeDao().findHikesWithKeyword(keyword);
        RecyclerView recyclerView = findViewById(R.id.searchHikeRecyclerView);
        HikeAdapter.OnItemClickListener listener = new HikeAdapter.OnItemClickListener() {
            @Override
            public void onMoreButtonClick(Hike hike) {
            }

            @Override
            public void onDeleteButtonClick(Hike hike) {
            }

            @Override
            public void onHikeNameClick(Hike hike) {
                showInformationDialog(hike);

            }
        };

        adapter = new HikeAdapter(hikes, listener, false);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


    }
    private void showInformationDialog(Hike hike) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage("Hike's information:\n\nName:"+hike.name+ "\n" +
                                    "Location: " + hike.location + "\n" +
                                    "Length of the hike: " + hike.length + "\n" +
                                    "Date of the hike: " + hike.date + "\n" +
                                    "Parking Available: " +  (hike.isParkingAvailable ? "Yes" : "No")+"\n" +
                                    "Difficulty level: " + hike.level + "\n")
                .setPositiveButton("OK",new DialogInterface.OnClickListener() {
            public void onClick (DialogInterface dialog,int id){
                // Handle the OK button click action here
                dialog.dismiss(); // Close the dialog
            }
        });

        // Create and show the AlertDialog
        AlertDialog dialog = builder.create();
                    dialog.show();
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        finish();
        startActivity(intent);

    }

}