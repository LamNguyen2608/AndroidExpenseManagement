package com.example.mexpensedemo;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class LoginActivity extends AppCompatActivity {
    private Button loginButton;
    private Button createAccButton;
    private ProgressBar progressBar;

    private FirebaseAuth mauth;
    private EditText inp_pwd, inp_email;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mauth = FirebaseAuth.getInstance();
        inp_email = findViewById(R.id.email_input);
        inp_pwd = findViewById(R.id.password_input);
        createAccButton = findViewById(R.id.btn_createaccount);
        loginButton = findViewById(R.id.btn_login);
        progressBar = findViewById(R.id.login_progress);

        createAccButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, CreateAccount.class));
            }
        });
        
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                login();
            }
        });

        createAccButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, CreateAccount.class));
            }
        });
    }

    private void login() {
        String user = inp_email.getText().toString().trim();
        String pwd = inp_pwd.getText().toString().trim();
        if (EmailValidation(user) == true && PasswordValidation(pwd) == true){
            mauth.signInWithEmailAndPassword(user, pwd)
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            //mauth.updateCurrentUser(authResult.getUser());
                            Toast.makeText(LoginActivity.this, "Login successfully", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                            String userId = authResult.getUser().getUid();
                            db.collection("users")
                                    .whereEqualTo("userId", userId)
                                    .get()
                                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                        @Override
                                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                            DocumentSnapshot userInfo = queryDocumentSnapshots.getDocuments().get(0);
                                            Log.d("current user", "==>" + userInfo.get("role").toString());
                                            SharedPreferences pref = getApplicationContext().getSharedPreferences("AuthState", Activity.MODE_PRIVATE);
                                            SharedPreferences.Editor editor = pref.edit();
                                            editor.putString("currentUserId", userId);
                                            editor.commit();
                                            if (userInfo.get("role").equals("user")) {
                                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                            } else {
                                                Toast.makeText(LoginActivity.this, "You're the admin", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(LoginActivity.this, "Login failed => " + e, Toast.LENGTH_LONG).show();
                        }
                    });
        }
    }

    private boolean EmailValidation(String email) {
        if (email.isEmpty()){
            inp_email.setError("Email cannot be empty");
            return false;
        }
        return  true;
    }

    private boolean PasswordValidation(String password) {
        if (password.isEmpty()){
            inp_pwd.setError("Password cannot be empty");
            return false;
        }
        return  true;
    }

    private void RefreshLocalDatabase(){
        //Reset local database

        //Fill local database with firebase database
    }



}