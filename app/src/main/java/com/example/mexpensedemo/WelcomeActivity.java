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
import com.google.firebase.auth.FirebaseUser;

public class WelcomeActivity extends AppCompatActivity {
    private Button btn_getStarted;
    private FirebaseAuth mauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        btn_getStarted = findViewById(R.id.btn_getStarted);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        btn_getStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //SharedPreferences sp = getApplicationContext().getSharedPreferences("AuthState", Context.MODE_PRIVATE);
                if (user != null) {
                    Log.d("Current user", "===>" + FirebaseAuth.getInstance().getCurrentUser().getEmail());
                    startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                }
                else {
                    Log.d("Null Pointer", "Already logged out");
                    startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
                }
            }
        });
    }
}