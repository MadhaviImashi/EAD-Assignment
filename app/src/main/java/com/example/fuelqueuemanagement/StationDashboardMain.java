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
import org.w3c.dom.Text;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class StationDashboardMain extends AppCompatActivity {
    private String station_id, d_arrival_date, p_arrival_date;
    private TextView petrolStatus, dieselStatus;
    private JSONObject fuelShed, diesel, petrol;
    private TextView d_arrival_time, d_arrived_qty, d_finishing_time, d_avaiable_total_amount;
    private TextView p_arrival_time, p_arrived_qty, p_finishing_time, p_avaiable_total_amount;
    private Button goToUpdateFuelDetailsBtn;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_dashboard_both);

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

        petrolStatus = findViewById(R.id.petrolStatus);
        dieselStatus = findViewById(R.id.dieselStatus);

        goToUpdateFuelDetailsBtn = findViewById(R.id.editFuelDetails);

        displayFuelStationDetails();

        goToUpdateFuelDetailsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //navigate to updateDetails activity
                Intent intent = new Intent(StationDashboardMain.this, StationOwnerUpdateActivity.class);
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

                        Toast.makeText(StationDashboardMain.this, "got fuel station details", Toast.LENGTH_SHORT).show();

//                        Boolean petrolAv = response.getBoolean("isPetrol");
//                        Boolean dieselAv = response.getBoolean("isDiesel");

//                        if(petrolAv && dieselAv) {
                            //get fuel station details from http response
                            fuelShed = response.getJSONObject("fuelShed");
                            diesel = fuelShed.getJSONObject("Diesel"); //access response body
                            petrol = fuelShed.getJSONObject("Petrol"); //access response body
                            String var = diesel.getString("avaiableTotalFuelAmount");
                            Log.e("inside stationManager", "diesel-arrival time:  "+ var);

                            //display diesel details
                            d_arrival_time.setText(diesel.getString("arrivalDate")+"   "+diesel.getString("arrivalTime"));
                            d_arrived_qty.setText(String.valueOf(diesel.getInt("arrivedQuantity")));
                            d_finishing_time.setText(diesel.getString("finishingTime"));
                            d_avaiable_total_amount.setText(String.valueOf(diesel.getInt("avaiableTotalFuelAmount")));
                            if((diesel.getInt("avaiableTotalFuelAmount"))>0) {
                                dieselStatus.setText("Available");
                            }
                            //display petrol details
                            p_arrival_time.setText(petrol.getString("arrivalDate")+"   "+petrol.getString("arrivalTime"));
                            p_arrived_qty.setText(String.valueOf(petrol.getInt("arrivedQuantity")));
                            p_finishing_time.setText(petrol.getString("finishingTime"));
                            p_avaiable_total_amount.setText(String.valueOf(petrol.getInt("avaiableTotalFuelAmount")));
                            if((petrol.getInt("avaiableTotalFuelAmount"))>0) {
                                petrolStatus.setText("Available");
                            }
                            Toast.makeText(StationDashboardMain.this, "fetched fuel details", Toast.LENGTH_SHORT).show();
//                        }
//                        else if(petrolAv) {
//                            Intent intent = new Intent(StationDashboardMain.this, StationDashboardPetrol.class);
////                            intent.putExtra("arrivalTime", );
////                            intent.putExtra("arrivedQuantity", );
////                            intent.putExtra("arrivalTime", );
////                            intent.putExtra("arrivalTime", );
//                            startActivity(intent);
//                        }
//                        else if(dieselAv) {
//                            Intent intent = new Intent(StationDashboardMain.this, StationDashboardDiesel.class);
////                            intent.putExtra("user_id", );
//                            startActivity(intent);
//                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(StationDashboardMain.this, "couldn't fetch station details", Toast.LENGTH_SHORT).show();
//                progressBar.setVisibility(View.GONE);
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