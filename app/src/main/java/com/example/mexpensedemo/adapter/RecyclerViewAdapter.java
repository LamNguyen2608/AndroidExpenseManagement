package com.example.mexpensedemo.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mexpensedemo.R;
import com.example.mexpensedemo.model.Trip;

import java.util.List;
import java.util.Objects;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private List<Trip> listOfTrips;
    private Context context;
    private OnTripClickListener tripClickListener;
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        OnTripClickListener onTripClickListener;
        public TextView trip_name;
        public TextView trip_date;
        public TextView trip_destination;
        public TextView trip_status;

        public ViewHolder(@NonNull View itemView, OnTripClickListener onTripClickListener) {
            super(itemView);
            trip_date = itemView.findViewById(R.id.txt_time);
            trip_name = itemView.findViewById(R.id.txt_tripname);
            trip_destination = itemView.findViewById(R.id.txt_destination);
            trip_status = itemView.findViewById(R.id.txt_status);

            this.onTripClickListener = onTripClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onTripClickListener.onTripClick(getAdapterPosition(), view);
        }
    }

    public RecyclerViewAdapter(List<Trip> listOfTrips, Context context, OnTripClickListener tripClickListener) {
        this.listOfTrips = listOfTrips;
        this.context = context;
        this.tripClickListener = tripClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trip_item, parent, false);
        return new ViewHolder(view, tripClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Trip trip = Objects.requireNonNull(listOfTrips.get(position));
        holder.trip_name.setText(trip.getTrip_name());
        holder.trip_date.setText(trip.getDate());
        holder.trip_destination.setText(trip.getDestination());
        holder.trip_status.setText(trip.getStatus());
    }

    @Override
    public int getItemCount() {
        return Objects.requireNonNull(listOfTrips.size());
    }

    public interface OnTripClickListener {
        void onTripClick(int position, View view);
    }

}
