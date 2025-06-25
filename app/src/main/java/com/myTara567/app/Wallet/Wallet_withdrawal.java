package com.myTara567.app.Wallet;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.myTara567.app.R;
import com.myTara567.app.Security;
import com.myTara567.app.serverApi.controller;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Wallet_withdrawal extends AppCompatActivity {
    Spinner spinner;
    ArrayList<String> spinnerList = new ArrayList<>();
    String[] item;
    JsonObject mainObject = new JsonObject();
    String appKey, unique;
    String min_amt = "0", max_amt = "0";
    ArrayList<wallet_withdrawal_hist_model> modelArrayList = new ArrayList<>();
    RecyclerView walletWithdrawalHistRecycler;
    ImageView backImg;
    Button withdrawalBtn;
    EditText points;
    String paymentMethod;
    String last_request_status;
    ConstraintLayout drawerLayout;
    TextView walletBalance;
    SwipeRefreshLayout swipeRefresh;
    LinearLayout noResults, transLayout;
    String number;
    String paytmNum;
    String gPayNum;
    String phonePeNum;
    int index = 0;
    ArrayAdapter<String> dataAdapter;
    String noNum = "Please set the method and number first";
    TextView withdraw_open_text, withdraw_close_text;
    String withdraw_open_time, withdraw_close_time;
    boolean withdraw = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wallet_withdrawal);

        appKey = getSharedPreferences("app_key", Context.MODE_MULTI_PROCESS).getString("app_key", "");
        Log.d("login appkey", "onCreate: " + appKey);

        unique = getApplicationContext().getSharedPreferences("unique_token", Context.MODE_MULTI_PROCESS).getString("unique_token", "");
        Log.d("signUp editor", "home: " + unique);


        mainObject.addProperty("env_type", "Prod");
        mainObject.addProperty("app_key", appKey);
        mainObject.addProperty("unique_token", unique);

