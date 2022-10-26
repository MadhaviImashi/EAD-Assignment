package com.example.fuelqueuemanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

public class UserMainActivity extends AppCompatActivity {
    //Main Activity should offer options for user to navigate to correct interface as per need
    ProgressBar progressBar;

    private Button goToStationDetailsBtn, checkFuelAvailabilityBtn;
    private String station_id, station_name, user_id,  msg;
    private EditText stationName;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_user);

        Intent intent = getIntent();
        station_id = intent.getStringExtra("station_id");
        station_name = intent.getStringExtra("station_name");
        user_id = intent.getStringExtra("user_id");

        Log.e("getExtra-stationID:", station_id+""+station_name);

        progressBar = findViewById(R.id.progress_bar);
        goToStationDetailsBtn = findViewById(R.id.goToStationDetailsBtn);
        checkFuelAvailabilityBtn = findViewById(R.id.checkFuelAvailabilityBtn);
        stationName = findViewById(R.id.heading);

        stationName.setText(station_name);

        goToStationDetailsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserMainActivity.this, StationDetailsActivity.class);
                intent.putExtra("station_id", station_id);
                intent.putExtra("user_id", user_id);
                startActivity(intent);
            }
        });
        checkFuelAvailabilityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserMainActivity.this, FuelAvailability.class);
                intent.putExtra("station_id", station_id);
                intent.putExtra("station_name", station_name);
                startActivity(intent);
            }
        });
    }
}
