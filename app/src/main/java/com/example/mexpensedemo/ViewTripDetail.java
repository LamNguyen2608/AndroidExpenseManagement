package com.example.mexpensedemo;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mexpensedemo.model.TripViewModel;

public class ViewTripDetail extends Fragment {
    private TextView trip_name;
    private TripViewModel tripViewModel;
    private TextView trip_date;
    private TextView trip_destination;
    private TextView trip_description;
    private TextView trip_status;
    private TextView trip_isRisk;
    private ImageView back_icon;
    private int trip_id;
    public static ViewTripDetail newInstance() {
        ViewTripDetail fragment = new ViewTripDetail();
        return fragment;
    }

    public ViewTripDetail() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView heading = getActivity().findViewById(R.id.txt_heading);
        ImageView ham_menu = getActivity().findViewById(R.id.ham_menu);
        back_icon = getActivity().findViewById(R.id.btn_back);
        heading.setText("Trip Details");
        ham_menu.setVisibility(View.GONE);
        back_icon.setVisibility(View.VISIBLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View frag = inflater.inflate(R.layout.fragment_view_trip_detail, container, false);
        tripViewModel = new ViewModelProvider.AndroidViewModelFactory(getActivity()
                .getApplication())
                .create(TripViewModel.class);
        trip_name = frag.findViewById(R.id.detail_tripname);
        trip_destination = frag.findViewById(R.id.detail_destination);
        trip_date = frag.findViewById(R.id.detail_date);
        trip_status = frag.findViewById(R.id.detail_status);
        trip_isRisk = frag.findViewById(R.id.detail_isRisk);
        trip_description = frag.findViewById(R.id.detail_description);
        getParentFragmentManager().setFragmentResultListener("datafromviewall", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                trip_id = result.getInt(ViewAllTripFragment.TRIP_ID);
                tripViewModel.getTrip(trip_id).observe(getActivity(), trip -> {
                    trip_name.setText(trip.getTrip_name());
                    trip_destination.setText(trip.getDestination());
                    trip_date.setText(trip.getDate());
                    trip_description.setText(trip.getDescription());
                    if (trip.getRisk() == true){
                        trip_isRisk.setText("Yes");
                    } else {trip_isRisk.setText("No");}

                });
            }
        });

        back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("click detail back", "clicked");
                Navigation.findNavController(frag).navigate(R.id.action_viewTripDetail_to_viewAllTripFragment);
            }
        });
        return frag;
    }

}