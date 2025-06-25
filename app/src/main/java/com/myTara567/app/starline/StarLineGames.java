package com.myTara567.app.starline;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.myTara567.app.R;

public class StarLineGames extends AppCompatActivity {
    TextView gameTitle;
    ImageView back, Single_Digit, Single_Pana, Double_Pana, Triple_Pana;
    ConstraintLayout constraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.star_line_games);

        starlineCardModel gameDataModel = (starlineCardModel) getIntent().getSerializableExtra("starlineCardModel");


        Log.d("game_id", "onCreate: " + gameDataModel.getOpenTime());
        Log.d("game_id", "onCreate: " + gameDataModel.getCloseTime());
        Log.d("game_id", "onCreate: " + gameDataModel.getCloseResult());
        Log.d("game_id", "onCreate: " + gameDataModel.getGameName());


        constraintLayout = findViewById(R.id.constraintLayout14);
        back = findViewById(R.id.gamesBackImageView);
        Single_Digit = findViewById(R.id.Single_Digit);
        Single_Digit = findViewById(R.id.Single_Digit);
        Single_Pana = findViewById(R.id.Single_Pana);
        Double_Pana = findViewById(R.id.Double_Pana);
        Triple_Pana = findViewById(R.id.Triple_Pana);
        gameTitle = findViewById(R.id.gameTitle);

        gameTitle.setText(gameDataModel.getGameName());

        back.setOnClickListener(v -> onBackPressed());


        Single_Digit.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(), StarLineBidPlacer.class);
            i.putExtra("Title", "Single Digit");
            i.putExtra("starLineDataObject", gameDataModel);
            startActivity(i);
        });

        Single_Pana.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(), StarLineBidPlacer.class);
            i.putExtra("Title", "Single Pana");
            i.putExtra("starLineDataObject", gameDataModel);
            startActivity(i);
        });

        Double_Pana.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(), StarLineBidPlacer.class);
            i.putExtra("Title", "Double Pana");
            i.putExtra("starLineDataObject", gameDataModel);
            startActivity(i);
        });

        Triple_Pana.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(), StarLineBidPlacer.class);
            i.putExtra("Title", "Triple Pana");
            i.putExtra("starLineDataObject", gameDataModel);
            startActivity(i);
        });


    }
}