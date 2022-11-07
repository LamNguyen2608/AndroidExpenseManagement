package com.example.mexpensedemo;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;

import com.example.mexpensedemo.model.Expense;
import com.example.mexpensedemo.model.ExpenseViewModel;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PieFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PieFragment extends Fragment {
    PieChart pieChart;
    public PieFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static PieFragment newInstance(String param1, String param2) {
        PieFragment fragment = new PieFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_pie, container, false);

         pieChart = view.findViewById(R.id.piechart);
         setupPieChart();
        ArrayList<PieEntry> entries = new ArrayList<>();

        ExpenseViewModel expenseViewModel;
        expenseViewModel = new ViewModelProvider.AndroidViewModelFactory(getActivity()
                .getApplication())
                .create(ExpenseViewModel.class);
        String[] expenseAttrs = {"Food", "Accommodation", "Transportation", "Equipment", "Outsource", "Other"};
        expenseViewModel.getAllExpenses().observe(getActivity(), expenses -> {
            double food = 0.0;
            double acco = 0.0;
            double trans = 0.0;
            double equip = 0.0;
            double outs = 0.0;
            double other = 0.0;

            for (Expense expense : expenses) {
                switch (expense.getExpense_type()) {
                    case "Food":
                        food = food + expense.getAmount();
                        break;
                    case "Accommodation":
                        acco = acco + expense.getAmount();
                        break;
                    case "Transportation":
                        trans = trans + expense.getAmount();
                        break;
                    case "Equipment":
                        equip = equip + expense.getAmount();
                        break;
                    case "Outsource":
                        outs = outs + expense.getAmount();
                        break;
                    case "Other":
                        other = other + expense.getAmount();
                }
            }

            entries.add(new PieEntry((float) food, "Food"));
            entries.add(new PieEntry((float) acco, "Accommodation"));
            entries.add(new PieEntry((float) trans, "Transportation"));
            entries.add(new PieEntry((float) outs, "Outsource"));
            entries.add(new PieEntry((float) other, "Other"));
            entries.add(new PieEntry((float) equip, "Equipment"));

            PieDataSet pieDataSet = new PieDataSet(entries, "All Expenses");
            pieDataSet.setValueTextColor(Color.BLACK);
            pieDataSet.setValueTextSize(15f);
            int[] colorsPie = {
                    Color.rgb(255,218,219),
                    Color.rgb(198,230,255),
                    Color.rgb(240,204,191),
                    Color.rgb(242,188,105),
                    Color.rgb(139,180,188),
                    Color.rgb(250,171,162),
            };

            pieDataSet.setColors(colorsPie);

            PieData pieData = new PieData(pieDataSet);
            pieData.setValueFormatter(new PercentFormatter(pieChart));


            pieChart.setData(pieData);
            pieChart.setUsePercentValues(true);
            pieChart.getDescription().setEnabled(false);
            pieChart.setCenterText("Total Expenses by Type");
            pieChart.animateY(2000, Easing.EaseInOutQuad);
            pieChart.setDrawSliceText(false);
            pieChart.animate();

        });

        return  view;
    }
    private void setupPieChart(){
        //Legend
        Legend legend = pieChart.getLegend();
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
    }
}