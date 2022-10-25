package com.example.mexpensedemo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
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

public class EditTripFragment extends AppCompatDialogFragment {

    private EditText enterTripName;
    private EditText enterDestination;
    private EditText enterDate;
    private CheckBox isRisk;
    private EditText enterDesc;
    private int trip_id;
    private boolean isEdit;
    private TripViewModel tripViewModel;

    public EditTripFragment(int trip_id, boolean isEdit) {
        this.trip_id = trip_id;
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
                            Trip trip = new Trip();
                            trip.setId(trip_id);
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
                            Trip trip = new Trip();
                            trip.setId(trip_id);
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
                            try {
                                TripViewModel.deleteTrip(trip);
                                Toast.makeText(getActivity(),"Delete Successfully!", Toast.LENGTH_SHORT).show();
                            } catch (Throwable error) {
                                Toast.makeText(getActivity(),"Error from back-end!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }

        enterTripName = view.findViewById(R.id.edit_tripname);
        enterDestination = view.findViewById(R.id.edit_tripdestination);
        enterDate = view.findViewById(R.id.edit_tripdate);
        isRisk = view.findViewById(R.id.edit_tripisRisk);
        enterDesc = view.findViewById(R.id.edit_tripdescription);

        tripViewModel = new ViewModelProvider.AndroidViewModelFactory(getActivity()
                .getApplication())
                .create(TripViewModel.class);

        tripViewModel.getTrip(trip_id).observe(getActivity(), trip -> {
            Log.d("edit trip" + trip_id, "===>:" + trip);
            if (trip == null) {
                dismiss();
            }
            else {
                enterTripName.setText(trip.getTrip_name());
                enterDestination.setText(trip.getDestination());
                enterDate.setText(trip.getDate());
                enterDesc.setText(trip.getDescription());
                if (trip.getRisk() == true) {
                    isRisk.setChecked(true);
                }

                if (isEdit == false) {
                    enterTripName.setEnabled(false);
                    enterDate.setEnabled(false);
                    enterDesc.setEnabled(false);
                    enterDestination.setEnabled(false);
                    isRisk.setEnabled(false);
                }
            }
        });

        return builder.create();
    }

}