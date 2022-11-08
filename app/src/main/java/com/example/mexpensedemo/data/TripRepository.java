package com.example.mexpensedemo.data;

import android.app.Application;
import android.util.Pair;

import androidx.lifecycle.LiveData;

import com.example.mexpensedemo.model.Trip;
import com.example.mexpensedemo.util.TripRoomDatabase;

import java.util.List;

import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.Dispatchers;

public class TripRepository {
    private TripDAO tripDao;
    private ExpenseDAO expenseDAO;
    private LiveData<List<TripDAO.TripWithSumExpenses>> allTripSum;
    private LiveData<List<Trip>> allTrips;

    public TripRepository(Application application) {
        TripRoomDatabase db = TripRoomDatabase.getDatabase(application);
        tripDao = db.tripDao();
        expenseDAO = db.expenseDao();
        allTripSum = tripDao.getAllTripsWithExpenseSum();
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
    public void deleteTrip(Trip trip) {
        TripRoomDatabase.databaseWriteExecutor.execute(() -> tripDao.delete(trip));
    }
    public LiveData<List<TripDAO.TripWithSumExpenses>> getTripAndSum(){
        return tripDao.getAllTripsWithExpenseSum();
    }
    public void resetLocalDatabase() {
        tripDao.deleteAll();
    }
    public void softDelete(int trip_id) {
        expenseDAO.cascadeSoftDelete(trip_id);
        tripDao.softDeleteTripById(trip_id);
    }
//    public List<Trip> backUp() {
//        return tripDao.getAllTripsForBackUp();
//    }
    public LiveData<List<Trip>> backUp() {
    return tripDao.getAllTripsForBackUp();
}
}
