package com.myTara567.app.starline;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.myTara567.app.R;
import com.myTara567.app.bid_placer;
import com.myTara567.app.mainGames.bidPlacerModel;
import com.myTara567.app.serverApi.controller;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StarLineBidPlacer extends AppCompatActivity implements bid_placer {
    TextView gameTextTitle, chooseDate, closePanaTxt, openDigitTxt, walletBalance;
    EditText points;
    LinearLayout chooseSession, openDigit, closePana;
    ImageView backImage;
    RadioGroup radioGroup;
    starLineBidAdapter bid_adapterList;
    ArrayList<bidPlacerModel> bidPlacerModelArrayList = new ArrayList<>();
    //RadioButton radioBtn, radioOpenButton, radioCloseButton;
    String radioBtnTxt, date;
    int walletBalanceTxt;
    int totalBidBal = 0;
    long systemTime;
    String gameSrt, game_id, title, unique, game_name, openDigits, closeDigits, pointsDigits;
    Button submitBtn, proceedBtn;
    AutoCompleteTextView enterOpenDigitTv, enterCloseDigitTv;
    JsonElement jsonElement;
    JsonObject new_result, result_object, js;
    JsonArray resultArray;
    ArrayList<String> Single_Digit, Single_Pana, Double_Pana, Triple_Pana;
    RecyclerView bidRecycler;
    bid_placer bid_placer_interface_obj;
    String session = "Open";
    String minBidAmt = "0", maxBidAmt = "0";
    boolean bidPlace = true;
    boolean submitBid = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bid_placer);

        bid_placer_interface_obj = this;

        //"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"
        //single digit array
        Single_Digit = new ArrayList<>(Arrays.asList("0", "1", "2", "3", "4", "5", "6", "7", "8", "9"));
        Log.d("single", "onCreate: " + Single_Digit);


        //single pana array
        Single_Pana = new ArrayList<>(Arrays.asList("120", "123", "124", "125", "126", "127", "128", "129", "130", "134", "135", "136", "137", "138", "139", "140", "145", "146", "147", "148", "149", "150", "156", "157", "158", "159", "160", "167", "168", "169", "170", "178", "179", "180", "189", "190", "230", "234", "235", "236", "237", "238", "239", "240", "245", "246", "247", "248", "249", "250", "256", "257", "258", "259", "260", "267", "268", "269", "270", "278", "279", "280", "289", "290", "340", "345", "346", "347", "348", "349", "350", "356", "357", "358", "359", "360", "367", "368", "369", "370", "378", "379", "380", "389", "390", "450", "456", "457", "458", "459", "460", "467", "468", "469", "470", "478", "479", "480", "489", "490", "560", "567", "568", "569", "570", "578", "579", "580", "589", "590", "670", "678", "679", "680", "689", "690", "780", "789", "790", "890"));

        //Double Pana array
        {
            Double_Pana = new ArrayList<>(Arrays.asList("100",
                    "110",
                    "112",
                    "113",
                    "114",
                    "115",
                    "116",
                    "117",
                    "118",
                    "119",
                    "122",
                    "133",
                    "144",
                    "155",
                    "166",
                    "177",
                    "188",
                    "199",
                    "200",
                    "220",
                    "223",
                    "224",
                    "225",
                    "226",
                    "227",
                    "228",
                    "229",
                    "233",
                    "244",
                    "255",
                    "266",
                    "277",
                    "288",
                    "299",
                    "300",
                    "330",
                    "334",
                    "335",
                    "336",
                    "337",


                    "338",
                    "339",
                    "344",
                    "355",
                    "366",
                    "377",
                    "388",
                    "399",
                    "400",
                    "440",


                    "445",
                    "446",
                    "447",
                    "448",
                    "449",
                    "455",
                    "466",
                    "477",
                    "488",


                    "499",
                    "500",
                    "550",
                    "556",
                    "557",
                    "558",
                    "559",
                    "566",
                    "577",
                    "588",

                    "599",
                    "600",
                    "660",
                    "667",
                    "668",
                    "669",
                    "677",
                    "688",
                    "699",
                    "700",

                    "770",
                    "778",
                    "779",
                    "788",
                    "799",
                    "800",
                    "880",
                    "889",
                    "899",
                    "900",
                    "990")
            );
        }

        //triple pana array
        {
            Triple_Pana = new ArrayList<>(Arrays.asList("000",
                    "111",
                    "222",
                    "333",
                    "444",
                    "555",
                    "666",
                    "777",
                    "888",
                    "999"));
        }


        Log.d("Double_Pana", "onCreate: " + Double_Pana);

        title = getIntent().getStringExtra("Title");

        starlineCardModel gameDataModel = (starlineCardModel) getIntent().getSerializableExtra("starLineDataObject");
