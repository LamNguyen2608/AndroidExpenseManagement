package com.example.mexpensedemo.util;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.mexpensedemo.data.ExpenseDAO;
import com.example.mexpensedemo.data.TripDAO;
import com.example.mexpensedemo.model.Expense;
import com.example.mexpensedemo.model.Trip;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Trip.class, Expense.class}, version = 7, exportSchema = false)
public abstract class TripRoomDatabase extends RoomDatabase {

    public abstract TripDAO tripDao();
    public abstract ExpenseDAO expenseDao();
    public static final int NUMBER_OF_THEADS = 4;

    private static volatile TripRoomDatabase INSTANCE;
    public static final ExecutorService databaseWriteExecutor
            = Executors.newFixedThreadPool(NUMBER_OF_THEADS);

    public static TripRoomDatabase getDatabase(final Context context){
        if (INSTANCE == null) {
            synchronized (TripRoomDatabase.class){
                if (INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            TripRoomDatabase.class, "expense_database")
                            .fallbackToDestructiveMigration()
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }

        return INSTANCE;
    }

//    private static final RoomDatabase.Callback sRoomDatabaseCallback =
//        new RoomDatabase.Callback() {
//            @Override
//            public void onCreate(@NonNull SupportSQLiteDatabase db) {
//                super.onCreate(db);
//
//                databaseWriteExecutor.execute(() -> {
//                    TripDAO tripDAO = INSTANCE.tripDao();
//                    Trip trip = new Trip();
//                    trip.setTrip_name("Example 1");
//                    trip.setDate("22/12/2021");
//                    trip.setRisk(true);
//                    trip.setDateEnd("25/12/2021");
//                    trip.setDestination("Melbourne");
//
//                    tripDAO.insert(trip);
//                    trip.setTrip_name("Example 2");
//                    tripDAO.insert(trip);
//                    trip.setTrip_name("Example 3");
//                    tripDAO.insert(trip);
//                });
//
//            }
//        };
}
