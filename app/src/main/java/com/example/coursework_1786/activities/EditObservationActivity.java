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

public class EditObservationActivity extends AppCompatActivity {

    private TextView timePickerTextView;
    private AppDatabase appDatabase;

    private int selectedHour;
    private int selectedMinute;
    List<Observation> observations;
    Observation currentObservation;
    String time="";
    long observationId;
    long hikeId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_observation);

        appDatabase = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "hiking")
                .allowMainThreadQueries() // For simplicity, don't use this in production
                .build();
//      get current observation using observation_id from previous intent
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        observationId = extras.getLong("observation_id");
        hikeId = extras.getLong("hikeId");

        observations = appDatabase.observationDao().getAllObservation();
        currentObservation = appDatabase.observationDao().getObservationById(observationId);
//set time


        Button updateObservationButton=findViewById(R.id.updateObservationButton);
        updateObservationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDetails();
            }
        });
        EditText nameTxt = findViewById(R.id.observationName_input);
        EditText commentTxt= findViewById(R.id.observationComment_input);
        nameTxt.setText(currentObservation.name);
        commentTxt.setText(currentObservation.comment);

        timePickerTextView = findViewById(R.id.time_picker);
        timePickerTextView.setText(currentObservation.daytime);
        time=currentObservation.daytime;
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
    private void updateDetails() {
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
            String confirmationMessage = "The observation will be updated:"+ "\n" +"Name: " + name + "\n" +
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
                    currentObservation.name = name;
                    currentObservation.daytime = time;
                    currentObservation.comment = comment;
                    appDatabase.observationDao().updateObservation(currentObservation);

                    // Show a success message
                    AlertDialog.Builder successBuilder = new AlertDialog.Builder(EditObservationActivity.this);
                    successBuilder.setTitle("Success");
                    successBuilder.setMessage("The observation is updated.");
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
