package com.myTara567.app;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class WebView extends AppCompatActivity {
    android.webkit.WebView webView;
    TextView nameWeb;
    ImageView img;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_view);

        String url = getIntent().getStringExtra("url");
        String name = getIntent().getStringExtra("name");

        webView = findViewById(R.id.webView);
        nameWeb = findViewById(R.id.title);
        img = findViewById(R.id.backBidHistImage);
        pd = ProgressDialog.show(this, "", "Loading....");

        nameWeb.setText(name);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(android.webkit.WebView view, String url, Bitmap favicon) {
                pd.show();
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(android.webkit.WebView view, String url) {
                pd.dismiss();
                super.onPageFinished(view, url);
            }
        });
        Log.d("url", "onCreate: " + url);
        webView.loadUrl(url);

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}