package com.myTara567.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.myTara567.app.Wallet.PaymentMethodOTPVerification;
import com.myTara567.app.serverApi.controller;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManagePhonePeActivity extends AppCompatActivity {

    private ImageView ivBack;
    JsonObject mainObject;
    String paytmNum = "";
    String gPayNum = "";
    String phonePeNum = "";
    String appKey, unique;
    MaterialButton btnSubmit;
    TextInputEditText edtMobile;
    RelativeLayout progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_phone_pe);


        ivBack = findViewById(R.id.gamesBackImageView);
        btnSubmit = findViewById(R.id.submitBtn);
        edtMobile = findViewById(R.id.mobile);
        progressBar = findViewById(R.id.progressLayout);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        appKey = getSharedPreferences("app_key", Context.MODE_MULTI_PROCESS).getString("app_key", "");
        Log.d("login appkey", "onCreate: " + appKey);

        unique = getApplicationContext().getSharedPreferences("unique_token", Context.MODE_MULTI_PROCESS).getString("unique_token", "");
        Log.d("signUp editor", "home: " + unique);

        mainObject = new JsonObject();
        mainObject.addProperty("env_type", "Prod");
        mainObject.addProperty("app_key", appKey);
        mainObject.addProperty("unique_token", unique);


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (edtMobile.getText().toString().trim().equals("")) {
                    Toast.makeText(getApplicationContext(), "Please enter number.", Toast.LENGTH_SHORT).show();
                } else if (edtMobile.getText().toString().trim().contains(".") || edtMobile.getText().toString().trim().contains(",")
                        || edtMobile.getText().toString().trim().contains("-") || edtMobile.getText().toString().trim().length() < 10) {
                    Toast.makeText(getApplicationContext(), "Please enter a valid number.", Toast.LENGTH_SHORT).show();
                } else if (edtMobile.getText().toString().trim().equals(phonePeNum)) {
                    Toast.makeText(getApplicationContext(), "You haven't made any changes.\n" +
                            "Please make some changes.", Toast.LENGTH_SHORT).show();
                } else {

                    progressBar.setVisibility(View.VISIBLE);
                    btnSubmit.setEnabled(false);
                    Call<JsonObject> call = controller.getInstance().getApi().validateBank(mainObject);
                    call.enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                            progressBar.setVisibility(View.GONE);
                            btnSubmit.setEnabled(true);
                            if (response.body().get("status").getAsBoolean()) {
                                Intent i = new Intent(getApplicationContext(), PaymentMethodOTPVerification.class);
                                i.putExtra("upiNumber", edtMobile.getText().toString().trim());
                                i.putExtra("paymentMethod", "3");
                                i.putExtra("otp", response.body().get("otp").getAsString());
                                startActivity(i);
                            }
                        }

                        @Override
                        public void onFailure(Call<JsonObject> call, Throwable t) {
                            progressBar.setVisibility(View.GONE);
                            btnSubmit.setEnabled(true);
                            Toast.makeText(getApplicationContext(), "Something want wrong. Try again later...", Toast.LENGTH_SHORT).show();
                        }

                    });
                }



            }
        });

    }

    private void setPaymentMethodNumber() {
        Call<JsonObject> call = controller.getInstance().getApi().userPaymentDetails(mainObject);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                boolean status = response.body().get("status").getAsBoolean();
                JsonArray jsonArray = response.body().get("payment_details").getAsJsonArray();

                if (jsonArray.size() > 0) {
                    if (status) {
                        for (int i = 0; i < jsonArray.size(); i++) {

                            JsonObject paymentObj = jsonArray.get(i).getAsJsonObject();
                            paytmNum = paymentObj.get("paytm_number").getAsString();
                            gPayNum = paymentObj.get("google_pay_number").getAsString();
                            phonePeNum = paymentObj.get("phone_pay_number").getAsString();
                            edtMobile.setText(phonePeNum);
                        }
                    }
                } else {

                    paytmNum = "N/A";
                    gPayNum = "N/A";
                    phonePeNum = "N/A";
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Something went wrong.... Try again later", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setPaymentMethodNumber();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}