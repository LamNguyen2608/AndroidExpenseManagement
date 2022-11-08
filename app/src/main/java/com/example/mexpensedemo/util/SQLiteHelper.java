package com.example.mexpensedemo.util;

import android.os.Environment;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class SQLiteHelper {
    StorageReference storageRef = FirebaseStorage.getInstance().getReference();
    public final void exportDB(){
        String DatabaseName = "mexpensedemo.db";
        File sd = Environment.getExternalStorageDirectory();
        File data = Environment.getDataDirectory();
        FileInputStream source=null;
        String currentDBPath = "/data/"+ "com.example.mexpensedemo" +"/databases/"+DatabaseName ;
        File currentDB = new File(data, currentDBPath);


        // Create a reference to "mountains.jpg"
        StorageReference dbBackUp = storageRef.child("database.db");

        // Create a reference to 'images/mountains.jpg'
        StorageReference dbBackUpRef = storageRef.child("backups/database.db");

        try {
            source = new FileInputStream(currentDB);
            dbBackUpRef.putStream(source);
            source.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
