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
import com.example.mexpensedemo.model.TripViewModel;

import java.util.List;

public class ViewTripDetail extends Fragment {
    private TextView trip_name;
    private TripViewModel tripViewModel;
    private ExpenseViewModel expenseViewModel;
    private TextView trip_date;
    private TextView trip_destination;
    private TextView trip_description;
    private TextView trip_status;
    private TextView trip_isRisk;
    private ImageView back_icon;
    private Button btn_addexpense;
    private Button btn_edittrip;
    private Button btn_deletetrip;
    private int trip_id;
    private RecyclerView expenseRecycler;
    private ExpenseViewAdapter expenseViewAdapter;
    private LiveData<List<Expense>> listOfExpenses;
    private NavHostFragment navHostFragment;
    private NavController navController;
    public static ViewTripDetail newInstance() {
        ViewTripDetail fragment = new ViewTripDetail();
        return fragment;
    }

    public ViewTripDetail() {
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
        View frag = inflater.inflate(R.layout.fragment_view_trip_detail, container, false);
        tripViewModel = new ViewModelProvider.AndroidViewModelFactory(getActivity()
                .getApplication())
                .create(TripViewModel.class);
        expenseViewModel = new ViewModelProvider.AndroidViewModelFactory(getActivity()
                .getApplication())
                .create(ExpenseViewModel.class);
        trip_name = frag.findViewById(R.id.detail_tripname);
        trip_destination = frag.findViewById(R.id.detail_destination);
        trip_date = frag.findViewById(R.id.detail_date);
        trip_status = frag.findViewById(R.id.detail_status);
        trip_isRisk = frag.findViewById(R.id.detail_isRisk);
        trip_description = frag.findViewById(R.id.detail_description);
        btn_addexpense = frag.findViewById(R.id.detail_addexpense);
        expenseRecycler = frag.findViewById(R.id.rv_expenses);
        btn_edittrip = frag.findViewById(R.id.detail_edittrip);
        btn_deletetrip = frag.findViewById(R.id.detail_deletetrip);
        getParentFragmentManager().setFragmentResultListener("datafromviewall", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                trip_id = result.getInt(ViewAllTripFragment.TRIP_ID);
                tripViewModel.getTrip(trip_id).observe(getActivity(), trip -> {
                    if (trip != null) {
                        trip_name.setText(trip.getTrip_name());
                        trip_destination.setText(trip.getDestination());
                        trip_date.setText(trip.getDate());
                        trip_description.setText(trip.getDescription());
                        if (trip.getRisk() == true) {
                            trip_isRisk.setText("Yes");
                        } else {
                            trip_isRisk.setText("No");
                        }
                    }
                    else {
                        ((MainActivity) getActivity()).NavigateToFragment(new ViewAllTripFragment());
                    }
                });
                expenseViewModel.getAllExpensesByTripId(trip_id).observe(getActivity(), expenses -> {
                    Log.d("trip id==>" + trip_id, "==>" + expenses);
                    expenseRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
                    expenseViewAdapter = new ExpenseViewAdapter(expenses, getActivity());
                    expenseRecycler.setAdapter(expenseViewAdapter);
                });
            }
        });

        back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("click detail back", "clicked");
                Navigation.findNavController(frag).navigateUp();
            }
        });
        btn_addexpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog(trip_id, "add_expense");
            }
        });

        btn_edittrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog(trip_id, "edit_trip");
            }
        });

        btn_deletetrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog(trip_id, "delete_trip");
            }
        });

        return frag;
    }

    private void openDialog(int trip_id, String type) {
        switch (type){
            case "add_expense":
                MutateExpenseFragment newExpense = new MutateExpenseFragment(trip_id);
                newExpense.show(getParentFragmentManager(), "add new expense");
                break;
            case "edit_trip":
                MutateTripFragment editTrip = new MutateTripFragment(trip_id, true);
                editTrip.show(getParentFragmentManager(), "edit this trip");
                break;
            case "delete_trip":
                MutateTripFragment deleteTrip = new MutateTripFragment(trip_id, false);
                deleteTrip.show(getParentFragmentManager(), "delete this trip");
                break;
        }

    }

}