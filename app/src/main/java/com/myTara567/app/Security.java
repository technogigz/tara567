package com.myTara567.app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.myTara567.app.home.HomePageActivity;
import com.myTara567.app.serverApi.controller;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Security extends AppCompatActivity {
    SharedPreferences spUnique_token;
    Boolean login = true;
    //PassCodeView passCodeView;
    String digit, appKey, unique_token;
    String checkActivity;
    int wrongSecurity = 4;
    int attempt = 4;
    JsonObject js;
    boolean checkmPin = true;
    String resetPinNumber;
    LinearLayout resetPinLayout;
    TextView resetPinNumberText;
    Vibrator vibe;
    RelativeLayout progressLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.security);

        vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        //app key
        appKey = getApplicationContext().getSharedPreferences("app_key", MODE_MULTI_PROCESS)
                .getString("app_key", null);
        Log.d("signUp appkey", "onCreate: " + appKey);

        //unique token
        spUnique_token = getApplicationContext().getSharedPreferences("unique_token", Context.MODE_MULTI_PROCESS);

        unique_token = getApplicationContext().getSharedPreferences("unique_token", Context.MODE_MULTI_PROCESS)
                .getString("unique_token", "");
        Log.d("signUp editor", "home: " + unique_token);

        //resetPin number
        resetPinNumber = getApplicationContext().getSharedPreferences("user_name", Context.MODE_MULTI_PROCESS)
                .getString("resetPin", "");
        Log.d("signUp editor", "home: " + resetPinNumber);

        resetPinNumberText = findViewById(R.id.resetPinNumberTxt);
        progressLayout = findViewById(R.id.progressLayout);
        resetPinNumberText.setText(resetPinNumber);

        checkActivity = getIntent().getStringExtra("checkActivity");
        checkmPIn();
       /* passCodeView = findViewById(R.id.mPin);
        passCodeView.setKeyTextColor(getResources().getColor(R.color.white));

        passCodeView.setOnTextChangeListener(new PassCodeView.TextChangeListener() {
            @Override
            public void onTextChanged(String text) {
                digit = passCodeView.getPassCodeText();
                Log.d("digit", "mPin: " + digit);

                if (digit.length() == 4) {
                    checkmPIn();
                }
            }
        });*/

        resetPinLayout = findViewById(R.id.linearLayout28);
        resetPinLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Security.this, UpdatePinActivity.class);
                startActivity(intent);
                /*String url = "https://wa.me/" + resetPinNumber;
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(i);*/
            }
        });

    }

    private void checkmPIn() {
        js = new JsonObject();
        js.addProperty("env_type", "Prod");
        js.addProperty("app_key", appKey);
        js.addProperty("unique_token", unique_token);
        js.addProperty("security_pin", digit);

        Call<JsonObject> call = controller.getInstance().getApi().checkMPin(js);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                boolean status = response.body().get("status").getAsBoolean();
                String message = response.body().get("msg").getAsString();
                Log.d("mPin", "onResponse: " + response.body().toString());

                if (status) {

                    if (checkActivity.equals("mainActivity")) {
                        if (login) {
                            Log.d("message", "onResponse: " + message);
                            startActivity(new Intent(getApplicationContext(), HomePageActivity.class));
                            finish();
                        }
                    } else if (checkActivity.equals("Login")) {
                        if (login) {
                            Log.d("message", "onResponse: " + message);
                            startActivity(new Intent(getApplicationContext(), HomePageActivity.class));
                            finish();
                        }
                    } else if (checkActivity.equals("wallet_withdrawal")) {

                        js.addProperty("payment_method", getIntent().getStringExtra("payment_method"));
                        js.addProperty("request_amount", getIntent().getStringExtra("request_amount"));
                        Log.d("message", "onResponse: " + js);
                        if (checkmPin) {

                            Call<JsonObject> call1 = controller.getInstance().getApi().walletWithdrawalRequest(js);
                            call1.enqueue(new Callback<JsonObject>() {
                                @Override
                                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                    if (response.body().get("status").getAsBoolean()) {
                                        Log.d("TAG", "onResponse: " + response.toString());
                                        Toast.makeText(getApplicationContext(), response.body().get("msg").getAsString(), Toast.LENGTH_SHORT).show();
                                        checkmPin = false;
                                        finish();
                                    } else {
                                        Toast.makeText(getApplicationContext(), response.body().get("msg").getAsString(), Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                }

                                @Override
                                public void onFailure(Call<JsonObject> call, Throwable t) {
                                    Log.d("fail response", "onFailure: " + t.toString());
                                    Toast.makeText(getApplicationContext(), "Something went wrong.... Try again later", Toast.LENGTH_SHORT).show();
                                }
                            });
                            return;
                        }
                    }


                } else {

                    //passCodeView.reset();
                    if (wrongSecurity < 1) {
                        vibe.vibrate(100);
                        if (checkActivity.equals("mainActivity")) {
                            userLogout();
                        } else if (checkActivity.equals("Login")) {
                            userLogout();
                        } else {
                            finish();
                        }
                    } else {
                        wrongSecurity -= 1;
                        Log.d("else message", "onResponse: " + message);
                        vibe.vibrate(100);
                        Toast.makeText(getApplicationContext(), message + " " + attempt + " attempt left", Toast.LENGTH_SHORT).show();

                        attempt -= 1;
                    }
                    return;
                }
                login = false;
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("fail message", "onResponse: " + t);
            }
        });
    }

    private void userLogout() {
        Log.d("signUp unique", "home: " + unique_token);
        SharedPreferences.Editor editor = spUnique_token.edit();
        editor.remove("unique_token");
        editor.apply();
//        Log.d("signUp editor", "home: " + unique);
        startActivity(new Intent(getApplicationContext(), Login.class));
        finish();
    }

}