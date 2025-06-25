package com.myTara567.app.Wallet;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
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
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddFund extends AppCompatActivity {
    EditText number;
    ConstraintLayout drawerLayout;
    Button VerifyBtn;
    JsonObject mainObject = new JsonObject();
    String appKey, unique;
    String min_amt = "0", max_amt = "0";
    String paymentMethod;
    String upi_payment_id, google_upi_payment_id, phonepay_upi_payment_id;
    ConstraintLayout rdGooglePay, rdPhonePe, rdOthers;
    RecyclerView addFundHistRecycler;
    LinearLayout noResults, transLayout;
    int method = 0;
    int UPI_REQUEST_CODE = 123;
    Uri.Builder url = new Uri.Builder();
    UUID uuid = UUID.randomUUID();
    ArrayList<wallet_withdrawal_hist_model> modelArrayList = new ArrayList<>();
    boolean addFund = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_fund);

//        drawerLayout = findViewById(R.id.drawerLayout);
//        drawerLayout.setAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_up));
        number = findViewById(R.id.Id);
        // paymentMethodSpinner = findViewById(R.id.paymentMethodSpinner);
        VerifyBtn = findViewById(R.id.VerifyBtn);

        appKey = getSharedPreferences("app_key", Context.MODE_MULTI_PROCESS).getString("app_key", "");
        Log.d("login appkey", "onCreate: " + appKey);

        unique = getApplicationContext().getSharedPreferences("unique_token", Context.MODE_MULTI_PROCESS).getString("unique_token", "");
        Log.d("signUp editor", "home: " + unique);


        mainObject.addProperty("env_type", "Prod");
        mainObject.addProperty("app_key", appKey);
        mainObject.addProperty("unique_token", unique);

        TextView title = findViewById(R.id.gameTextTitle);
        title.setText(getIntent().getStringExtra("Title"));

        //set wallet balance

        setWalletBalance();

        //get min and max amount
        Call<JsonObject> call = controller.getInstance().getApi().lastFundRequestDetail(mainObject);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                min_amt = response.body().get("min_amt").getAsString();
                max_amt = response.body().get("max_amt").getAsString();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Something went wrong.... Try again later", Toast.LENGTH_SHORT).show();
            }
        });

        //get admin bank details
        Call<JsonObject> adminObj = controller.getInstance().getApi().adminBankDetails(mainObject);
        adminObj.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.body().get("status").getAsBoolean()) {

                    JsonArray bank_details = response.body().getAsJsonArray("bank_details");
                    for (int i = 0; i < bank_details.size(); i++) {
                        JsonObject jsonObject = bank_details.get(i).getAsJsonObject();
                        upi_payment_id = jsonObject.get("upi_payment_id").getAsString();
                        google_upi_payment_id = jsonObject.get("google_upi_payment_id").getAsString();
                        phonepay_upi_payment_id = jsonObject.get("phonepay_upi_payment_id").getAsString();

                    }

                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Something went wrong.... Try again later", Toast.LENGTH_SHORT).show();

            }
        });

        //set transaction history
        setAddFundTransactionHist();

        SwipeRefreshLayout swipeRefresh = findViewById(R.id.swipeRefresh);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setAddFundTransactionHist();
                setWalletBalance();
                swipeRefresh.setRefreshing(false);
            }
        });


        ImageView backImg = findViewById(R.id.gamesBackImageView);
        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        rdGooglePay = findViewById(R.id.rdGooglePay);
        rdPhonePe = findViewById(R.id.rdPhonePe);
        rdOthers = findViewById(R.id.rdOthers);
        ImageView gpayImg = findViewById(R.id.imageView15);
        ImageView phonePeImg = findViewById(R.id.imageView16);
        ImageView othersImg = findViewById(R.id.imageView19);
        rdGooglePay.setOnClickListener(v -> {
            method = 1;
            gpayImg.setImageResource(R.drawable.add_fund_checked);
            phonePeImg.setImageResource(R.drawable.add_find_uncheckd);
            othersImg.setImageResource(R.drawable.add_find_uncheckd);
        });
        rdPhonePe.setOnClickListener(v -> {
            method = 2;
            gpayImg.setImageResource(R.drawable.add_find_uncheckd);
            phonePeImg.setImageResource(R.drawable.add_fund_checked);
            othersImg.setImageResource(R.drawable.add_find_uncheckd);
        });
        rdOthers.setOnClickListener(v -> {
            method = 3;
            gpayImg.setImageResource(R.drawable.add_find_uncheckd);
            phonePeImg.setImageResource(R.drawable.add_find_uncheckd);
            othersImg.setImageResource(R.drawable.add_fund_checked);
        });

        VerifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (number.getText().toString().trim().equals("")) {
                    Toast.makeText(getApplicationContext(), "Please Enter some amount", Toast.LENGTH_SHORT).show();
                } else if (number.getText().toString().trim().contains(".") || number.getText().toString().trim().contains(",") ||
                        number.getText().toString().trim().contains("-")) {
                    Toast.makeText(getApplicationContext(), "please enter valid amount", Toast.LENGTH_SHORT).show();
                } else if (Integer.parseInt(min_amt) > Integer.parseInt(number.getText().toString().trim())) {
                    Toast.makeText(getApplicationContext(), "Minimum amount is: " + min_amt, Toast.LENGTH_SHORT).show();
                } else if (Integer.parseInt(max_amt) < Integer.parseInt(number.getText().toString().trim())) {
                    Toast.makeText(getApplicationContext(), "Maximum amount is: " + max_amt, Toast.LENGTH_SHORT).show();
                } else {
                    if (addFund) {
                        Log.d("UUID", "onClick: " + uuid.toString());
                        //12b1bf50-9d94-447f-a37a-018722838ec8

                        Log.d("UUID", uuid.toString().replace("-", ""));
                        //12b1bf509d94447fa37a018722838ec8

                        url.scheme("upi");
                        url.authority("pay");
                        url.appendQueryParameter("pn", "Matka");
                        url.appendQueryParameter("tr", uuid.toString().replace("-", ""));
                        url.appendQueryParameter("tn", "testing");
                        url.appendQueryParameter("am", number.getText().toString().trim());
                        url.appendQueryParameter("cu", "INR");

//                    Uri uri =
//                            Uri.parse("upi://pay")
//                                    .buildUpon()
//                                    .appendQueryParameter("pn", "Matka")
//                                    .appendQueryParameter("tr", "123113123")
//                                    .appendQueryParameter("tn", "testing")
//                                    .appendQueryParameter("am", "1")
//                                    .appendQueryParameter("cu", "INR")
//                                    .build();

                        if (method == 1) {
                            paymentMethod = "1";

                            String GOOGLE_PAY_PACKAGE_NAME = "com.google.android.apps.nbu.paisa.user";

                            url.appendQueryParameter("pa", google_upi_payment_id);
                            url.build();

                            Log.d("Url", "onClick: " + url);
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse(url.toString()));
                            intent.setPackage(GOOGLE_PAY_PACKAGE_NAME);
                            try {
                                startActivityForResult(intent, UPI_REQUEST_CODE);
                            } catch (Exception e) {
                                Toast.makeText(getApplicationContext(), "Google Pay not found.", Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
//                        startActivity(intent);

                        } else if (method == 2) {
                            paymentMethod = "2";

                            String PHONE_PE_PACKAGE_NAME = "com.phonepe.app";

                            ///Log.d("phone pe uri", "onClick: " + uri);

                            url.appendQueryParameter("pa", phonepay_upi_payment_id);
                            url.build();

                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse(url.toString()));
                            intent.setPackage(PHONE_PE_PACKAGE_NAME);
                            try {
                                Log.d("package", String.valueOf(isPackageInstalled(PHONE_PE_PACKAGE_NAME)));
                                startActivityForResult(intent, UPI_REQUEST_CODE);
                            } catch (Exception e) {
                                Toast.makeText(getApplicationContext(), "PhonePe not found.", Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                                Log.d("Phone Pe", "onClick: " + e.toString());
                            }

                        } else if (method == 3) {

                            paymentMethod = "3";

                            url.appendQueryParameter("pa", upi_payment_id);
                            url.build();

                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse(url.toString()));
                            try {
                                startActivityForResult(intent, UPI_REQUEST_CODE);
                            } catch (Exception e) {
                                Toast.makeText(getApplicationContext(), "Others payment mode not found.", Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                                Log.d("Phone Pe", "onClick: " + e.toString());
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Please select one method to add fund.", Toast.LENGTH_SHORT).show();
                        }
                        addFund = false;
                    }
                }
            }
        });

    }

    private void setWalletBalance() {
        TextView walletBalance = findViewById(R.id.walletBalance);
        Call<JsonObject> call1 = controller.getInstance().getApi().getWalletBalance(mainObject);
        call1.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                walletBalance.setText(String.valueOf(response.body().get("wallet_amt").getAsInt()));

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Something went wrong.... Try again later", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setAddFundTransactionHist() {
        addFundHistRecycler = findViewById(R.id.addFundHistRecycler);
        noResults = findViewById(R.id.noResults);
        transLayout = findViewById(R.id.linearLayout21);
        Call<JsonObject> call2 = controller.getInstance().getApi().getAutoDepositList(mainObject);
        call2.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.body().get("status").getAsBoolean()) {
                    JsonArray HistArray = response.body().getAsJsonArray("result");

                    if (HistArray.size() == 0) {
                        addFundHistRecycler.setVisibility(View.INVISIBLE);
                        noResults.setVisibility(View.VISIBLE);
                        transLayout.setVisibility(View.GONE);
                    } else {
                        noResults.setVisibility(View.GONE);
                        transLayout.setVisibility(View.VISIBLE);
                        modelArrayList.clear();
                        for (int i = 0; i < HistArray.size(); i++) {
                            JsonObject jsonObject = HistArray.get(i).getAsJsonObject();

                            String request_number = jsonObject.get("txn_id").getAsString();
                            String request_amount = jsonObject.get("amount").getAsString();
                            String request_status = jsonObject.get("fund_status").getAsString();
                            String payment_method = jsonObject.get("payment_method").getAsString();
                            String deposit_type = jsonObject.get("deposit_type").getAsString();
                            String remark = jsonObject.get("reject_remark").getAsString();
                            String insert_date = jsonObject.get("insert_date").getAsString();


                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(),
                                    RecyclerView.VERTICAL, false);

                            addFundHistRecycler.setLayoutManager(layoutManager);

                            wallet_withdrawal_hist_model model = new wallet_withdrawal_hist_model();

                            model.setRequest_number(request_number);
                            model.setRequest_amount(request_amount);
                            model.setInsert_date(insert_date);
                            model.setRequest_status(request_status);
                            model.setRemark(remark);
                            model.setPayment_method(payment_method);
                            model.setFund_status(deposit_type);


                            modelArrayList.add(model);
                        }

                        add_fund_hist_adapter adapter = new add_fund_hist_adapter(modelArrayList);
                        addFundHistRecycler.setAdapter(adapter);
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d("Activity result", "onActivityResult: " + requestCode);
        Log.d("Activity result", "onActivityResult: " + resultCode);

        if (requestCode == UPI_REQUEST_CODE && resultCode == RESULT_OK && data.getExtras() != null) {
            //Toast.makeText(getApplicationContext(), "Payment Success", Toast.LENGTH_SHORT).show();
            addFundTransaction(data.getExtras(), paymentMethod);

        } else {
            Toast.makeText(getApplicationContext(), "Payment failed", Toast.LENGTH_SHORT).show();
        }

    }

    private void addFundTransaction(Bundle extras, String paymentMethod) {
//        Log.d("success", "addFundTransaction " + "Status: " + extras.get("Status").toString());
//        Log.d("success", "addFundTransaction " + "txnRef: " + extras.get("txnRef").toString());
//        Log.d("success", "addFundTransaction " + "txnId: " + extras.get("txnId").toString());

        mainObject.addProperty("amount", number.getText().toString().trim());
        try {
            mainObject.addProperty("txn_id", extras.get("txnId").toString());
            mainObject.addProperty("txn_ref", extras.get("txnRef").toString());
        } catch (Exception e) {
            //Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }

        if (paymentMethod.equals("1")) {
            mainObject.addProperty("upigpay", "1");
            mainObject.addProperty("upiphonepe", "0");
            mainObject.addProperty("otherupi", "0");
        } else if (paymentMethod.equals("2")) {
            mainObject.addProperty("upigpay", "0");
            mainObject.addProperty("upiphonepe", "1");
            mainObject.addProperty("otherupi", "0");
        } else {
            mainObject.addProperty("upigpay", "0");
            mainObject.addProperty("upiphonepe", "0");
            mainObject.addProperty("otherupi", "1");
        }
        Log.d("JsonObject", "addFundTransaction " + mainObject);

        if (extras.get("Status").toString().equalsIgnoreCase("SUCCESS")) {
            Call<JsonObject> call = controller.getInstance().getApi().addMoneyViaUpi(mainObject);
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if (response.body().get("status").getAsBoolean()) {
                        Toast.makeText(getApplicationContext(), response.body().get("msg").getAsString(), Toast.LENGTH_SHORT).show();
                        //Log.d("addFundResponse", "onResponse: " + response.body().getAsJsonArray());
//                        "result": [
//                        {
//                            "id": "5",
//                                "user_id": "4",
//                                "amount": "100",
//                                "tx_request_number": "2048306",
//                                "txn_id": "PTM0a1d340bf2c7474fb864dc6a07c1340b",
//                                "txn_ref": "84e648ca97c841719ce1e7c860268623",
//                                "insert_date": "2021-11-19 14:44:37",
//                                "payment_method": "Others",
//                                "paid_upi": "",
//                                "fund_status": "0",
//                                "deposit_type": "0",
//                                "reject_remark": ""
//                        },
//                        {
//                            "id": "4",
//                                "user_id": "4",
//                                "amount": "1",
//                                "tx_request_number": "2140259",
//                                "txn_id": "83e3954e34e34a9a9b9f925cefbf27fa",
//                                "txn_ref": "PTM5538f2442a7a4909a176c49e35ebd975",
//                                "insert_date": "2021-11-19 14:27:12",
//                                "payment_method": "Others",
//                                "paid_upi": "",
//                                "fund_status": "0",
//                                "deposit_type": "0",
//                                "reject_remark": ""
//                        }
//    ]
                        setWalletBalance();
                        setAddFundTransactionHist();
                        number.getText().clear();
                    } else {
                        Toast.makeText(getApplicationContext(), response.body().get("msg").getAsString(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "Something went wrong.... Try again later", Toast.LENGTH_SHORT).show();

                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "Payment failed", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isPackageInstalled(String packageName) {
        PackageManager packageManager = getPackageManager();
        try {
            packageManager.getPackageInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setWalletBalance();
        setAddFundTransactionHist();
        addFund = true;
    }
}