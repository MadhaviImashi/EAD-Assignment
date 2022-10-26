package com.example.fuelqueuemanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class StationOwnerUpdateActivity extends AppCompatActivity {
    private String station_id;
    private EditText d_arrival_time, d_arrived_qty, d_finishing_time;
    private EditText p_arrival_time, p_arrived_qty, p_finishing_time;
    private Button updateFuelDetailsBtn;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_owner_update);

        Intent intent = getIntent();
        station_id = intent.getStringExtra("station_id");

        d_arrival_time = findViewById(R.id.etDieselArrivalTime);
        d_arrived_qty = findViewById(R.id.etDieselArrivalQuantity);
        d_finishing_time = findViewById(R.id.etDieselFinishedTime);

        p_arrival_time = findViewById(R.id.etPetrolArrivalTime);
        p_arrived_qty = findViewById(R.id.etPetrolArrivalQuantity);
        p_finishing_time = findViewById(R.id.etPetrolFinishedTime);

        updateFuelDetailsBtn = findViewById(R.id.editFuelDetails);

        updateFuelDetailsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateAndSaveFuelDetails();
            }
        });
    }

    public void updateAndSaveFuelDetails() {
        final HashMap<String, String> params = new HashMap<>();
        params.put("station_id", station_id);
        params.put("diesel_arrival_time", d_arrival_time.getText().toString());
        params.put("diesel_arrived_quantity", d_arrived_qty.getText().toString());
        params.put("diesel_finishing_time", d_finishing_time.getText().toString());
        params.put("petrol_arrival_time", p_arrival_time.getText().toString());
        params.put("petrol_arrived_quantity", p_arrived_qty.getText().toString());
        params.put("petrol_finishing_time", p_finishing_time.getText().toString());

        String apiKey = "https://ead-fuel-app.herokuapp.com/api/fuel-station/update";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                apiKey, new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getBoolean("success")) {

                        Toast.makeText(StationOwnerUpdateActivity.this, "got fuel station details", Toast.LENGTH_SHORT).show();

                        Log.e("inside stationManager", "details updated ");

                        Toast.makeText(StationOwnerUpdateActivity.this, "fuel details updated", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(StationOwnerUpdateActivity.this, "Couldn't updated fuel details", Toast.LENGTH_SHORT).show();
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