package com.example.mexpensedemo;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mexpensedemo.model.Expense;
import com.example.mexpensedemo.model.ExpenseViewModel;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;

public class BarChartFragment extends Fragment {

    String chosenYear = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
    ArrayList<BarEntry> entries = new ArrayList<>();
    BarChart barChart;
    Double[] monthData = {0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0, 0.0};

    public BarChartFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_bar_chart, container, false);
        barChart = view.findViewById(R.id.barchart);
        //setupPieChart();

        ExpenseViewModel expenseViewModel;
        expenseViewModel = new ViewModelProvider.AndroidViewModelFactory(getActivity()
                .getApplication())
                .create(ExpenseViewModel.class);
        expenseViewModel.getAllExpenses().observe(getActivity(), expenses -> {
            for (Expense expense : expenses) {
                String[] expenseData = expense.getDate().toString().split("/");
                if (expenseData[2].equals(chosenYear)) {
                    Log.d("Month","====>" + Integer.parseInt(expenseData[1]));
                    monthData[Integer.parseInt(expenseData[1])] = monthData[Integer.parseInt(expenseData[1])] + expense.getAmount();
                }
            }
            for (int i = 1; i <= 12; i++) {
                entries.add(new BarEntry(i - 1, Float.parseFloat(String.valueOf(monthData[i])), "Data"));
            }

            BarDataSet barDataSet = new BarDataSet(entries, "All Expenses");
            barDataSet.setValueTextColor(Color.BLACK);
            barDataSet.setValueTextSize(15f);
            int[] colorsPie = {
                    Color.rgb(255,218,219),
                    Color.rgb(198,230,255),
                    Color.rgb(240,204,191),
                    Color.rgb(242,188,105),
                    Color.rgb(139,180,188),
                    Color.rgb(250,171,162),
            };

            barDataSet.setColors(colorsPie);
            String[] xAxisLables = new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

            barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xAxisLables));
//            barChart.getXAxis().setLabelRotationAngle(-45);
//            barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
            BarData barData = new BarData(barDataSet);

            barChart.setFitBars(true);
            barChart.setData(barData);
            barChart.getDescription().setEnabled(false);
            barChart.animateY(2000);




        });

        return  view;
    }
//    private void setupPieChart(){
//        //Legend
//        Legend legend = pieChart.getLegend();
//        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
//        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
//    }
}