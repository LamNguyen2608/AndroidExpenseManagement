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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mexpensedemo.adapter.RecyclerViewAdapter;
import com.example.mexpensedemo.model.Trip;
import com.example.mexpensedemo.model.TripViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class ViewAllTripFragment extends Fragment implements RecyclerViewAdapter.OnTripClickListener {
    public static final String TRIP_ID = "trip_id";
    private AppBarConfiguration appBarConfiguration;
    //private ActivityMainBinding binding;
    private TripViewModel tripViewModel;
    private FloatingActionButton btnAddTrip;
    private List<Trip> listOfTrips;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private SearchView searchTrip;
    private String searchAttr = "name";
    String[] tripAttrs = {"Name","Date", "Destination", "Status"};
    AutoCompleteTextView textOptionsTripAttr;
    ArrayAdapter<String> arrayAdapterTripAttr;

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
        ImageView ham_menu = getActivity().findViewById(R.id.ham_menu);
        ham_menu.setVisibility(View.VISIBLE);
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
        searchTrip = frag.findViewById(R.id.search_trip);
        searchTrip.clearFocus();

        textOptionsTripAttr = frag.findViewById(R.id.dropdown_tripfield);
        arrayAdapterTripAttr = new ArrayAdapter<String>(getActivity(), R.layout.list_item, tripAttrs);
        textOptionsTripAttr.setAdapter(arrayAdapterTripAttr);
        textOptionsTripAttr.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                searchAttr = adapterView.getItemAtPosition(i).toString();
                Log.d("search attr", searchAttr);
            }
        });

        searchTrip.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                filterList(s);
                return true;
            }
        });
        //recyclerView.setHasFixedSize(true);

        tripViewModel.getAllTrips().observe(getActivity(), trips -> {
            Log.d("trip==>", "==>" + trips);
            listOfTrips = trips;
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
        ((MainActivity)getActivity()).NavigateToFragment(new ViewTripDetail());
    }

    private void filterList(String textString) {
        List<Trip> filterTrips = new ArrayList<>();
        switch (searchAttr) {
            case "Destination":
                for (Trip trip : listOfTrips ){
                    if (trip.getDestination().toLowerCase().contains(textString.toLowerCase())){
                        filterTrips.add(trip);
                    }
                }
                break;
            case "Status":
                for (Trip trip : listOfTrips ){
                    if (trip.getStatus().toLowerCase().contains(textString.toLowerCase())){
                        filterTrips.add(trip);
                    }
                }
                break;
            case "Date":
                for (Trip trip : listOfTrips ){
                    if (trip.getDate().toLowerCase().contains(textString.toLowerCase())){
                        filterTrips.add(trip);
                    }
                }
                break;
            default:
                for (Trip trip : listOfTrips ){
                    if (trip.getTrip_name().toLowerCase().contains(textString.toLowerCase())){
                        filterTrips.add(trip);
                    }
                }
                break;
        }

        if (filterTrips.isEmpty()){
            filterTrips.clear();
            recyclerViewAdapter.setFilteredTrips(filterTrips);
            Toast.makeText(getActivity(),"No trip found!!", Toast.LENGTH_SHORT).show();
        } else {
            recyclerViewAdapter.setFilteredTrips(filterTrips);
        }
    }
}