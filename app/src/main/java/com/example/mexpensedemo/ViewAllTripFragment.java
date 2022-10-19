package com.example.mexpensedemo;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
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

import com.example.mexpensedemo.adapter.RecyclerViewAdapter;
import com.example.mexpensedemo.model.Trip;
import com.example.mexpensedemo.model.TripViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.Objects;

public class ViewAllTripFragment extends Fragment implements RecyclerViewAdapter.OnTripClickListener {
    public static final String TRIP_ID = "trip_id";
    private AppBarConfiguration appBarConfiguration;
    //private ActivityMainBinding binding;
    private TripViewModel tripViewModel;
    private FloatingActionButton btnAddTrip;
    private LiveData<List<Trip>> listOfTrips;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;


    public ViewAllTripFragment() {
        // Required empty public constructor
    }

    public static ViewAllTripFragment newInstance() {
        ViewAllTripFragment fragment = new ViewAllTripFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView heading = getActivity().findViewById(R.id.txt_heading);
        heading.setText("All Trips");
        ImageView goback_icon = getActivity().findViewById(R.id.btn_back);
        goback_icon.setVisibility(View.GONE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View frag = inflater.inflate(R.layout.fragment_view_all_trip, container, false);
        tripViewModel = new ViewModelProvider.AndroidViewModelFactory(getActivity()
                .getApplication())
                .create(TripViewModel.class);

        FloatingActionButton btnAddTrip = frag.findViewById(R.id.btn_addtrip);

        btnAddTrip.setOnClickListener(view -> {
            Intent addTrip = new Intent(getActivity(), NewTrip.class);
            startActivity(addTrip);
        });

        recyclerView = frag.findViewById(R.id.recyclerView);
        //recyclerView.setHasFixedSize(true);

        tripViewModel.getAllTrips().observe(getActivity(), trips -> {
            Log.d("trip==>", "==>" + trips);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerViewAdapter = new RecyclerViewAdapter(trips, getActivity(), this);
            recyclerView.setAdapter(recyclerViewAdapter);
        });
        return frag;

    }

    @Override
    public void onTripClick(int position, View view) {
        Trip trip = Objects.requireNonNull(tripViewModel.allTrips.getValue().get(position));
        Log.d("click position", "pst" + trip.getId());
        Bundle result_viewall = new Bundle();
        result_viewall.putInt(TRIP_ID, trip.getId());
        getParentFragmentManager().setFragmentResult("datafromviewall", result_viewall);
        Navigation.findNavController(view).navigate(R.id.action_viewAllTripFragment_to_viewTripDetail);
    }
}