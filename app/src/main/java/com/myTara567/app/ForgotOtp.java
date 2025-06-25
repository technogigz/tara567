package com.myTara567.app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.myTara567.app.serverApi.controller;
import com.chaos.view.PinView;
import com.google.gson.JsonObject;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotOtp extends AppCompatActivity {
    ImageView back;
    PinView otpPin;
    Button verifyBtn;
    TextView textView, resendOtp, otpTimer, tvErrorOtp, tvMobile;
    String appKey, forgetOtp, number;
    int wrongOtp = 4;
    int attempt = 4;
    Vibrator vibe;
    private RelativeLayout progressLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.otp);


        SharedPreferences sp = getApplicationContext().getSharedPreferences("app_key", Context.MODE_MULTI_PROCESS);
        appKey = sp.getString("app_key", "");
        Log.d("login appkey", "onCreate: " + appKey);

        forgetOtp = getIntent().getStringExtra("forgetOtp");
        number = getIntent().getStringExtra("number");

       // resendOtp = findViewById(R.id.resendOtp);
        otpTimer = findViewById(R.id.otpTimer);
        otpPin = findViewById(R.id.otp);
        verifyBtn = findViewById(R.id.verifyOtp);
       // tvErrorOtp = findViewById(R.id.tv_error_otp);
        progressLayout = findViewById(R.id.progressLayout);
        //textView = findViewById(R.id.textView2);

       // tvMobile = findViewById(R.id. tv_mobile);
        tvMobile.setText(number);


        otpTimer.setVisibility(View.VISIBLE);
        resendOtp.setVisibility(View.INVISIBLE);
        startTimer();

        /*StringBuilder stringBuilder = new StringBuilder();
        for (int i = 7; i < number.length(); i++) {
            stringBuilder.append(number.charAt(i));
            Log.d("three digits", "onCreate: " + i);
        }
        Log.d("string", "onCreate: " + stringBuilder);
        textView.setText("Verify your phone number by 4-digits code we sent to +91-xxx-xxx-" + stringBuilder);
*/
        otpPin.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                tvErrorOtp.setVisibility(View.GONE);

            }

            @Override
            public void afterTextChanged(Editable s) {
                String phone = otpPin.getText().toString().trim();
                if (phone.length() == 4) {
                    if (phone.equals(forgetOtp)) {

                        Intent i = new Intent(new Intent(getApplicationContext(), NewPassword.class));
                        i.putExtra("phone", number);
                        startActivity(i);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "You have entered wrong OTP, " + attempt + " attempts left", Toast.LENGTH_SHORT).show();
                        if (wrongOtp < 1) {
                            vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                            vibe.vibrate(100);
                            onBackPressed();
                        } else {
                            wrongOtp -= 1;
                            Log.d("wrong", "afterTextChanged: " + wrongOtp);
                            otpPin.getText().clear();
                            vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                            vibe.vibrate(100);
                            attempt -= 1;

                        }
                    }

                }
            }
        });

        verifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Otp = otpPin.getText().toString().trim();
                if (Otp.isEmpty()) {
                    tvErrorOtp.setText("Please enter Otp!");
                    tvErrorOtp.setVisibility(View.VISIBLE);

                    return;
                }
                if (Otp.length() < 4) {
                    tvErrorOtp.setText("Please enter complete Otp!");
                    tvErrorOtp.setVisibility(View.VISIBLE);
                }
            }
        });

        resendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resendOtp.setVisibility(View.INVISIBLE);
                otpTimer.setVisibility(View.VISIBLE);
                progressLayout.setVisibility(View.VISIBLE);
                JsonObject js = new JsonObject();
                js.addProperty("env_type", "Prod");
                js.addProperty("app_key", appKey);
                js.addProperty("mobile", number);

                Call<JsonObject> call = controller.getInstance().getApi().resendOtp(js);
                call.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        progressLayout.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "OTP sent", Toast.LENGTH_SHORT).show();
                        Log.d("resend otp: ", response.body().toString());
                        String resend_otp = response.body().get("otp").toString();
                        forgetOtp = resend_otp;
                        Log.d("onResponse: ", forgetOtp);

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