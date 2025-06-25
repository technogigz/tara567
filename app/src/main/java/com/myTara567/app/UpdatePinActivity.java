package com.myTara567.app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.gson.JsonObject;
import com.myTara567.app.serverApi.controller;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdatePinActivity extends AppCompatActivity {

    MaterialButton mbUpdate;
    TextView tvErrorMpin;
    EditText mPin;
    private RelativeLayout progressLayout;

    SharedPreferences spUnique_token;
    Boolean login = true;

    String digit, appKey, unique_token, loginMobile;
    String checkActivity;
    JsonObject js;
    boolean checkmPin = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_pin);

        //app key
        appKey = getApplicationContext().getSharedPreferences("app_key", MODE_MULTI_PROCESS)
                .getString("app_key", null);
        Log.d("signUp appkey", "onCreate: " + appKey);

        //unique token
        spUnique_token = getApplicationContext().getSharedPreferences("unique_token", Context.MODE_MULTI_PROCESS);

        unique_token = getApplicationContext().getSharedPreferences("unique_token", Context.MODE_MULTI_PROCESS)
                .getString("unique_token", "");
        Log.d("signUp editor", "home: " + unique_token);

        //Login mobile number
        loginMobile = getApplicationContext().getSharedPreferences("user_name", Context.MODE_MULTI_PROCESS)
                .getString("phone", "");
        Log.d("updated_pin", "Login mobile number: " + loginMobile);

        mbUpdate = findViewById(R.id.mb_submit);
        tvErrorMpin = findViewById(R.id.tv_error_mpin);
        mPin = findViewById(R.id.mPin);
        progressLayout = findViewById(R.id.progressLayout);

        mbUpdate.setOnClickListener(v -> {
            if (validation()) {
                updatedPin();
            }
        });

    }

    private void updatedPin() {

        progressLayout.setVisibility(View.VISIBLE);
        js = new JsonObject();
        js.addProperty("env_type", "Prod");
        js.addProperty("app_key", appKey);
        js.addProperty("mobile", loginMobile);
        js.addProperty("security_pin", mPin.getText().toString().trim());

        Call<JsonObject> call = controller.getInstance().getApi().updateMPIN(js);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                progressLayout.setVisibility(View.GONE);
                boolean status = response.body().get("status").getAsBoolean();
                String message = response.body().get("msg").getAsString();
                Log.d("updated_pin", "onResponse: " + response.body().toString());

                if (status) {
                    Toast.makeText(UpdatePinActivity.this, message, Toast.LENGTH_SHORT).show();
                    onBackPressed();
                } else {
                    Toast.makeText(UpdatePinActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("updated_pin", "onResponse: fail = " + t);
                progressLayout.setVisibility(View.GONE);
                Toast.makeText(UpdatePinActivity.this, "Something went wrong...try again!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private boolean validation() {

        String MPin = mPin.getText().toString().trim();

        mPin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                tvErrorMpin.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        if (MPin.isEmpty()) {

            tvErrorMpin.setText("Please create new PIN!");
            tvErrorMpin.setVisibility(View.VISIBLE);

            return false;
        } else if (MPin.length() != 4) {

            tvErrorMpin.setText("Pin Required at least 4 digits!");
            tvErrorMpin.setVisibility(View.VISIBLE);

            return false;
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}