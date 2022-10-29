package com.example.mexpensedemo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class WelcomeActivity extends AppCompatActivity {
    private Button btn_getStarted;
    private FirebaseAuth mauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        btn_getStarted = findViewById(R.id.btn_getStarted);

        btn_getStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sp = getApplicationContext().getSharedPreferences("AuthState", Context.MODE_PRIVATE);
                String userId = sp.getString("userId", "");
                if(userId != ""){
                    startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                }else{
//User is Not logged in, show login Form
                }
                try {

                } catch (Exception e) {
                    Log.d("Null Pointer", "===>" + e.getMessage());
                    startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
                }
            }
        });
    }
}