package com.example.login;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class Transfer extends AppCompatActivity {

    private EditText destinationIdEditText;
    private EditText amountEditText;
    private Button transferButton;
    private WebView webView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);

        // Initialize views
        destinationIdEditText = findViewById(R.id.editTextText4);
        amountEditText = findViewById(R.id.editTextText6);
        transferButton = findViewById(R.id.button8);
        webView = findViewById(R.id.webViewtransfer);

        // Set WebViewClient to handle errors
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                // Handle error
                Toast.makeText(Transfer.this, "Failed to load page", Toast.LENGTH_SHORT).show();
            }
        });

        // Handle transfer button click
        transferButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String destinationSsn = destinationIdEditText.getText().toString().trim();
                String amountString = amountEditText.getText().toString().trim();

                if (destinationSsn.isEmpty() || amountString.isEmpty()) {
                    Toast.makeText(Transfer.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    int amount = Integer.parseInt(amountString);

                    // Construct the transfer URL
                    String ssn = getIntent().getStringExtra("ssn");
                    String transferUrl = String.format(Locale.US, "http://10.0.2.2:8080/sleepyhollow/Transfer.jsp?ssn1=%s&ssn2=%s&amount=%d",
                            ssn, destinationSsn, amount);

                    // Load the URL into the WebView
                    webView.loadUrl(transferUrl);
                    Toast.makeText(Transfer.this, "Transaction executed successfully", Toast.LENGTH_SHORT).show();
                } catch (NumberFormatException e) {
                    Toast.makeText(Transfer.this, "Invalid amount", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
