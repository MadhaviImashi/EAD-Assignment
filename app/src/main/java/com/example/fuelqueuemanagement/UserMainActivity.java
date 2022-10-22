package com.example.fuelqueuemanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.fuelqueuemanagement.UtilsService.SharedPreferenceClass;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserMainActivity extends AppCompatActivity {
    //Main Activity should give a search bar to search nearest fuel station
    SharedPreferenceClass sharedPreferenceClass;

    private TextView user_name, user_email;
    private CircleImageView userImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_user);
        sharedPreferenceClass = new SharedPreferenceClass(this);

//        getUserProfile();
    }

    private void getUserProfile() {
        String url = " https://ead-fuel-app.herokuapp.com/api/auth";
        final String token = sharedPreferenceClass.getValue_string("token");

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getBoolean("success")) {
                        JSONObject userObj = response.getJSONObject("user");
                        user_name.setText(userObj.getString("username"));
                        user_email.setText(userObj.getString("email"));

                        Picasso.with(getApplicationContext())
                                .load(userObj.getString("avatar"))
                                .placeholder(R.drawable.ic_account)
                                .error(R.drawable.ic_account)
                                .into(userImage);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(UserMainActivity.this, "Error " + error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", token);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }
}