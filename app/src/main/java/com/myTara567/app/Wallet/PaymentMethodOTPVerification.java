package com.myTara567.app.Wallet;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.myTara567.app.R;
import com.myTara567.app.serverApi.controller;
import com.chaos.view.PinView;
import com.google.gson.JsonObject;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentMethodOTPVerification extends AppCompatActivity {
    Button verifyBtn;
    TextView textView, resendOtp, otpTimer,tvMobile;
    PinView otpPin;
    String phone, appKey, unique, otp, paymentMethod, upiNumber;
    String otp_pin;
    int wrongOtp = 4;
    int attempt = 4;
    Vibrator vibe;
    JsonObject mainObject = new JsonObject();
    RelativeLayout progressLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.otp);

        appKey = getSharedPreferences("app_key", Context.MODE_MULTI_PROCESS).getString("app_key", "");
        Log.d("login appkey", "onCreate: " + appKey);

        unique = getApplicationContext().getSharedPreferences("unique_token", Context.MODE_MULTI_PROCESS).getString("unique_token", "");
        Log.d("signUp editor", "home: " + unique);

        phone = getApplicationContext().getSharedPreferences("user_name", Context.MODE_MULTI_PROCESS).getString("phone", "");
        Log.d("signUp editor", "home: " + phone);


        otp = getIntent().getStringExtra("otp");
        paymentMethod = getIntent().getStringExtra("paymentMethod");
        upiNumber = getIntent().getStringExtra("upiNumber");

        mainObject.addProperty("env_type", "Prod");
        mainObject.addProperty("app_key", appKey);
        mainObject.addProperty("unique_token", unique);

        progressLayout = findViewById(R.id.progressLayout);

        otpPin = findViewById(R.id.otp);
        otpTimer = findViewById(R.id.otpTimer);
       // resendOtp = findViewById(R.id.resendOtp);
        textView = findViewById(R.id.textView2);

        //tvMobile = findViewById(R.id.tv_mobile);
        tvMobile.setText(phone);

        otpTimer.setVisibility(View.VISIBLE);
        resendOtp.setVisibility(View.INVISIBLE);
        startTimer();

      /*  StringBuilder stringBuilder = new StringBuilder();
        for (int i = 7; i < phone.length(); i++) {
            stringBuilder.append(phone.charAt(i));
            Log.d("three digits", "onCreate: " + i);
        }
        Log.d("string", "onCreate: " + stringBuilder);
        textView.setText("Verify your phone number by 4-digits code we sent to +91-xxx-xxx-" + stringBuilder);
*/
        verifyBtn = findViewById(R.id.verifyOtp);
        verifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (otpPin.getText().toString().trim().equals("")) {
                    Toast.makeText(getApplicationContext(), "Please enter Otp", Toast.LENGTH_SHORT).show();
                } else if (otpPin.getText().toString().trim().length() < 4) {
                    Toast.makeText(getApplicationContext(), "Please enter completel Otp", Toast.LENGTH_SHORT).show();
                }
            }
        });

        otpPin.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {

                otp_pin = otpPin.getText().toString().trim();

                if (otp_pin.length() == 4) {
                    Log.d("otp", "onCreate: " + otp_pin.length());
                    if (otp_pin.equals(otp)) {
                        mainObject.addProperty("upi_type", paymentMethod);
                        if (paymentMethod.equals("1")) {
                            mainObject.addProperty("paytm_no", upiNumber);
                            mainObject.addProperty("google_pay_no", "");
                            mainObject.addProperty("phon_pay_no", "");

                        } else if (paymentMethod.equals("2")) {
                            mainObject.addProperty("paytm_no", "");
                            mainObject.addProperty("google_pay_no", upiNumber);
                            mainObject.addProperty("phon_pay_no", "");

                        } else if (paymentMethod.equals("3")) {
                            mainObject.addProperty("paytm_no", "");
                            mainObject.addProperty("google_pay_no", "");
                            mainObject.addProperty("phon_pay_no", upiNumber);

                        }

                        progressLayout.setVisibility(View.VISIBLE);
                        Call<JsonObject> call = controller.getInstance().getApi().addUserUpiDetails(mainObject);
                        call.enqueue(new Callback<JsonObject>() {
                            @Override
                            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                progressLayout.setVisibility(View.GONE);
                                Toast.makeText(getApplicationContext(), response.body().get("msg").getAsString(), Toast.LENGTH_SHORT).show();
                                finish();
                            }

                            @Override
                            public void onFailure(Call<JsonObject> call, Throwable t) {
                                progressLayout.setVisibility(View.GONE);
                                Toast.makeText(getApplicationContext(), "Something went wrong.... Try again later", Toast.LENGTH_SHORT).show();
                            }
                        });

                    } else {
                        Toast.makeText(getApplicationContext(), "You have entered wrong OTP, " + attempt + " attempts left", Toast.LENGTH_SHORT).show();
                        otpPin.getText().clear();
                        if (wrongOtp < 1) {
                            vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                            vibe.vibrate(100);
                            onBackPressed();
                        } else {
                            wrongOtp -= 1;
                            otpPin.getText().clear();
                            vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                            vibe.vibrate(100);
                            attempt -= 1;

                        }
                    }
                }
            }
        });

        resendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resendOtp.setVisibility(View.INVISIBLE);
                otpTimer.setVisibility(View.VISIBLE);

                progressLayout.setVisibility(View.VISIBLE);
                Call<JsonObject> call = controller.getInstance().getApi().validateBank(mainObject);
                call.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        progressLayout.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "OTP sent", Toast.LENGTH_SHORT).show();
                        Log.d("resend otp: ", response.body().toString());
                        String resend_otp = response.body().get("otp").toString();
                        otp = resend_otp;
                        Log.d("onResponse: ", otp);
                        startTimer();
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        progressLayout.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "Something went wrong.... Try again later", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }

    private void startTimer() {
        new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {
                String timer = String.format(Locale.ENGLISH, "Resend otp in: " + "%02d : %02d",
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
                ;
                otpTimer.setText(timer);
                //here you can have your logic to set text to edittext
            }

            public void onFinish() {
                otpTimer.setText("");
                resendOtp.setVisibility(View.VISIBLE);
                otpTimer.setVisibility(View.INVISIBLE);
            }

        }.start();
    }
}