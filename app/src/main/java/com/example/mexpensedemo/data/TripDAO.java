package com.example.mexpensedemo.data;

import android.util.Log;
import android.util.Pair;

import androidx.lifecycle.LiveData;
import androidx.room.ColumnInfo;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Embedded;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.mexpensedemo.model.Trip;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Dao
public interface TripDAO {
    //CRUD
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Trip trip);

    @Query("DELETE FROM Trips")
    void deleteAll();

    @Query("SELECT * FROM Trips WHERE Trips.isDeleted = 0")
    LiveData<List<Trip>> getAllTrips();

    @Query("SELECT * FROM Trips WHERE Trips.trip_id = :id AND Trips.isDeleted = 0")
    LiveData<Trip> getTrip(int id);

    @Query("UPDATE Trips SET isDeleted = 1 WHERE Trips.trip_id = :trip_id")
    void softDeleteTripById(int trip_id);

    @Update
    void update(Trip trip);

    @Delete
    void delete(Trip trip);

    @Query("SELECT Trips.*, (IFNULL(SUM(Expenses.amount), 0.0)) as SumOfExpenses " +
            "FROM Trips LEFT OUTER JOIN Expenses " +
            "ON Trips.trip_id = Expenses.trip_id AND Expenses.isDelete = 0 " +
            "GROUP BY Trips.trip_id " +
            "HAVING Trips.isDeleted = 0")
    LiveData<List<TripWithSumExpenses>> getAllTripsWithExpenseSum ();
    class TripWithSumExpenses {
        @Embedded
        public Trip trip;
        @ColumnInfo(name = "SumOfExpenses")
        public Float SumOfExpense;

        public Trip getTrip() {
            return trip;
        }

        public Float getSumOfExpense() {
            Log.d("Expense sum", "==>" + SumOfExpense);
            return SumOfExpense;
        }
    }

//    @Query("SELECT * FROM Trips")
//    List<Trip> getAllTripsForBackUp();
    @Query("SELECT * FROM Trips")
    LiveData<List<Trip>> getAllTripsForBackUp();
}
