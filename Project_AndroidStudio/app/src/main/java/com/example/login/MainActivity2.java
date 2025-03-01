package com.example.login;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        // Retrieve employee name, SSN, and total sales from Intent extras
        String employeeName = getIntent().getStringExtra("employeeName");
        String ssn = getIntent().getStringExtra("ssn");
        String totalSales = getIntent().getStringExtra("totalSales");


        // Set employee name in TextView
        TextView textView4 = findViewById(R.id.textView4);
        textView4.setText(employeeName);

        // Set total sales in TextView
        TextView textView5 = findViewById(R.id.textView5);
        textView5.setText("$" + totalSales);

        // Ensure SSN is not null before proceeding
        if (ssn != null && !ssn.isEmpty()) {
            // Construct the image URL using the last 4 digits of SSN
            String imageId = ssn.substring(ssn.length() - 4);
            String imageUrl = "http://10.0.2.2:8080/sleepyhollow/img/" + imageId + ".jpg";

            // Load image into ImageView using AsyncTask
            ImageView imageView = findViewById(R.id.imageView);
            new LoadImageTask(imageView).execute(imageUrl);
        } else {
            // Handle case where SSN is null or empty
            Toast.makeText(this, "SSN is null or empty", Toast.LENGTH_SHORT).show();
        }
        // Button to open transaction.jsp
        Button transactionButton = findViewById(R.id.button2);
        transactionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start TransDetails activity
                Intent intent = new Intent(MainActivity2.this, Transactions.class);
                intent.putExtra("ssn", ssn); // Pass SSN to TransDetails activity
                startActivity(intent);
            }
        });

        // Button to open TransactionDetails.jsp
        Button transdt = findViewById(R.id.button3);
        transdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start TransDetails activity
                Intent intent = new Intent(MainActivity2.this, TransactionDetails.class);
                intent.putExtra("ssn", ssn); // Pass SSN to TransDetails activity
                startActivity(intent);
            }
        });

        // Button to open Award Details
        Button awarddet = findViewById(R.id.button5);
        awarddet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start TransDetails activity
                Intent intent = new Intent(MainActivity2.this, AwardDetails.class);
                intent.putExtra("ssn", ssn); // Pass SSN to TransDetails activity
                startActivity(intent);
            }
        });

        // Button to execute Tranfer
        Button transferbut = findViewById(R.id.button6);
        transferbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start TransDetails activity
                Intent intent = new Intent(MainActivity2.this, Transfer.class);
                intent.putExtra("ssn", ssn); // Pass SSN to TransDetails activity
                startActivity(intent);
            }
        });

        // Button to execute exit
        Button exitbut = findViewById(R.id.button7);
        exitbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start TransDetails activity
                Intent intent = new Intent(MainActivity2.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }

    private static class LoadImageTask extends AsyncTask<String, Void, Bitmap> {
        private final ImageView imageView;

        LoadImageTask(ImageView imageView) {
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(String... urls) {
            String imageUrl = urls[0];
            Bitmap bitmap = null;
            try {
                URL url = new URL(imageUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                bitmap = BitmapFactory.decodeStream(input);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            if (result != null) {
                imageView.setImageBitmap(result);
            }
        }
    }
}