//        gameSrt = gameDataModel.getTime_srt();
        game_id = gameDataModel.getGameId();
        game_name = gameDataModel.getGameName();
        Log.d("gamDataObject", "onCreate: " + gameSrt);
        Log.d("gamDataObject", "onCreate: " + game_name);

        systemTime = System.currentTimeMillis() / 1000;
        Log.d("time", "onCreate: " + systemTime / 1000);

        //        "Single Digit"
//        "Jodi Digit"
//        "Single Pana"
//        "Double Pana"
//        "Triple Pana"
//        "Half Sangam"
//        "Full Sangam"

        String appKey = getSharedPreferences("app_key", Context.MODE_MULTI_PROCESS).getString("app_key", "");
        Log.d("login appkey", "onCreate: " + appKey);

        unique = getApplicationContext().getSharedPreferences("unique_token", Context.MODE_MULTI_PROCESS).getString("unique_token", "");
        Log.d("signUp editor", "home: " + unique);

        bidRecycler = findViewById(R.id.bidRecycler);
        backImage = findViewById(R.id.gamesBackImageView);
        gameTextTitle = findViewById(R.id.gameTextTitle);
        walletBalance = findViewById(R.id.walletBalance);
        submitBtn = findViewById(R.id.submitBtn);
        proceedBtn = findViewById(R.id.proceed);
        chooseDate = findViewById(R.id.chooseDate);
        //chooseSession = findViewById(R.id.linearLayout11);
        //openDigit = findViewById(R.id.linearLayout12);
       // closePana = findViewById(R.id.linearLayout14);
        //closePanaTxt = findViewById(R.id.closePana);
       // openDigitTxt = findViewById(R.id.openDigitTxt);
       // radioGroup = findViewById(R.id.radio);
//        radioOpenButton = findViewById(R.id.open);
//        radioCloseButton = findViewById(R.id.close);
        enterOpenDigitTv = findViewById(R.id.enterOpenDigitTv);
        enterCloseDigitTv = findViewById(R.id.enterCloseDigitTv);
        //points = findViewById(R.id.points);

