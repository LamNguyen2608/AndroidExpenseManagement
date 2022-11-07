package com.example.mexpensedemo.model;

import static androidx.room.ForeignKey.CASCADE;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = {@ForeignKey(onDelete = CASCADE,entity = Trip.class,
        parentColumns = "trip_id",
        childColumns = "trip_id")
}, tableName = "Expenses")
public class Expense {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "expense_id")
    private int id;

    @NonNull
    @ColumnInfo(index = true)
    private int trip_id;

    @NonNull
    private String expense_name;

    @NonNull
    private String expense_type;

    @NonNull
    private String time;

    @NonNull
    private String date;

    private String image_uri;

    @NonNull
    private float amount;

    private String comment;

    private Boolean isDelete;

    private String action;

    private String fireBaseImageLink;

    private Boolean isSync;

    public Expense() {
        isDelete = false;
        action = "C";
        isSync = false;
    }


    public void setId(int id) {
        this.id = id;
    }

    public void setTrip_id(int trip_id) {
        this.trip_id = trip_id;
    }

    public void setExpense_type(@NonNull String expense_type) {
        this.expense_type = expense_type;
    }

    public void setTime(@NonNull String time) {
        this.time = time;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public int getId() {
        return id;
    }

    @NonNull
    public int getTrip_id() {
        return trip_id;
    }

    @NonNull
    public String getExpense_type() {
        return expense_type;
    }

    @NonNull
    public String getTime() {
        return time;
    }

    @NonNull
    public float getAmount() {
        return amount;
    }

    @NonNull
    public String getExpense_name() {
        return expense_name;
    }

    public void setExpense_name(@NonNull String expense_name) {
        this.expense_name = expense_name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Boolean getDelete() {
        return isDelete;
    }

    public String getAction() {
        return action;
    }

    public void setDelete(Boolean delete) {
        isDelete = delete;
    }

    public void setAction(String action) {
        this.action = action;
    }

    @NonNull
    public String getDate() {
        return date;
    }

    public String getImage_uri() {
        return image_uri;
    }

    public void setDate(@NonNull String date) {
        this.date = date;
    }

    public void setImage_uri(String image_uri) {
        this.image_uri = image_uri;
    }

    public String getFireBaseImageLink() {
        return fireBaseImageLink;
    }

    public void setFireBaseImageLink(String fireBaseImageLink) {
        this.fireBaseImageLink = fireBaseImageLink;
    }

    public void setSync(Boolean sync) {
        isSync = sync;
    }

    public Boolean getSync() {
        return isSync;
    }
}
