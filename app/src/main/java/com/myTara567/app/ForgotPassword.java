package com.myTara567.app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.myTara567.app.serverApi.controller;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPassword extends AppCompatActivity {
    ImageView backImg;
    Button verifyBtn;
    EditText forgetPhone;
    String appKey;
    Boolean apiCall = true;
    private TextView tvErrorPhone;
    private RelativeLayout progressLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password);


        SharedPreferences sp = getApplicationContext().getSharedPreferences("app_key", Context.MODE_MULTI_PROCESS);
        appKey = sp.getString("app_key", "");
        Log.d("login appkey", "onCreate: " + appKey);


        progressLayout = findViewById(R.id.progressLayout);
        tvErrorPhone = findViewById(R.id.tv_error_phone);
        forgetPhone = findViewById(R.id.forgetPhone);
        verifyBtn = findViewById(R.id.forgetVerifyButton);


        verifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = forgetPhone.getText().toString().trim();

                forgetPhone.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                        tvErrorPhone.setVisibility(View.GONE);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });





                if (phone.isEmpty()) {

                    tvErrorPhone.setText("Phone number is required!");
                    tvErrorPhone.setVisibility(View.VISIBLE);

                    return;
                } else if (phone.length() != 10) {

                    tvErrorPhone.setText("Phone number must be 10 digits!!");
                    tvErrorPhone.setVisibility(View.VISIBLE);

                    return;
                }  else {

                    progressLayout.setVisibility(View.VISIBLE);
                    JsonObject js = new JsonObject();
                    js.addProperty("env_type", "Prod");
                    js.addProperty("app_key", appKey);
                    js.addProperty("mobile", phone);

                    Call<JsonObject> call = controller.getInstance().getApi().forgetUser(js);
                    call.enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                            progressLayout.setVisibility(View.GONE);
                            Log.d("forget check ", response.body().toString());
                            Boolean message = response.body().get("status").getAsBoolean();

                            if (message.equals(true)) {
                                if (apiCall) {
                                    String otp = response.body().get("otp").getAsString();
                                    Intent i = new Intent(getApplicationContext(), ForgotOtp.class);
                                    i.putExtra("forgetOtp", otp);
                                    i.putExtra("number", phone);
                                    startActivity(i);
                                }
                                //apiCall = false;
                            } else {
                                String msg = response.body().get("msg").getAsString();
                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<JsonObject> call, Throwable t) {
                            progressLayout.setVisibility(View.GONE);
                            Log.d("forget check failure ", t.toString());
                            Toast.makeText(getApplicationContext(), "Something went wrong.... Try again later", Toast.LENGTH_SHORT).show();
                        }
                    });
                    return;
                }
            }
        });
    }
}