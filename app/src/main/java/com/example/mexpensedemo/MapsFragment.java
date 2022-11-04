package com.example.mexpensedemo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
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

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class MapsFragment extends AppCompatDialogFragment implements OnMapReadyCallback {
    GoogleMap mMap;
    private EditText enter_search;
    private Address currentAddress;
    passingAddress dataPasser;
    private LatLng editLatLng = null;

    public MapsFragment() {
    }

    public MapsFragment(LatLng editLatLng) {
        this.editLatLng = editLatLng;
    }

    public interface OnEditListener {
        void sendNewLocation(Address address);
    }
    public MapsFragment.OnEditListener onEditListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onAttachToParentFragment(getParentFragment());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_maps, container, false);

        Button btn_add_location = rootView.findViewById(R.id.btn_addLocation);
        enter_search = rootView.findViewById(R.id.search_location);

        enter_search.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
                    String location = enter_search.getText().toString();
                    if (location == null) {
                        enter_search.setError("Please enter your location name");
                        Toast.makeText(getContext(), "Please enter your location name", Toast.LENGTH_SHORT).show();
                    } else {
                        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
                        try {
                            List<Address> addressList = geocoder.getFromLocationName(location, 1);
                            if (addressList.size() > 0) {
                                LatLng search_location = new LatLng(addressList.get(0).getLatitude(), addressList.get(0).getLongitude());
                                currentAddress = addressList.get(0);
                                getLocation(search_location);
                                btn_add_location.setVisibility(View.VISIBLE);
                            } else {
                                Toast.makeText(getContext(), "No location found! Please re-check and retry", Toast.LENGTH_SHORT).show();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    return true;
                }
                return false;
            }
        });
        btn_add_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editLatLng == null) {
                    Log.d("Location", "Clicked");
                    dataPasser.onDataPass(currentAddress);
                    dismiss();
                } else {
                    onEditListener.sendNewLocation(currentAddress);
                    dismiss();
                }


            }
        });
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
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
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        if (editLatLng != null){
            getLocation(editLatLng);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Window window = getDialog().getWindow();
        if(window == null) return;
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = 1000;
        params.height = 1600;
        window.setAttributes(params);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (editLatLng == null) {
            dataPasser = (passingAddress) context;
        }
    }

    public interface passingAddress {
        public void onDataPass(Address chosenAddress);
    }



    public void onAttachToParentFragment(Fragment fragment)
    {
        try
        {
            onEditListener = (MapsFragment.OnEditListener) fragment;

        }
        catch (ClassCastException e)
        {
            throw new ClassCastException(
                    fragment.toString() + " must implement OnPlayerSelectionSetListener");
        }
    }

}



