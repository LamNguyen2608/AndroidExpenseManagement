package com.example.mexpensedemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.location.Address;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Checkable;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationHolder;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.basgeekball.awesomevalidation.utility.custom.CustomErrorReset;
import com.basgeekball.awesomevalidation.utility.custom.CustomValidation;
import com.basgeekball.awesomevalidation.utility.custom.CustomValidationCallback;
import com.basgeekball.awesomevalidation.utility.custom.SimpleCustomValidation;
import com.example.mexpensedemo.model.Trip;
import com.example.mexpensedemo.model.TripViewModel;
import com.google.android.libraries.places.api.net.PlacesClient;



import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class NewTrip extends AppCompatActivity implements MapsFragment.passingAddress {
    private EditText enterTripName;
    private EditText enterDestination;

    private TextView enterStartDate, enterEndDate;
    private Switch isRisk;
    private EditText enterDesc;
    private Button btnSave;
    DatePickerDialog.OnDateSetListener startdateSetListener, enddateSetListener;
    LinearLayout start_date, end_date;
    private String ggl_map_api_key = "AIzaSyBSXf6kJlYcM3iMPnXrO3IkMGTKzjxigco";
    private PlacesClient placesClient;
    private String addressInput;
    private List<String> lstofErrors;
    private AwesomeValidation awesomeValidation;
    private ImageView btn_openmap;

    private TripViewModel tripViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_trip);


        enterTripName = findViewById(R.id.enter_tripname);
        enterDestination = findViewById(R.id.enter_destination);
        enterDestination.setEnabled(false);
        start_date = findViewById(R.id.start_date_button);
        end_date = findViewById(R.id.end_date_button);

        enterStartDate = findViewById(R.id.txt_start_date);
        enterEndDate = findViewById(R.id.txt_end_date);
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);
        enterStartDate.setText(day + "/" + month + "/" + year);
        enterEndDate.setText(day + "/" + month + "/" + year);
        isRisk = findViewById(R.id.risk_assess);
        enterDesc = findViewById(R.id.enter_description);
        btnSave = findViewById(R.id.btn_save);
        btn_openmap = findViewById(R.id.btn_map);

        //Validation
        awesomeValidation = new AwesomeValidation(ValidationStyle.UNDERLABEL);
        awesomeValidation.setContext(this);
        awesomeValidation.addValidation(
                enterTripName,
                RegexTemplate.NOT_EMPTY,
                getString(R.string.tripname_val));
        awesomeValidation.addValidation(
                enterDestination,
                RegexTemplate.NOT_EMPTY,
                getString(R.string.destination_val)
        );
        awesomeValidation.addValidation(enterStartDate, new CustomValidation() {
            @Override
            public boolean compare(ValidationHolder validationHolder) {
                return DateValidator(enterStartDate.getText().toString(), enterEndDate.getText().toString());
            }
        },  new CustomValidationCallback() {
            @Override
            public void execute(ValidationHolder validationHolder) {
                TextView start_date = (TextView) validationHolder.getView();
                start_date.setError(validationHolder.getErrMsg());
                start_date.setTextColor(Color.RED);
            }
        }, new CustomErrorReset() {
            @Override
            public void reset(ValidationHolder validationHolder) {
                TextView start_date = (TextView) validationHolder.getView();
                start_date.setError(null);
                start_date.setTextColor(Color.BLACK);
            }
        }
        , "Start date must before End Date");



        tripViewModel = new ViewModelProvider.AndroidViewModelFactory(NewTrip.this
                .getApplication())
                .create(TripViewModel.class);


        //Google MAP API
        btn_openmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MapsFragment mapsFragment = new MapsFragment();
                mapsFragment.show(getSupportFragmentManager(), "Select location");
            }
        });


        //Set start and end date and calendar
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
            if (awesomeValidation.validate()) {
                String message = "Trip name: " + enterTripName.getText().toString() + "\n"
                        + "Destination: " + enterDestination.getText().toString() + "\n"
                        + "Start Date: " + enterStartDate.getText().toString() + "\n"
                        + "End Date: " + enterEndDate.getText().toString() + "\n"
                        + "Description: " + enterDesc.getText().toString() + "\n"
                        + "Require risk assessment: ";
                AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                if (isRisk.isChecked()) {
                    message = message + "yes";
                } else {
                    message = message + "no";
                }
                builder1.setTitle("New Trip Confirm");
                builder1.setMessage(message);
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                AlertDialog alert11 = builder1.create();
                                alert11.show();
                                Trip trip = new Trip();
                                trip.setTrip_name(enterTripName.getText().toString());
                                trip.setDestination(addressInput);
                                trip.setDateStart(enterStartDate.getText().toString());
                                trip.setDateEnd(enterEndDate.getText().toString());
                                trip.setDescription(enterDesc.getText().toString());
                                trip.setAction("C");
                                if (isRisk.isChecked()) {
                                    trip.setRisk(true);
                                } else {
                                    trip.setRisk(false);
                                }
                                trip.setStatus("requested");
                                TripViewModel.insert(trip);
                                dialog.cancel();
                                finish();
                            }
                        });

                builder1.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder1.create();
                alert.show();
            }
        });
    }

    //Get address from chosen location from Maps Fragment
    @Override
    public void onDataPass(Address chosenAddress) {
        enterDestination.setText(chosenAddress.getFeatureName());
        addressInput = chosenAddress.getFeatureName() + '/' + chosenAddress.getLatitude() + '/' + chosenAddress.getLongitude();
    }



    public static final boolean DateValidator(String start, String end) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date start_date = null;
        Date end_date = null;
        try {
            start_date = sdf.parse(start);
            end_date = sdf.parse(end);
        } catch (ParseException e) {
            return false;
        }
        if (start_date.getTime() > end_date.getTime()) {
            return false;
        }
        return true;
    }
}