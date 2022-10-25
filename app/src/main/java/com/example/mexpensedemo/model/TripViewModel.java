package com.example.mexpensedemo.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.mexpensedemo.data.TripRepository;

import java.util.List;

public class TripViewModel extends AndroidViewModel {

    public static TripRepository repository;
    public final LiveData<List<Trip>> allTrips;
    public final LiveData<List<Trip>> recentTrips;

    public TripViewModel(@NonNull Application application) {
        super(application);
        repository = new TripRepository(application);
        allTrips = repository.getAllData();
        recentTrips = repository.getRecentTrips();
    }

    public LiveData<List<Trip>> getAllTrips() {return allTrips; }
    public LiveData<List<Trip>> getRecentTrips() {return recentTrips; }
    public static void insert(Trip trip) { repository.insert(trip); }
    public LiveData<Trip> getTrip(int id) { return repository.getTrip(id);}
    public static void updateTrip(Trip trip) {repository.updateTrip(trip);}
    public static void deleteTrip(Trip trip) {repository.deleteTrip(trip);}
    public Float getTripSum(int trip_id) {return repository.getSum(trip_id);}

}
