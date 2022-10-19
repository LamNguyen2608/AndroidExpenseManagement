package com.example.mexpensedemo.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.mexpensedemo.model.Trip;

import java.util.List;

@Dao
public interface TripDAO {
    //CRUD
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Trip trip);

    @Query("DELETE FROM Trips")
    void deleteAll();

    @Query("SELECT * FROM Trips")
    LiveData<List<Trip>> getAllTrips();

    @Query("SELECT * FROM Trips ORDER BY date DESC LIMIT 5")
    LiveData<List<Trip>> getRecentTrips();

    @Query("SELECT * FROM Trips WHERE Trips.trip_id = :id")
    LiveData<Trip> getTrip(int id);

    @Update
    void update(Trip trip);

    @Delete
    void delete(Trip trip);
}
