package com.myTara567.app.starline;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.myTara567.app.R;
import com.myTara567.app.WebView;
import com.myTara567.app.serverApi.controller;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StarLine extends AppCompatActivity {
    TextView title, single_digit_val_1, single_digit_val_2, single_pana_val_1, single_pana_val_2,
            double_pana_val_1, double_pana_val_2, tripple_pana_val_1, tripple_pana_val_2;
    CardView bidHistBtn, winHisBtn;
    LinearLayout seeChartBtn;
    String appKey, chart;
    RecyclerView recyclerView;
    ImageView back;
    JsonObject js;
    ArrayList<starlineCardModel> starlineCardModelArrayList = new ArrayList<>();
    SwipeRefreshLayout swipeRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.star_line);

        seeChartBtn = findViewById(R.id.seeChartBtn);
        bidHistBtn = findViewById(R.id.bidHistBtn);
        winHisBtn = findViewById(R.id.winHisBtn);
        back = findViewById(R.id.backStarImage);
        recyclerView = findViewById(R.id.starlineRecycler);
        single_digit_val_1 = findViewById(R.id.single_digit_val_1);
        single_digit_val_2 = findViewById(R.id.single_digit_val_2);
        single_pana_val_1 = findViewById(R.id.single_pana_val_1);
        single_pana_val_2 = findViewById(R.id.single_pana_val_2);
        double_pana_val_1 = findViewById(R.id.double_pana_val_1);
        double_pana_val_2 = findViewById(R.id.double_pana_val_2);
        tripple_pana_val_1 = findViewById(R.id.tripple_pana_val_1);
        tripple_pana_val_2 = findViewById(R.id.tripple_pana_val_2);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        appKey = getApplicationContext().getSharedPreferences("app_key", MODE_MULTI_PROCESS)
                .getString("app_key", null);
        Log.d("signUp appkey", "onCreate: " + appKey);

        js = new JsonObject();
        js.addProperty("env_type", "Prod");
        js.addProperty("app_key", appKey);

        //game rates
        Call<JsonObject> call = controller.getInstance().getApi().starLine_game_rates(js);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                boolean status = response.body().get("status").getAsBoolean();
                JsonArray game_rates = response.body().getAsJsonArray("game_rates");

                if (status) {
                    for (int i = 0; i < game_rates.size(); i++) {
                        JsonObject jsonObject = game_rates.get(i).getAsJsonObject();

                        String single_digit_1 = jsonObject.get("single_digit_val_1").getAsString();
                        single_digit_val_1.setText(single_digit_1);

                        String single_digit_2 = jsonObject.get("single_digit_val_2").getAsString();
                        single_digit_val_2.setText(single_digit_2);

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
                    }
                } else {
                    Toast.makeText(getApplicationContext(), response.body().get("msg").getAsString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Something went wrong.... Try again later", Toast.LENGTH_SHORT).show();
            }
        });

        //game details
        getGameData();

        swipeRefresh = findViewById(R.id.swipeRefresh);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getGameData();
                swipeRefresh.setRefreshing(false);
            }
        });

        seeChartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), WebView.class);
                i.putExtra("url", chart);
                i.putExtra("name", "Starline Game Chart");
                startActivity(i);
            }
        });

        bidHistBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), StarLineGameBidHistory.class));
            }
        });

        winHisBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), StarLineGameWinHistory.class));
            }
        });


    }

    private void getGameData() {

        Call<JsonObject> call1 = controller.getInstance().getApi().starLine_game_details(js);
        call1.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                chart = response.body().get("web_starline_chart_url").getAsString();
                JsonArray jsonArray = response.body().getAsJsonArray("result");
                starlineCardModelArrayList.clear();

                for (int j = 0; j < jsonArray.size(); j++) {
                    JsonObject jsonObject = jsonArray.get(j).getAsJsonObject();

                    String game_name = jsonObject.get("game_name").getAsString();
                    String open_time = jsonObject.get("open_time").getAsString();
                    String close_time = jsonObject.get("close_time").getAsString();
                    String open_result = jsonObject.get("open_result").getAsString();
                    String close_result = jsonObject.get("close_result").getAsString();
                    String market = jsonObject.get("msg").getAsString();
                    String marketStatus = jsonObject.get("msg_status").getAsString();
                    String game_id = jsonObject.get("game_id").getAsString();


                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(),
                            LinearLayoutManager.VERTICAL, false);

                    recyclerView.setLayoutManager(linearLayoutManager);

                    starlineCardModel starlineCardModel = new starlineCardModel();
                    starlineCardModel.setGameName(game_name);
                    starlineCardModel.setOpenTime(open_time);
                    starlineCardModel.setCloseTime(close_time);
                    starlineCardModel.setOpenResult(open_result);
                    starlineCardModel.setCloseResult(close_result);
                    starlineCardModel.setMarket(market);
                    starlineCardModel.setMarketStatus(marketStatus);
                    starlineCardModel.setGameId(game_id);

                    starlineCardModelArrayList.add(starlineCardModel);

                    starLineGameAdapter adapter = new starLineGameAdapter(starlineCardModelArrayList);
                    recyclerView.setAdapter(adapter);

                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Something went wrong.... Try again later", Toast.LENGTH_SHORT).show();
            }
        });
    }
}