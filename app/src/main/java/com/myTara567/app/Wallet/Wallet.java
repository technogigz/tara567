package com.myTara567.app.Wallet;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.myTara567.app.ManageGooglePayActivity;
import com.myTara567.app.ManagePaytmActivity;
import com.myTara567.app.ManagePhonePeActivity;
import com.myTara567.app.R;
import com.myTara567.app.dashboard.DepositOnlineActivity;
import com.myTara567.app.dashboard.allbis.adapter.FundRequestHistoryAdapter;
import com.myTara567.app.serverApi.controller;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Wallet extends AppCompatActivity {
    TextView title;
    String appKey, unique;
    int walletBalance;
    LinearLayout addFund;
    RecyclerView recyclerView;
    RadioGroup rdGroup;
    LinearLayout transfer, withdrawal, withdrawMethodBtn;
    JsonObject mainObject;
    TextView walletBalanceTxt;
    ConstraintLayout drawerLayout;
    ArrayList<TransactionHistModel> histModelsList = new ArrayList<>();
    String withdrawal_status, transfer_point_status, withdraw_open_time, withdraw_close_time;
    SwipeRefreshLayout swipeRefresh;
    LinearLayout noResults;
    String paytmNum = "";
    String gPayNum = "";
    String phonePeNum = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wallet);

        withdrawal_status = getIntent().getStringExtra("withdraw_status");
        transfer_point_status = getIntent().getStringExtra("transfer_point_status");

        //drawerLayout = findViewById(R.id.constraintLayout10);
        // drawerLayout.setAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_up));

        ImageView backImg = findViewById(R.id.backImage);
        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        walletBalanceTxt = findViewById(R.id.walletBalanceTxt);
        recyclerView = findViewById(R.id.histRecycler);

        appKey = getSharedPreferences("app_key", Context.MODE_MULTI_PROCESS).getString("app_key", "");
        Log.d("login appkey", "onCreate: " + appKey);

        unique = getApplicationContext().getSharedPreferences("unique_token", Context.MODE_MULTI_PROCESS).getString("unique_token", "");
        Log.d("signUp editor", "home: " + unique);

        mainObject = new JsonObject();
        mainObject.addProperty("env_type", "Prod");
        mainObject.addProperty("app_key", appKey);
        mainObject.addProperty("unique_token", unique);


        transfer = findViewById(R.id.rdTransfer);
        withdrawal = findViewById(R.id.rdWithdrawal);
        //withdrawMethodBtn = findViewById(R.id.rdWithdrawalMethod);

        getWalletBalance();

        //set transaction history
        setTransactionHist();

        //addFund click
        addFund = findViewById(R.id.addFund);
        addFund.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent k = new Intent(getApplicationContext(), DepositOnlineActivity.class);
                k.putExtra("Title", "Add Fund");
                startActivity(k);
                /*Intent k = new Intent(getApplicationContext(), AddFund.class);
                k.putExtra("Title", "Add Fund");
                startActivity(k);*/
            }
        });

        //transfer
        transfer.setOnClickListener(v -> {
            if (transfer_point_status.equals("0")) {
                Toast.makeText(getApplicationContext(), "Transfer fund is currently disabled, Please Contact to Admin", Toast.LENGTH_LONG).show();
            } else {
                Intent i = new Intent(getApplicationContext(), WalletTransfer.class);
                i.putExtra("Title", "Transfer Fund");
                startActivity(i);
            }
        });

        //withdraw
        withdrawal.setOnClickListener(v -> {
            Intent j = new Intent(getApplicationContext(), Wallet_withdrawal.class);
            j.putExtra("Title", "Withdraw Fund");
            j.putExtra("withdraw_status", withdrawal_status);
            j.putExtra("withdraw_open_time", withdraw_open_time);
            j.putExtra("withdraw_close_time", withdraw_close_time);
            startActivity(j);
        });

        //payment methods
        LinearLayout wdMPhonePe = findViewById(R.id.linearLayout15);
        wdMPhonePe.setOnClickListener(v -> {
            //setPaymentMethodNumber();
            //showDialogBox(R.drawable.phone_pey, "3", phonePeNum);
            startActivity(new Intent(Wallet.this, ManagePhonePeActivity.class));
        });

        LinearLayout wdMPaytm = findViewById(R.id.linearLayout14);
        wdMPaytm.setOnClickListener(v -> {
            //setPaymentMethodNumber();
            //showDialogBox(R.drawable.paytm, "1", paytmNum);
            startActivity(new Intent(Wallet.this, ManagePaytmActivity.class));
        });

        LinearLayout wdMGoogle = findViewById(R.id.linearLayout16);
        wdMGoogle.setOnClickListener(v -> {

            //setPaymentMethodNumber();
            //showDialogBox(R.drawable.gpay, "2", gPayNum);

            startActivity(new Intent(Wallet.this, ManageGooglePayActivity.class));
        });

        //on refresh

        ImageView refresh = findViewById(R.id.refresh);
        refresh.setOnClickListener(v -> {
            setTransactionHist();
            getWalletBalance();
            refresh.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate));
        });

        /*swipeRefresh = findViewById(R.id.swipeRefresh);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setTransactionHist();
                getWalletBalance();
                swipeRefresh.setRefreshing(false);
            }
        });
*/

    }

    private void getWalletBalance() {
        Call<JsonObject> call1 = controller.getInstance().getApi().getWalletBalance(mainObject);
        call1.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                withdraw_open_time = response.body().get("withdraw_open_time").getAsString();
                withdraw_close_time = response.body().get("withdraw_close_time").getAsString();
                walletBalance = response.body().get("wallet_amt").getAsInt();
                walletBalanceTxt.setText(String.valueOf(walletBalance));
                Log.d("withdraw", "onCreate: " + withdraw_open_time);


            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Something went wrong.... Try again later", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setTransactionHist() {
        noResults = findViewById(R.id.noResults);
        TextView transLayout = findViewById(R.id.textView10);
        Call<JsonObject> walletTransactionHistory = controller.getInstance().getApi().walletTransactionHistory(mainObject);
        walletTransactionHistory.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonArray transaction_hist = response.body().getAsJsonArray("transaction_history");
                boolean status = response.body().get("status").getAsBoolean();
                Log.d("history", "onResponse: " + transaction_hist.toString());
                if (status) {

                    if (transaction_hist.size() == 0) {
                        recyclerView.setVisibility(View.INVISIBLE);
                        noResults.setVisibility(View.VISIBLE);
                    } else {
                        noResults.setVisibility(View.GONE);
                        transLayout.setVisibility(View.VISIBLE);
                        histModelsList.clear();
                        for (int i = 0; i < transaction_hist.size(); i++) {
                            JsonObject jsonObject = transaction_hist.get(i).getAsJsonObject();

                            String transaction_type = jsonObject.get("transaction_type").getAsString();
                            String amount = jsonObject.get("amount").getAsString();
                            String transaction_note = jsonObject.get("transaction_note").getAsString();
                            String insert_date = jsonObject.get("insert_date").getAsString();

                            RecyclerView.LayoutManager layoutManager =
                                    new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);

                            recyclerView.setLayoutManager(layoutManager);

                            TransactionHistModel model = new TransactionHistModel();

                            model.transaction_note = transaction_note;
                            model.amount = amount;
                            model.insert_date = insert_date;
                            model.transaction_type = transaction_type;

                            histModelsList.add(model);
                        }

                    }
                    FundRequestHistoryAdapter adapter = new FundRequestHistoryAdapter(histModelsList);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Something went wrong.... Try again later", Toast.LENGTH_SHORT).show();
            }
        });

