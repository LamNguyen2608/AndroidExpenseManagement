package com.example.mexpensedemo.data;

import android.util.Pair;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
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

    @Query("SELECT Trips.*, SUM(Expenses.amount) " +
            "FROM Trips LEFT JOIN Expenses " +
            "ON Trips.trip_id = Expenses.trip_id " +
            "WHERE Trips.isDeleted = 0 AND Expenses.isDelete = 0 " +
            "GROUP BY Trips.trip_id, Expenses.amount")
    LiveData<List<Pair<Trip, Double>>> getAllTripsWithExpenseSum ();
}
