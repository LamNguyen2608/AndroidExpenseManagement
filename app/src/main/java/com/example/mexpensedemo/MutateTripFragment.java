package com.example.mexpensedemo;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.location.Address;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.mexpensedemo.model.Expense;
import com.example.mexpensedemo.model.ExpenseViewModel;
import com.example.mexpensedemo.model.Trip;
import com.example.mexpensedemo.model.TripViewModel;
import com.google.android.gms.maps.model.LatLng;

import java.util.Calendar;

public class MutateTripFragment extends AppCompatDialogFragment implements MapsFragment.OnEditListener{

    private EditText enterTripName;
    private EditText enterDestination;
    private TextView enterStartDate, enterEndDate;
    DatePickerDialog.OnDateSetListener startdateSetListener, enddateSetListener;
    LinearLayout start_date, end_date;
    private Switch isRisk;
    private EditText enterDesc;
    private boolean isEdit;
    private TripViewModel tripViewModel;
    private Trip trip;
    private LatLng currentLL;
    private String formatDestination = null;


    public MutateTripFragment(Trip trip, boolean isEdit) {
        this.trip = trip;
        this.isEdit = isEdit;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_edit_trip, null);

        if (isEdit == true){
            builder.setView(view)
                    .setTitle("Edit Trip")
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    })
                    .setPositiveButton("update", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            trip.setId(trip.getId());
                            trip.setTrip_name(enterTripName.getText().toString());
                            if(formatDestination != null) {
                                trip.setDestination(formatDestination);
                            }
                            trip.setDateStart(enterStartDate.getText().toString());
                            trip.setDateEnd(enterEndDate.getText().toString());
                            trip.setAction("U");
                            trip.setDescription(enterDesc.getText().toString());
                            if (isRisk.isChecked()) {
                                trip.setRisk(true);
                            }
                            else {
                                trip.setRisk(false);
                            }
                            try {
                                TripViewModel.updateTrip(trip);
                                Toast.makeText(getActivity(),"Trip updated Successfully!", Toast.LENGTH_SHORT).show();
                            } catch (Throwable error) {
                                Toast.makeText(getActivity(),"Error from back-end!", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
        } else {
            builder.setView(view)
                    .setTitle("Delete Trip")
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    })
                    .setPositiveButton("delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            try {
                                trip.setAction("D");
                                trip.setDeleted(true);
                                TripViewModel.updateTrip(trip);
                                Toast.makeText(getActivity(),"Delete Successfully!", Toast.LENGTH_SHORT).show();
                            } catch (Throwable error) {
                                Log.d("delete error", "==>" + error);
                                Toast.makeText(getActivity(),"Error from back-end!" + error, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }

        enterTripName = view.findViewById(R.id.edit_tripname);
        enterDestination = view.findViewById(R.id.edit_tripdestination);
        start_date = view.findViewById(R.id.start_date_button);
        end_date = view.findViewById(R.id.end_date_button);
        enterStartDate = view.findViewById(R.id.txt_start_date);
        enterEndDate = view.findViewById(R.id.txt_end_date);
        isRisk = view.findViewById(R.id.edit_tripisRisk);
        enterDesc = view.findViewById(R.id.edit_tripdescription);
        ImageView btn_openMap = view.findViewById(R.id.btn_edittrip_map);

        btn_openMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MapsFragment mapsFragment = new MapsFragment(currentLL);
                mapsFragment.show(getChildFragmentManager(), "Get Location From Map");
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

                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), startdateSetListener, year, month, day);
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

                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), enddateSetListener, year, month, day);
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


        tripViewModel = new ViewModelProvider.AndroidViewModelFactory(getActivity()
                .getApplication())
                .create(TripViewModel.class);

        enterTripName.setText(trip.getTrip_name());
        enterStartDate.setText(trip.getDateStart());
        enterEndDate.setText(trip.getDateEnd());
        enterDesc.setText(trip.getDescription());
        if (trip.getRisk() == true) {
            isRisk.setChecked(true);
        }
        String[] addressDetail = trip.getDestination().split("/", 3);
        currentLL = new LatLng(Float.parseFloat(addressDetail[1]), Float.parseFloat(addressDetail[2]));
        Log.d("Long", "==>" + Float.parseFloat(addressDetail[1]));
        Log.d("Lat", "==>" + Float.parseFloat(addressDetail[2]));
        enterDestination.setText(addressDetail[0]);

        if(isEdit == false) {
            enterTripName.setEnabled(false);
            enterStartDate.setEnabled(false);
            enterEndDate.setEnabled(false);
            enterDesc.setEnabled(false);
            enterDestination.setEnabled(false);
            isRisk.setEnabled(false);
        }


        return builder.create();
    }

    //Get address from map fragment
    @Override
    public void sendNewLocation(Address address) {
        enterDestination.setText(address.getFeatureName());
        currentLL = new LatLng(address.getLatitude(), address.getLongitude());
        formatDestination = address.getFeatureName() + '/' + address.getLatitude() + '/' + address.getLongitude();
    }
}