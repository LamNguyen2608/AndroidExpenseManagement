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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.lifecycle.ViewModelProvider;

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
    private TripViewModel tripViewModel;

    public EditTripFragment(int trip_id) {
        this.trip_id = trip_id;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_edit_trip, null);

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
                        TripViewModel.updateTrip(trip);

                    }
                });

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
            enterTripName.setText(trip.getTrip_name());
            enterDestination.setText(trip.getDestination());
            enterDate.setText(trip.getDate());
            enterDesc.setText(trip.getDescription());
            if (trip.getRisk() == true){
                isRisk.setChecked(true);
            }

        });

        return builder.create();
    }

}