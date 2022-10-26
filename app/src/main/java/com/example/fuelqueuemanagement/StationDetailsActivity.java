package com.example.fuelqueuemanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class StationDetailsActivity extends AppCompatActivity {

    private String station_id, current_time;
    private TextView diesel_ql_bus, diesel_ql_3wheeler, petrol_ql_car, petrol_ql_bike, petrol_ql_3wheeler;
    private TextView diesel_wt_bus, diesel_wt_3wheeler, petrol_wt_car, petrol_wt_bike, petrol_wt_3wheeler;
    private JSONObject queue_lengths, queue_waiting_times;
    private EditText heading, petrol_status, diesel_status;

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_details);

        Intent intent = getIntent();
        station_id = intent.getStringExtra("station_id");

        Log.e("getExtra-stationID:", station_id);

        progressBar = findViewById(R.id.progress_bar);
        //initialize queue length variables
        diesel_ql_bus = findViewById(R.id.diesel_bus_ql);
        diesel_ql_3wheeler = findViewById(R.id.diesel_threewheeler_ql);
        petrol_ql_car = findViewById(R.id.p_car_ql);
        petrol_ql_bike = findViewById(R.id.p_bike_ql);
        petrol_ql_3wheeler = findViewById(R.id.p_threewheeler_ql);

        //initialize waiting time variables
        diesel_wt_bus = findViewById(R.id.p_threewheeler_wt);
        diesel_wt_3wheeler = findViewById(R.id.diesel_threewheeler_wt);
        petrol_wt_car = findViewById(R.id.p_car_wt);
        petrol_wt_bike = findViewById(R.id.p_bike_wt);
        petrol_wt_3wheeler = findViewById(R.id.p_threewheeler_wt);

        displayFuelQueueDetails();
    }

    public void displayFuelQueueDetails() {
        getQueueLengths();
        getWaitingTimes();
    }

    public void getQueueLengths() {
        final HashMap<String, String> params = new HashMap<>();
        params.put("station_id", station_id);

        String apiKey = "https://ead-fuel-app.herokuapp.com/api/fuel-station/q-lengths";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                apiKey, new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getBoolean("success")) {

                        Toast.makeText(StationDetailsActivity.this, "got fuel queue details", Toast.LENGTH_SHORT).show();
                        queue_lengths = response.getJSONObject("queueLengths"); //access response body
                        Integer var = queue_lengths.getInt("diesel_bus_queue_length");
                        Log.e("queue length obj", "diesel_bus-q-lenght:  "+var);

                        //display lengths of each queue
                        diesel_ql_bus.setText(String.valueOf(queue_lengths.getInt("diesel_bus_queue_length")));
                        diesel_ql_3wheeler.setText(String.valueOf(queue_lengths.getInt("diesel_threeWheeler_queue_length")));
                        petrol_ql_car.setText(String.valueOf(queue_lengths.getInt("petrol_car_queue_length")));
                        petrol_ql_bike.setText(String.valueOf(queue_lengths.getInt("petrol_bike_queue_length")));
                        petrol_ql_3wheeler.setText(String.valueOf(queue_lengths.getInt("petrol_threeWheeler_queue_length")));
                    }
//                    progressBar.setVisibility(View.GONE);
                } catch (JSONException e) {
                    e.printStackTrace();
//                    progressBar.setVisibility(View.GONE);
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
                        Toast.makeText(StationDetailsActivity.this, "Couldn't fetch fuel details", Toast.LENGTH_SHORT).show();
//                        progressBar.setVisibility(View.GONE);
                    } catch (JSONException | UnsupportedEncodingException je) {
                        je.printStackTrace();
//                        progressBar.setVisibility(View.GONE);
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

    public void getWaitingTimes() {
        final HashMap<String, String> params = new HashMap<>();
        params.put("station_id", station_id);

        String apiKey = "https://ead-fuel-app.herokuapp.com/api/fuel-station/q-waiting-times";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                apiKey, new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getBoolean("success")) {

                        Toast.makeText(StationDetailsActivity.this, "got fuel queue waiting times", Toast.LENGTH_SHORT).show();
                        queue_waiting_times = response.getJSONObject("waitingTimes"); //access response body
                        String var2 = queue_waiting_times.getString("w_time_for_diesel_bus_queue");
                        Log.e("waiting times obj", "diesel_bus-q-lenght:  "+var2);

                        //display lengths of each queue
                        diesel_wt_bus.setText(queue_waiting_times.getString("w_time_for_diesel_bus_queue"));
                        diesel_wt_3wheeler.setText(queue_waiting_times.getString("w_time_for_diesel_threewheeler_queue"));
                        petrol_wt_car.setText(queue_waiting_times.getString("w_time_for_petrol_car_queue"));
                        petrol_wt_bike.setText(queue_waiting_times.getString("w_time_for_petrol_bike_queue"));
                        petrol_wt_3wheeler.setText(queue_waiting_times.getString("w_time_for_petrol_threewheeler_queue"));
                    }
//                    progressBar.setVisibility(View.GONE);
                } catch (JSONException e) {
                    e.printStackTrace();
//                    progressBar.setVisibility(View.GONE);
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
                        Toast.makeText(StationDetailsActivity.this, "Couldn't fetch fuel details", Toast.LENGTH_SHORT).show();
//                        progressBar.setVisibility(View.GONE);
                    } catch (JSONException | UnsupportedEncodingException je) {
                        je.printStackTrace();
//                        progressBar.setVisibility(View.GONE);
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