//        drawerLayout = findViewById(R.id.drawerLayout);
//        drawerLayout.setAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_up));

        walletBalance = findViewById(R.id.walletBalance);
        getWalletBalance();

        spinner = findViewById(R.id.spinner);

        TextView title = findViewById(R.id.gameTextTitle);
        title.setText(getIntent().getStringExtra("Title"));

        backImg = findViewById(R.id.gamesBackImageView);
        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        walletWithdrawalHistRecycler = findViewById(R.id.walletWithdrawalHistRecycler);

        //show withdraw transaction
        getPaymentMethod("", "");

        swipeRefresh = findViewById(R.id.swipeRefresh);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getWalletBalance();
                getWalletWithdrawHistory();
                swipeRefresh.setRefreshing(false);
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                index = parent.getPositionForView(view);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        getWalletWithdrawHistory();

        points = findViewById(R.id.points);
        withdrawalBtn = findViewById(R.id.withdrawalBtn);
        withdrawalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getIntent().getStringExtra("withdraw_status").equals("0")) {
                    Toast.makeText(getApplicationContext(), "You can withdraw between " + withdraw_open_time + "-" + withdraw_close_time, Toast.LENGTH_SHORT).show();
                } else {
                    String amount = points.getText().toString().trim();
                    Call<JsonObject> call = controller.getInstance().getApi().lastFundRequestDetail(mainObject);
                    call.enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                            if (amount.equals("")) {
                                Toast.makeText(getApplicationContext(), "Please enter some amount", Toast.LENGTH_SHORT).show();
                            } else if (points.getText().toString().trim().contains(".") || points.getText().toString().trim().contains(",") ||
                                    points.getText().toString().trim().contains("-")) {
                                Toast.makeText(getApplicationContext(), "please enter valid amount", Toast.LENGTH_SHORT).show();
                            } else if (Integer.parseInt(amount) < Integer.parseInt(min_amt)) {
                                Toast.makeText(getApplicationContext(), "Minimum Amount required: " + min_amt, Toast.LENGTH_SHORT).show();
                            } else if (Integer.parseInt(amount) > Integer.parseInt(max_amt)) {
                                Toast.makeText(getApplicationContext(), "Maximum Amount required: " + max_amt, Toast.LENGTH_SHORT).show();
                            } else {
                                if (withdraw) {
                                    if (last_request_status.equals("0")) {
                                        Toast.makeText(getApplicationContext(), "You already have a pending request.", Toast.LENGTH_SHORT).show();
                                    } else {
                                        if (index == 0) {
                                            getPaymentMethod(String.valueOf(index), amount);
                                            withdraw = false;
                                        } else if (index == 1) {
                                            getPaymentMethod(String.valueOf(index), amount);
                                            withdraw = false;
                                        } else if (index == 2) {
                                            getPaymentMethod(String.valueOf(index), amount);
                                            withdraw = false;
                                        }

                                        return;
                                    }
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
        });

    }

    private void getWalletBalance() {
        withdraw_close_text = findViewById(R.id.withdrawCloseTime);
        withdraw_open_text = findViewById(R.id.withdrawOpenTime);
        Call<JsonObject> call1 = controller.getInstance().getApi().getWalletBalance(mainObject);
        call1.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                walletBalance.setText(String.valueOf(response.body().get("wallet_amt").getAsInt()));
                min_amt = response.body().get("min_withdrawal").getAsString();
                max_amt = response.body().get("max_withdrawal").getAsString();
                withdraw_open_time = response.body().get("withdraw_open_time").getAsString();
                withdraw_close_time = response.body().get("withdraw_close_time").getAsString();
                withdraw_open_text.setText(withdraw_open_time);
                withdraw_close_text.setText(withdraw_close_time);

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Something went wrong.... Try again later", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkSecurityPin(String paymentMethod, String amount) {
        Intent i = new Intent(Wallet_withdrawal.this, Security.class);
        i.putExtra("checkActivity", "wallet_withdrawal");
        i.putExtra("payment_method", paymentMethod);
        i.putExtra("request_amount", amount);

        startActivity(i);
        points.getText().clear();
    }

    private void getPaymentMethod(String s, String amount) {
        Call<JsonObject> call = controller.getInstance().getApi().userPaymentMethodList(mainObject);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                boolean status = response.body().get("status").getAsBoolean();
                JsonArray jsonArray = response.body().get("result").getAsJsonArray();
                spinnerList.clear();
                if (status) {
                    if (jsonArray.size() == 0) {
                        Toast.makeText(getApplicationContext(), response.body().get("msg").getAsString(), Toast.LENGTH_SHORT).show();
                    } else {

                        for (int i = 0; i < jsonArray.size(); i++) {

                            JsonObject paymentObj = jsonArray.get(i).getAsJsonObject();
                            paymentMethod = paymentObj.get("type").getAsString();
                            String value = paymentObj.get("value").getAsString();
                            String name = paymentObj.get("name").getAsString();

                            spinnerList.add(name + " (" + value + ")");

                        }

                        switch (s) {
                            case "0":
                            case "2":
                            case "1":
                                JsonObject jsonObject = jsonArray.get(Integer.parseInt(s)).getAsJsonObject();
                                // Toast.makeText(getApplicationContext(), jsonObject.get("type").getAsString(), Toast.LENGTH_SHORT).show();
                                checkSecurityPin(jsonObject.get("type").getAsString(), amount);
                                break;
                        }
                    }

                } else {
                    spinnerList.add(response.body().get("msg").getAsString());
                }
                item = spinnerList.toArray(new String[spinnerList.size()]);
                Log.d("item", "onCreate: " + item.length);

                item = new String[spinnerList.size()];
                Log.d("item", "onCreate: " + item.length);
                spinnerList.toArray(item);
                Log.d("TAG", "onResponse: ");
                if (item.length == 0) {
                    item = new String[]{noNum};
                    dataAdapter = new ArrayAdapter<String>(Wallet_withdrawal.this, R.layout.spinner_txt, item);
                } else {

                    dataAdapter = new ArrayAdapter<String>(Wallet_withdrawal.this, R.layout.spinner_txt, item);
                }
                // Drop down layout style - list view with radio button
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                // attaching data adapter to spinner
                spinner.setAdapter(dataAdapter);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Something went wrong.... Try again later", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getWalletWithdrawHistory() {
        noResults = findViewById(R.id.noResults);
        transLayout = findViewById(R.id.linearLayout21);
        Call<JsonObject> call = controller.getInstance().getApi().withdrawTransactionHist(mainObject);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonArray withdrawHistArray = response.body().getAsJsonArray("withdrawdata");
                last_request_status = response.body().get("last_request_status").getAsString();
                if (response.body().get("status").getAsBoolean()) {

                    if (withdrawHistArray.size() == 0) {
                        walletWithdrawalHistRecycler.setVisibility(View.INVISIBLE);
                        noResults.setVisibility(View.VISIBLE);
                    } else {
                        noResults.setVisibility(View.GONE);
                        transLayout.setVisibility(View.VISIBLE);
                        modelArrayList.clear();
                        for (int i = 0; i < withdrawHistArray.size(); i++) {
                            JsonObject jsonObject = withdrawHistArray.get(i).getAsJsonObject();

                            String request_number = jsonObject.get("request_number").getAsString();
                            String request_amount = jsonObject.get("request_amount").getAsString();
                            String request_status = jsonObject.get("request_status").getAsString();
                            String payment_method = jsonObject.get("payment_method").getAsString();
                            String remark = jsonObject.get("remark").getAsString();
                            String insert_date = jsonObject.get("insert_date").getAsString();
                            String payment_receipt = jsonObject.get("payment_receipt").getAsString();

                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(),
                                    RecyclerView.VERTICAL, false);

                            walletWithdrawalHistRecycler.setLayoutManager(layoutManager);

                            wallet_withdrawal_hist_model model = new wallet_withdrawal_hist_model();

                            model.setRequest_number(request_number);
                            model.setRequest_amount(request_amount);
                            model.setInsert_date(insert_date);
                            model.setRequest_status(request_status);
                            model.setRemark(remark);
                            model.setPayment_method(payment_method);
                            model.setPayment_receipt(payment_receipt);

                            modelArrayList.add(model);
                        }

                        wallet_withdrawal_hist_adapter adapter = new wallet_withdrawal_hist_adapter(modelArrayList);
                        walletWithdrawalHistRecycler.setAdapter(adapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Something went wrong.... Try again later", Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getWalletBalance();
        getWalletWithdrawHistory();
        withdraw = true;
    }
}