package com.example.coursework_1786.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.coursework_1786.R;
import com.example.coursework_1786.database.AppDatabase;
import com.example.coursework_1786.models.Observation;

import java.util.Calendar;
import java.util.List;

public class AddObservationActivity extends AppCompatActivity {
    private TextView timePickerTextView;
    private AppDatabase appDatabase;

    private int selectedHour;
    private int selectedMinute;
    List<Observation> observations;
    String time="";
    long hikeId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_observation);

        appDatabase = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "hiking")
                .allowMainThreadQueries() // For simplicity, don't use this in production
                .build();
//        List<Hike> hikes = appDatabase.hikeDao().getAllHikes();
        observations = appDatabase.observationDao().getAllObservation();
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        hikeId = extras.getLong("hikeId");

        Button addObservationButton=findViewById(R.id.saveObservationButton);
        addObservationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDetails();
            }
        });
        timePickerTextView = findViewById(R.id.time_picker);
        setCurrentTime();
        timePickerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog();
            }
        });
    }
    private void showTimePickerDialog() {
        // Get the current time
        Calendar calendar = Calendar.getInstance();
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        int currentMinute = calendar.get(Calendar.MINUTE);

        // Create a TimePickerDialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                // Update the selectedHour and selectedMinute variables
                selectedHour = hourOfDay;
                selectedMinute = minute;

                // Update the TextView with the selected time
                time = String.format("%02d:%02d", selectedHour, selectedMinute);
                timePickerTextView.setText(time);
            }
        }, currentHour, currentMinute, true);

        // Show the dialog
        timePickerDialog.show();
    }
    private void saveDetails() {
        EditText nameTxt = findViewById(R.id.observationName_input);
        EditText commentTxt= findViewById(R.id.observationComment_input);

        String name = nameTxt.getText().toString();
        String comment = commentTxt.getText().toString();


        if (name.isEmpty() || time.isEmpty()) {
            // Display a dialog to inform the user to fill in all fields
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Error");
            builder.setMessage("All required fields must be filled");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // Close the dialog if the user clicks OK
                    dialog.dismiss();
                }
            });
            builder.show();
        } else {
            // All fields are filled, prepare the data for the confirmation dialog
            String confirmationMessage = "New observation will be added:"+ "\n" +"Name: " + name + "\n" +
                    "Time: " + time + "\n" +
//                    "Description: " + description + "\n" +
                    "\n" +"Are you sure?";

            // Display a confirmation dialog
            AlertDialog.Builder confirmBuilder = new AlertDialog.Builder(this);
            confirmBuilder.setTitle("Confirmation");
            confirmBuilder.setMessage(confirmationMessage);
            confirmBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // User confirmed, proceed to save data to the database
                    Observation observation = new Observation();
                    observation.name = name;
                    observation.daytime = time;
                    observation.comment = comment;
                    observation.hike_id=hikeId;
                    appDatabase.observationDao().insertObservation(observation);

                    // Show a success message
                    AlertDialog.Builder successBuilder = new AlertDialog.Builder(AddObservationActivity.this);
                    successBuilder.setTitle("Success");
                    successBuilder.setMessage("New observation is added.");
                    successBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            onBackPressed();
//                            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
//                            startActivity(intent);
                        }
                    });
                    successBuilder.show();
                }
            });
            confirmBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // User canceled, do nothing
                    dialog.dismiss();
                }
            });
            confirmBuilder.show();
        }
    }
    private void setCurrentTime() {
        Calendar calendar = Calendar.getInstance();
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        int currentMinute = calendar.get(Calendar.MINUTE);
        String currentTime = String.format("%02d:%02d", currentHour, currentMinute);
        timePickerTextView.setText(currentTime);
        time=currentTime;
    }
    @Override
    public void onBackPressed() {
        // Implement your desired behavior here
        // For example, if using fragments, you can pop the back stack:
        Intent intent = new Intent(getApplicationContext(), ObservationActivity.class);
        intent.putExtra("hikeId", hikeId);
        finish();
        startActivity(intent);
    }
}
