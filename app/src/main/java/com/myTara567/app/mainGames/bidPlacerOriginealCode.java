package com.myTara567.app.mainGames;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.myTara567.app.R;
import com.myTara567.app.bid_adapter;
import com.myTara567.app.bid_placer;
import com.myTara567.app.dashboard.gameDataModel;
import com.myTara567.app.serverApi.controller;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class bidPlacerOriginealCode extends AppCompatActivity implements bid_placer {
    TextView gameTextTitle, chooseDate, walletBalance,SelectDate,tvOpenKamalDay,tvCloseKamalDay;
    EditText points;
    ConstraintLayout chooseSession, openDigit, closePana;
    ImageView backImage,mSelectTime;
    RadioGroup radioGroup;
    bid_adapter bid_adapterList;
    ArrayList<bidPlacerModel> bidPlacerModelArrayList = new ArrayList<>();
    RadioButton radioOpenButton, radioCloseButton;
    String radioBtnTxt, date, minBidAmt = "0", maxBidAmt = "0";
    int walletBalanceTxt;
    int totalBidBal;
    long systemTime;
    String gameSrt, game_id, title, unique, game_name, openDigits, closeDigits, pointsDigits;
    Button submitBtn, proceedBtn;
    AutoCompleteTextView enterOpenDigitTv, enterCloseDigitTv;
    JsonObject new_result, js;
    JsonArray resultArray;
    ArrayList<String> Single_Digit, Jodi_Digit, Single_Pana, Double_Pana, Triple_Pana, Half_Sangam, Full_Sangam;
    RecyclerView bidRecycler;
    bid_placer bid_placer_interface_obj;
    boolean bidPlace = true;
    boolean submitBid = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bid_placer);

        bid_placer_interface_obj = this;

        //single digit array
        Single_Digit = new ArrayList<>(Arrays.asList("0", "1", "2", "3", "4", "5", "6", "7", "8", "9"));
        Log.d("single", "onCreate: " + Single_Digit);

        //jodi digit array
        Jodi_Digit = new ArrayList<>(Arrays.asList("00", "01", "02", "03", "04", "05", "06", "07", "08", "09"));
        for (int i = 10; i < 100; i++) {
            Jodi_Digit.add(String.valueOf(i));
        }
        Log.d("Jodi_Digit", "onCreate: " + Jodi_Digit);

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

        //half sangam array
        {
            Half_Sangam = new ArrayList<>(Arrays.asList("000",
                    "100",
                    "110",
                    "111",
                    "112",
                    "113",
                    "114",
                    "115",
                    "116",
                    "117",
                    "118",
                    "119",
                    "120",
                    "122",
                    "123",
                    "124",
                    "125",
                    "126",
                    "127",
                    "128",
                    "129",
                    "130",
                    "133",
                    "134",
                    "135",
                    "136",
                    "137",
                    "138",
                    "139",
                    "140",
                    "144",
                    "145",
                    "146",
                    "147",
                    "148",
                    "149",
                    "150",
                    "155",
                    "156",
                    "157",
                    "158",
                    "159",
                    "160",
                    "166",
                    "167",
                    "168",
                    "169",
                    "170",
                    "177",
                    "178",
                    "179",
                    "180",
                    "188",
                    "189",
                    "190",
                    "199",
                    "200",
                    "220",
                    "222",
                    "223",
                    "224",
                    "225",
                    "226",
                    "227",
                    "228",
                    "229",
                    "230",
                    "233",
                    "234",
                    "235",
                    "236",
                    "237",
                    "238",
                    "239",
                    "240",
                    "244",
                    "245",
                    "246",
                    "247",
                    "248",
                    "249",
                    "250",
                    "255",
                    "256",
                    "257",
                    "258",
                    "259",
                    "260",
                    "266",
                    "267",
                    "268",
                    "269",
                    "270",
                    "277",
                    "278",
                    "279",
                    "280",
                    "288",
                    "289",
                    "290",
                    "299",
                    "300",
                    "330",
                    "333",
                    "334",
                    "335",
                    "336",
                    "337",
                    "338",
                    "339",
                    "340",
                    "344",
                    "345",
                    "346",
                    "347",
                    "348",
                    "349",
                    "350",
                    "355",
                    "356",
                    "357",
                    "358",
                    "359",
                    "360",
                    "366",
                    "367",
                    "368",
                    "369",
                    "370",
                    "377",
                    "378",
                    "379",
                    "380",
                    "388",
                    "389",
                    "390",
                    "399",
                    "400",
                    "440",
                    "444",
                    "445",
                    "446",
                    "447",
                    "448",
                    "449",
                    "450",
                    "455",
                    "456",
                    "457",
                    "458",
                    "459",
                    "460",
                    "466",
                    "467",
                    "468",
                    "469",
                    "470",
                    "477",
                    "478",
                    "479",
                    "480",
                    "488",
                    "489",
                    "490",
                    "499",
                    "500",
                    "550",
                    "555",
                    "556",
                    "557",
                    "558",
                    "559",
                    "560",
                    "566",
                    "567",
                    "568",
                    "569",
                    "570",
                    "577",
                    "578",
                    "579",
                    "580",
                    "588",
                    "589",
                    "590",
                    "599",
                    "600",
                    "660",
                    "666",
                    "667",
                    "668",
                    "669",
                    "670",
                    "677",
                    "678",
                    "679",
                    "680",
                    "688",
                    "689",
                    "690",
                    "699",
                    "700",
                    "770",
                    "777",
                    "778",
                    "779",
                    "780",
                    "788",
                    "789",
                    "790",
                    "799",
                    "800",
                    "880",
                    "888",
                    "889",
                    "890",
                    "899",
                    "900",
                    "990",
                    "999"));
        }

        //full sangam array
        {
            Full_Sangam = new ArrayList<>(Arrays.asList("000",
                    "100",
                    "110",
                    "111",
                    "112",
                    "113",
                    "114",
                    "115",
                    "116",
                    "117",
                    "118",
                    "119",
                    "120",
                    "122",
                    "123",
                    "124",
                    "125",
                    "126",
                    "127",
                    "128",
                    "129",
                    "130",
                    "133",
                    "134",
                    "135",
                    "136",
                    "137",
                    "138",
                    "139",
                    "140",
                    "144",
                    "145",
                    "146",
                    "147",
                    "148",
                    "149",
                    "150",
                    "155",
                    "156",
                    "157",
                    "158",
                    "159",
                    "160",
                    "166",
                    "167",
                    "168",
                    "169",
                    "170",
                    "177",
                    "178",
                    "179",
                    "180",
                    "188",
                    "189",
                    "190",
                    "199",
                    "200",
                    "220",
                    "222",
                    "223",
                    "224",
                    "225",
                    "226",
                    "227",
                    "228",
                    "229",
                    "230",
                    "233",
                    "234",
                    "235",
                    "236",
                    "237",
                    "238",
                    "239",
                    "240",
                    "244",
                    "245",
                    "246",
                    "247",
                    "248",
                    "249",
                    "250",
                    "255",
                    "256",
                    "257",
                    "258",
                    "259",
                    "260",
                    "266",
                    "267",
                    "268",
                    "269",
                    "270",
                    "277",
                    "278",
                    "279",
                    "280",
                    "288",
                    "289",
                    "290",
                    "299",
                    "300",
                    "330",
                    "333",
                    "334",
                    "335",
                    "336",
                    "337",
                    "338",
                    "339",
                    "340",
                    "344",
                    "345",
                    "346",
                    "347",
                    "348",
                    "349",
                    "350",
                    "355",
                    "356",
                    "357",
                    "358",
                    "359",
                    "360",
                    "366",
                    "367",
                    "368",
                    "369",
                    "370",
                    "377",
                    "378",
                    "379",
                    "380",
                    "388",
                    "389",
                    "390",
                    "399",
                    "400",
                    "440",
                    "444",
                    "445",
                    "446",
                    "447",
                    "448",
                    "449",
                    "450",
                    "455",
                    "456",
                    "457",
                    "458",
                    "459",
                    "460",
                    "466",
                    "467",
                    "468",
                    "469",
                    "470",
                    "477",
                    "478",
                    "479",
                    "480",
                    "488",
                    "489",
                    "490",
                    "499",
                    "500",
                    "550",
                    "555",
                    "556",
                    "557",
                    "558",
                    "559",
                    "560",
                    "566",
                    "567",
                    "568",
                    "569",
                    "570",
                    "577",
                    "578",
                    "579",
                    "580",
                    "588",
                    "589",
                    "590",
                    "599",
                    "600",
                    "660",
                    "666",
                    "667",
                    "668",
                    "669",
                    "670",
                    "677",
                    "678",
                    "679",
                    "680",
                    "688",
                    "689",
                    "690",
                    "699",
                    "700",
                    "770",
                    "777",
                    "778",
                    "779",
                    "780",
                    "788",
                    "789",
                    "790",
                    "799",
                    "800",
                    "880",
                    "888",
                    "889",
                    "890",
                    "899",
                    "900",
                    "990",
                    "999"));
        }

        Log.d("Double_Pana", "onCreate: " + Double_Pana);

        title = getIntent().getStringExtra("Title");

        gameDataModel gameDataModel = (gameDataModel) getIntent().getSerializableExtra("gamDataObject");
        gameSrt = gameDataModel.getTime_srt();
        game_id = gameDataModel.getGame_id();
        game_name = gameDataModel.getGame_name();
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

        //closePanaTxt = findViewById(R.id.closePana);
        //openDigitTxt = findViewById(R.id.openDigitTxt);
        radioGroup = findViewById(R.id.radio);
        radioOpenButton = findViewById(R.id.open);
        radioCloseButton = findViewById(R.id.close);
        enterOpenDigitTv = findViewById(R.id.enterOpenDigitTv);
        enterCloseDigitTv = findViewById(R.id.enterCloseDigitTv);
        points = findViewById(R.id.points);

        chooseSession = findViewById(R.id.linearLayout11);
        openDigit = findViewById(R.id.linearLayout12);
        closePana = findViewById(R.id.linearLayout14);


        mSelectTime = findViewById(R.id.mSelectTime);
        SelectDate = findViewById(R.id.SelectDate);
        tvOpenKamalDay = findViewById(R.id.tvOpenKamalDay);
        tvCloseKamalDay = findViewById(R.id.tvCloseKamalDay);



