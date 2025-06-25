package com.myTara567.app.dashboard;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

public class Contact_us extends AppCompatActivity {
    String appKey;
    JsonObject mainObj = new JsonObject();
    TextView title;
    TextView whatsAppDetail, callDetail, emailDetail, telegramDetail;
    String whatsAppNum, mobile, email, telegram;
    RelativeLayout whatsappLayout, callLayout, mailLayout, telegramLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_us);

        ImageView backImg = findViewById(R.id.gamesBackImageView);
        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        whatsAppDetail = findViewById(R.id.whatsAppDetail);
        callDetail = findViewById(R.id.callDetail);
        emailDetail = findViewById(R.id.emailDetail);
        telegramDetail = findViewById(R.id.telegramDetail);
        whatsappLayout = findViewById(R.id.linearLayout10);
        callLayout = findViewById(R.id.constraintLayout13);
        mailLayout = findViewById(R.id.constraintLayout12);
        telegramLayout = findViewById(R.id.telegramLayout);

        appKey = getSharedPreferences("app_key", Context.MODE_MULTI_PROCESS).getString("app_key", "");
        Log.d("login appkey", "onCreate: " + appKey);

        mainObj.addProperty("env_type", "Prod");
        mainObj.addProperty("app_key", appKey);

        Call<JsonObject> call = controller.getInstance().getApi().contactDetails(mainObj);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                whatsAppNum = response.body().get("whatsapp_no").getAsString();
                mobile = response.body().get("mobile_1").getAsString();
                email = response.body().get("email_1").getAsString();
                telegram = response.body().get("mobile_2").getAsString();

                whatsAppDetail.setText(whatsAppNum);
                emailDetail.setText(email);
                callDetail.setText(mobile);

                if (telegram.equals("")) {
                    telegramLayout.setVisibility(View.GONE);
                } else {
                    telegramDetail.setText(telegram);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Something want wrong. Try again later...", Toast.LENGTH_SHORT).show();
            }
        });

        whatsappLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://wa.me/" + whatsAppNum;
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(i);
            }
        });

        callLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + mobile));
                startActivity(intent);
            }
        });

        mailLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message");
                i.setPackage("com.google.android.gm");
                i.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
                try {
                    startActivity(Intent.createChooser(i, "Send mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(Contact_us.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        telegramLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://telegram.me/" + telegram;
                Intent telegramIntent = new Intent(Intent.ACTION_VIEW);
                telegramIntent.setData(Uri.parse(url));
                startActivity(telegramIntent);
            }
        });
    }
}