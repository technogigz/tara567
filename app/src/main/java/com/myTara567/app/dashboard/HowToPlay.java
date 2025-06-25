package com.myTara567.app.dashboard;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.myTara567.app.R;
import com.myTara567.app.serverApi.controller;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HowToPlay extends AppCompatActivity {
    TextView how_to_play_content, video_link;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.how_to_play);

        how_to_play_content = findViewById(R.id.how_to_play_content);
        video_link = findViewById(R.id.video_link);

        ImageView backPassImage = findViewById(R.id.backPassImage);
        backPassImage.setOnClickListener(v -> {
            onBackPressed();
        });

        JsonObject mainObj = new JsonObject();
        mainObj.addProperty("env_type", "Prod");
        mainObj.addProperty("app_key", getApplicationContext().getSharedPreferences("app_key", MODE_MULTI_PROCESS)
                .getString("app_key", null));

        Call<JsonObject> callback = controller.getInstance().getApi().how_to_play(mainObj);
        callback.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.body().get("status").getAsBoolean()) {
                    JsonArray contentArray = response.body().getAsJsonArray("content");
                    for (int i = 0; i < contentArray.size(); i++) {
                        JsonObject jsonObject = contentArray.get(i).getAsJsonObject();
                        how_to_play_content.setText(Html.fromHtml(jsonObject.get("how_to_play_content").getAsString()).toString());
                        video_link.setText(jsonObject.get("video_link").getAsString());

                        LinearLayout videoLinkLayout = findViewById(R.id.videoLinkLayout);
                        videoLinkLayout.setOnClickListener(v -> {
                            watchYoutubeVideo(jsonObject.get("video_link").getAsString());
                        });
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Something want wrong....Try again later.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void watchYoutubeVideo(String url) {
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse(url));
        try {
            startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            startActivity(webIntent);
        }
    }
}