//system = 1635760384
//srt = 1635761700
        chooseSession.setVisibility(View.GONE);
        openDigitTxt.setText("Digit:");
        closePanaTxt.setText("Pana:");
        gameTextTitle.setText(title);
        submitBtn.setVisibility(View.GONE);

        js = new JsonObject();
        js.addProperty("env_type", "Prod");
        js.addProperty("app_key", appKey);
        js.addProperty("unique_token", unique);
        js.addProperty("game_id", game_id);

        Call<JsonObject> call = controller.getInstance().getApi().getCurrentDate(js);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                date = response.body().get("new_date").getAsString();
                minBidAmt = response.body().get("min_bid_amount").getAsString();
                maxBidAmt = response.body().get("max_bid_amount").getAsString();
                chooseDate.setText(date);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Something went wrong.... Try again later", Toast.LENGTH_SHORT).show();
            }
        });

        Call<JsonObject> call1 = controller.getInstance().getApi().getWalletBalance(js);
        call1.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                walletBalanceTxt = response.body().get("wallet_amt").getAsInt();
                walletBalance.setText(String.valueOf(walletBalanceTxt));

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Something went wrong.... Try again later", Toast.LENGTH_SHORT).show();
            }
        });


        switch (title) {

            case "Single Digit": {

                closePana.setVisibility(View.GONE);
                Log.d("systemTime", "onCreate: " + systemTime);
                Log.d("systemTime", "onCreate: " + gameSrt);

                //checkSession(title);

                setAutoTextAdapter(Single_Digit, enterOpenDigitTv);

                proceedBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        RecyclerView.LayoutManager layoutManager =
                                new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                        bidRecycler.setLayoutManager(layoutManager);

                        openDigits = enterOpenDigitTv.getText().toString().trim();
                        closeDigits = enterCloseDigitTv.getText().toString().trim();
                        pointsDigits = points.getText().toString().trim();

                        Log.d("pointsDigits", "onClick: " + pointsDigits);
                        Log.d("pointsDigits", "onClick: " + maxBidAmt);

                        if (openDigits.equals("")) {
                            Toast.makeText(getApplicationContext(), "Please enter the number", Toast.LENGTH_SHORT).show();
                            return;
                        } else if (pointsDigits.equals("")) {
                            Toast.makeText(getApplicationContext(), "Please enter point", Toast.LENGTH_SHORT).show();
                            return;
                        } else if (points.getText().toString().trim().contains(".") || points.getText().toString().trim().contains(",") ||
                                points.getText().toString().trim().contains("-")) {
                            Toast.makeText(getApplicationContext(), "please enter valid amount", Toast.LENGTH_SHORT).show();
                            return;
                        } else if (!minBidAmt.equals("") && Integer.parseInt(pointsDigits) < Integer.parseInt(minBidAmt)) {
                            Toast.makeText(getApplicationContext(), "Required maximum bid amount is " + minBidAmt, Toast.LENGTH_SHORT).show();
                            return;
                        } else if (!maxBidAmt.equals("") && Integer.parseInt(pointsDigits) > Integer.parseInt(maxBidAmt)) {
                            Toast.makeText(getApplicationContext(), "Required maximum bid amount is " + maxBidAmt, Toast.LENGTH_SHORT).show();
                            return;
                        } else if (!Single_Digit.contains(openDigits)) {
                            Toast.makeText(getApplicationContext(), "Number " + openDigits + " is not valid", Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            //submitBtn.setVisibility(View.VISIBLE);
                            Log.d("openDigits", "onClick: " + openDigits);
                            Log.d("closeDigits", "onClick: " + closeDigits);
                            Log.d("pointsDigits", "onClick: " + pointsDigits);

                            addBid();
                            //updateWalletBalance();

                        }


                        js.add("new_result", new_result);
                        Log.d("json Object", "onCreate: " + js);

                    }
                });

                break;
            }


            case "Single Pana": {
                enterOpenDigitTv.setHint("Enter Pana");
                setAutoTextAdapter(Single_Pana, enterOpenDigitTv);
                onButtonClick(Single_Pana);
                break;
            }

            case "Double Pana": {
                enterOpenDigitTv.setHint("Enter Pana");
                setAutoTextAdapter(Double_Pana, enterOpenDigitTv);
                onButtonClick(Double_Pana);
                break;
            }

            case "Triple Pana": {
                enterOpenDigitTv.setHint("Enter Pana");
                setAutoTextAdapter(Triple_Pana, enterOpenDigitTv);
                onButtonClick(Triple_Pana);

                break;
            }


        }

        Log.d("JsonObject", "onCreate: " + js);
        Log.d("JsonObject", "onCreate: " + resultArray);

        submitBtn.setOnClickListener(v -> {
            if (bidPlace) {
                View view = LayoutInflater.from(StarLineBidPlacer.this).inflate(R.layout.bid_placeer_dialog_box, null);
                AlertDialog.Builder alertDialogBuilder = new
                        AlertDialog.Builder(StarLineBidPlacer.this);

                Log.d("TAG", "total Bid: " + bidPlacerModelArrayList.size());
                Log.d("TAG", "total points: " + getTotalPoints());
                Log.d("TAG", "session: " + session);

                TextView gameTitle = view.findViewById(R.id.title);
                gameTitle.setText(title);
                TextView totalBid = view.findViewById(R.id.totalBid);
                totalBid.setText(String.valueOf(bidPlacerModelArrayList.size()));
                TextView totalPoints = view.findViewById(R.id.totalPoints);
                totalPoints.setText(String.valueOf(getTotalPoints()));
                TextView gameType = view.findViewById(R.id.gameType);
                if (session.equals("Open")) {
                    gameType.setText("-");
                }
                alertDialogBuilder.setView(view);
                alertDialogBuilder.setCancelable(false);
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

                Button cancel = view.findViewById(R.id.cancel_button);
                cancel.setOnClickListener(v1 -> {
                    alertDialog.cancel();
                    bidPlace = true;
                });
                Button submit = view.findViewById(R.id.submitBtn);
                submit.setOnClickListener(v12 -> {
                    if (submitBid) {
                        getTotalBid();
                        alertDialog.cancel();
                        submitBid = false;
                    }
                });
                bidPlace = false;
            }
        });

        backImage.setOnClickListener(v -> onBackPressed());
    }

    private void onButtonClick(ArrayList<String> panaList) {
        proceedBtn.setOnClickListener(v -> {
            RecyclerView.LayoutManager layoutManager =
                    new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
            bidRecycler.setLayoutManager(layoutManager);

            openDigits = enterOpenDigitTv.getText().toString().trim();
            closeDigits = enterCloseDigitTv.getText().toString().trim();
            pointsDigits = points.getText().toString().trim();

            if (openDigits.equals("")) {
                Toast.makeText(getApplicationContext(), "Please enter the number", Toast.LENGTH_SHORT).show();
                return;
            } else if (pointsDigits.equals("")) {
                Toast.makeText(getApplicationContext(), "Please enter point", Toast.LENGTH_SHORT).show();
                return;
            } else if (points.getText().toString().trim().contains(".") || points.getText().toString().trim().contains(",") ||
                    points.getText().toString().trim().contains("-")) {
                Toast.makeText(getApplicationContext(), "please enter valid amount", Toast.LENGTH_SHORT).show();
                return;
            } else if (!minBidAmt.equals("") && Integer.parseInt(pointsDigits) < Integer.parseInt(minBidAmt)) {
                Toast.makeText(getApplicationContext(), "Required maximum bid amount is " + minBidAmt, Toast.LENGTH_SHORT).show();
                return;
            } else if (!maxBidAmt.equals("") && Integer.parseInt(pointsDigits) > Integer.parseInt(maxBidAmt)) {
                Toast.makeText(getApplicationContext(), "Required maximum bid amount is " + maxBidAmt, Toast.LENGTH_SHORT).show();
                return;
            } else if (!panaList.contains(openDigits)) {
                Toast.makeText(getApplicationContext(), "Number " + openDigits + " is not valid", Toast.LENGTH_SHORT).show();
                return;
            } else {
                Log.d("openDigits", "onClick: " + openDigits);
                Log.d("closeDigits", "onClick: " + closeDigits);
                Log.d("pointsDigits", "onClick: " + pointsDigits);


                addBid();
                // updateWalletBalance();

            }


            js.add("new_result", new_result);
            Log.d("json Object", "onCreate: " + js);
        });
    }

    private void updateWalletBalance() {
        int remainWltBalance = walletBalanceTxt - Integer.parseInt(pointsDigits);
        String dueWalletBalance = String.valueOf(remainWltBalance);
        //totalBidBal -= Integer.parseInt(pointsDigits);
        //Log.d("updateWalletBalance", "updateWalletBalance: " + totalBidBal);
        walletBalanceTxt = Integer.parseInt(dueWalletBalance);
        walletBalance.setText(dueWalletBalance);
    }

    private void setAutoTextAdapter(ArrayList<String> arrayList, AutoCompleteTextView DigitTv) {
        closePana.setVisibility(View.GONE);
        if (title.equals("Single Pana") || title.equals("Double Pana") || title.equals("Triple Pana")) {
            openDigitTxt.setText("Pana:");
        } else {
            openDigitTxt.setText("Digit:");
        }
        //lToast.makeText(getApplicationContext(), "You fucked here", Toast.LENGTH_SHORT).show();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, arrayList);
        DigitTv.setAdapter(adapter);
        DigitTv.setThreshold(1);//start searching from 1 character
        DigitTv.setAdapter(adapter);   //set the adapter for displaying country name list

    }

    private void addBid() {

        bidPlacerModel bidPlacerModel = new bidPlacerModel();
        if (Integer.parseInt(points.getText().toString().trim()) > walletBalanceTxt
                || walletBalanceTxt == 0 || walletBalanceTxt < 0) {
            Toast.makeText(getApplicationContext(), "Insufficient balance please refill your account.", Toast.LENGTH_SHORT).show();
            return;
        } else {
            submitBtn.setVisibility(View.VISIBLE);
            bidPlacerModel.setDigits(openDigits);
            bidPlacerModel.setCloseDigits(closeDigits);
            bidPlacerModel.setPoints(pointsDigits);
            bidPlacerModel.setSession(session);
            bidPlacerModel.setGameTitle(title);
            Log.d("getGameTitle", "addBid: " + title);
            bidPlacerModel.setRemainWalletBal(String.valueOf(walletBalanceTxt));
            //Log.d("totalBidBalance1", "updateWalletBalance: " + totalBidBal);

            for (bidPlacerModel obj : bidPlacerModelArrayList) {
                if (obj.getDigits().equals(openDigits)) {
                    Log.d("walletBal", "addBid: " + walletBalanceTxt);
                    walletBalanceTxt = walletBalanceTxt + Integer.parseInt(obj.getPoints());
                    walletBalance.setText(String.valueOf(walletBalanceTxt));
                    bidPlacerModelArrayList.remove(obj);
                    break;

                }
            }

            bidPlacerModelArrayList.add(bidPlacerModel);
            bid_adapterList = new starLineBidAdapter(bidPlacerModelArrayList, bid_placer_interface_obj);
            bidRecycler.setAdapter(bid_adapterList);


            if (!enterOpenDigitTv.getText().equals("")) {
                enterOpenDigitTv.getText().clear();
            }
            if (!enterOpenDigitTv.getText().equals("")) {
                enterCloseDigitTv.getText().clear();
            }
            points.getText().clear();
            updateWalletBalance();
        }
    }

    @Override
    public void getBidRemainBal(bidPlacerModel obj) {
        String points = obj.getPoints();
        String remain = obj.getRemainWalletBal();
        Log.d("getData", "getBidRemainBal: " + walletBalanceTxt);
        Log.d("getData", "getBidRemainBal: " + points);


        int current = walletBalanceTxt + Integer.parseInt(points);
        walletBalanceTxt = current;
        walletBalance.setText(String.valueOf(current));

        totalBidBal = getTotalPoints() - Integer.parseInt(points);


        Log.d("bidarray", "getBidRemainBal: " + bidPlacerModelArrayList.size());
        Log.d("bidarray", "getBidRemainBal: " + bidPlacerModelArrayList);
        if (bidPlacerModelArrayList.size() == 1) {

            submitBtn.setVisibility(View.GONE);
        }

    }

    public void getTotalBid() {
        addJsonObject();
        for (bidPlacerModel obj : bidPlacerModelArrayList) {
            Log.d("arrayList", "getTotalBid: " + bidPlacerModelArrayList);

            JsonObject gameObj = new JsonObject();
            Log.d("object digits", "getTotalBid: " + obj.getDigits());
            gameObj.addProperty("digits", obj.getDigits());
            gameObj.addProperty("closedigits", obj.getCloseDigits());
            gameObj.addProperty("points", obj.getPoints());
            gameObj.addProperty("session", session);

            resultArray.add(gameObj);
        }


        new_result.add("result", resultArray);
        js.add("new_result", new_result);


        Call<JsonObject> call = controller.getInstance().getApi().starLineSubmitBid(js);
        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                String msg = response.body().get("msg").getAsString();
                boolean status = response.body().get("status").getAsBoolean();
                if (status) {
                    bidPlacerModelArrayList.clear();
                    bid_adapterList.notifyDataSetChanged();
                    submitBtn.setVisibility(View.GONE);

                    View v = LayoutInflater.from(StarLineBidPlacer.this).inflate(R.layout.bid_final_dialog_box, null);
                    AlertDialog.Builder alertDialogBuilder = new
                            AlertDialog.Builder(StarLineBidPlacer.this);

                    TextView message = v.findViewById(R.id.msg);
                    message.setText(msg);

                    alertDialogBuilder.setView(v);
                    alertDialogBuilder.setCancelable(false);
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                    alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

                    Button playAgain = v.findViewById(R.id.playAgain);
                    playAgain.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.cancel();
                            submitBid = true;
                            bidPlace = true;
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), response.body().get("msg").getAsString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Something went wrong.... Try again later", Toast.LENGTH_SHORT).show();

            }
        });

        Log.d("js digits", "getTotalBid: " + js);
        Log.d("object digits", "getTotalBid: " + resultArray);


    }

    private void addJsonObject() {
        new_result = new JsonObject();
        // result_object = new JsonObject();
        resultArray = new JsonArray();
        new_result.addProperty("unique_token", unique);
        new_result.addProperty("Gamename", game_name);
        new_result.addProperty("totalbit", String.valueOf(getTotalPoints()));
        new_result.addProperty("gameid", game_id);
        new_result.addProperty("pana", title);
        new_result.addProperty("bid_date", date);
        new_result.addProperty("session", session);

    }

    private int getTotalPoints() {
        int totalBidBal = 0;
        for (bidPlacerModel obj : bidPlacerModelArrayList) {
            Log.d("modelObj", "getTotalPoints: " + obj);
            Log.d("arrayList", "getTotalBid: " + bidPlacerModelArrayList);

            Log.d("object digits", "getTotalBid: " + obj.getPoints());

            totalBidBal += Integer.parseInt(obj.getPoints());

        }
        return totalBidBal;
    }
}