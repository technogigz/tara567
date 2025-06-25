package com.myTara567.app.starline;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.myTara567.app.R;
import com.myTara567.app.bid_placer;
import com.myTara567.app.mainGames.bidPlacerModel;

import java.util.ArrayList;

public class starLineBidAdapter extends RecyclerView.Adapter<starLineBidAdapter.bidView> {
    ArrayList<bidPlacerModel> bidPlacerModels;
    bid_placer starLineBidPlacer;

    public starLineBidAdapter(ArrayList<bidPlacerModel> bidPlacerModels, bid_placer obj) {
        this.bidPlacerModels = bidPlacerModels;
        this.starLineBidPlacer = obj;
    }

    @NonNull
    @Override
    public bidView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.bid, parent, false);
        return new bidView(v);
    }

    @Override
    public void onBindViewHolder(@NonNull bidView holder, @SuppressLint("RecyclerView") int position) {

        bidPlacerModel obj = bidPlacerModels.get(position);
        String openDigit = obj.getDigits();
        String closeDigit = obj.getCloseDigits();
        String points = obj.getPoints();
        String gameTitle = obj.getGameTitle();
        Log.d("AdaptergetGameTitle", "addBid: " + gameTitle);
        // bid_placer_obj.getTotalBid(bidPlacerModels);

        if (gameTitle.equals("Single Pana") || gameTitle.equals("Double Pana") || gameTitle.equals("Triple Pana")) {

            holder.panaLayout.setVisibility(View.GONE);
            holder.openDigitTxt.setText("Pana: ");
            holder.openDigit.setText(openDigit);

        } else {
            if (openDigit.equals("")) {
                holder.digitLayout.setVisibility(View.GONE);
            } else {
                holder.openDigit.setText(openDigit);
            }

            if (closeDigit.equals("")) {
                holder.panaLayout.setVisibility(View.GONE);
            } else {
                holder.pana.setText(closeDigit);
            }
        }
        holder.points.setText(points);

        holder.cancel_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                starLineBidPlacer.getBidRemainBal(obj);
                bidPlacerModels.remove(position);
                notifyDataSetChanged();
            }
        });
    }


    @Override
    public int getItemCount() {
        return bidPlacerModels.size();
    }


    public static class bidView extends RecyclerView.ViewHolder {
        LinearLayout digitLayout, panaLayout, pointsLayout;
        TextView openDigit, pana, points, openDigitTxt;
        TextView cancel_action;

        public bidView(@NonNull View itemView) {
            super(itemView);
            openDigit = itemView.findViewById(R.id.openDigit);
           // openDigitTxt = itemView.findViewById(R.id.digitTxt);
            pana = itemView.findViewById(R.id.pana);
            points = itemView.findViewById(R.id.points);
           // cancel_action = itemView.findViewById(R.id.cancel_action);
            digitLayout = itemView.findViewById(R.id.linearLayout9);
            panaLayout = itemView.findViewById(R.id.linearLayout16);
            //pointsLayout = itemView.findViewById(R.id.linearLayout17);
        }
    }
}
