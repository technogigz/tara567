package com.myTara567.app.Wallet;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.myTara567.app.R;
import com.myTara567.app.serverApi.controller;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WalletTransfer extends AppCompatActivity {
    JsonObject mainObject;
    String appKey, unique, userDetails;
    EditText points, receiverPhone;
    String receiverPhoneTxt;
    ImageView checkImg, backImg;
    Button transferBtn;
    String userName;
    private boolean numberIsValid = false;
    TextView walletBalance;
    String min_transfer = "0", max_transfer = "0";
    boolean transfer = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wallet_transfer);

        appKey = getSharedPreferences("app_key", Context.MODE_MULTI_PROCESS).getString("app_key", "");
        Log.d("login appkey", "onCreate: " + appKey);

        unique = getApplicationContext().getSharedPreferences("unique_token", Context.MODE_MULTI_PROCESS).getString("unique_token", "");
        Log.d("signUp editor", "home: " + unique);

        userDetails = getApplicationContext().getSharedPreferences("user_name", Context.MODE_MULTI_PROCESS).getString("phone", "");
        Log.d("signUp editor", "home: " + userDetails);

        TextView title = findViewById(R.id.gameTextTitle);
        title.setText(getIntent().getStringExtra("Title"));

        mainObject = new JsonObject();
        mainObject.addProperty("env_type", "Prod");
        mainObject.addProperty("app_key", appKey);
        mainObject.addProperty("unique_token", unique);

        receiverPhone = findViewById(R.id.receiverNumber);
        checkImg = findViewById(R.id.checkImg);


        walletBalance = findViewById(R.id.walletBalance);
        getWalletBalance();


        backImg = findViewById(R.id.gamesBackImageView);
        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        receiverPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mainObject.addProperty("mobile_no", s.toString());
                if (s.length() == 10) {
                    checkImg.setVisibility(View.VISIBLE);
                    receiverPhoneTxt = s.toString();
                    Log.d("text change", "afterTextChanged: " + s.length());

                    Call<JsonObject> call = controller.getInstance().getApi().checkUserForTransferAmt(mainObject);
                    call.enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                            boolean status = response.body().get("status").getAsBoolean();
                            Log.d("sta", "onResponse: " + status);
                            if (status) {
                                // checkImg.setImageResource(R.drawable.ic_outline_check_circle_24);
                                numberIsValid = true;
                                userName = response.body().get("user_name").getAsString();
                                return;
                            }
                        }

                        @Override
                        public void onFailure(Call<JsonObject> call, Throwable t) {
                            Toast.makeText(getApplicationContext(), "Something went wrong.... Try again later", Toast.LENGTH_SHORT).show();

                        }
                    });

                } else if (s.length() == 1 || s.length() < 10) {
                    checkImg.setVisibility(View.VISIBLE);
                    //checkImg.setImageResource(R.drawable.ic_baseline_block_24);
                    numberIsValid = false;

                }

            }
        });

        transferBtn = findViewById(R.id.transferBtn);
        points = findViewById(R.id.points);
        transferBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (points.getText().toString().trim().length() == 0) {
                    Toast.makeText(getApplicationContext(), "please enter some amount", Toast.LENGTH_SHORT).show();
                    return;
                } else if (points.getText().toString().trim().contains(".") || points.getText().toString().trim().contains(",") ||
                        points.getText().toString().trim().contains("-")) {
                    Toast.makeText(getApplicationContext(), "please enter valid amount", Toast.LENGTH_SHORT).show();
                    return;
                } else if (receiverPhone.getText().toString().trim().length() != 10) {
                    Toast.makeText(getApplicationContext(), "Please enter 10 digits number", Toast.LENGTH_SHORT).show();
                    return;
                } else if (Long.parseLong(points.getText().toString().trim()) < Integer.parseInt(min_transfer)) {
                    Toast.makeText(getApplicationContext(), "Minimum transfer amount is " + min_transfer, Toast.LENGTH_SHORT).show();
                    return;
                } else if (Long.parseLong(points.getText().toString().trim()) > Integer.parseInt(max_transfer)) {
                    Toast.makeText(getApplicationContext(), "Maximum transfer amount is " + max_transfer, Toast.LENGTH_SHORT).show();
                    return;
                } else if (!numberIsValid) {
                    Toast.makeText(getApplicationContext(), "User not found", Toast.LENGTH_SHORT).show();
                    return;
                } else if (receiverPhone.getText().toString().trim().equals(userDetails)) {
                    Toast.makeText(getApplicationContext(), "You can not use your number", Toast.LENGTH_SHORT).show();
                    return;
                } else if (Integer.parseInt(walletBalance.getText().toString().trim()) < Integer.parseInt(points.getText().toString().trim())) {
                    Toast.makeText(getApplicationContext(), "Insufficient balance please refill your account.", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    if (transfer) {
                        mainObject.addProperty("mobile_no", receiverPhone.getText().toString().trim());
                        mainObject.addProperty("amount", points.getText().toString().trim());
                        mainObject.addProperty("transfer_note", "Testing Note");

                        showDilogBox();
                        transfer = false;
                    }
                }
            }
        });


    }

    private void getWalletBalance() {
        Call<JsonObject> call1 = controller.getInstance().getApi().getWalletBalance(mainObject);
        call1.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                walletBalance.setText(String.valueOf(response.body().get("wallet_amt").getAsInt()));
                min_transfer = response.body().get("min_transfer").getAsString();
                max_transfer = response.body().get("max_transfer").getAsString();

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Something went wrong.... Try again later", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showDilogBox() {

        View v = LayoutInflater.from(this).inflate(R.layout.wallet_transfer_dialog_box, null);
        AlertDialog.Builder alertDialogBuilder = new
                AlertDialog.Builder(this);

        TextView amount = v.findViewById(R.id.amount);
        amount.setText(points.getText().toString().trim());

        TextView phoneNumber = v.findViewById(R.id.phoneNumber);
        phoneNumber.setText(receiverPhone.getText().toString().trim());

        TextView amountTransferToTxt = v.findViewById(R.id.amountTransferToTxt);
        amountTransferToTxt.setText(userName);

        alertDialogBuilder.setView(v);
        alertDialogBuilder.setCancelable(false);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        Button cancel = v.findViewById(R.id.cancel_button);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
                transfer = true;
            }
        });

        Button submit = v.findViewById(R.id.submitBtn);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call<JsonObject> call = controller.getInstance().getApi().transferWalletBalance(mainObject);
                call.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        boolean status = response.body().get("status").getAsBoolean();
                        if (status) {
                            Toast.makeText(getApplicationContext(), response.body().get("msg").getAsString(), Toast.LENGTH_SHORT).show();
                            receiverPhone.getText().clear();
                            points.getText().clear();
                            checkImg.setVisibility(View.INVISIBLE);
                            getWalletBalance();
                            alertDialog.cancel();
                            transfer = true;
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "Something went wrong.... Try again later", Toast.LENGTH_SHORT).show();

                    }
                });

            }
        });


    }
}