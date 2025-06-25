package com.myTara567.app.starline;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.myTara567.app.R;
import com.myTara567.app.dashboard.allbis.adapter.bidHistoryAdapter;
import com.myTara567.app.dashboard.bidHistoryModel;
import com.myTara567.app.serverApi.controller;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StarLineGameBidHistory extends AppCompatActivity {
    ImageView backBidHistImage;
    LinearLayout bid_from, bid_to, noResults;
    int mYear, mMonth, mDay;
    TextView dateFromText, dateToText, title;
    Button submitBtn;
    String appKey, unique_token;
    RecyclerView bidHistRecycler;
    ArrayList<bidHistoryModel> bidHistoryModels = new ArrayList<>();
    SwipeRefreshLayout swipeRefresh;
    ImageView threeDot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bid_history);

        //app key
        appKey = getApplicationContext().getSharedPreferences("app_key", MODE_MULTI_PROCESS)
                .getString("app_key", null);
        Log.d("signUp appkey", "onCreate: " + appKey);

        //unique token
        unique_token = getApplicationContext().getSharedPreferences("unique_token", Context.MODE_MULTI_PROCESS)
                .getString("unique_token", "");
        Log.d("signUp editor", "home: " + unique_token);


        String label = this.getTitle().toString();
        Log.d("title", "onCreate: " + label);
        title = findViewById(R.id.title);
        title.setText(label);

        backBidHistImage = findViewById(R.id.backBidHistImage);
        bid_from = findViewById(R.id.bid_from);
        bid_to = findViewById(R.id.bid_to);
        dateFromText = findViewById(R.id.date_from_text);
        dateToText = findViewById(R.id.date_to_text);
        noResults = findViewById(R.id.noResults);
        submitBtn = findViewById(R.id.submitBtn);
        bidHistRecycler = findViewById(R.id.bidHistRecycler);


        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        Log.d("date", "onCreate: " + mYear);
        mMonth = c.get(Calendar.MONTH);
        Log.d("date", "onCreate: " + mMonth);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        Log.d("date", "onCreate: " + mDay);

        StringBuilder currentDate = new StringBuilder();
        currentDate.append(mDay).append("-").append(mMonth + 1).append("-").append(mYear);
        dateToText.setText(currentDate);
        dateFromText.setText(currentDate);

        backBidHistImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        bid_from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog datePickerDialog = new DatePickerDialog(StarLineGameBidHistory.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                dateFromText.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });

        bid_to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog datePickerDialog = new DatePickerDialog(StarLineGameBidHistory.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                dateToText.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });

        starLineBidHist();

        swipeRefresh = findViewById(R.id.swipeRefresh);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                starLineBidHist();
                swipeRefresh.setRefreshing(false);
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                starLineBidHist();

            }
        });

    }

    private void starLineBidHist() {

        String onClickDateFromText = dateFromText.getText().toString();
        Log.d("js1", "onClick: " + onClickDateFromText);
        String onClickDateToText = dateToText.getText().toString();
        Log.d("js2", "onClick: " + onClickDateToText);


        JsonObject js = new JsonObject();
        js.addProperty("env_type", "Prod");
        js.addProperty("app_key", appKey);
        js.addProperty("unique_token", unique_token);
        js.addProperty("bid_from", onClickDateFromText);
        js.addProperty("bid_to", onClickDateToText);

        Log.d("js", "onClick: " + js);
        Call<JsonObject> call = controller.getInstance().getApi().starLine_game_bidHist(js);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.d("bidHistory", "onResponse: " + response.body().toString());
                JsonArray win_data = response.body().get("bid_data").getAsJsonArray();
                String msg = response.body().get("msg").getAsString();
                boolean status = response.body().get("status").getAsBoolean();

                if (status) {
                    bidHistoryModels.clear();
                    noResults.setVisibility(View.INVISIBLE);
                    bidHistRecycler.setVisibility(View.VISIBLE);
                    for (int i = 0; i < win_data.size(); i++) {
                        JsonObject jsonObject = win_data.get(i).getAsJsonObject();

                        String game_name = jsonObject.get("game_name").getAsString();
                        String pana = jsonObject.get("pana").getAsString();
                        String session = jsonObject.get("session").getAsString();
                        String openDigits = jsonObject.get("digits").getAsString();
                        String closeDigits = jsonObject.get("closedigits").getAsString();
                        String points = jsonObject.get("points").getAsString();
                        String bid_date = jsonObject.get("bid_date").getAsString();

                        RecyclerView.LayoutManager layoutManager =
                                new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);

                        bidHistRecycler.setLayoutManager(layoutManager);

                        bidHistoryModel model = new bidHistoryModel();
                        model.setGame_name(game_name);
                        model.setPana(pana);
                        model.setSession(session);
                        model.setOpenDigits(openDigits);
                        model.setCloseDigits(closeDigits);
                        model.setPoints(points);
                        model.setBid_date(bid_date);

                        bidHistoryModels.add(new bidHistoryModel(game_name,
                                pana, session, openDigits, closeDigits, points, bid_date));
                        bidHistoryAdapter adapter = new bidHistoryAdapter("starLineGameBidHistory", bidHistoryModels);
                        bidHistRecycler.setAdapter(adapter);
                    }


                } else {
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                    noResults.setVisibility(View.VISIBLE);
                    bidHistRecycler.setVisibility(View.INVISIBLE);
                }


            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Something went wrong.... Try again later", Toast.LENGTH_SHORT).show();
            }
        });
    }
}