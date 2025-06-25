package com.myTara567.app.starline;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.myTara567.app.R;

import java.util.ArrayList;

public class starLineGameAdapter extends RecyclerView.Adapter<starLineGameAdapter.starLineGameView> {
    ArrayList<starlineCardModel> starlineCardModels;
    Vibrator vibe;

    public starLineGameAdapter(ArrayList<starlineCardModel> starlineCardModelArrayList) {
        this.starlineCardModels = starlineCardModelArrayList;
    }

    @NonNull
    @Override
    public starLineGameView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.star_line_card, parent, false);
        return new starLineGameView(v);
    }

    @Override
    public void onBindViewHolder(@NonNull starLineGameView holder, int position) {
        starlineCardModel starlineCardModel = starlineCardModels.get(position);

        String msg_status = starlineCardModel.getMarketStatus();
        holder.head.setText(starlineCardModel.getGameName());
//        holder.open.setText(starlineCardModel.getOpenTime());
//        holder.close.setText(starlineCardModel.getCloseTime());
        holder.market.setText(starlineCardModel.getMarket());

        String closeTime = starlineCardModel.getCloseTime();

        if (!msg_status.equals("2")) {
            holder.market.setTextColor(Color.parseColor("#2ac200"));
        } else {
            holder.market.setTextColor(Color.parseColor("#f71000"));
        }

//        if (closeTime.equals("")) {
//            holder.closeLayout.setVisibility(View.INVISIBLE);
//        }
        String openResult = starlineCardModel.getOpenResult();
        if (openResult.equals("")) {
            holder.openResult.setText("XXX-X");
        } else {
            holder.openResult.setText(openResult);
        }


        holder.starLineCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (msg_status.equals("2")) {
                    vibe = (Vibrator) v.getContext().getSystemService(Context.VIBRATOR_SERVICE);
                    vibe.vibrate(100);
                    holder.starLineCard.startAnimation(AnimationUtils.loadAnimation(v.getContext(), R.anim.shake));
                } else {
                    Intent i = new Intent(v.getContext(), StarLineGames.class);
                    i.putExtra("starlineCardModel", starlineCardModel);
                    v.getContext().startActivity(i);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return starlineCardModels.size();
    }

    public static class starLineGameView extends RecyclerView.ViewHolder {
        TextView head, open, close, market, openResult, closeResult;
        LinearLayout closeLayout;
        CardView starLineCard;

        public starLineGameView(@NonNull View itemView) {
            super(itemView);
            starLineCard = itemView.findViewById(R.id.starLineCard);
            head = itemView.findViewById(R.id.textView17);
            open = itemView.findViewById(R.id.desHis);
            close = itemView.findViewById(R.id.closeGameTime);
            market = itemView.findViewById(R.id.market);
            openResult = itemView.findViewById(R.id.openResult);
            closeResult = itemView.findViewById(R.id.closeResult);

        }
    }
}
