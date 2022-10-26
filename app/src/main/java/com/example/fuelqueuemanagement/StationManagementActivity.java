package com.example.fuelqueuemanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class StationManagementActivity extends AppCompatActivity {
    private String station_id;
    private JSONObject fuelShed, diesel, petrol;
    private TextView d_arrival_time, d_arrived_qty, d_finishing_time, d_avaiable_total_amount;
    private TextView p_arrival_time, p_arrived_qty, p_finishing_time, p_avaiable_total_amount;
    private Button goToUpdateFuelDetailsBtn;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_management);

        Intent intent = getIntent();
        station_id = intent.getStringExtra("user_id");

        d_arrival_time = findViewById(R.id.dieselArrivalTime);
        d_arrived_qty = findViewById(R.id.dieselArrivalQuantity);
        d_finishing_time = findViewById(R.id.dieselFinishedTime);
        d_avaiable_total_amount = findViewById(R.id.dieselTotal);

        p_arrival_time = findViewById(R.id.petrolArrivalTime);
        p_arrived_qty = findViewById(R.id.petrolArrivalQuantity);
        p_finishing_time = findViewById(R.id.petrolFinishedTime);
        p_avaiable_total_amount = findViewById(R.id.petrolTotal);

        goToUpdateFuelDetailsBtn = findViewById(R.id.editFuelDetails);

        displayFuelStationDetails();

        goToUpdateFuelDetailsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //navigate to updateDetails activity
                Intent intent = new Intent(StationManagementActivity.this, StationOwnerUpdateActivity.class);
                intent.putExtra("station_id", station_id);
                startActivity(intent);
            }
        });
    }

    public void displayFuelStationDetails() {
        final HashMap<String, String> params = new HashMap<>();
        params.put("station_id", station_id);

        String apiKey = "https://ead-fuel-app.herokuapp.com/api/fuel-station/station-details";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                apiKey, new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getBoolean("success")) {

                        Toast.makeText(StationManagementActivity.this, "got fuel station details", Toast.LENGTH_SHORT).show();

                        //get fuel station details from http response
                        fuelShed = response.getJSONObject("fuelShed");
                        diesel = fuelShed.getJSONObject("Diesel"); //access response body
                        petrol = fuelShed.getJSONObject("Petrol"); //access response body
                        String var = diesel.getString("arrivalTime");
                        Log.e("inside stationManager", "diesel-arrival time:  "+ fuelShed);

                        //display diesel details
                        d_arrival_time.setText(diesel.getString("arrivalTime"));
                        d_arrived_qty.setText(diesel.getString("arrivedQuantity"));
                        d_finishing_time.setText(diesel.getString("finishingTime"));
                        d_avaiable_total_amount.setText(diesel.getString("avaiableTotalFuelAmount"));
                        //display petrol details
                        p_arrival_time.setText(petrol.getString("arrivalTime"));
                        p_arrived_qty.setText(petrol.getString("arrivedQuantity"));
                        p_finishing_time.setText(petrol.getString("finishingTime"));
                        p_avaiable_total_amount.setText(petrol.getString("avaiableTotalFuelAmount"));

                        Toast.makeText(StationManagementActivity.this, "fetched fuel details", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                NetworkResponse response = error.networkResponse;
                if (error instanceof ServerError && response != null) {
                    try {
                        String res = new String(response.data, HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                        JSONObject obj = new JSONObject(res);
                        Toast.makeText(StationManagementActivity.this, "Couldn't fetch fuel details", Toast.LENGTH_SHORT).show();
                    } catch (JSONException | UnsupportedEncodingException je) {
                        je.printStackTrace();
                    }
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return params;
            }
        };
        // request add
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }
}