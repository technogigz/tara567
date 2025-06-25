package com.myTara567.app.dashboard;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.myTara567.app.R;
import com.myTara567.app.serverApi.controller;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Game_rates extends AppCompatActivity {
    TextView single_digit_val_1, single_digit_val_2, jodi_digit_val_1, jodi_digit_val_2,
            single_pana_val_1, single_pana_val_2, double_pana_val_1, double_pana_val_2,
            tripple_pana_val_1, tripple_pana_val_2, half_sangam_val_1, half_sangam_val_2,
            full_sangam_val_1, full_sangam_val_2;
    String appKey;
    ImageView btnImg;
    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_rates);

        //app key
        appKey = getApplicationContext().getSharedPreferences("app_key", MODE_MULTI_PROCESS)
                .getString("app_key", null);
        Log.d("signUp appkey", "onCreate: " + appKey);

        btnImg = findViewById(R.id.backBidHistImage);
        single_digit_val_1 = findViewById(R.id.single_digit_val_1);
        single_digit_val_2 = findViewById(R.id.single_digit_val_2);
        jodi_digit_val_1 = findViewById(R.id.jodi_digit_val_1);
        jodi_digit_val_2 = findViewById(R.id.jodi_digit_val_2);
        single_pana_val_1 = findViewById(R.id.single_pana_val_1);
        single_pana_val_2 = findViewById(R.id.single_pana_val_2);
        double_pana_val_1 = findViewById(R.id.double_pana_val_1);
        double_pana_val_2 = findViewById(R.id.double_pana_val_2);
        tripple_pana_val_1 = findViewById(R.id.tripple_pana_val_1);
        tripple_pana_val_2 = findViewById(R.id.tripple_pana_val_2);
        half_sangam_val_1 = findViewById(R.id.half_sangam_val_1);
        half_sangam_val_2 = findViewById(R.id.half_sangam_val_2);
        full_sangam_val_1 = findViewById(R.id.full_sangam_val_1);
        full_sangam_val_2 = findViewById(R.id.full_sangam_val_2);

        btnImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        JsonObject js = new JsonObject();
        js.addProperty("env_type", "Prod");
        js.addProperty("app_key", appKey);

        Call<JsonObject> call = controller.getInstance().getApi().game_rates(js);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonArray jsonArray = response.body().getAsJsonArray("game_rates");
                boolean status = response.body().get("status").getAsBoolean();
                if (status) {
                    for (int i = 0; i < jsonArray.size(); i++) {
                        JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();

                        String single_digit_1 = jsonObject.get("single_digit_val_1").getAsString();
                        single_digit_val_1.setText(single_digit_1);

                        String single_digit_2 = jsonObject.get("single_digit_val_2").getAsString();
                        single_digit_val_2.setText(single_digit_2);

                        String jodi_digit_1 = jsonObject.get("jodi_digit_val_1").getAsString();
                        jodi_digit_val_1.setText(jodi_digit_1);

                        String jodi_digit_2 = jsonObject.get("jodi_digit_val_2").getAsString();
                        jodi_digit_val_2.setText(jodi_digit_2);

                        String single_pana_1 = jsonObject.get("single_pana_val_1").getAsString();
                        single_pana_val_1.setText(single_pana_1);

                        String single_pana_2 = jsonObject.get("single_pana_val_2").getAsString();
                        single_pana_val_2.setText(single_pana_2);

                        String double_pana_1 = jsonObject.get("double_pana_val_1").getAsString();
                        double_pana_val_1.setText(double_pana_1);

                        String double_pana_2 = jsonObject.get("double_pana_val_2").getAsString();
                        double_pana_val_2.setText(double_pana_2);

                        String tripple_pana_1 = jsonObject.get("tripple_pana_val_1").getAsString();
                        tripple_pana_val_1.setText(tripple_pana_1);

                        String tripple_pana_2 = jsonObject.get("tripple_pana_val_2").getAsString();
                        tripple_pana_val_2.setText(tripple_pana_2);

                        String half_sangam_1 = jsonObject.get("half_sangam_val_1").getAsString();
                        half_sangam_val_1.setText(half_sangam_1);

                        String half_sangam_2 = jsonObject.get("half_sangam_val_2").getAsString();
                        half_sangam_val_2.setText(half_sangam_2);

                        String full_sangam_1 = jsonObject.get("full_sangam_val_1").getAsString();
                        full_sangam_val_1.setText(full_sangam_1);

                        String full_sangam_2 = jsonObject.get("full_sangam_val_2").getAsString();
                        full_sangam_val_2.setText(full_sangam_2);

                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Something went wrong.... Try again later", Toast.LENGTH_SHORT).show();
            }
        });
    }
}