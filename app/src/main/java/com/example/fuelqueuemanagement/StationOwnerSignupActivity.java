package com.example.fuelqueuemanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.example.fuelqueuemanagement.dto.FuelStation;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class StationOwnerSignupActivity extends AppCompatActivity {

    EditText station_name, owner_name, station_email, station_password;
    CheckBox petrol, diesel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_owner_signup);


        //When the user is already logged in, it will directly start the MainActivity (profile) activity
        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, StationManagementActivity.class));
            return;
        }

        station_name = findViewById(R.id.station_name);
        owner_name = findViewById(R.id.owner_name);
        station_email = findViewById(R.id.station_email);
        station_password = findViewById(R.id.station_password);
        petrol = findViewById(R.id.petrol);
        diesel = findViewById(R.id.diesel);

        findViewById(R.id.station_register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //When the user pressed on button register, it will register the user to server
                try {
                    registerStation();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

        findViewById(R.id.stationLogin).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //When the user pressed on textview that already register open LoginActivity
                finish();
                startActivity(new Intent(StationOwnerSignupActivity.this, LoginActivity.class));

            }
        });
    }

    private void registerStation() throws JSONException {

        final String stationName = station_name.getText().toString().trim();
        final String ownerName = owner_name.getText().toString().trim();
        final String email = station_email.getText().toString().trim();
        final String password = station_password.getText().toString().trim();

        final boolean isPetrol = petrol.isChecked();
        final boolean isDiesel = diesel.isChecked();


        // validations
        if (TextUtils.isEmpty(stationName)) {

            station_name.setError("Please enter the station name");
            station_name.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(ownerName)) {

            owner_name.setError("Please enter the owner's name");
            owner_name.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(email)) {
            station_email.setError("Please enter the email");
            station_email.requestFocus();
            return;
        }

        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            station_email.setError("Enter a valid email address");
            station_email.requestFocus();
            return;

        }

        if (TextUtils.isEmpty(password)) {
            station_password.setError("Enter a password");
            station_password.requestFocus();
            return;

        }


        JSONObject jsonBody = new JSONObject();
        jsonBody.put("station_name", stationName);
        jsonBody.put("owner_name", stationName);
        jsonBody.put("petrol", isPetrol);
        jsonBody.put("diesel", isDiesel);
        jsonBody.put("email", email);
        jsonBody.put("password", password);
        final String requestBody = jsonBody.toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_REGISTER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            //converting the response to json object
                            JSONObject obj = new JSONObject(response);

                            //create a new user object
                            FuelStation fuelStation = new FuelStation(
                                    obj.getString("_id"),
                                    obj.getString("station_name"),
                                    obj.getString("owner_name"),
                                    obj.getString("email"),
                                    obj.getBoolean("petrol"),
                                    obj.getBoolean("diesel")
                            );

                            //store the user in shared preferences
                            SharedPrefManager.getInstance(getApplicationContext()).stationLogin(fuelStation);

                            //starte the profile activity
                            finish();
                            startActivity(new Intent(getApplicationContext(), StationManagementActivity.class));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }})
        {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                    return null;
                }
            }
        };

//        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);

    }
}