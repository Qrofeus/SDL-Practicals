package com.qrofeus.sdlpracticals;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class Dashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        if (ActivityCompat.checkSelfPermission(Dashboard.this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(Dashboard.this, new String[]{Manifest.permission.INTERNET}, 100);

        if (ActivityCompat.checkSelfPermission(Dashboard.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(Dashboard.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 101);

        if (ActivityCompat.checkSelfPermission(Dashboard.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(Dashboard.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 102);
    }

    public void audioPlayer(View view) {
        startActivity(new Intent(Dashboard.this, AudioPlayer.class));
    }

    public void storeData(View view) {
        startActivity(new Intent(Dashboard.this, FileStorage.class));
    }

    public void gpsLocator(View view) {
        startActivity(new Intent(Dashboard.this, LocationSearch.class));
    }
}