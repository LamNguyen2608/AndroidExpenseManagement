package com.example.mexpensedemo;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.mexpensedemo.adapter.RecyclerViewAdapter;
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
    private LiveData<List<Trip>> listOfTrips;
    private RecyclerView recentTripRecycler;
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
        // Inflate the layout for this fragment
        View frag = inflater.inflate(R.layout.fragment_home, container, false);
        tripViewModel = new ViewModelProvider.AndroidViewModelFactory(getActivity()
                .getApplication())
                .create(TripViewModel.class);

        recentTripRecycler = frag.findViewById(R.id.rv_recent_trips);
        //recyclerView.setHasFixedSize(true);

        tripViewModel.getRecentTrips().observe(getActivity(), trips -> {
            Log.d("trip==>", "==>" + trips);
            recentTripRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
            recyclerViewAdapter = new RecyclerViewAdapter(trips, getActivity(), this);
            recentTripRecycler.setAdapter(recyclerViewAdapter);
        });
        return frag;
    }

    @Override
    public void onTripClick(int position, View view) {
        Trip trip = Objects.requireNonNull(tripViewModel.allTrips.getValue().get(position));
        Log.d("click position", "pst" + trip.getId());
        Intent intent = new Intent(getActivity(), NewTrip.class);
        intent.putExtra(TRIP_ID, trip.getId());
        startActivity(intent);
    }
}