package com.example.mexpensedemo.model;

import android.app.Application;
import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.mexpensedemo.data.TripDAO;
import com.example.mexpensedemo.data.TripRepository;

import java.util.List;

public class TripViewModel extends AndroidViewModel {

    public static TripRepository repository;
    public final LiveData<List<Trip>> allTrips;

    public TripViewModel(@NonNull Application application) {
        super(application);
        repository = new TripRepository(application);
        allTrips = repository.getAllData();
    }

    public LiveData<List<Trip>> getAllTrips() {return allTrips; }
    public static void insert(Trip trip) { repository.insert(trip); }
    public LiveData<Trip> getTrip(int id) { return repository.getTrip(id);}
    public static void updateTrip(Trip trip) {repository.updateTrip(trip);}
    public static void deleteTrip(Trip trip) {repository.deleteTrip(trip);}
    public LiveData<List<TripDAO.TripWithSumExpenses>> getTripSum() {return repository.getTripAndSum();}
    public void deleteAll() {repository.resetLocalDatabase();}
    public void softDelete(int trip_id) {repository.softDelete(trip_id);}
}
