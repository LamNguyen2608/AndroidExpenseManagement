package com.example.mexpensedemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.example.mexpensedemo.model.Trip;
import com.example.mexpensedemo.model.TripViewModel;

import java.util.Calendar;

public class NewTrip extends AppCompatActivity {

    private EditText enterTripName;
    private EditText enterDestination;
    private TextView enterDate;
    private Switch isRisk;
    private EditText enterDesc;
    private Button btnSave;
    DatePickerDialog.OnDateSetListener dateSetListener;
    private Button btnAddExpense;
    LinearLayout add_date;
    private boolean isEdit;
    private int trip_id;

    private TripViewModel tripViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_trip);

        enterTripName = findViewById(R.id.enter_tripname);
        enterDestination = findViewById(R.id.enter_destination);
        add_date = findViewById(R.id.date_button);
        enterDate = findViewById(R.id.date_view);
        isRisk = findViewById(R.id.risk_assess);
        enterDesc = findViewById(R.id.enter_description);
        btnSave = findViewById(R.id.btn_save);


        tripViewModel = new ViewModelProvider.AndroidViewModelFactory(NewTrip.this
                .getApplication())
                .create(TripViewModel.class);
        add_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                DatePickerDialog datePickerDialog = new DatePickerDialog(NewTrip.this, dateSetListener, year, month, day);
                datePickerDialog.show();
            }
        });
        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = day + "/" + month + "/" + year;
                enterDate.setText(date);
            }
        };


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


        //Delete button

    }
}