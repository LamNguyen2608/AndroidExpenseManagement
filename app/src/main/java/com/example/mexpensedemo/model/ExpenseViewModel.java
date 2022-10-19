package com.example.mexpensedemo.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.mexpensedemo.data.ExpenseRepository;
import com.example.mexpensedemo.data.TripRepository;

import java.util.List;

public class ExpenseViewModel extends AndroidViewModel {

    public static ExpenseRepository repository;
    public final LiveData<List<Expense>> allExpenses;

    public ExpenseViewModel(@NonNull Application application) {
        super(application);
        repository = new ExpenseRepository(application);
        allExpenses = repository.getAllData();
    }

    public LiveData<List<Expense>> getAllExpenses() {return allExpenses; }
    public static void insert(Expense expense) { repository.insert(expense); }
}
