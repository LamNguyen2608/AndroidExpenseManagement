package com.example.mexpensedemo;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mexpensedemo.adapter.ExpenseViewAdapter;
import com.example.mexpensedemo.adapter.RecyclerViewAdapter;
import com.example.mexpensedemo.data.TripDAO;
import com.example.mexpensedemo.model.ExpenseViewModel;
import com.example.mexpensedemo.model.Trip;
import com.example.mexpensedemo.model.TripViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.Objects;

public class HomeFragment extends Fragment implements RecyclerViewAdapter.OnTripClickListener{

    public static final String TRIP_ID = "trip_id";
    private AppBarConfiguration appBarConfiguration;
    //private ActivityMainBinding binding;
    private TripViewModel tripViewModel;
    private List<TripDAO.TripWithSumExpenses> listOfTrips;
    private RecyclerView recentTripRecycler;
    private RecyclerView recentExpensesRecycler;
    private ExpenseViewModel expenseViewModel;
    private ExpenseViewAdapter expenseViewAdapter;
    private RecyclerViewAdapter recyclerViewAdapter;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ImageView goback_icon = getActivity().findViewById(R.id.btn_back);
        goback_icon.setVisibility(View.GONE);
        ImageView ham_menu = getActivity().findViewById(R.id.ham_menu);
        ham_menu.setVisibility(View.VISIBLE);
        TextView heading = getActivity().findViewById(R.id.txt_heading);
        heading.setText("Expense Management");
        // Inflate the layout for this fragment
        View frag = inflater.inflate(R.layout.fragment_home, container, false);
        tripViewModel = new ViewModelProvider.AndroidViewModelFactory(getActivity()
                .getApplication())
                .create(TripViewModel.class);
        expenseViewModel = new ViewModelProvider.AndroidViewModelFactory(getActivity()
                .getApplication())
                .create(ExpenseViewModel.class);

        recentExpensesRecycler = frag.findViewById(R.id.rv_recent_expenses);
        recentTripRecycler = frag.findViewById(R.id.rv_recent_trips);
        //recyclerView.setHasFixedSize(true);

        tripViewModel.getTripSum().observe(getActivity(), trips -> {
            Log.d("trip==>", "==>" + trips);
            listOfTrips = trips;
            recentTripRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
            recyclerViewAdapter = new RecyclerViewAdapter(trips, getActivity(), this);
            recentTripRecycler.setAdapter(recyclerViewAdapter);
        });
        expenseViewModel.getAllExpenses().observe(getActivity(), expenses -> {
            Log.d("hompage expenses", "==>" + expenses);
            recentExpensesRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
            expenseViewAdapter = new ExpenseViewAdapter(expenses, getActivity());
            recentExpensesRecycler.setAdapter(expenseViewAdapter);
        });

        return frag;
    }

    @Override
    public void onTripClick(int position, View view) {
        Trip trip = Objects.requireNonNull(listOfTrips.get(position).getTrip());
        Log.d("click position", "pst" + trip.getId());
        Bundle result_viewall = new Bundle();
        result_viewall.putInt(TRIP_ID, trip.getId());
        getParentFragmentManager().setFragmentResult("datafromviewall", result_viewall);
        Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_viewTripDetail);
    }
}