package com.example.mexpensedemo.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Trips")
public class Trip {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "trip_id")
    private int id;

    @NonNull
    private String trip_name;

    @NonNull
    private String destination;

    @NonNull
    private String date;

    @NonNull
    private Boolean isRisk;

    private String description;

    @NonNull
    @ColumnInfo(defaultValue = "requested")
    private String status;

    public Trip() {
        this.status = "requested";
    }

    public Trip(String trip_name, String destination, String date, Boolean isRisk, String description) {
        this.trip_name = trip_name;
        this.destination = destination;
        this.date = date;
        this.isRisk = isRisk;
        this.description = description;
        this.status = "requested";
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTrip_name(String trip_name) {
        this.trip_name = trip_name;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setRisk(Boolean risk) {
        isRisk = risk;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getTrip_name() {
        return trip_name;
    }

    public String getDestination() {
        return destination;
    }

    public String getDate() {
        return date;
    }

    public Boolean getRisk() {
        return isRisk;
    }

    public String getDescription() {
        return description;
    }

    public void setStatus(@NonNull String status) {
        this.status = status;
    }

    @NonNull
    public String getStatus() {
        return status;
    }
}
