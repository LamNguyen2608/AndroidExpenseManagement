package com.example.mexpensedemo;

import android.content.Intent;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CreateAccount extends AppCompatActivity {
    private FirebaseAuth mauth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();


    private EditText inp_email;
    private EditText inp_password;
    private ProgressBar progressBar;
    private Button btn_createaccount;
    private EditText inp_fullname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        mauth = FirebaseAuth.getInstance();
        inp_email = findViewById(R.id.email_create);
        inp_password = findViewById(R.id.password_create);
        inp_fullname = findViewById(R.id.fullname_create);
        btn_createaccount = findViewById(R.id.btn_createaccount);
        progressBar = findViewById(R.id.createact_progress);

        btn_createaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                RegisterNewAccount();
            }
        });
    }

    private void RegisterNewAccount() {
        String email = inp_email.getText().toString().trim();
        String password = inp_password.getText().toString().trim();
        String fullname = inp_fullname.getText().toString();

        Map<String, Object> user = new HashMap<>();
        user.put("fullname", fullname);
        user.put("role", "user");
        if(EmailValidation(email) && PasswordValidation(password) && FullNameValidation(fullname)){
            mauth.createUserWithEmailAndPassword(email, password)
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            Map<String, Object> user = new HashMap<>();
                            user.put("fullname", fullname);
                            user.put("role", "user");
                            user.put("userId", Objects.requireNonNull(authResult.getUser()).getUid());

                            //Add user information
                            db.collection("users")
                                    .add(user)
                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            progressBar.setVisibility(View.GONE);
                                            Toast.makeText(CreateAccount.this, email + " is registered successfully!!", Toast.LENGTH_LONG).show();
                                            startActivity(new Intent(CreateAccount.this, LoginActivity.class));
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            progressBar.setVisibility(View.GONE);
                                            Toast.makeText(CreateAccount.this, "Register unsuccessfully ==> " + e , Toast.LENGTH_LONG).show();
                                        }
                                    });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(CreateAccount.this, "Register unsuccessfully ==> " + e , Toast.LENGTH_LONG).show();
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
            inp_password.setError("Password cannot be empty");
            return false;
        }
        return  true;
    }

    private boolean FullNameValidation(String password) {
        if (password.isEmpty()){
            inp_fullname.setError("Fullname cannot be empty");
            return false;
        }
        return  true;
    }

}