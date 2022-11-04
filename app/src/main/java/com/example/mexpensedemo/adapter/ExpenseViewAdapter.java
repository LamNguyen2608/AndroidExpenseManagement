package com.example.mexpensedemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mexpensedemo.MutateExpenseFragment;
import com.example.mexpensedemo.R;
import com.example.mexpensedemo.model.Expense;

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
        public TextView exp_date;
        public TextView exp_comment;
        public Button btn_edit;
        public Button btn_delete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            exp_name = itemView.findViewById(R.id.txt_expensename);
            exp_type = itemView.findViewById(R.id.txt_expensetype);
            exp_date = itemView.findViewById(R.id.txt_expensedate);
            exp_time = itemView.findViewById(R.id.txt_expensetime);
            exp_amount = itemView.findViewById(R.id.txt_expenseamt);
            exp_comment = itemView.findViewById(R.id.txt_expensecomment);
            btn_delete = itemView.findViewById(R.id.expense_delete);
            btn_edit = itemView.findViewById(R.id.expense_edit);

            btn_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Expense expense = listOfExpenses.get(getAdapterPosition());
                    FragmentActivity fm = (FragmentActivity) view.getContext();
                    MutateExpenseFragment editExpense = new MutateExpenseFragment(expense, "edit_expense");
                    editExpense.show(fm.getSupportFragmentManager(), "edit expense" );
                }
            });

            btn_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Expense expense = listOfExpenses.get(getAdapterPosition());
                    FragmentActivity fm = (FragmentActivity) view.getContext();
                    MutateExpenseFragment deleteExpense = new MutateExpenseFragment(expense, "delete_expense");
                    deleteExpense.show(fm.getSupportFragmentManager(), "delete expense" );
                }
            });
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
        Expense expense = Objects.requireNonNull(listOfExpenses.get(holder.getAdapterPosition()));
        holder.exp_name.setText(expense.getExpense_name());
        holder.exp_type.setText(expense.getExpense_type());
        holder.exp_time.setText(expense.getTime());
        holder.exp_comment.setText(expense.getComment());
        holder.exp_date.setText(expense.getDate());
        holder.exp_amount.setText(String.format("%.2f", expense.getAmount()));
    }

    @Override
    public int getItemCount() {
        return Objects.requireNonNull(listOfExpenses.size());
    }


}
