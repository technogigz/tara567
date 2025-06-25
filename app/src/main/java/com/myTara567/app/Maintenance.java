package com.myTara567.app;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Maintenance extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maintenance);

        TextView maintenanceText = findViewById(R.id.maintenanceTxt);
        maintenanceText.setText(getIntent().getStringExtra("app_maintainence_msg"));

        Button closeApp = findViewById(R.id.closeApp);
        closeApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAffinity();
            }
        });
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}