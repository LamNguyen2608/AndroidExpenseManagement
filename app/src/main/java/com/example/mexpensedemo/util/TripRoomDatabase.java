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

@Database(entities = {Trip.class, Expense.class}, version = 6, exportSchema = false)
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
                            TripRoomDatabase.class, "trip_database")
                            .addCallback(sRoomDatabaseCallback)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }

        return INSTANCE;
    }

    private static final RoomDatabase.Callback sRoomDatabaseCallback =
        new RoomDatabase.Callback() {
            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                super.onCreate(db);

                databaseWriteExecutor.execute(() -> {
                    TripDAO tripDAO = INSTANCE.tripDao();
                    tripDAO.deleteAll();

                    Trip trip1 = new Trip("Annual Conference", "Tokushima", "22/03/2021", true, "Lamie kawaii");
                    Trip trip2 = new Trip("On-site for Japan", "Tokyo", "22/05/2022", false, "Sousuke-kun");
                    Trip trip3 = new Trip("Cultural Exchange", "Melbourne", "12/08/2022", false, "Peter");

                    tripDAO.insert(trip2);
                    tripDAO.insert(trip1);
                    tripDAO.insert(trip3);
                });

            }
        };
}
