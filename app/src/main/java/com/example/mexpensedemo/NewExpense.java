package com.example.mexpensedemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.mexpensedemo.model.Expense;
import com.example.mexpensedemo.model.ExpenseViewModel;
import com.example.mexpensedemo.model.TripViewModel;

public class NewExpense extends AppCompatActivity {

    private Button btnExpense;
    private ExpenseViewModel expenseViewModel;
    private EditText enterExpenseName;
    private EditText enterType;
    private EditText enterTime;
    private EditText enterAmount;
    private EditText enterComment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_expense);

        Button btnExpense = findViewById(R.id.btn_expense);
        expenseViewModel = new ViewModelProvider.AndroidViewModelFactory(NewExpense.this
                .getApplication())
                .create(ExpenseViewModel.class);

        enterExpenseName = findViewById(R.id.inp_expname);
        enterTime = findViewById(R.id.inp_exptime);
        enterAmount = findViewById(R.id.inp_expamt);
        enterComment = findViewById(R.id.inp_expcmt);
        enterType = findViewById(R.id.inp_exptype);

        btnExpense.setOnClickListener(view -> {
            Expense expense1 = new Expense();

            expense1.setTrip_id(1);
            expense1.setExpense_type(enterType.getText().toString());
            expense1.setExpense_name(enterExpenseName.getText().toString());
            expense1.setTime(enterTime.getText().toString());
            expense1.setAmount(Float.parseFloat(enterAmount.getText().toString()));

            ExpenseViewModel.insert(expense1);

        });
    }
}