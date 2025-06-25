package com.myTara567.app.dashboard;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.myTara567.app.R;
import com.myTara567.app.serverApi.controller;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class User_profile extends AppCompatActivity {
    EditText name, Email;
    ImageView backImg;
    TextView mobile, profileText, pencil, title;
    Button submitBtn;
    String appKey, unique_token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);

        //app key
        appKey = getApplicationContext().getSharedPreferences("app_key", MODE_MULTI_PROCESS)
                .getString("app_key", null);
        Log.d("signUp appkey", "onCreate: " + appKey);

        //unique token
        unique_token = getApplicationContext().getSharedPreferences("unique_token", Context.MODE_MULTI_PROCESS)
                .getString("unique_token", "");
        Log.d("signUp editor", "home: " + unique_token);

        profileText = findViewById(R.id.profileText);
        mobile = findViewById(R.id.phone);
        name = findViewById(R.id.name);
        Email = findViewById(R.id.email);
        pencil = findViewById(R.id.pencil);
        backImg = findViewById(R.id.backProfileImage);
        submitBtn = findViewById(R.id.submitBtn);

        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        JsonObject js = new JsonObject();
        js.addProperty("env_type", "Prod");
        js.addProperty("app_key", appKey);
        js.addProperty("unique_token", unique_token);

        Call<JsonObject> call = controller.getInstance().getApi().get_profile(js);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                boolean status = response.body().get("status").getAsBoolean();
                JsonArray array = response.body().getAsJsonArray("profile");
                Log.d("array", "onResponse: " + array);
                if (status) {
                    name.setEnabled(false);
                    Email.setEnabled(false);

                    for (int i = 0; i < array.size(); i++) {
                        JsonObject jsonObject = array.get(i).getAsJsonObject();

                        String phone = jsonObject.get("mobile").getAsString();
                        String userName = jsonObject.get("user_name").getAsString();
                        String email = jsonObject.get("email").getAsString();
                        String isActive = jsonObject.get("").getAsString();
                        mobile.setText(phone);
                        name.setText(userName);
                        Email.setText(email);


                        if (userName.equals("")) {
                            profileText.setText("");
                        } else {
                            StringBuilder s = new StringBuilder();
                            for (int j = 0; j < 1; j++) {
                                s.append(name.getText().charAt(i));
                            }
                            profileText.setText(s);
                        }
                    }

                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Something went wrong.... Try again later", Toast.LENGTH_SHORT).show();
            }
        });

        pencil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name.setEnabled(true);
                Email.setEnabled(true);
                submitBtn.setVisibility(View.VISIBLE);
                submitBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String updateName = name.getText().toString().trim();
                        String updateEmail = Email.getText().toString().trim();

                        if (updateName.equals("")) {
                            Toast.makeText(getApplicationContext(), "Please enter your name", Toast.LENGTH_SHORT).show();
                            return;
                        } else if (!updateEmail.equals("") && !isValidEmail(updateEmail)) {
                            Toast.makeText(getApplicationContext(), "Please enter a valid email", Toast.LENGTH_SHORT).show();
                            return;
                        } else {

                            JsonObject updateJs = new JsonObject();
                            updateJs.addProperty("env_type", "Prod");
                            updateJs.addProperty("app_key", appKey);
                            updateJs.addProperty("unique_token", unique_token);
                            if (updateEmail.equals("")) {
                                updateJs.addProperty("email", "");
                            } else {
                                updateJs.addProperty("email", updateEmail);
                            }
                            updateJs.addProperty("user_name", updateName);

                            Call<JsonObject> jsonObjectCall = controller.getInstance().getApi().update_profile(updateJs);
                            jsonObjectCall.enqueue(new Callback<JsonObject>() {
                                @Override
                                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                    boolean status = response.body().get("status").getAsBoolean();
                                    String message = response.body().get("msg").getAsString();
                                    if (status) {
                                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                        name.setEnabled(false);
                                        Email.setEnabled(false);
                                        submitBtn.setVisibility(View.INVISIBLE);

                                        String Name = name.getText().toString();
                                        StringBuilder updated = new StringBuilder();
                                        if (Name.equals("")) {
                                            profileText.setText("");
                                        } else {

                                            for (int j = 0; j < 1; j++) {
                                                updated.append(name.getText().charAt(j));
                                            }
                                        }
                                        profileText.setText(updated);
                                        getSharedPreferences("user_name", MODE_MULTI_PROCESS)
                                                .edit()
                                                .putString("name", Name)
                                                .apply();

                                    } else {
                                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<JsonObject> call, Throwable t) {
                                    Toast.makeText(getApplicationContext(), "Something went wrong.... Try again later", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                });

            }
        });
    }

    private boolean isValidEmail(String target) {
        return Pattern.compile(Patterns.EMAIL_ADDRESS.pattern()).matcher(target).matches();
    }
}