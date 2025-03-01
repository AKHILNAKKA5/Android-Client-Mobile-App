package com.example.login;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class AwardDetails extends AppCompatActivity {

    private Spinner spinner;
    private WebView webView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_award_details);

        spinner = findViewById(R.id.awardspinner);
        webView = findViewById(R.id.awarddetailsview);

        String ssn = getIntent().getStringExtra("ssn");
        new FetchAwardIdsTask().execute("http://10.0.2.2:8080/sleepyhollow/AwardIds.jsp?ssn=" + ssn);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.awardview), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        webView.setWebViewClient(new WebViewClient());
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, android.view.View view, int position, long id) {
                String selectedAwardId = (String) parent.getItemAtPosition(position);
                String awardDetailsUrl = "http://10.0.2.2:8080/sleepyhollow/GrantedDetails.jsp?awardid=" + selectedAwardId + "&ssn=" + ssn;
                webView.loadUrl(awardDetailsUrl);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle when no item is selected
            }
        });
    }

    private void populateSpinner(ArrayList<String> awardIds) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, awardIds);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private class FetchAwardIdsTask extends AsyncTask<String, Void, ArrayList<String>> {
        @Override
        protected ArrayList<String> doInBackground(String... urls) {
            ArrayList<String> txnIds = new ArrayList<>();
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    InputStream inputStream = connection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line);
                    }
                    String response = stringBuilder.toString();

                    // Manually parse HTML response to extract Txn_ID values
                    int startIndex = response.indexOf("<table");
                    int endIndex = response.indexOf("</table>", startIndex);
                    String tableHtml = response.substring(startIndex, endIndex);

                    // Find all table rows
                    int trIndex = 0;
                    while ((trIndex = tableHtml.indexOf("<tr", trIndex)) != -1) {
                        int endTrIndex = tableHtml.indexOf("</tr>", trIndex);
                        String rowHtml = tableHtml.substring(trIndex, endTrIndex);

                        // Find all table cells in the row
                        int tdIndex = 0;
                        while ((tdIndex = rowHtml.indexOf("<td", tdIndex)) != -1) {
                            int endTdIndex = rowHtml.indexOf("</td>", tdIndex);
                            String cellHtml = rowHtml.substring(tdIndex, endTdIndex);
                            String cellText = cellHtml.replaceAll("\\<.*?\\>", "").trim(); // Extract text from cell HTML
                            txnIds.add(cellText); // Add text to txnIds list
                            tdIndex = endTdIndex + 1;
                        }

                        trIndex = endTrIndex + 1;
                    }

                    bufferedReader.close();
                    inputStream.close();
                } else {
                    Log.e("FetchAwardIdsTask", "HTTP error code: " + responseCode);
                }
                connection.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return txnIds;
        }

        @Override
        protected void onPostExecute(ArrayList<String> txnIds) {
            if (txnIds.isEmpty()) {
                Toast.makeText(AwardDetails.this, "No Award IDs found", Toast.LENGTH_SHORT).show();
            } else {
                populateSpinner(txnIds);
            }
        }


    }
    }
