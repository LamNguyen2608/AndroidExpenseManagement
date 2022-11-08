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

import com.example.mexpensedemo.MutateTripFragment;
import com.example.mexpensedemo.R;
import com.example.mexpensedemo.data.TripDAO;
import com.example.mexpensedemo.model.Trip;
import com.example.mexpensedemo.model.TripViewModel;

import java.util.List;
import java.util.Objects;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private List<TripDAO.TripWithSumExpenses> listOfTrips;
    private Context context;
    private OnTripClickListener tripClickListener;

    public void setFilteredTrips(List<TripDAO.TripWithSumExpenses> filteredTrips){
        listOfTrips = filteredTrips;
        notifyDataSetChanged();
    }
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        OnTripClickListener onTripClickListener;
        public TextView trip_name;
        public TextView trip_date;
        public TextView trip_destination;
        public TextView trip_status;
        public TextView total_expense;
        public Button btn_delete;

        public ViewHolder(@NonNull View itemView, OnTripClickListener onTripClickListener) {
            super(itemView);
            trip_date = itemView.findViewById(R.id.txt_time);
            trip_name = itemView.findViewById(R.id.txt_tripname);
            trip_destination = itemView.findViewById(R.id.txt_destination);
            trip_status = itemView.findViewById(R.id.txt_status);
            btn_delete = itemView.findViewById(R.id.tripitem_deletetrip);
            total_expense = itemView.findViewById(R.id.txt_trip_total);

//            btn_delete.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    TripDAO.TripWithSumExpenses trip = listOfTrips.get(getAdapterPosition());
//                    FragmentActivity fm = (FragmentActivity) view.getContext();
//                    MutateTripFragment deleteTrip = new MutateTripFragment(trip, false);
//                    deleteTrip.show(fm.getSupportFragmentManager(), "delete trip" );
//                }
//            });

            this.onTripClickListener = onTripClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onTripClickListener.onTripClick(getAdapterPosition(), view);
        }
    }

    public RecyclerViewAdapter(List<TripDAO.TripWithSumExpenses> listOfTrips, Context context, OnTripClickListener tripClickListener) {
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
        TripDAO.TripWithSumExpenses tripsum = Objects.requireNonNull(listOfTrips.get(position));
        Trip trip = tripsum.getTrip();
        String [] destinationString = trip.getDestination().split("/", 3);
        holder.trip_name.setText(trip.getTrip_name());
        holder.trip_date.setText(trip.getDateStart());
        holder.trip_destination.setText(destinationString[0]);
        holder.trip_status.setText(trip.getStatus());
        holder.total_expense.setText(String.format("%.2f", tripsum.getSumOfExpense()));
    }

    @Override
    public int getItemCount() {
        return Objects.requireNonNull(listOfTrips.size());
    }

    public interface OnTripClickListener {
        void onTripClick(int position, View view);
    }

}
