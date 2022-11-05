package com.example.mexpensedemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mexpensedemo.MainActivity;
import com.example.mexpensedemo.MapTripsFragment;
import com.example.mexpensedemo.MutateExpenseFragment;
import com.example.mexpensedemo.R;
import com.example.mexpensedemo.model.Expense;

import java.util.List;
import java.util.Objects;

public class GraphViewAdapter extends RecyclerView.Adapter<GraphViewAdapter.ViewHolder> {
    private List<Fragment> listOfFragments;
    private Context context;


    public class ViewHolder extends RecyclerView.ViewHolder{
        public RecyclerView rclv_graph;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rclv_graph = itemView.findViewById(R.id.graph_container);
        }
    }

    public GraphViewAdapter(List<Fragment> listOfFragments, Context context) {
        this.listOfFragments = listOfFragments;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.graph_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Fragment fragment = Objects.requireNonNull(listOfFragments.get(holder.getAdapterPosition()));
        ((MainActivity) context).getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.graph_container, fragment)
                .addToBackStack(null).commit();
    }

    @Override
    public int getItemCount() {
        return Objects.requireNonNull(listOfFragments.size());
    }


}
