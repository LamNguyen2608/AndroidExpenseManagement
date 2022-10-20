package com.example.mexpensedemo;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
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
import com.example.mexpensedemo.adapter.RecyclerViewAdapter;
import com.example.mexpensedemo.model.Expense;
import com.example.mexpensedemo.model.ExpenseViewModel;
import com.example.mexpensedemo.model.Trip;
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
    private int trip_id;
    private RecyclerView expenseRecycler;
    private ExpenseViewAdapter expenseViewAdapter;
    private LiveData<List<Expense>> listOfExpenses;
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
        getParentFragmentManager().setFragmentResultListener("datafromviewall", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                trip_id = result.getInt(ViewAllTripFragment.TRIP_ID);
                tripViewModel.getTrip(trip_id).observe(getActivity(), trip -> {
                    trip_name.setText(trip.getTrip_name());
                    trip_destination.setText(trip.getDestination());
                    trip_date.setText(trip.getDate());
                    trip_description.setText(trip.getDescription());
                    if (trip.getRisk() == true){
                        trip_isRisk.setText("Yes");
                    } else {trip_isRisk.setText("No");}

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
                Navigation.findNavController(frag).navigate(R.id.action_viewTripDetail_to_viewAllTripFragment);
            }
        });
        btn_addexpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog(trip_id);
            }
        });
        //expenseViewModel.getAllExpensesByTripId(trip_id)
        return frag;
    }

    private void openDialog(int trip_id) {
        NewExpenseFragment newExpense = new NewExpenseFragment(trip_id);
        newExpense.show(getParentFragmentManager(), "add new expense");
    }

}