package com.example.mexpensedemo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.mexpensedemo.model.Expense;
import com.example.mexpensedemo.model.ExpenseViewModel;

public class NewExpenseFragment extends AppCompatDialogFragment {

    private ExpenseViewModel expenseViewModel;
    private EditText enterExpenseName;
    private EditText enterType;
    private EditText enterTime;
    private EditText enterAmount;
    private EditText enterComment;
    private Expense expense = null;
    private String mutate = "";
    private int trip_id;

    public NewExpenseFragment(int trip_id) {
        this.trip_id = trip_id;
    }

    public NewExpenseFragment(Expense expense, String mutate){
        this.expense = expense;
        this.mutate = mutate;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_new_expense, null);
        enterExpenseName = view.findViewById(R.id.inp_expname);
        enterTime = view.findViewById(R.id.inp_exptime);
        enterAmount = view.findViewById(R.id.inp_expamt);
        enterComment = view.findViewById(R.id.inp_expcmt);
        enterType = view.findViewById(R.id.inp_exptype);


        expenseViewModel = new ViewModelProvider.AndroidViewModelFactory(getActivity()
                .getApplication())
                .create(ExpenseViewModel.class);

        if (expense != null){
            enterExpenseName.setText(expense.getExpense_name());
            enterTime.setText(expense.getTime());
            enterAmount.setText(String.valueOf(expense.getAmount()));
            enterComment.setText(expense.getComment());
            enterType.setText(expense.getExpense_type());
        }

        switch (mutate) {
            case "edit_expense":
                builder.setView(view)
                        .setTitle("Edit this expense")
                        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .setPositiveButton("update", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Expense expense1 = new Expense();

                                expense1.setId(expense.getId());
                                expense1.setTrip_id(expense.getTrip_id());
                                expense1.setExpense_type(enterType.getText().toString());
                                expense1.setExpense_name(enterExpenseName.getText().toString());
                                expense1.setTime(enterTime.getText().toString());
                                expense1.setAmount(Float.parseFloat(enterAmount.getText().toString()));
                                expense1.setComment(enterComment.getText().toString());

                                ExpenseViewModel.updateExpense(expense1);

                            }
                        });
                break;
            case "delete_expense":
                enterExpenseName.setEnabled(false);
                enterTime.setEnabled(false);
                enterAmount.setEnabled(false);
                enterComment.setEnabled(false);
                enterType.setEnabled(false);
                builder.setView(view)
                        .setTitle("Delete this expense")
                        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .setPositiveButton("delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Expense expense1 = new Expense();

                                expense1.setId(expense.getId());
                                expense1.setTrip_id(expense.getTrip_id());
                                expense1.setExpense_type(enterType.getText().toString());
                                expense1.setExpense_name(enterExpenseName.getText().toString());
                                expense1.setTime(enterTime.getText().toString());
                                expense1.setAmount(Float.parseFloat(enterAmount.getText().toString()));
                                expense1.setComment(enterComment.getText().toString());

                                ExpenseViewModel.deleteExpense(expense1);

                            }
                        });
                break;
            default:
                builder.setView(view)
                        .setTitle("New Expense")
                        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .setPositiveButton("create", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Expense expense1 = new Expense();

                                expense1.setTrip_id(trip_id);
                                expense1.setExpense_type(enterType.getText().toString());
                                expense1.setExpense_name(enterExpenseName.getText().toString());
                                expense1.setTime(enterTime.getText().toString());
                                expense1.setAmount(Float.parseFloat(enterAmount.getText().toString()));
                                expense1.setComment(enterComment.getText().toString());

                                ExpenseViewModel.insert(expense1);

                            }
                        });
        }

        return builder.create();
    }

}