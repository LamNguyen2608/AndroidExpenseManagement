package com.example.mexpensedemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mexpensedemo.R;
import com.example.mexpensedemo.model.Expense;
import com.example.mexpensedemo.model.Trip;

import java.util.List;
import java.util.Objects;

public class ExpenseViewAdapter extends RecyclerView.Adapter<ExpenseViewAdapter.ViewHolder> {
    private List<Expense> listOfExpenses;
    private Context context;

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView exp_name;
        public TextView exp_type;
        public TextView exp_time;
        public TextView exp_amount;
        public TextView exp_comment;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            exp_name = itemView.findViewById(R.id.txt_expensename);
            exp_type = itemView.findViewById(R.id.txt_expensetype);
            exp_time = itemView.findViewById(R.id.txt_expensetime);
            exp_amount = itemView.findViewById(R.id.txt_expenseamt);
            exp_comment = itemView.findViewById(R.id.txt_expensecomment);

        }
    }

    public ExpenseViewAdapter(List<Expense> listOfExpenses, Context context) {
        this.listOfExpenses = listOfExpenses;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.expense_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Expense expense = Objects.requireNonNull(listOfExpenses.get(position));
        holder.exp_name.setText(expense.getExpense_name());
        holder.exp_type.setText(expense.getExpense_type());
        holder.exp_time.setText(expense.getTime());
        holder.exp_comment.setText(expense.getComment());
        holder.exp_amount.setText(Float.toString(expense.getAmount()));
    }

    @Override
    public int getItemCount() {
        return Objects.requireNonNull(listOfExpenses.size());
    }


}
