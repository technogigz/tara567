package com.myTara567.app;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.myTara567.app.mainGames.bidPlacerModel;

import java.util.ArrayList;

public class bid_adapter extends RecyclerView.Adapter<bid_adapter.bidView> {

    ArrayList<bidPlacerModel> bidPlacerModels;
    bid_placer bid_placer_obj;

    public bid_adapter(ArrayList<bidPlacerModel> bidPlacerModels, bid_placer obj) {
        this.bidPlacerModels = bidPlacerModels;
        this.bid_placer_obj = obj;
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
        String session = obj.getSession();
        Log.d("AdaptergetGameTitle", "addBid: " + gameTitle);

        // bid_placer_obj.getTotalBid(bidPlacerModels);

        if (gameTitle.equals("Jodi Digit")) {
            holder.tv_game_type_value.setText("--");
        } else {
            holder.tv_game_type_value.setText(obj.getSession());
        }

        if (gameTitle.equals("Single Pana") || gameTitle.equals("Double Pana") || gameTitle.equals("Triple Pana")) {

//            holder.panaLayout.setVisibility(View.GONE);
//            holder.openDigitTxt.setText("Pana: ");

            if (session.equals("OPEN")) {
                holder.openDigit.setText(openDigit);
            } else {
                holder.openDigit.setText(openDigit);
            }

        } else if (gameTitle.equals("Single Digit")) {
           // holder.panaLayout.setVisibility(View.GONE);
            if (session.equals("CLOSE")) {
                holder.openDigit.setText(openDigit);
            } else {
                holder.openDigit.setText(openDigit);
            }
        } else if (gameTitle.equals("Full Sangam")) {
            if (session.equals("CLOSE")) {
                holder.openDigit.setText(openDigit);
            } else {
                holder.openDigit.setText(openDigit);
            }

//            if (openDigit.equals("")) {
//                holder.digitLayout.setVisibility(View.GONE);
//            } else {
//                //holder.openDigitTxt.setText("Open Pana: ");
//                holder.openDigit.setText(openDigit);
//            }
//
//            if (closeDigit.equals("")) {
//               // holder.panaLayout.setVisibility(View.GONE);
//            } else {
//                //holder.panaTxt.setText("Close Pana: ");
//                holder.pana.setText(closeDigit);
//            }
        } else {
            if (openDigit.equals("")) {
                holder.digitLayout.setVisibility(View.GONE);
            } else {

                holder.openDigit.setText(openDigit);
            }

            if (closeDigit.equals("")) {
               // holder.panaLayout.setVisibility(View.GONE);
            } else {
               // holder.pana.setText(closeDigit);
            }
        }
        holder.points.setText(points);

        holder.cancel_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bid_placer_obj.getBidRemainBal(obj);
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
        TextView openDigit, pana, points, openDigitTxt, panaTxt, tv_game_type_value;
        ImageView cancel_action;

        public bidView(@NonNull View itemView) {
            super(itemView);
            openDigit = itemView.findViewById(R.id.openDigit);
           // openDigitTxt = itemView.findViewById(R.id.digitTxt);
            pana = itemView.findViewById(R.id.pana);
            tv_game_type_value = itemView.findViewById(R.id.tv_game_type_value);
           // panaTxt = itemView.findViewById(R.id.panaTxt);
            points = itemView.findViewById(R.id.points);
            cancel_action = itemView.findViewById(R.id.cancel_action);
            digitLayout = itemView.findViewById(R.id.linearLayout9);
           // panaLayout = itemView.findViewById(R.id.linearLayout16);
           // pointsLayout = itemView.findViewById(R.id.linearLayout17);
        }
    }
}
