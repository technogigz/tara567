package com.myTara567.app.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.myTara567.app.R;
import com.myTara567.app.serverApi.controller;

import org.json.JSONException;
import org.json.JSONObject;

public class DepositOnlineActivity extends AppCompatActivity {

    private ImageView ivBack, ivQrCode;
    private TextView tvSteps, tvUpiId;
    private LinearLayout llConfirmPayment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposit_online);

        ivBack = findViewById(R.id.backPassImage);
        ivQrCode = findViewById(R.id.iv_qr_code);
        tvSteps = findViewById(R.id.tv_steps);
        tvUpiId = findViewById(R.id.tv_upi_id);
        llConfirmPayment = findViewById(R.id.ll_confirm_payment);

        //for get data from the api
        getDataPayment();

        llConfirmPayment.setOnClickListener(view -> {
            startActivity(new Intent(DepositOnlineActivity.this, FundRequestAddActivity.class));
        });

        ivBack.setOnClickListener(view -> {
            onBackPressed();
        });
    }

    private void getDataPayment() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, controller.url+"api-payment-data", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("sdfsdf", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean status = jsonObject.getBoolean("status");
                    if (status) {
                        JSONObject jsonObject1 = jsonObject.getJSONObject("payment_data");
                        String step1 = jsonObject1.getString("step1");
                        String step2 = jsonObject1.getString("step2");
                        String step3 = jsonObject1.getString("step3");
                        String step4 = jsonObject1.getString("step4");
                        String upi = jsonObject1.getString("upi");
                        String qrImage = jsonObject1.getString("qr");

                        tvSteps.setText("1) "+step1+"\n2) "+step2+"\n3) "+step3+"\n4) "+step4);
                        tvUpiId.setText("UPI ID - "+upi);

                        Glide.with(DepositOnlineActivity.this)
                                .load(qrImage)
                                .into(ivQrCode);

                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DepositOnlineActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}