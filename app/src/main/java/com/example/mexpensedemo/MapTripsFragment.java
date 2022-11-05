package com.example.mexpensedemo;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.mexpensedemo.adapter.RecyclerViewAdapter;
import com.example.mexpensedemo.data.TripDAO;
import com.example.mexpensedemo.model.TripViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class MapTripsFragment extends Fragment implements OnMapReadyCallback {
    GoogleMap mMap;
    private LatLng editLatLng = null;
    private TripViewModel tripViewModel;
    private List<TripDAO.TripWithSumExpenses> listOfTrips;

    public MapTripsFragment() {
    }


    public interface OnEditListener {
        void sendNewLocation(Address address);
    }
    public MapTripsFragment.OnEditListener onEditListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_map_trips, container, false);

        tripViewModel = new ViewModelProvider.AndroidViewModelFactory(getActivity()
                .getApplication())
                .create(TripViewModel.class);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.home_map);
        mapFragment.getMapAsync(this);
        return rootView;
    }

    private void getLocation(LatLng latLng) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,11));
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        mMap.addMarker(markerOptions);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        tripViewModel.getTripSum().observe(getActivity(), trips -> {
            listOfTrips = trips;
            for (TripDAO.TripWithSumExpenses trip : listOfTrips) {
                String[] trip_location = trip.getTrip().getDestination().split("/", 3);

                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(new LatLng(Float.parseFloat(trip_location[1]), Float.parseFloat(trip_location[2])));
                markerOptions.title(trip.getTrip().getTrip_name());
                //markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
                mMap.addMarker(markerOptions);
            }
        });
    }
}



