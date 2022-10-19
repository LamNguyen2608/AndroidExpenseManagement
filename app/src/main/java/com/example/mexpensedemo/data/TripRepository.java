package com.example.mexpensedemo.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.mexpensedemo.model.Trip;
import com.example.mexpensedemo.util.TripRoomDatabase;

import java.util.List;

public class TripRepository {
    private TripDAO tripDao;
    private LiveData<List<Trip>> allTrips;
    private LiveData<List<Trip>> recentTrips;

    public TripRepository(Application application) {
        TripRoomDatabase db = TripRoomDatabase.getDatabase(application);
        tripDao = db.tripDao();
        recentTrips = tripDao.getRecentTrips();
        allTrips = tripDao.getAllTrips();
    }
    public LiveData<List<Trip>> getAllData() { return allTrips; }
    public void insert(Trip trip) {
        TripRoomDatabase.databaseWriteExecutor.execute(() -> {
            tripDao.insert(trip);
        });
    }
    public LiveData<Trip> getTrip(int id) {
        return tripDao.getTrip(id);
    }

    public void updateTrip(Trip trip){
        TripRoomDatabase.databaseWriteExecutor.execute(() -> tripDao.update(trip));
    }

    public void deleteTrip(Trip trip){
        TripRoomDatabase.databaseWriteExecutor.execute(() -> tripDao.delete(trip));
    }

    public LiveData<List<Trip>>  getRecentTrips(){ return recentTrips; }

}
