package com.myTara567.app.dashboard;

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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.myTara567.app.R;
import com.myTara567.app.serverApi.controller;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Win_history extends AppCompatActivity {
    ImageView backImg;
    LinearLayout dateFrom, dateTo, noResults;
    int mYear, mMonth, mDay;
    TextView dateFromText, dateToText, title;
    Button submitBtn;
    String appKey, unique_token;
    RecyclerView winHistRecycler;
    ArrayList<winHistoryModel> winHistoryModels = new ArrayList<>();
    SwipeRefreshLayout swipeRefresh;
    ConstraintLayout winHistLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.win_history);

        String label = this.getTitle().toString();
        Log.d("title", "onCreate: " + label);
        title = findViewById(R.id.title);
        title.setText(label);

        //app key
        appKey = getApplicationContext().getSharedPreferences("app_key", MODE_MULTI_PROCESS)
                .getString("app_key", null);
        Log.d("signUp appkey", "onCreate: " + appKey);

        //unique token
        unique_token = getApplicationContext().getSharedPreferences("unique_token", Context.MODE_MULTI_PROCESS)
                .getString("unique_token", "");
        Log.d("signUp editor", "home: " + unique_token);

        backImg = findViewById(R.id.backWinHistImage);
        dateFrom = findViewById(R.id.date_from);
        dateTo = findViewById(R.id.date_to);
        dateFromText = findViewById(R.id.date_from_text);
        dateToText = findViewById(R.id.date_to_text);
        noResults = findViewById(R.id.noResults);
        submitBtn = findViewById(R.id.submitBtn);
        winHistRecycler = findViewById(R.id.winHistRecycler);
        winHistLayout = findViewById(R.id.winHistLayout);

        //winHistLayout.setAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_up));

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

        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        dateFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog datePickerDialog = new DatePickerDialog(Win_history.this,
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

        dateTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog datePickerDialog = new DatePickerDialog(Win_history.this,
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

        showWinHistory();

        swipeRefresh = findViewById(R.id.swipeRefresh);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                showWinHistory();
                swipeRefresh.setRefreshing(false);
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showWinHistory();
            }
        });

    }

    private void showWinHistory() {
        String onClickDateFromText = dateFromText.getText().toString();
        String onClickDateToText = dateToText.getText().toString();


        JsonObject js = new JsonObject();
        js.addProperty("env_type", "Prod");
        js.addProperty("app_key", appKey);
        js.addProperty("unique_token", unique_token);
        js.addProperty("date_from", onClickDateFromText);
        js.addProperty("date_to", onClickDateToText);

        Call<JsonObject> call = controller.getInstance().getApi().win_history(js);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.d("winHistory", "onResponse: " + response.body().toString());
                JsonArray win_data = response.body().get("win_data").getAsJsonArray();
                String msg = response.body().get("msg").getAsString();
                boolean status = response.body().get("status").getAsBoolean();

                if (status) {
                    winHistoryModels.clear();
                    noResults.setVisibility(View.INVISIBLE);
                    winHistRecycler.setVisibility(View.VISIBLE);
                    for (int i = 0; i < win_data.size(); i++) {
                        JsonObject jsonObject = win_data.get(i).getAsJsonObject();

                        String transaction_note = jsonObject.get("transaction_note").getAsString();
                        String amount = jsonObject.get("amount").getAsString();
                        String tx_request_number = jsonObject.get("tx_request_number").getAsString();
                        String wining_date = jsonObject.get("wining_date").getAsString();

                        RecyclerView.LayoutManager layoutManager =
                                new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);

                        winHistRecycler.setLayoutManager(layoutManager);

                        winHistoryModel model = new winHistoryModel();
                        model.setAmount(amount);
                        model.setTransaction_note(transaction_note);
                        model.setWining_date(wining_date);
                        model.setTx_request_number(tx_request_number);

                        winHistoryModels.add(new winHistoryModel(amount, transaction_note, tx_request_number, wining_date));
                    }
                    winHistoryAdapter adapter = new winHistoryAdapter(winHistoryModels);
                    winHistRecycler.setAdapter(adapter);

                } else {
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                    noResults.setVisibility(View.VISIBLE);
                    winHistRecycler.setVisibility(View.INVISIBLE);
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Something went wrong.... Try again later", Toast.LENGTH_SHORT).show();
            }
        });
    }

}