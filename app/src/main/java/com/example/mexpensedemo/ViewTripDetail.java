package com.example.mexpensedemo;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mexpensedemo.adapter.ExpenseViewAdapter;
import com.example.mexpensedemo.model.Expense;
import com.example.mexpensedemo.model.ExpenseViewModel;
import com.example.mexpensedemo.model.Trip;
import com.example.mexpensedemo.model.TripViewModel;

import java.util.List;

public class ViewTripDetail extends Fragment {
    private TextView trip_name;
    private TripViewModel tripViewModel;
    private ExpenseViewModel expenseViewModel;
    private TextView trip_startdate, trip_enddate;
    private TextView trip_destination;
    private TextView trip_description;
    private TextView trip_status, trip_sync;
    private TextView trip_isRisk;
    private ImageView back_icon;
    private Button btn_addexpense;
    private Button btn_edittrip;
    private Button btn_deletetrip;
    private Trip trip;
    private RecyclerView expenseRecycler;
    private ExpenseViewAdapter expenseViewAdapter;
    private LiveData<List<Expense>> listOfExpenses;
    private NavHostFragment navHostFragment;
    private NavController navController;

    public ViewTripDetail(Trip trip) {
        this.trip = trip;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView heading = getActivity().findViewById(R.id.txt_heading);
        ImageView ham_menu = getActivity().findViewById(R.id.ham_menu);
        back_icon = getActivity().findViewById(R.id.btn_back);
        heading.setText("Trip Details");
        ham_menu.setVisibility(View.GONE);
        back_icon.setVisibility(View.VISIBLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Init value
        View frag = inflater.inflate(R.layout.fragment_view_trip_detail, container, false);
        tripViewModel = new ViewModelProvider.AndroidViewModelFactory(getActivity()
                .getApplication())
                .create(TripViewModel.class);
        expenseViewModel = new ViewModelProvider.AndroidViewModelFactory(getActivity()
                .getApplication())
                .create(ExpenseViewModel.class);
        trip_sync = frag.findViewById(R.id.detail_sync);
        trip_name = frag.findViewById(R.id.detail_tripname);
        trip_destination = frag.findViewById(R.id.detail_destination);
        trip_startdate = frag.findViewById(R.id.detail_start_date);
        trip_enddate = frag.findViewById(R.id.detail_end_date);
        trip_status = frag.findViewById(R.id.detail_status);
        trip_isRisk = frag.findViewById(R.id.detail_isRisk);
        trip_description = frag.findViewById(R.id.detail_description);
        btn_addexpense = frag.findViewById(R.id.detail_addexpense);
        expenseRecycler = frag.findViewById(R.id.rv_expenses);
        btn_edittrip = frag.findViewById(R.id.detail_edittrip);
        btn_deletetrip = frag.findViewById(R.id.detail_deletetrip);

        //Check if trip is passed successfully from viewalltrip/homepage
        if (trip.getDeleted().equals(false)) {
            tripViewModel.getTrip(trip.getId()).observe(getActivity(), trip -> {
                String[] addressDetail = trip.getDestination().split("/", 3);
                trip_name.setText(trip.getTrip_name());
                trip_destination.setText(addressDetail[0]);
                trip_startdate.setText(trip.getDateStart());
                trip_enddate.setText(trip.getDateEnd());
                trip_description.setText(trip.getDescription());
                if(trip.getAction().equals("R")) {
                    trip_sync.setText("finished");
                } else {
                    trip_sync.setText("not yet");
                }
                if (trip.getRisk() == true) {
                    trip_isRisk.setText("Yes");
                } else {
                    trip_isRisk.setText("No");
                }
            });
            expenseViewModel.getAllExpensesByTripId(trip.getId()).observe(getActivity(), expenses -> {
                expenseRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
                expenseViewAdapter = new ExpenseViewAdapter(expenses, getActivity());
                expenseRecycler.setAdapter(expenseViewAdapter);
            });
        }
        else {
            ((MainActivity) getActivity()).NavigateToFragment(new ViewAllTripFragment());
        }

        back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).NavigateBack();
            }
        });
        btn_addexpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog( "add_expense");
            }
        });

        btn_edittrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog( "edit_trip");
            }
        });

        btn_deletetrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog("delete_trip");
            }
        });

        return frag;
    }

    private void openDialog( String type) {
        switch (type){
            case "add_expense":
                MutateExpenseFragment newExpense = new MutateExpenseFragment(trip.getId());
                newExpense.show(getParentFragmentManager(), "add new expense");
                break;
            case "edit_trip":
                MutateTripFragment editTrip = new MutateTripFragment(trip, true);
                editTrip.show(getParentFragmentManager(), "edit this trip");
                break;
            case "delete_trip":
                MutateTripFragment deleteTrip = new MutateTripFragment(trip, false);
                deleteTrip.show(getParentFragmentManager(), "delete this trip");
                break;
        }

    }

}