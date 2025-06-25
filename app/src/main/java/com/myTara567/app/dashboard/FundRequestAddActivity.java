package com.myTara567.app.dashboard;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonObject;
import com.myTara567.app.R;
import com.myTara567.app.home.HomePageActivity;
import com.myTara567.app.serverApi.controller;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FundRequestAddActivity extends AppCompatActivity {

    EditText oldPassword, newPassword;
    ImageView backImg;
    Button change;
    TextView tvErrorOldPass, tvErrorNewPass;
    String appKey, unique_token;
    private RelativeLayout progressLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fund_request_add);

        //app key
        appKey = getApplicationContext().getSharedPreferences("app_key", MODE_MULTI_PROCESS)
                .getString("app_key", null);
        Log.d("signUp appkey", "onCreate: " + appKey);

        //unique token
        unique_token = getApplicationContext().getSharedPreferences("unique_token", Context.MODE_MULTI_PROCESS)
                .getString("unique_token", "");
        Log.d("signUp editor", "home: " + unique_token);

        oldPassword = findViewById(R.id.oldPassword);
        newPassword = findViewById(R.id.newPassword);
        change = findViewById(R.id.submitBtn);
        backImg = findViewById(R.id.backPassImage);
        tvErrorOldPass = findViewById(R.id.tv_error_old_pass);
        tvErrorNewPass = findViewById(R.id.tv_error_new_pass);
        progressLayout = findViewById(R.id.progressLayout);

        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldPass = oldPassword.getText().toString().trim();
                String newPass = newPassword.getText().toString().trim();

                oldPassword.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        tvErrorOldPass.setVisibility(View.GONE);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

                newPassword.addTextChangedListener(new TextWatcher() {
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



                if (oldPass.isEmpty()) {
                    tvErrorOldPass.setText("Please enter payment UTR number!");
                    tvErrorOldPass.setVisibility(View.VISIBLE);
                    return;
                } else if (newPass.isEmpty()) {
                    tvErrorNewPass.setText("Please enter amount!");
                    tvErrorNewPass.setVisibility(View.VISIBLE);
                    return;
                }  else {
                    progressLayout.setVisibility(View.VISIBLE);
                    JsonObject js = new JsonObject();
                    js.addProperty("env_type", "Prod");
                    js.addProperty("app_key", appKey);
                    js.addProperty("unique_token", unique_token);
                    js.addProperty("utr_number", oldPass);
                    js.addProperty("request_amount", newPass);


                    Call<JsonObject> jsonObjectCall = controller.getInstance().getApi().api_fund_request_add(js);
                    jsonObjectCall.enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                            Log.d("sdfsdf", response.body().toString());
                            progressLayout.setVisibility(View.GONE);
                            boolean status = response.body().get("status").getAsBoolean();
                            String message = response.body().get("msg").getAsString();
                            if (!status) {
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                            } else {
                                //Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                                /*oldPassword.getText().clear();
                                newPassword.getText().clear();*/

                                View v = LayoutInflater.from(FundRequestAddActivity.this).inflate(R.layout.dialog_fund_request_layout, null);
                                android.app.AlertDialog.Builder alertDialogBuilder = new
                                        android.app.AlertDialog.Builder(FundRequestAddActivity.this);

                                alertDialogBuilder.setView(v);
                                alertDialogBuilder.setCancelable(false);
                                AlertDialog alertDialog = alertDialogBuilder.create();
                                alertDialog.show();
                                alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

                                TextView tvMessage = v.findViewById(R.id.tv_message);
                                tvMessage.setText(message);

                                Button submit = v.findViewById(R.id.submitBtn);
                                submit.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        alertDialog.dismiss();
                                        startActivity(new Intent(FundRequestAddActivity.this, HomePageActivity.class));
                                        finishAffinity();
                                    }
                                });

                            }
                        }

                        @Override
                        public void onFailure(Call<JsonObject> call, Throwable t) {
                            progressLayout.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), "Something went wrong.... Try again later", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
}