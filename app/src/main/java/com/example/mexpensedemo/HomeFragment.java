package com.example.mexpensedemo;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mexpensedemo.adapter.ExpenseViewAdapter;
import com.example.mexpensedemo.adapter.RecyclerViewAdapter;
import com.example.mexpensedemo.data.TripDAO;
import com.example.mexpensedemo.model.ExpenseViewModel;
import com.example.mexpensedemo.model.Trip;
import com.example.mexpensedemo.model.TripViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.Objects;

public class HomeFragment extends Fragment implements RecyclerViewAdapter.OnTripClickListener{

    private TripViewModel tripViewModel;
    private List<TripDAO.TripWithSumExpenses> listOfTrips;
    private RecyclerView recentTripRecycler;
    private RecyclerView recentExpensesRecycler;
    private ExpenseViewModel expenseViewModel;
    private ExpenseViewAdapter expenseViewAdapter;
    private RecyclerViewAdapter recyclerViewAdapter;
    private ImageButton next,prev;
    private Fragment[] graphs = {new InfographicFragment(), new PieFragment(), new BarChartFragment(), new MapTripsFragment()};
    private int currentFragment = 0;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

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

        next = frag.findViewById(R.id.btn_next_graph);
        prev = frag.findViewById(R.id.btn_prev_graph);
        TextView txt_welcome = frag.findViewById((R.id.txt_welcome));

        db.collection("users")
                .whereEqualTo("userId", user.getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        DocumentSnapshot userInfo = queryDocumentSnapshots.getDocuments().get(0);
                        txt_welcome.setText("Welcome back, " + userInfo.get("fullname") + "!!!");
                    }
                });




        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.graph_container, graphs[currentFragment])
                .addToBackStack(null).commit();
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentFragment == 0) {
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.graph_container, graphs[3])
                            .addToBackStack(null).commit();
                    currentFragment = 3;
                } else {
                    currentFragment = currentFragment - 1;
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.graph_container, graphs[currentFragment])
                            .addToBackStack(null).commit();

                }
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentFragment == 3) {
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.graph_container, graphs[0])
                            .addToBackStack(null).commit();
                    currentFragment = 0;
                } else {
                    currentFragment = currentFragment + 1;
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.graph_container, graphs[currentFragment])
                            .addToBackStack(null).commit();
                }
            }
        });

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
        ViewTripDetail viewTripDetail = new ViewTripDetail(trip);
        ((MainActivity) getActivity()).NavigateToFragment(viewTripDetail);
    }
}