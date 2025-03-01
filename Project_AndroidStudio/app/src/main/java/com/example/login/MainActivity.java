package com.example.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        EditText editText = findViewById(R.id.editTextText);
        EditText editText1 = findViewById(R.id.editTextText2);
        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editText.getText().toString();
                String pass = editText1.getText().toString();

                // Check if username or password is empty
                if (username.isEmpty() || pass.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Username or password is empty", Toast.LENGTH_LONG).show();
                    return;
                }

                // Request queue to handle Volley requests
                RequestQueue queue = Volley.newRequestQueue(MainActivity.this);

                // URL to access Info.jsp
                String loginUrl = "http://10.0.2.2:8080/sleepyhollow/login?user=" + username + "&pass=" + pass;

                // Request to validate login credentials and retrieve SSN
                StringRequest loginRequest = new StringRequest(Request.Method.GET, loginUrl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                       // Log.d("success","Entered onResponse 2"+response);
                        // If login is successful, response will contain the SSN
                        if (!response.isEmpty()) {
                            // Construct URL for Info.jsp using retrieved SSN
                            String infoUrl = "http://10.0.2.2:8080/sleepyhollow/Info.jsp?ssn=" + response;
                            Log.d("success","infourl"+infoUrl);
                            // Start MainActivity2 and pass employeeName, totalSales, and SSN as extras
                            Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                            intent.putExtra("ssn", response); // Assuming 'response' contains the SSN
                            // Request to retrieve employee name and total sales using SSN
                            StringRequest infoRequest = new StringRequest(Request.Method.GET, infoUrl, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String s) {
                                    //Log.d("success","Entered onResponse 3"+s);
                                    // Split the response to extract employee name and total sales
                                    String[] parts = s.split(":");
                                    if (parts.length == 2) {
                                        String employeeName = parts[0].trim();
                                        String totalSales = parts[1].trim();
                                        intent.putExtra("employeeName", employeeName);
                                        intent.putExtra("totalSales", totalSales);
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(MainActivity.this, "Invalid response from server", Toast.LENGTH_LONG).show();
                                    }
                                }

                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    // Handle error
                                    Toast.makeText(MainActivity.this, "Error retrieving data from server", Toast.LENGTH_LONG).show();
                                }
                            });

                            // Add the request to the queue
                            queue.add(infoRequest);
                        } else {
                            Toast.makeText(MainActivity.this, "Incorrect Username/Password", Toast.LENGTH_LONG).show();
                        }
                    }

                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                        Toast.makeText(MainActivity.this, "Error logging in", Toast.LENGTH_LONG).show();
                    }
                });

                // Add the login request to the queue
                queue.add(loginRequest);
            }
        });
    }
}
