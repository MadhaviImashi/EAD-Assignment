package com.example.fuelqueuemanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.fuelqueuemanagement.UtilsService.SharedPreferenceClass;
import com.example.fuelqueuemanagement.UtilsService.UtilService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class StationOwnerSignupActivity extends AppCompatActivity {
    private Button registerBtn, loginBtn;
    private EditText station_name_ET, admin_name_ET, email_ET, password_ET;
    private CheckBox petrol, diesel;
    private boolean isPetrol, isDiesel;
    ProgressBar progressBar;

    private String stationName, adminName, email, password;
    UtilService utilService;
    SharedPreferenceClass sharedPreferenceClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_owner_signup);

        station_name_ET = findViewById(R.id.station_name);
        admin_name_ET = findViewById(R.id.owner_name);
        email_ET = findViewById(R.id.station_email);
        password_ET = findViewById(R.id.station_password);
        progressBar = findViewById(R.id.progress_bar);
        registerBtn = findViewById(R.id.station_register);
        loginBtn = findViewById(R.id.stationLogin);
        petrol = findViewById(R.id.petrolCheckbox);
        diesel = findViewById(R.id.dieselCheckbox);
        utilService = new UtilService();

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                utilService.hideKeyboard(view, StationOwnerSignupActivity.this);
                stationName = station_name_ET.getText().toString();
                adminName = admin_name_ET.getText().toString();
                email = email_ET.getText().toString();
                password = password_ET.getText().toString();
                isPetrol = petrol.isChecked();
                isDiesel = diesel.isChecked();

                if (validate(view)) {
                    registerFuelStation(view);
                }
            }
        });
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StationOwnerSignupActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void registerFuelStation(View view) {
        progressBar.setVisibility(View.VISIBLE);

        final HashMap<String, String> params = new HashMap<>();
        params.put("stationName", stationName);
        params.put("adminName", adminName);
        params.put("email", email);
        params.put("password", password);
        params.put("isPetrol", String.valueOf(isPetrol));
        params.put("isDiesel", String.valueOf(isDiesel));

        String apiKey = "https://ead-fuel-app.herokuapp.com/api/auth/shed-owner/register";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                apiKey, new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getBoolean("success")) {
                        Log.e("register successful", "success!");
                        Toast.makeText(StationOwnerSignupActivity.this, "Registration successful!", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                        startActivity(new Intent(StationOwnerSignupActivity.this, LoginActivity.class));
                    } else {
                        Log.e("unsuccess", "inside else!");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    progressBar.setVisibility(View.GONE);
                    Log.e("register unsuccessful", "couldn't register this user ");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(StationOwnerSignupActivity.this, "A similar user already exists", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
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


    public boolean validate(View view) {
        boolean isValid;
        if (!TextUtils.isEmpty(stationName)) {
            if (!TextUtils.isEmpty(adminName)) {
                if (!TextUtils.isEmpty(email)) {
                    if (!TextUtils.isEmpty(password)) {
                        isValid = true;
                    } else {
                        utilService.showSnackBar(view, "please enter password....");
                        isValid = false;
                    }
                } else {
                    utilService.showSnackBar(view, "please enter email....");
                    isValid = false;
                }
            } else {
                utilService.showSnackBar(view, "please enter your name....");
                isValid = false;
            }
        } else {
            utilService.showSnackBar(view, "please enter the station name....");
            isValid = false;
        }
        return isValid;
    }
}
