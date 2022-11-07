package com.example.mexpensedemo;

import static com.example.mexpensedemo.NewTrip.DateValidator;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
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

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationHolder;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.basgeekball.awesomevalidation.utility.custom.CustomErrorReset;
import com.basgeekball.awesomevalidation.utility.custom.CustomValidation;
import com.basgeekball.awesomevalidation.utility.custom.CustomValidationCallback;
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
    private AwesomeValidation awesomeValidation;


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
        Button update = view.findViewById(R.id.trip_btn_update);
        Button cancel = view.findViewById(R.id.trip_btn_cancel);
        if (isEdit == true){
            builder.setView(view)
                    .setTitle("Edit Trip");
            update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (awesomeValidation.validate()) {
                        String message = "Trip name: " + enterTripName.getText().toString() + "\n"
                                + "Destination: " + enterDestination.getText().toString() + "\n"
                                + "Start Date: " + enterStartDate.getText().toString() + "\n"
                                + "End Date: " + enterEndDate.getText().toString() + "\n"
                                + "Description: " + enterDesc.getText().toString() + "\n"
                                + "Require risk assessment: ";
                        if (isRisk.isChecked()) {
                            message = message + "yes";
                        } else {
                            message = message + "no";
                        }
                        androidx.appcompat.app.AlertDialog.Builder builder1 = new androidx.appcompat.app.AlertDialog.Builder(getContext());
                        builder1.setTitle("Update Trip Confirm");
                        builder1.setMessage(message);
                        builder1.setCancelable(true);

                        builder1.setPositiveButton(
                                "Yes",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        trip.setId(trip.getId());
                                        trip.setTrip_name(enterTripName.getText().toString());
                                        if (formatDestination != null) {
                                            trip.setDestination(formatDestination);
                                        }
                                        trip.setDateStart(enterStartDate.getText().toString());
                                        trip.setDateEnd(enterEndDate.getText().toString());
                                        trip.setAction("U");
                                        trip.setDescription(enterDesc.getText().toString());
                                        if (isRisk.isChecked()) {
                                            trip.setRisk(true);
                                        } else {
                                            trip.setRisk(false);
                                        }
                                        try {
                                            TripViewModel.updateTrip(trip);
                                            Toast.makeText(getActivity(), "Trip updated Successfully!", Toast.LENGTH_SHORT).show();
                                            dismiss();
                                        } catch (Throwable error) {
                                            Toast.makeText(getActivity(), "Error from back-end!", Toast.LENGTH_SHORT).show();
                                        }

                                        dialog.cancel();
                                    }
                                });

                        builder1.setNegativeButton(
                                "No",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                        androidx.appcompat.app.AlertDialog alert = builder1.create();
                        alert.show();
                    }
                }
            });

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });
        } else {
            builder.setView(view)
                    .setTitle("Delete Trip");
            update.setText("Delete");
            update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    androidx.appcompat.app.AlertDialog.Builder builder1 = new androidx.appcompat.app.AlertDialog.Builder(getContext());
                    builder1.setTitle("Delete Trip Confirm");
                    builder1.setMessage("Please confirm your deletion of " + enterTripName.getText().toString() +" trip");
                    builder1.setCancelable(true);

                    builder1.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            try {
                                trip.setAction("D");
                                trip.setDeleted(true);
                                TripViewModel.updateTrip(trip);
                                dialogInterface.cancel();
                                Toast.makeText(getActivity(),"Delete Successfully!", Toast.LENGTH_SHORT).show();
                                dismiss();
                            } catch (Throwable error) {
                                Log.d("delete error", "==>" + error);
                                Toast.makeText(getActivity(),"Error from back-end!" + error, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    builder1.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });

                    androidx.appcompat.app.AlertDialog alert = builder1.create();
                    alert.show();
                }
            });
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
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

        awesomeValidation = new AwesomeValidation(ValidationStyle.UNDERLABEL);
        awesomeValidation.setContext(getContext());
        awesomeValidation.addValidation(
                enterTripName,
                RegexTemplate.NOT_EMPTY,
                getString(R.string.tripname_val));
        awesomeValidation.addValidation(
                enterDestination,
                RegexTemplate.NOT_EMPTY,
                getString(R.string.destination_val)
        );

        awesomeValidation = new AwesomeValidation(ValidationStyle.UNDERLABEL);
        awesomeValidation.setContext(getContext());
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
                        //start_date.setTextColor(Color.RED);

                        //Alert
                        androidx.appcompat.app.AlertDialog.Builder builder1 = new androidx.appcompat.app.AlertDialog.Builder(getContext());
                        builder1.setTitle("Error!!!");
                        builder1.setMessage("Error: Start date is after End date");
                        builder1.setCancelable(true);
                        androidx.appcompat.app.AlertDialog alert = builder1.create();
                        alert.show();
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
        enterDestination.setEnabled(false);

        if(isEdit == false) {
            enterTripName.setEnabled(false);
            enterStartDate.setEnabled(false);
            enterEndDate.setEnabled(false);
            enterDesc.setEnabled(false);
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