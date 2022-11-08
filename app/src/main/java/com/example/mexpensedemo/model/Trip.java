package com.example.mexpensedemo.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
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
    private String destination; //format {city name}/{longtitude}/{latitude}

    @NonNull
    private String dateStart; //format dd/mm/yyyy

    @NonNull
    private String dateEnd; //format dd/mm/yyyy

    @NonNull
    private Boolean isRisk;

    @NonNull
    private Boolean isDeleted; //soft delete

    @NonNull
    private String action; //CRUD for synchronizing with firebase

    private String description;

    @NonNull
    private String status; //['requested/in review/refunded/denied]

    //private Boolean isSync;

    public Trip() {
        this.status = "requested";
        this.isDeleted = false;
        this.action = "C";
        //this.isSync = false;
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

    public Boolean getRisk() {
        return isRisk;
    }

    public String getDescription() {
        return description;
    }
    @NonNull
    public String getDateStart() {
        return dateStart;
    }

    @NonNull
    public String getDateEnd() {
        return dateEnd;
    }

    @NonNull
    public Boolean getDeleted() {
        return isDeleted;
    }

    @NonNull
    public String getAction() {
        return action;
    }

    public void setDateStart(@NonNull String dateStart) {
        this.dateStart = dateStart;
    }

    public void setDateEnd(@NonNull String dateEnd) {
        this.dateEnd = dateEnd;
    }

    public void setDeleted(@NonNull Boolean deleted) {
        isDeleted = deleted;
    }

    public void setAction(@NonNull String action) {
        this.action = action;
    }

    public void setStatus(@NonNull String status) {
        this.status = status;
    }

    @NonNull
    public String getStatus() {
        return status;
    }

//    public void setSync(Boolean sync) {
//        isSync = sync;
//    }
//
//    public Boolean getSync() {
//        return isSync;
//    }
}
