package com.example.mexpensedemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.DatePickerDialog;
import android.location.Address;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mexpensedemo.model.Trip;
import com.example.mexpensedemo.model.TripViewModel;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.util.Arrays;
import java.util.Calendar;

public class NewTrip extends AppCompatActivity implements MapsFragment.passingAddress {

    private EditText enterTripName;
    private EditText enterDestination;
    private TextView enterStartDate, enterEndDate;
    private Switch isRisk;
    private EditText enterDesc;
    private Button btnSave;
    DatePickerDialog.OnDateSetListener startdateSetListener, enddateSetListener;
    LinearLayout start_date, end_date;
    private boolean isEdit;
    private int trip_id;
    private String ggl_map_api_key = "AIzaSyBSXf6kJlYcM3iMPnXrO3IkMGTKzjxigco";
    private PlacesClient placesClient;

    private TripViewModel tripViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_trip);

        enterTripName = findViewById(R.id.enter_tripname);
        enterDestination = findViewById(R.id.enter_destination);
        start_date = findViewById(R.id.start_date_button);
        end_date = findViewById(R.id.end_date_button);
        enterStartDate = findViewById(R.id.txt_start_date);
        enterEndDate = findViewById(R.id.txt_end_date);
        isRisk = findViewById(R.id.risk_assess);
        enterDesc = findViewById(R.id.enter_description);
        btnSave = findViewById(R.id.btn_save);
        tripViewModel = new ViewModelProvider.AndroidViewModelFactory(NewTrip.this
                .getApplication())
                .create(TripViewModel.class);

        //Google MAP API
        enterDestination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MapsFragment().show(getSupportFragmentManager(), "Select location");
            }
        });


        //Set date and calendar
        start_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                DatePickerDialog datePickerDialog = new DatePickerDialog(NewTrip.this, startdateSetListener, year, month, day);
                datePickerDialog.show();
            }
        });

        end_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                DatePickerDialog datePickerDialog = new DatePickerDialog(NewTrip.this, enddateSetListener, year, month, day);
                datePickerDialog.show();
            }
        });
        startdateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = day + "/" + month + "/" + year;
                enterStartDate.setText(date);
            }
        };

        enddateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = day + "/" + month + "/" + year;
                enterEndDate.setText(date);
            }
        };


        btnSave.setOnClickListener(view -> {
            String trip_name = enterTripName.getText().toString();

            Trip trip = new Trip();
            trip.setTrip_name(enterTripName.getText().toString());
            trip.setDestination(enterDestination.getText().toString());
            trip.setDateStart(enterStartDate.getText().toString());
            trip.setDateEnd(enterEndDate.getText().toString());
            trip.setDescription(enterDesc.getText().toString());
            trip.setAction("C");
            if (isRisk.isChecked()) {
                trip.setRisk(true);
            }
            else {
                trip.setRisk(false);
            }
            trip.setStatus("requested");
            TripViewModel.insert(trip);
            finish();
        });


    }





    @Override
    public void onDataPass(Address chosenAddress) {
        enterDestination.setText(chosenAddress.getFeatureName());
    }
}