//system = 1635760384
//srt = 1635761700
        gameTextTitle.setText(title);
        submitBtn.setVisibility(View.GONE);

        js = new JsonObject();
        js.addProperty("env_type", "Prod");
        js.addProperty("app_key", appKey);
        js.addProperty("unique_token", unique);
        js.addProperty("game_id", game_id);



        mSelectTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomDialog("Sample Title", SelectDate);
            }
        });

        SelectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showCustomDialog("Sample Title", SelectDate);
            }
        });



        Call<JsonObject> call = controller.getInstance().getApi().getCurrentDate(js);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                date = response.body().get("new_date").getAsString();
                minBidAmt = response.body().get("min_bid_amount").getAsString();
                maxBidAmt = response.body().get("max_bid_amount").getAsString();

                Log.d("min", "onClick: " + minBidAmt);
                Log.d("max", "onClick: " + maxBidAmt);
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

                checkSession(title);

                setAutoTextAdapter(Single_Digit, enterOpenDigitTv);

                proceedBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (radioOpenButton.isChecked()) {
                            Log.d("Radio btn text", "onClick: " + radioOpenButton.getText());
                            radioBtnTxt = radioOpenButton.getText().toString();
                        } else {
                            Log.d("Radio btn text", "onClick: " + radioCloseButton.getText());
                            radioBtnTxt = radioCloseButton.getText().toString();
                        }


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
                        } else if (!Single_Digit.contains(openDigits)) {
                            Toast.makeText(getApplicationContext(), "Number " + openDigits + " is not valid", Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            // submitBtn.setVisibility(View.VISIBLE);
                            Log.d("openDigits", "onClick: " + openDigits);
                            Log.d("closeDigits", "onClick: " + closeDigits);
                            Log.d("pointsDigits", "onClick: " + pointsDigits);

                            addBid(title, radioBtnTxt);
                        }

                        js.add("new_result", new_result);
                        Log.d("json Object", "onCreate: " + js);
//                        enterOpenDigitTv.getText().clear();
//                        points.getText().clear();
                    }
                });


                break;
            }

            case "Jodi Digit": {
                chooseSession.setVisibility(View.GONE);
               // openDigitTxt.setText("Digit:");
                closePana.setVisibility(View.GONE);

                setAutoTextAdapter(Jodi_Digit, enterOpenDigitTv);

                proceedBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


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
                        } else if (!Jodi_Digit.contains(openDigits)) {
                            Toast.makeText(getApplicationContext(), "Number " + openDigits + " is not valid", Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            //submitBtn.setVisibility(View.VISIBLE);
                            Log.d("openDigits", "onClick: " + openDigits);
                            Log.d("closeDigits", "onClick: " + closeDigits);
                            Log.d("pointsDigits", "onClick: " + pointsDigits);

                            addBid(title, radioBtnTxt);
//                            updateWalletBalance();

                        }


                        js.add("new_result", new_result);
                        Log.d("json Object", "onCreate: " + js);
                    }
                });
                break;
            }

            case "Single Pana": {
                forPana();
                //forPana(closePana, openDigitTxt, radioGroup, radioOpenButton);
                setAutoTextAdapter(Single_Pana, enterOpenDigitTv);
                onButtonClick(Single_Pana);
                break;
            }

            case "Double Pana": {

                forPana();
                //forPana(openDigit, closePanaTxt, radioGroup, radioOpenButton);
                setAutoTextAdapter(Double_Pana, enterOpenDigitTv);
                onButtonClick(Double_Pana);

                break;
            }

            case "Triple Pana": {

                forPana();
                // forPana(openDigit, closePanaTxt, radioGroup, radioOpenButton);
                setAutoTextAdapter(Triple_Pana, enterOpenDigitTv);
                onButtonClick(Triple_Pana);
                break;
            }

            case "Half Sangam": {

                checkSession(title);
                setAutoTextAdapter(Single_Digit, enterOpenDigitTv);
                setAutoTextAdapter(Half_Sangam, enterCloseDigitTv);
                proceedBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (radioOpenButton.isChecked()) {
                            Log.d("Radio btn text", "onClick: " + radioOpenButton.getText());
                            radioBtnTxt = radioOpenButton.getText().toString();
                        } else {
                            Log.d("Radio btn text", "onClick: " + radioCloseButton.getText());
                            radioBtnTxt = radioCloseButton.getText().toString();
                        }


                        RecyclerView.LayoutManager layoutManager =
                                new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                        bidRecycler.setLayoutManager(layoutManager);

                        openDigits = enterOpenDigitTv.getText().toString().trim();
                        closeDigits = enterCloseDigitTv.getText().toString().trim();
                        pointsDigits = points.getText().toString().trim();

                        if (openDigits.equals("")) {
                            Toast.makeText(getApplicationContext(), "Please enter the number", Toast.LENGTH_SHORT).show();
                            return;
                        } else if (closeDigits.equals("")) {
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
                        } else if (!Half_Sangam.contains(closeDigits)) {
                            Toast.makeText(getApplicationContext(), "Number " + closeDigits + " is not valid", Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            // submitBtn.setVisibility(View.VISIBLE);
                            Log.d("openDigits", "onClick: " + openDigits);
                            Log.d("closeDigits", "onClick: " + closeDigits);
                            Log.d("pointsDigits", "onClick: " + pointsDigits);


                            addBid(title, radioBtnTxt);
                            //updateWalletBalance();
                        }

                        js.add("new_result", new_result);
                        Log.d("json Object", "onCreate: " + js);
                    }
                });
                break;
            }

            case "Full Sangam": {

                chooseSession.setVisibility(View.GONE);
                //openDigitTxt.setText("Open Pana");
                enterOpenDigitTv.setHint("Enter Pana");
                setAutoTextAdapter(Full_Sangam, enterOpenDigitTv);
                setAutoTextAdapter(Full_Sangam, enterCloseDigitTv);
                proceedBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        RecyclerView.LayoutManager layoutManager =
                                new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                        bidRecycler.setLayoutManager(layoutManager);

                        openDigits = enterOpenDigitTv.getText().toString().trim();
                        closeDigits = enterCloseDigitTv.getText().toString().trim();
                        pointsDigits = points.getText().toString().trim();

                        if (openDigits.equals("")) {
                            Toast.makeText(getApplicationContext(), "Please enter the number", Toast.LENGTH_SHORT).show();
                            return;
                        } else if (closeDigits.equals("")) {
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
                        } else if (!Full_Sangam.contains(openDigits)) {
                            Toast.makeText(getApplicationContext(), "Number " + openDigits + " is not valid", Toast.LENGTH_SHORT).show();
                            return;
                        } else if (!Full_Sangam.contains(closeDigits)) {
                            Toast.makeText(getApplicationContext(), "Number " + closeDigits + " is not valid", Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            //submitBtn.setVisibility(View.VISIBLE);
                            Log.d("openDigits", "onClick: " + openDigits);
                            Log.d("closeDigits", "onClick: " + closeDigits);
                            Log.d("pointsDigits", "onClick: " + pointsDigits);

                            addBid(title, radioBtnTxt);
                            //updateWalletBalance();
                        }

                        js.add("new_result", new_result);
                        Log.d("json Object", "onCreate: " + js);
                    }
                });
            }

        }

        Log.d("JsonObject", "onCreate: " + js);
        Log.d("JsonObject", "onCreate: " + resultArray);

        submitBtn.setOnClickListener(v -> {
            if (bidPlace) {
                View view = LayoutInflater.from(bidPlacerOriginealCode.this).inflate(R.layout.bid_placeer_dialog_box, null);
                AlertDialog.Builder alertDialogBuilder = new
                        AlertDialog.Builder(bidPlacerOriginealCode.this);

                TextView gameTitle = view.findViewById(R.id.title);
                gameTitle.setText(title);
                TextView totalBid = view.findViewById(R.id.totalBid);
                totalBid.setText(String.valueOf(bidPlacerModelArrayList.size()));
                TextView totalPoints = view.findViewById(R.id.totalPoints);
                totalPoints.setText(String.valueOf(getTotalPoints()));
                TextView gameType = view.findViewById(R.id.gameType);
                if (radioBtnTxt == null) {
                    gameType.setText("-");
                } else {
                    gameType.setText(radioBtnTxt);
                }
                alertDialogBuilder.setView(view);
                alertDialogBuilder.setCancelable(false);
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

                Button cancel = view.findViewById(R.id.cancel_button);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.cancel();
                        bidPlace = true;
                    }
                });
                Button submit = view.findViewById(R.id.submitBtn);
                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (submitBid) {
                            getTotalBid();
                            alertDialog.cancel();
                            submitBid = false;
                        }
                    }
                });
                bidPlace = false;
            }

        });

        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void checkSession(String title) {

        //for single digit
        if (title.equals("Single Digit")) {
            //Toast.makeText(getApplicationContext(), "on single digit", Toast.LENGTH_SHORT).show();
            if (systemTime <= Integer.parseInt(gameSrt)) {
                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        if (radioOpenButton.isChecked()) {
                           // openDigitTxt.setText("Open Digit:");
                        } else {
                          //  openDigitTxt.setText("Close Digit:");
                        }
                    }
                });

            } else {
                radioOpenButton.setEnabled(false);
                radioCloseButton.setChecked(true);
                if (radioCloseButton.isChecked()) {
                   // openDigitTxt.setText("Close Digit");
                }

            }
        }

        //for pana
        if (title.equals("Single Pana") || title.equals("Double Pana") || title.equals("Triple Pana")) {
            if (systemTime <= Integer.parseInt(gameSrt)) {
               // openDigitTxt.setText("Open Pana:");
                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        if (radioOpenButton.isChecked()) {
                          //  openDigitTxt.setText("Open Pana:");
                        } else {
                           // openDigitTxt.setText("Close Pana:");
                        }
                    }
                });
            } else {
                radioOpenButton.setEnabled(false);
                radioCloseButton.setChecked(true);
                if (radioCloseButton.isChecked()) {
                   // openDigitTxt.setText("Close Pana");
                }
            }
        }

        //for half sangam
        if (title.equals("Half Sangam")) {
            if (systemTime <= Integer.parseInt(gameSrt)) {

                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        if (radioCloseButton.isChecked()) {
                           // openDigitTxt.setText("Close Digit:");
                            //closePanaTxt.setText("Open Pana:");
                        } else {
                            //openDigitTxt.setText("Open Digit:");
                           // closePanaTxt.setText("Close Pana:");
                        }
                    }
                });

            } else {
                radioOpenButton.setEnabled(false);
                radioCloseButton.setChecked(true);
                if (radioCloseButton.isChecked()) {
                  //  openDigitTxt.setText("Close Digit:");
                   // closePanaTxt.setText("Open Pana:");
                }
            }
        }
    }

    private void onButtonClick(ArrayList<String> panaList) {
        proceedBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (radioOpenButton.isChecked()) {
                    Log.d("Radio btn text", "onClick: " + radioOpenButton.getText());
                    radioBtnTxt = radioOpenButton.getText().toString();
                } else {
                    Log.d("Radio btn text", "onClick: " + radioCloseButton.getText());
                    radioBtnTxt = radioCloseButton.getText().toString();
                }

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
                    //submitBtn.setVisibility(View.VISIBLE);
                    Log.d("openDigits", "onClick: " + openDigits);
                    Log.d("closeDigits", "onClick: " + closeDigits);
                    Log.d("pointsDigits", "onClick: " + pointsDigits);


                    addBid(title, radioBtnTxt);
                    // updateWalletBalance();

                }


                js.add("new_result", new_result);
                Log.d("json Object", "onCreate: " + js);
            }
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

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, arrayList);
        DigitTv.setAdapter(adapter);
        DigitTv.setThreshold(1);//start searching from 1 character
        DigitTv.setAdapter(adapter);   //set the adapter for displaying country name list

    }

    private void addBid(String title, String radioBtnTxt) {

        radioOpenButton.setEnabled(false);
        radioCloseButton.setEnabled(false);

        bidPlacerModel bidPlacerModel = new bidPlacerModel();

        if (Integer.parseInt(points.getText().toString().trim()) > walletBalanceTxt
                || walletBalanceTxt == 0 || walletBalanceTxt < 0) {
            Toast.makeText(getApplicationContext(), "Insufficient balance please refill your account.", Toast.LENGTH_SHORT).show();
            return;
        } else {
            submitBtn.setVisibility(View.VISIBLE);
            Log.d("Title", "add bid" + title);
            if (title.equals("Single Digit")) {

                if (radioBtnTxt.equals("Open")) {
                    bidPlacerModel.setDigits(openDigits);
                    bidPlacerModel.setCloseDigits("");
                } else {
                    bidPlacerModel.setDigits(openDigits);
                    bidPlacerModel.setCloseDigits("");
                }
            } else if (title.equals("Jodi Digit")) {
                bidPlacerModel.setDigits(openDigits);
                bidPlacerModel.setCloseDigits("");
            } else if (title.equals("Single Pana") || title.equals("Double Pana") || title.equals("Triple Pana")) {
                if (radioBtnTxt.equals("Open")) {
                    bidPlacerModel.setDigits(openDigits);
                    bidPlacerModel.setCloseDigits("");
                } else {
                    bidPlacerModel.setDigits(openDigits);
                    bidPlacerModel.setCloseDigits("");
                }
            } else if (title.equals("Half Sangam")) {
                if (radioBtnTxt.equals("Open")) {
                    bidPlacerModel.setDigits(openDigits);
                    bidPlacerModel.setCloseDigits(closeDigits);
                } else {
                    bidPlacerModel.setDigits(openDigits);
                    bidPlacerModel.setCloseDigits(closeDigits);
                }
            } else {
                bidPlacerModel.setDigits(openDigits);
                bidPlacerModel.setCloseDigits(closeDigits);
            }

            bidPlacerModel.setPoints(pointsDigits);
            //Log.d("pointsDigit", "addBid: " + pointsDigits);
            bidPlacerModel.setSession(radioBtnTxt);
            bidPlacerModel.setGameTitle(title);
            //Log.d("getGameTitle", "addBid: " + title);
            bidPlacerModel.setRemainWalletBal(String.valueOf(walletBalanceTxt));
            //Log.d("totalBidBalance1", "updateWalletBalance: " + totalBidBal);


            for (bidPlacerModel obj : bidPlacerModelArrayList) {

                if (obj.getDigits().equals(openDigits) || obj.getCloseDigits().equals(openDigits)) {
                    Log.d("walletBal", "addBid: " + walletBalanceTxt);
                    walletBalanceTxt = walletBalanceTxt + Integer.parseInt(obj.getPoints());
                    walletBalance.setText(String.valueOf(walletBalanceTxt));
                    bidPlacerModelArrayList.remove(obj);
                    break;
                }
            }

            bidPlacerModelArrayList.add(bidPlacerModel);

            bid_adapterList = new bid_adapter(bidPlacerModelArrayList, bid_placer_interface_obj);
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

    private void forPana() {
        closePana.setVisibility(View.GONE);
        enterOpenDigitTv.setHint("Enter Pana");
        checkSession(title);

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

        Log.d("getTotalPoints", "getBidRemainBal: " + getTotalPoints());
        totalBidBal = getTotalPoints() - Integer.parseInt(points);


        Log.d("bidarray", "getBidRemainBal: " + bidPlacerModelArrayList.size());
        Log.d("bidarray", "getBidRemainBal: " + bidPlacerModelArrayList);
        if (bidPlacerModelArrayList.size() == 1) {
            radioOpenButton.setEnabled(true);
            radioCloseButton.setEnabled(true);
            checkSession(title);
            submitBtn.setVisibility(View.GONE);
        }

    }

    public void getTotalBid() {
        addJsonObject();
        for (bidPlacerModel obj : bidPlacerModelArrayList) {
            Log.d("arrayList", "getTotalBid: " + bidPlacerModelArrayList);

            JsonObject gameObj = new JsonObject();
            //String Digit = obj.get(i).getDigits();
            Log.d("object digits", "getTotalBid: " + obj.getDigits());
            gameObj.addProperty("digits", obj.getDigits());
            Log.d("getTo", "getTotalBid: " + obj.getCloseDigits());
            gameObj.addProperty("closedigits", obj.getCloseDigits());
            gameObj.addProperty("points", obj.getPoints());
            String session = obj.getSession();
            if (session == null) {
                gameObj.addProperty("session", "");
            } else {
                gameObj.addProperty("session", radioBtnTxt);
            }
            resultArray.add(gameObj);
        }


        new_result.add("result", resultArray);
        js.add("new_result", new_result);


        Call<JsonObject> call = controller.getInstance().getApi().submitBid(js);
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

                    View v = LayoutInflater.from(bidPlacerOriginealCode.this).inflate(R.layout.bid_final_dialog_box, null);
                    AlertDialog.Builder alertDialogBuilder = new
                            AlertDialog.Builder(bidPlacerOriginealCode.this);

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
                            radioOpenButton.setEnabled(true);
                            radioCloseButton.setEnabled(true);
                            checkSession(title);
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
        Log.d("getTotalbal", "addJsonObject: " + getTotalPoints());
        new_result.addProperty("gameid", game_id);
        new_result.addProperty("pana", title);
        new_result.addProperty("bid_date", date);
        if (radioBtnTxt == null) {
            new_result.addProperty("session", "");
        } else {
            new_result.addProperty("session", radioBtnTxt);
        }
    }

    private int getTotalPoints() {
        int totalBidBal = 0;
        for (bidPlacerModel obj : bidPlacerModelArrayList) {
            Log.d("arrayList", "getTotalBid: " + obj);

            Log.d("object digits", "getTotalBid: " + obj.getPoints());

            totalBidBal += Integer.parseInt(obj.getPoints());

        }
        return totalBidBal;
    }


    private void showCustomDialog(String title, final TextView targetTextView) {
        // Create the dialog
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.select_dailog);

        // Make the dialog fullscreen
        if (dialog.getWindow() != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        TextView tvCloseKamalDay = dialog.findViewById(R.id.tvCloseKamalDay);
        TextView tvOpenKamalDay = dialog.findViewById(R.id.tvOpenKamalDay);

        if (tvCloseKamalDay != null && tvOpenKamalDay != null) {
            tvCloseKamalDay.setText("OPEN " + title);
            tvOpenKamalDay.setText("CLOSE " + title);
        } else {
            // Handle the case where the TextView IDs are incorrect or missing
            Log.e("CustomDialog", "TextView IDs not found in the layout.");
        }


        // Set up close button
        ImageView closeIcon = dialog.findViewById(R.id.closeIcon);
        if (closeIcon != null) {
            closeIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    dialog.dismiss();
                }
            });
        }
        // Set up Open Kamal Day button
        CardView openKamalDayButton = dialog.findViewById(R.id.cardOpenKamalDay);
        if (openKamalDayButton != null) {
            openKamalDayButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Set value to the TextView dynamically
                    if (targetTextView != null) {
                        targetTextView.setText("OPEN");
                    }
                    dialog.dismiss(); // Close dialog
                }
            });
        }

        // Set up Close Kamal Day button
        CardView closeKamalDayButton = dialog.findViewById(R.id.cardCloseKamalDay);
        if (closeKamalDayButton != null) {
            closeKamalDayButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Set value to the TextView dynamically
                    if (targetTextView != null) {
                        targetTextView.setText("CLOSE");
                    }
                    dialog.dismiss(); // Close dialog
                }
            });
        }

        dialog.show();
    }
}