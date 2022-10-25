package com.example.mexpensedemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.mexpensedemo.model.Trip;
import com.example.mexpensedemo.model.TripViewModel;

public class NewTrip extends AppCompatActivity {

    private EditText enterTripName;
    private EditText enterDestination;
    private EditText enterDate;
    private CheckBox isRisk;
    private EditText enterDesc;
    private Button btnSave;
    private Button btnAddExpense;
    private boolean isEdit;
    private int trip_id;

    private TripViewModel tripViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_trip);

        enterTripName = findViewById(R.id.enter_tripname);
        enterDestination = findViewById(R.id.enter_destination);
        enterDate = findViewById(R.id.enter_date);
        isRisk = findViewById(R.id.check_isRisk);
        enterDesc = findViewById(R.id.enter_desc);
        btnSave = findViewById(R.id.btn_save);
        btnAddExpense = findViewById(R.id.btn_addexpense);

        tripViewModel = new ViewModelProvider.AndroidViewModelFactory(NewTrip.this
                .getApplication())
                .create(TripViewModel.class);


        btnSave.setOnClickListener(view -> {
            String trip_name = enterTripName.getText().toString();

            Trip trip = new Trip();
            trip.setTrip_name(enterTripName.getText().toString());
            trip.setDestination(enterDestination.getText().toString());
            trip.setDate(enterDate.getText().toString());
            trip.setDescription(enterDesc.getText().toString());
            if (isRisk.isChecked()) {
                trip.setRisk(true);
            }
            else {
                trip.setRisk(false);
            }
            trip.setStatus("sent");
            TripViewModel.insert(trip);
            finish();

        });

        btnAddExpense.setOnClickListener(view -> {
            Intent addExpense = new Intent(this, MutateExpenseFragment.class);
            startActivity(addExpense);
        });

        if (getIntent().hasExtra(ViewAllTripFragment.TRIP_ID)) {
            trip_id = getIntent().getIntExtra(ViewAllTripFragment.TRIP_ID, 0);
            tripViewModel.getTrip(trip_id).observe(this, trip -> {
                enterTripName.setText(trip.getTrip_name());
                enterDestination.setText(trip.getDestination());
                enterDate.setText(trip.getDate());
                enterDesc.setText(trip.getDescription());
                if (trip.getRisk() == true){
                    isRisk.setChecked(true);
                }

            });
            isEdit = true;
        }

        //Update button
        Button updateButton = findViewById(R.id.btn_update);
        updateButton.setOnClickListener(view -> {
            int id = trip_id;
            Trip trip = new Trip();
            trip.setId(id);
            trip.setTrip_name(enterTripName.getText().toString());
            trip.setDestination(enterDestination.getText().toString());
            trip.setDate(enterDate.getText().toString());
            trip.setDescription(enterDesc.getText().toString());
            if (isRisk.isChecked()) {
                trip.setRisk(true);
            }
            else {
                trip.setRisk(false);
            }
            TripViewModel.updateTrip(trip);
            finish();
        });

        //Delete button
        Button deleteButton = findViewById(R.id.btn_delete);
        deleteButton.setOnClickListener(view -> {
            int id = trip_id;
            Trip trip = new Trip();
            trip.setId(id);
            trip.setTrip_name(enterTripName.getText().toString());
            trip.setDestination(enterDestination.getText().toString());
            trip.setDate(enterDate.getText().toString());
            trip.setDescription(enterDesc.getText().toString());
            if (isRisk.isChecked()) {
                trip.setRisk(true);
            }
            else {
                trip.setRisk(false);
            }
            TripViewModel.deleteTrip(trip);
            //finish();
        });

        if(isEdit){
            btnSave.setVisibility(View.GONE);
        } else {
            updateButton.setVisibility(View.GONE);
            deleteButton.setVisibility((View.GONE));
        }

    }
}