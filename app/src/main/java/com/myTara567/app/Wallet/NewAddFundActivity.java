package com.myTara567.app.Wallet;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import com.myTara567.app.R;

import java.io.ByteArrayOutputStream;

public class NewAddFundActivity extends AppCompatActivity {

    WebView mWebView;
    Context context;
    String TAG = "MainActivity";
    private Intent intent = new Intent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_add_fund);
        if (0 != (getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE)) {
            WebView.setWebContentsDebuggingEnabled(true);
        }

        context = this;
        mWebView = (WebView) findViewById(R.id.payment_webview);
        initWebView();

        // 👍 Call the Create Order API from your server and you will get the Payment URL.
        // 🚫 Do not Call UpiGetway API in Android App Directly
        String PAYMENT_URL = "https://xxxxxxxx.in/order/payment/45feb849dc05f910fc0aee992fd5be82";
        mWebView.loadUrl(PAYMENT_URL);
    }

    @SuppressLint({ "SetJavaScriptEnabled" })
    private void initWebView() {
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setSupportMultipleWindows(true);
        mWebView.getSettings().setUserAgentString("Mozilla/5.0 (Linux; Android 4.4.4; One Build/KTU84L.H4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/36.0.1985.135 Mobile Safari/537.36");
        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.addJavascriptInterface(new WebviewInterface(), "AndroidInterface");
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("upi:")) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    startActivity(intent);
                    return true;
                }
                view.loadUrl(url);
                return true;
            }
        });
    }

    public class WebviewInterface {

        @JavascriptInterface
        public void paymentResponse(String txnStatus, String orderId, String txnId) {
            Log.i(TAG, txnStatus);
            Log.i(TAG, orderId);
            Log.i(TAG, txnId);
            // this function is called when payment is done (txnStatus, orderId ,txnId).
            // You must call the check status API in server and get update about payment.
            // 🚫 Do not Call Order Create API in Android App Directly.
            Toast.makeText(context, "Status: "+txnStatus+", Order ID: "+orderId+", Txn ID: "+txnId, Toast.LENGTH_SHORT).show();
            // Close the Webview.
        }

        @JavascriptInterface
        public void paymentError(String errorMessage) {
            Log.i(TAG, errorMessage);
            // this function is called when payment is error (errorMessage).
            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
            // Close the Webview.
        }



        //call shareImage function in ur webview page
        @JavascriptInterface
        public void shareImage(String imageDataURL) {
            // Implement logic to share the image
            // For example, you can use an Intent to share the image URL
            String imageData = imageDataURL.replace("data:image/png;base64,", "");

            // Convert Base64 string to byte array
            byte[] decodedString = Base64.decode(imageData, Base64.DEFAULT);

            // Convert byte array to Bitmap
            Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

            // Share the image using an Intent
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("image/png");

            // Add the image as an extra to the share Intent
            shareIntent.putExtra(Intent.EXTRA_STREAM, getBitmapUri(bitmap));

            // Launch the share Intent
            startActivity(Intent.createChooser(shareIntent, "Share QR Code"));

        }

        private Uri getBitmapUri(Bitmap bitmap) {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, bytes);
            String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "QR_Code", null);
            return Uri.parse(path);
        }

    }


}