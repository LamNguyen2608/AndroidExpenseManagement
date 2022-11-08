package com.example.mexpensedemo.util;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;

import com.example.mexpensedemo.CreateAccount;
import com.example.mexpensedemo.LoginActivity;
import com.example.mexpensedemo.MainActivity;
import com.example.mexpensedemo.data.ExpenseRepository;
import com.example.mexpensedemo.data.TripRepository;
import com.example.mexpensedemo.model.Expense;
import com.example.mexpensedemo.model.ExpenseViewModel;
import com.example.mexpensedemo.model.Trip;
import com.example.mexpensedemo.model.TripViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class BackUpData {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    TripRepository tripRepository;
    ExpenseRepository expenseRepository;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    Context context;

    public BackUpData(Context context) {
        this.context = context;
        tripRepository = new TripRepository((Application) context.getApplicationContext());
        expenseRepository = new ExpenseRepository((Application) context.getApplicationContext());
    }

    public void ImportData() {
        db.collection(user.getUid()+"?tbl=trips")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> trips = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot trip : trips) {
                            Trip insertTrip = new Trip();

                            insertTrip.setId(Integer.parseInt(trip.get("id").toString()));
                            insertTrip.setTrip_name(trip.get("trip_name").toString());
                            insertTrip.setDateStart(trip.get("dateEnd").toString());
                            insertTrip.setDateEnd(trip.get("dateStart").toString());
                            insertTrip.setAction("R");
                            insertTrip.setDestination(trip.get("destination").toString());
                            insertTrip.setStatus(trip.get("status").toString());
                            insertTrip.setDeleted((Boolean) trip.get("deleted"));
                            insertTrip.setRisk((Boolean) trip.get("risk"));
                            insertTrip.setDescription(trip.get("description").toString());

                            tripRepository.insert(insertTrip);
                        }
                    }
                });

        db.collection(user.getUid()+"?tbl=expenses")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> expenses = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot expense : expenses) {
                            Expense insertExpense = new Expense();

                            insertExpense.setTrip_id(Integer.parseInt(expense.get("trip_id").toString()));
                            insertExpense.setId(Integer.parseInt(expense.get("id").toString()));
                            insertExpense.setAmount(Float.parseFloat(expense.get("amount").toString()));
                            insertExpense.setAction("C");
                            insertExpense.setComment(expense.get("comment").toString());
                            insertExpense.setDate(expense.get("date").toString());
                            insertExpense.setDelete((Boolean) expense.get("delete"));
                            insertExpense.setExpense_name(expense.get("expense_name").toString());
                            insertExpense.setExpense_type(expense.get("expense_type").toString());
                            insertExpense.setFireBaseImageLink("");
                            insertExpense.setImage_uri(expense.get("image_uri").toString());
                            insertExpense.setTime(expense.get("time").toString());

                            expenseRepository.insert(insertExpense);
                        }
                    }
                });
    }

    public void SyncData() {
        tripRepository.backUp().observe((LifecycleOwner) context, trips -> {
            for (Trip trip : trips) {
                if (trip.getAction().equals("C") || trip.getAction().equals("U")) {
                    trip.setAction("R");
                    db.collection(user.getUid()+"?tbl=trips").document(user.getUid()+"?Id="+trip.getId()).set(trip);
                }
            }
        });

        expenseRepository.backUp().observe((LifecycleOwner) context, expenses -> {
            for (Expense expense: expenses) {
                if (expense.getAction().equals("C") || expense.getAction().equals("U")){
                    expense.setAction("R");
                    db.collection(user.getUid()+"?tbl=expenses").document(user.getUid()+"?Id="+expense.getId()).set(expense);
                }
            }
        });
    }
    public void ResetData() {
        tripRepository.resetLocalDatabase();
    }
}
