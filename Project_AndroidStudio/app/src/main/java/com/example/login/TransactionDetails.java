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

public class TransactionDetails extends AppCompatActivity {

    private Spinner spinner;
    private WebView webView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_transaction_details);

        spinner = findViewById(R.id.transspinner);
        webView = findViewById(R.id.webView2);

        // Fetch data from the server
        String ssn = getIntent().getStringExtra("ssn"); // Replace with the actual SSN
        Log.d("Fetchedssn", "Response from server: " + ssn);
        new FetchTransactionIdsTask().execute("http://10.0.2.2:8080/sleepyhollow/OnlyTransactionId.jsp?ssn=" + ssn);

        // Apply insets to handle system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.awardview), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Set up WebView
        webView.setWebViewClient(new WebViewClient());
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        // Set listener for spinner item selection
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, android.view.View view, int position, long id) {
                // Get the selected item (transaction ID)
                String selectedTxnId = (String) parent.getItemAtPosition(position);
                // Construct the URL for fetching transaction details based on the selected transaction ID
                String transactionDetailsUrl = "http://10.0.2.2:8080/sleepyhollow/TransactionDetails.jsp?txnid=" + selectedTxnId;
                // Load the URL in the WebView
                webView.loadUrl(transactionDetailsUrl);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle when no item is selected (optional)
            }
        });
    }

    private void populateSpinner(ArrayList<String> txnIds) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, txnIds);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private class FetchTransactionIdsTask extends AsyncTask<String, Void, ArrayList<String>> {

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
                    Log.e("FetchTransactionIdsTask", "HTTP error code: " + responseCode);
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
                Toast.makeText(TransactionDetails.this, "No transaction IDs found", Toast.LENGTH_SHORT).show();
            } else {
                populateSpinner(txnIds);
            }
        }
    }

}