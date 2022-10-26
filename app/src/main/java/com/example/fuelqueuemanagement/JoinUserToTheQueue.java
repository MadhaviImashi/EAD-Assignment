package com.example.fuelqueuemanagement;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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

public class JoinUserToTheQueue extends AppCompatActivity {
    private String station_id, station_name, user_id, vehical_type, fuel_type;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exit_queue);

        Intent intent = getIntent();
        station_id = intent.getStringExtra("station_id");
        user_id = intent.getStringExtra("user_id");
        fuel_type = intent.getStringExtra("fuel_type");
        vehical_type = intent.getStringExtra("vehical_type");

        //add user to the correct queue
        addToQueue();

    }
    public void addToQueue() {
        final HashMap<String, String> params = new HashMap<>();
        params.put("station_id", station_id);
        params.put("user_id", user_id);
        params.put("fuel_type", fuel_type);
        params.put("vehical_type", vehical_type);


        String apiKey = "https://ead-fuel-app.herokuapp.com/api/fuel-station/add-to-queue";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                apiKey, new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getBoolean("success")) {
                        Log.e("HttpClient", "inside onResponse");
                        String msg = response.getString("message"); //access response body
                        Toast.makeText(JoinUserToTheQueue.this, msg, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(JoinUserToTheQueue.this, StationDetailsActivity.class);
                        startActivity(intent);
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
                        Toast.makeText(JoinUserToTheQueue.this, "Couldn't add user to the queue", Toast.LENGTH_SHORT).show();
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