package com.myTara567.app.dashboard;

import android.content.Context;
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

import com.myTara567.app.R;
import com.myTara567.app.serverApi.controller;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Change_password extends AppCompatActivity {
    EditText oldPassword, newPassword, confirmPassword;
    ImageView backImg;
    Button change;
    TextView  tvErrorOldPass, tvErrorNewPass, tvErrorConfirmPass;
    String appKey, unique_token;
    private RelativeLayout progressLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password);

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
        confirmPassword = findViewById(R.id.confirmPassword);
        //pencil = findViewById(R.id.pencil);
        change = findViewById(R.id.submitBtn);
        backImg = findViewById(R.id.backPassImage);
        tvErrorOldPass = findViewById(R.id.tv_error_old_pass);
        tvErrorNewPass = findViewById(R.id.tv_error_new_pass);
        tvErrorConfirmPass = findViewById(R.id.tv_error_confirm_pass);
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
                String confirmNewPass = confirmPassword.getText().toString().trim();

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

                confirmPassword.addTextChangedListener(new TextWatcher() {
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






                if (oldPass.isEmpty()) {
                    tvErrorOldPass.setText("Empty field is not acceptable!");
                    tvErrorOldPass.setVisibility(View.VISIBLE);
                    return;
                } else if (oldPass.length() < 6) {
                    tvErrorOldPass.setText("Old password required at least 6 digits!");
                    tvErrorOldPass.setVisibility(View.VISIBLE);
                    return;
                } else if (newPass.isEmpty()) {
                    tvErrorNewPass.setText("Empty field is not acceptable!");
                    tvErrorNewPass.setVisibility(View.VISIBLE);
                    return;
                } else if (newPass.length() < 6) {
                    tvErrorNewPass.setText("New password required at least 6 digits!");
                    tvErrorNewPass.setVisibility(View.VISIBLE);
                    return;
                } else if (confirmNewPass.isEmpty()) {
                    tvErrorConfirmPass.setText("Empty field is not acceptable!");
                    tvErrorConfirmPass.setVisibility(View.VISIBLE);

                    return;
                } else if (confirmNewPass.length() < 6) {
                    tvErrorConfirmPass.setText("Confirm password required at least 6 digits!");
                    tvErrorConfirmPass.setVisibility(View.VISIBLE);

                    return;
                } else if (!newPass.equals(confirmNewPass)) {
                    tvErrorConfirmPass.setText("Passwords are not matching!");
                    tvErrorConfirmPass.setVisibility(View.VISIBLE);

                    tvErrorNewPass.setText("Passwords are not matching!");
                    tvErrorNewPass.setVisibility(View.VISIBLE);
                    return;
                } else if (oldPass.equals(confirmNewPass)) {
                    tvErrorConfirmPass.setText("The new password would not be the same as the old password!");
                    tvErrorConfirmPass.setVisibility(View.VISIBLE);


                    return;
                } else {
                    progressLayout.setVisibility(View.VISIBLE);
                    JsonObject js = new JsonObject();
                    js.addProperty("env_type", "Prod");
                    js.addProperty("app_key", appKey);
                    js.addProperty("unique_token", unique_token);
                    js.addProperty("old_pass", oldPass);
                    js.addProperty("new_pass", confirmNewPass);

                    Call<JsonObject> jsonObjectCall = controller.getInstance().getApi().change_password(js);
                    jsonObjectCall.enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                            progressLayout.setVisibility(View.GONE);
                            boolean status = response.body().get("status").getAsBoolean();
                            String message = response.body().get("msg").getAsString();
                            if (!status) {
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                                oldPassword.getText().clear();
                                newPassword.getText().clear();
                                confirmPassword.getText().clear();
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