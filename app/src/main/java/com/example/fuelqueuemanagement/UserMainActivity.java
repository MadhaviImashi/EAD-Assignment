package com.example.fuelqueuemanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.fuelqueuemanagement.UtilsService.SharedPreferenceClass;
import com.example.fuelqueuemanagement.UtilsService.UtilService;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserMainActivity extends AppCompatActivity {
    //Main Activity should offer options for user to navigate to correct interface as per need
    ProgressBar progressBar;

    private Button goToStationDetailsBtn, checkFuelAvailabilityBtn;
    private String station_id, station_name,  msg;
    private EditText stationName;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_user);

        Intent intent = getIntent();
        station_id = intent.getStringExtra("station_id");
        station_name = intent.getStringExtra("station_name");

        Log.e("getExtra-stationID:", station_id+""+station_name);

        progressBar = findViewById(R.id.progress_bar);
        goToStationDetailsBtn = findViewById(R.id.goToStationDetailsBtn);
        checkFuelAvailabilityBtn = findViewById(R.id.checkFuelAvailabilityBtn);
        stationName = findViewById(R.id.stationName);

        stationName.setText(station_name);

        goToStationDetailsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserMainActivity.this, StationDetailsActivity.class);
                intent.putExtra("station_id", station_id);
                startActivity(intent);
            }
        });
        checkFuelAvailabilityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserMainActivity.this, StationDetailsActivity.class);
                intent.putExtra("station_id", station_id);
                startActivity(intent);
            }
        });
    }
}
