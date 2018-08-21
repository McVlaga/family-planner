package com.planner.family.therapist.nearby;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.planner.family.therapist.R;

public class NearbyFragment extends Fragment implements View.OnClickListener {

    private final LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            longitude = location.getLongitude();
            latitude = location.getLatitude();
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };

    private static final int LOCATION_PERMISSIONS_REQUEST_CODE = 26;

    private double longitude;
    private double latitude;

    public NearbyFragment() {

    }

    public static NearbyFragment newInstance() {
        return new NearbyFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.nearby_fragment, container, false);

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSIONS_REQUEST_CODE);
        } else {
            LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null) {
                longitude = location.getLongitude();
                latitude = location.getLatitude();
            }

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, locationListener);
        }

        Button foodButton = rootView.findViewById(R.id.food_button);
        Button hospitalsButton = rootView.findViewById(R.id.hospitals_button);
        Button gasStationsButton = rootView.findViewById(R.id.gas_stations_button);
        Button pharmaciesButton = rootView.findViewById(R.id.pharmacies_button);

        foodButton.setOnClickListener(this);
        hospitalsButton.setOnClickListener(this);
        gasStationsButton.setOnClickListener(this);
        pharmaciesButton.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        locationManager.removeUpdates(locationListener);

    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.food_button:
                String foodUri = "geo:"+ longitude + "," + latitude +"?q=food";
                intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(foodUri));
                break;
            case R.id.hospitals_button:
                String hospitalsUri = "geo:"+ longitude + "," + latitude +"?q=hospital";
                intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(hospitalsUri));
                break;
            case R.id.gas_stations_button:
                String gasStationsUri = "geo:"+ longitude + "," + latitude +"?q=gas station";
                intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(gasStationsUri));
                break;
            case R.id.pharmacies_button:
                String pharmaciesUri = "geo:"+ longitude + "," + latitude +"?q=pharmacy";
                intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(pharmaciesUri));
                break;
        }
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
        } else{
            Toast.makeText(getActivity(), "You do not have a maps application", Toast.LENGTH_SHORT).show();
        }
    }
}
