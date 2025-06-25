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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.myTara567.app.serverApi.controller;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewPassword extends AppCompatActivity {
    Button setPassword;
    EditText password, reEnterPass;
    String appKey, number;
    RelativeLayout progressLayout;
    private TextView tvErrorNewPass, tvErrorConfirmPass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_password);


        SharedPreferences sp = getApplicationContext().getSharedPreferences("app_key", Context.MODE_MULTI_PROCESS);
        appKey = sp.getString("app_key", "");
        Log.d("login appkey", "onCreate: " + appKey);

        number = getIntent().getStringExtra("phone");

        password = findViewById(R.id.password);
        reEnterPass = findViewById(R.id.newPassword);
        setPassword = findViewById(R.id.setPassword);
        progressLayout = findViewById(R.id.progressLayout);
        tvErrorNewPass = findViewById(R.id.tv_error_new_pass);
        tvErrorConfirmPass = findViewById(R.id.tv_error_confirm_pass);


        setPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String pass = password.getText().toString().trim();
                String newPass = reEnterPass.getText().toString().trim();

                password.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        tvErrorNewPass.setVisibility(View.GONE);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

                reEnterPass.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        tvErrorConfirmPass.setVisibility(View.GONE);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

                if (pass.isEmpty()) {

                    tvErrorNewPass.setText("Password is required!");
                    tvErrorNewPass.setVisibility(View.VISIBLE);

                    return;
                }
                if (newPass.isEmpty()) {

                    tvErrorConfirmPass.setText("New password is required!");
                    tvErrorConfirmPass.setVisibility(View.VISIBLE);

                    return;
                }
                if (pass.length() < 6) {

                    tvErrorNewPass.setText("Password required at least 6 digits!");
                    tvErrorNewPass.setVisibility(View.VISIBLE);


                    return;
                }
                if (newPass.length() < 6) {

                    tvErrorConfirmPass.setText("New password required at least 6 digits!");
                    tvErrorConfirmPass.setVisibility(View.VISIBLE);

                    return;
                }
                if (pass.equals(newPass)) {

                    progressLayout.setVisibility(View.VISIBLE);

                    JsonObject js = new JsonObject();
                    js.addProperty("env_type", "Prod");
                    js.addProperty("app_key", appKey);
                    js.addProperty("mobile", number);
                    js.addProperty("new_pass", newPass);

                    Log.d("new pass", js.toString());

                    Call<JsonObject> call = controller.getInstance().getApi().forgetPassword(js);
                    call.enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                            progressLayout.setVisibility(View.GONE);
                            Log.d("forget check new pass ", response.body().toString());
                            Boolean status = response.body().get("status").getAsBoolean();
                            String msg = response.body().get("msg").getAsString();
                            if (status.equals(true)) {
                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), Login.class));
                                finishAffinity();
                            } else {
                                Toast.makeText(getApplicationContext(), "Try again later...", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<JsonObject> call, Throwable t) {
                            progressLayout.setVisibility(View.GONE);
                            Log.d("forget check failure ", t.toString());
                            Toast.makeText(getApplicationContext(), "Something went wrong.... Try again later", Toast.LENGTH_SHORT).show();
                        }
                    });


                } else {
                    Toast.makeText(getApplicationContext(), "Passwords does not match", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}