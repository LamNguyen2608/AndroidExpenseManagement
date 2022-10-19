package com.example.mexpensedemo.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.mexpensedemo.model.Expense;
import com.example.mexpensedemo.model.Trip;

import java.util.List;

@Dao
public interface ExpenseDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Expense expense);

    @Query("DELETE FROM Expenses")
    void deleteAll();

    @Query("SELECT * FROM Expenses")
    LiveData<List<Expense>> getAllExpenses();

    @Query("SELECT * FROM Expenses WHERE Expenses.trip_id = :trip_id")
    LiveData<List<Expense>> getAllExpensesByTripId(int trip_id);

    @Update
    void update(Expense expense);

    @Delete
    void delete(Expense expense);
}
