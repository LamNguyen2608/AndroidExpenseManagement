package com.example.mexpensedemo.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.mexpensedemo.model.Expense;
import com.example.mexpensedemo.model.Trip;
import com.example.mexpensedemo.util.TripRoomDatabase;

import java.util.List;

public class ExpenseRepository {
    private ExpenseDAO expenseDao;
    private LiveData<List<Expense>> allExpenses;
    private LiveData<List<Expense>> allExpensesByTripId;

    public ExpenseRepository(Application application) {
        TripRoomDatabase db = TripRoomDatabase.getDatabase(application);
        expenseDao = db.expenseDao();

        allExpenses = expenseDao.getAllExpenses();
    }
    public LiveData<List<Expense>> getAllData() { return allExpenses; }
    public void insert(Expense expense) {
        TripRoomDatabase.databaseWriteExecutor.execute(() -> {
            expenseDao.insert(expense);
        });
    }
    public LiveData<List<Expense>> getAllExpensesByTripId(int trip_id) {
        return expenseDao.getAllExpensesByTripId(trip_id);
    }
    public void updateExpense(Expense expense){
        TripRoomDatabase.databaseWriteExecutor.execute(() -> expenseDao.update(expense));
    }
    public void deleteExpense(Expense expense){
        TripRoomDatabase.databaseWriteExecutor.execute(() -> expenseDao.delete(expense));
    }
    public void softDelete(int expense_id) {expenseDao.softDelete(expense_id);}
//    public List<Expense> backUp() {
//        return expenseDao.getAllExpensesForBackUp();
//    }
    public LiveData<List<Expense>> backUp() {
        return expenseDao.getAllExpensesForBackUp();
    }
}
