package com.example.fuelqueuemanagement;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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
import com.example.fuelqueuemanagement.UtilsService.SharedPreferenceClass;
import com.example.fuelqueuemanagement.UtilsService.UtilService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private Button userRegisterBtn, shedRegisterBtn;

    private Button loginBtn;
    private EditText et_email, et_password;
    ProgressBar progressBar;

    private String email, password; //string  variables
    UtilService utilService;
    SharedPreferenceClass sharedPreferenceClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //called when activity created
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        shedRegisterBtn = findViewById(R.id.createUserBtn);
        userRegisterBtn = findViewById(R.id.userRegisterBtn);

        userRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, UserRegisterActivity.class);
                startActivity(intent);
            }
        });

        shedRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, StationOwnerSignupActivity.class);
                startActivity(intent);
            }
        });

        loginBtn = findViewById(R.id.loginBtn);
        et_email = findViewById(R.id.email_ET);
        et_password = findViewById(R.id.password_ET);
        progressBar = findViewById(R.id.progress_bar);
        utilService = new UtilService();

        sharedPreferenceClass = new SharedPreferenceClass(this);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                utilService.hideKeyboard(view, LoginActivity.this);
                //get user inputs
                email = et_email.getText().toString();
                password = et_password.getText().toString();
                //authenticate the user if the input fields are successfully validated
                if(validate(view)) {
                    loginUser(view);
                }
            }
        });
    }

    private void loginUser(View view) {
        progressBar.setVisibility(View.VISIBLE);

        final HashMap<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);

        String apiKey = "https://ead-fuel-app.herokuapp.com/api/auth/login";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                apiKey, new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if(response.getBoolean("success")) {
                        String token = response.getString("token"); //access response body
                        String userType = response.getString("type");
                        String user_id = response.getString("userId");

                        sharedPreferenceClass.setValue_string("token", token);
                        Toast.makeText(LoginActivity.this, "login successfull!", Toast.LENGTH_SHORT).show();

                        //navigate user to the correct main screen
                        if(userType.equals("user")) {
                            Intent intent = new Intent(LoginActivity.this, SearchStationActivity.class);
                            intent.putExtra("user_id", user_id);
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(LoginActivity.this, StationDashboardMain.class);
                            intent.putExtra("user_id", user_id);
                            startActivity(intent);
                        }

                    }
                    progressBar.setVisibility(View.GONE);
                } catch (JSONException e) {
                    e.printStackTrace();
                    progressBar.setVisibility(View.GONE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LoginActivity.this, "A user with this email couldn't be found", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        })
        {
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

    private boolean validate(View view) {
        boolean isValid;
        if(!TextUtils.isEmpty(email)) {
            if(!TextUtils.isEmpty(password)) {
                isValid = true;
            } else {
                utilService.showSnackBar(view,"please enter password.");
                isValid = false;
            }
        } else {
            utilService.showSnackBar(view,"please enter email.");
            isValid = false;
        }
        return  isValid;
    }

    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences todo_pref = getSharedPreferences("user_todo", MODE_PRIVATE);
        //go to main activity if the user is already logged in
        if(todo_pref.contains("token")) {
            startActivity(new Intent(LoginActivity.this, UserMainActivity.class));
            finish();
        }
    }
}