package com.qrofeus.sdlpracticals;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;

public class LocationSearch extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    GoogleMap map;
    SupportMapFragment fragment;
    SearchView searchView;
    FusedLocationProviderClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Internet connection required
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_search);

        client = LocationServices.getFusedLocationProviderClient(this);

        ConnectivityManager manager = (ConnectivityManager) this.getSystemService(CONNECTIVITY_SERVICE);
        if (manager.getActiveNetworkInfo() == null) {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }

        searchView = findViewById(R.id.searchBar);
        fragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment);

        // On search
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Clear all previous markers
                map.clear();

                String location = searchView.getQuery().toString();
                List<Address> addresses = null;

                if (!location.isEmpty()) {
                    Geocoder geocoder = new Geocoder(LocationSearch.this);
                    try {
                        // Search for locations
                        addresses = geocoder.getFromLocationName(location, 5);
                    } catch (IOException e) {
                        Toast.makeText(LocationSearch.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }

                    if (addresses != null) {
                        for (Address address : addresses) {
                            // Get latitude and longitude for all results and place markers
                            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                            map.addMarker(new MarkerOptions().position(latLng).title(location));
                        }

                        // Update Map Camera for first result
                        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(addresses.get(0).getLatitude(), addresses.get(0).getLongitude()), 10.0f));
                    } else
                        Toast.makeText(LocationSearch.this, "No Locations found", Toast.LENGTH_SHORT).show();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        fragment.getMapAsync(this);
    }

    public void myLocation(View view) {
        // Check for Permissions
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(LocationSearch.this, "Permissions not granted", Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(LocationSearch.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            ActivityCompat.requestPermissions(LocationSearch.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 102);
            return;
        }

        // Get Location
        client.getLastLocation()
                .addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null){
                            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                            map.addMarker(new MarkerOptions().position(latLng));
                            map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10.0f));
                        } else
                            Toast.makeText(LocationSearch.this, "No Location Found", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(LocationSearch.this, "Couldn't get location", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

    }
}