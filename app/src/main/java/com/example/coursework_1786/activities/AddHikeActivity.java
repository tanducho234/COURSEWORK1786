package com.example.coursework_1786.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.room.Room;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.coursework_1786.R;
import com.example.coursework_1786.database.AppDatabase;
import com.example.coursework_1786.models.Hike;
import com.example.coursework_1786.models.Observation;

import java.time.LocalDate;
import java.util.List;

public class AddHikeActivity extends AppCompatActivity {
    private AppDatabase appDatabase;

    TextView dobControl;
    String date="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        dobControl = findViewById(R.id.time_picker);
        dobControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });

        appDatabase = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "hiking")
                .allowMainThreadQueries() // For simplicity, don't use this in production
                .build();
        List<Hike> hikes = appDatabase.hikeDao().getAllHikes();
        List<Observation> observations = appDatabase.observationDao().getAllObservation();

        //for level drop down list
        String[] options = {"High", "Medium", "Low"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, options);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner levelSpinner = findViewById(R.id.level_input);
        levelSpinner.setAdapter(adapter);
        levelSpinner.setSelection(0);
        //

        Button btnAdd = findViewById(R.id.btnAdd);
        Button btnHome = findViewById(R.id.btnHome);
        Button btnSearch = findViewById(R.id.btnSearch);

        Typeface boldTypeface = Typeface.defaultFromStyle(Typeface.BOLD);
        btnAdd.setTypeface(boldTypeface);
        btnAdd.setTextColor(Color.parseColor("#7e57c2"));


        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a new intent to navigate to the HomeActivity
//                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);

                // Start the HomeActivity
//                startActivity(intent);
                onBackPressed();
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a new intent to navigate to the SearchActivity
                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                finish();
                // Start the SearchActivity
                startActivity(intent);
            }
        });
        Button saveDetailsButton = findViewById(R.id.saveDetailsButton);
        saveDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveDetails();
            }
        });

    }

    private void saveDetails() {
        EditText nameTxt = findViewById(R.id.name_input);
        EditText locationTxt = findViewById(R.id.location_input);
        Spinner levelSpinner = findViewById(R.id.level_input);
        EditText lengthInputTxt = findViewById(R.id.length_input);
        EditText descriptionTxt= findViewById(R.id.hikeDescription_input);
        RadioGroup radioGroup = findViewById(R.id.radioGroup1);
        int selectedRadioButtonId = radioGroup.getCheckedRadioButtonId();
        RadioButton selectedRadioButton = findViewById(selectedRadioButtonId);

        String name = nameTxt.getText().toString();
        String location = locationTxt.getText().toString();
        String level = levelSpinner.getSelectedItem().toString();
        int length =0;
        if (!TextUtils.isEmpty(lengthInputTxt.getText())) {
            length=Integer.parseInt(lengthInputTxt.getText().toString());
        }
        String description = descriptionTxt.getText().toString();
        Boolean isParkingAvailable= true;
        if (selectedRadioButtonId==R.id.radioButton0){
            isParkingAvailable=false;
        }


        if (name.isEmpty() || location.isEmpty() || length == 0 || date.isEmpty()) {
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
            String confirmationMessage = "New hike will be added:"+ "\n" +"Name: " + name + "\n" +
                    "Location: " + location + "\n" +
                    "Length of the hike: " + length + "\n" +
                    "Date of the hike: " + date + "\n" +
                    "Parking Available: " + isParkingAvailable + "\n" +
                    "Difficulty level: " + level + "\n" +
//                    "Description: " + description + "\n" +
                    "\n" +"Are you sure?";

            // Display a confirmation dialog
            AlertDialog.Builder confirmBuilder = new AlertDialog.Builder(this);
            confirmBuilder.setTitle("Confirmation");
            confirmBuilder.setMessage(confirmationMessage);
            int finalLength = length;
            Boolean finalIsParkingAvailable = isParkingAvailable;
            confirmBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // User confirmed, proceed to save data to the database
                    Hike hike = new Hike();
                    hike.name = name;
                    hike.location = location;
                    hike.length = finalLength;
                    hike.isParkingAvailable = finalIsParkingAvailable;
                    hike.level = level;
                    hike.desc = description;
                    hike.date = date;
//                    long hikeId = appDatabase.hikeDao().insertHike(hike);
                    appDatabase.hikeDao().insertHike(hike);
                    // Show a success message
                    AlertDialog.Builder successBuilder = new AlertDialog.Builder(AddHikeActivity.this);
                    successBuilder.setTitle("Success");
                    successBuilder.setMessage("New hike is added.");
                    successBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            onBackPressed();
                        }
                    });
                    successBuilder.show();
                }
            });
            confirmBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // User canceled, do nothing
                    dialog.dismiss();
//                    onBackPressed();
                }
            });
            confirmBuilder.show();
        }


    }

    public static class DatePickerFragment extends DialogFragment implements
            DatePickerDialog.OnDateSetListener {
        private LocalDate date;
        @NonNull
        @Override
        public Dialog onCreateDialog(@Nullable Bundle savedInstanceState)
        {
            LocalDate d = LocalDate.now();
            int year = d.getYear();
            int month = d.getMonthValue();
            int day = d.getDayOfMonth();
            return new DatePickerDialog(getActivity(), this, year, --month, day);}
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day){
            date = LocalDate.of(year, ++month, day);
            ((AddHikeActivity)getActivity()).updateDay(date);
        }
    }
    public void updateDay(LocalDate Date){
        TextView dobControl = findViewById(R.id.time_picker);
        dobControl.setText(Date.toString());
        date=Date.toString();

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        finish();
        startActivity(intent);

    }
}