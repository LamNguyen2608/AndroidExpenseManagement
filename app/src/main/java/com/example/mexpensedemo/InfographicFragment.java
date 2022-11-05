package com.example.mexpensedemo;

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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mexpensedemo.adapter.ExpenseViewAdapter;
import com.example.mexpensedemo.adapter.RecyclerViewAdapter;
import com.example.mexpensedemo.model.Expense;
import com.example.mexpensedemo.model.ExpenseViewModel;
import com.example.mexpensedemo.model.Trip;
import com.example.mexpensedemo.model.TripViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.Objects;

public class InfographicFragment extends Fragment {

    private RecyclerView recentExpensesRecycler;
    private ExpenseViewModel expenseViewModel;
    private TextView total_expense, total_refunded, percent_refunded;
    double total, refunded, non_refunded;

    public InfographicFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View frag = inflater.inflate(R.layout.fragment_status_graph, container, false);
        expenseViewModel = new ViewModelProvider.AndroidViewModelFactory(getActivity()
                .getApplication())
                .create(ExpenseViewModel.class);
        total_refunded = frag.findViewById(R.id.numtotal);
        total_expense = frag.findViewById(R.id.exp_total);
        Button percent_refunded = frag.findViewById(R.id.percentage);
        total = 0;

        expenseViewModel.getAllExpenses().observe(getActivity(), expenses -> {
            for(Expense expense : expenses) {
                total = total + expense.getAmount();
            }
            total_expense.setText(String.valueOf(total));
            total_refunded.setText(String.valueOf(total - 75));
            //percent_refunded.setText(String.valueOf(((total - 75)/total)*100));
        });

        return frag;
    }
}