/*        rdGroup = findViewById(R.id.radioGroup);

        rdGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int id = group.getCheckedRadioButtonId();

                switch (id) {
                    case R.id.rdTransfer:

                        Intent i = new Intent(getApplicationContext(), walletTransfer.class);
                        i.putExtra("Title", transfer.getText().toString());
                        i.putExtra("transfer_point_status", transfer_point_status);
                        startActivity(i);
                        transfer.setChecked(false);

                        break;
                    case R.id.rdWithdrawal:

                        Intent j = new Intent(getApplicationContext(), wallet_withdrawal.class);
                        j.putExtra("Title", withdrawal.getText().toString());
                        j.putExtra("withdraw_status", withdrawal_status);
                        j.putExtra("withdraw_open_time", withdraw_open_time);
                        j.putExtra("withdraw_close_time", withdraw_close_time);
                        startActivity(j);
                        withdrawal.setChecked(false);


                        break;

                    case R.id.rdWithdrawalMethod:
                        Intent k = new Intent(getApplicationContext(), wallet_withdrawMethod.class);
                        k.putExtra("Title", withdrawMethodBtn.getText().toString());
                        startActivity(k);
                        withdrawMethodBtn.setChecked(false);
                        break;
                }
            }
        });*/


    }

    private void showDialogBox(int image, String method, String number) {
        View view = LayoutInflater.from(Wallet.this).inflate(R.layout.withdraw_method_dialog_box, null);
        AlertDialog.Builder alertDialogBuilder = new
                AlertDialog.Builder(Wallet.this);

        alertDialogBuilder.setView(view);
        alertDialogBuilder.setCancelable(false);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        EditText methodNumber = view.findViewById(R.id.Number);
        TextView textView = view.findViewById(R.id.heading);
        switch (method) {
            case "1":
                textView.setText("Add PayTm Number");
                break;
            case "2":
                textView.setText("Add Google Pay Number");
                break;
            case "3":
                textView.setText("Add PhonePe Number");
                break;
            default:
                textView.setText("Add Number");
                break;
        }
        ImageView imageView = view.findViewById(R.id.imageView);
        imageView.setImageResource(image);

        if (number.equals("")) {
            methodNumber.setHint("Enter Number");
        } else if (number.equals("N/A")) {
            methodNumber.setHint("Enter Number");
        } else {
            methodNumber.setText(number);
            methodNumber.setHint("Enter Number");
        }


        Button cancel = view.findViewById(R.id.cancel_button);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        Button submit = view.findViewById(R.id.submitBtn);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (methodNumber.getText().toString().trim().equals("")) {
                    Toast.makeText(getApplicationContext(), "Please enter number.", Toast.LENGTH_SHORT).show();
                } else if (methodNumber.getText().toString().trim().contains(".") || methodNumber.getText().toString().trim().contains(",")
                        || methodNumber.getText().toString().trim().contains("-") || methodNumber.getText().toString().trim().length() < 10) {
                    Toast.makeText(getApplicationContext(), "Please enter a valid number.", Toast.LENGTH_SHORT).show();
                } else if (methodNumber.getText().toString().trim().equals(number)) {
                    Toast.makeText(getApplicationContext(), "You haven't made any changes.\n" +
                            "Please make some changes.", Toast.LENGTH_SHORT).show();
                } else {
                    Call<JsonObject> call = controller.getInstance().getApi().validateBank(mainObject);
                    call.enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                            if (response.body().get("status").getAsBoolean()) {
                                Intent i = new Intent(getApplicationContext(), PaymentMethodOTPVerification.class);
                                i.putExtra("upiNumber", methodNumber.getText().toString().trim());
                                i.putExtra("paymentMethod", method);
                                i.putExtra("otp", response.body().get("otp").getAsString());
                                startActivity(i);
                                alertDialog.dismiss();
                            }
                            alertDialog.cancel();
                        }

                        @Override
                        public void onFailure(Call<JsonObject> call, Throwable t) {
                            Toast.makeText(getApplicationContext(), "Something want wrong. Try again later...", Toast.LENGTH_SHORT).show();
                        }

                    });
                }
            }
        });
    }

    private void setPaymentMethodNumber() {
        Call<JsonObject> call = controller.getInstance().getApi().userPaymentDetails(mainObject);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                boolean status = response.body().get("status").getAsBoolean();
                JsonArray jsonArray = response.body().get("payment_details").getAsJsonArray();

                if (jsonArray.size() > 0) {
                    if (status) {
                        for (int i = 0; i < jsonArray.size(); i++) {

                            JsonObject paymentObj = jsonArray.get(i).getAsJsonObject();
                            paytmNum = paymentObj.get("paytm_number").getAsString();
                            gPayNum = paymentObj.get("google_pay_number").getAsString();
                            phonePeNum = paymentObj.get("phone_pay_number").getAsString();

                        }
                    }
                } else {

                    paytmNum = "N/A";
                    gPayNum = "N/A";
                    phonePeNum = "N/A";
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
        setTransactionHist();
        setPaymentMethodNumber();
    }
}