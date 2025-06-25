package com.myTara567.app.dashboard.allbis.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.myTara567.app.R;
import com.myTara567.app.dashboard.bidHistoryModel;

import java.util.ArrayList;

public class bidHistoryAdapter extends RecyclerView.Adapter<bidHistoryAdapter.bidHistoryView> {

    ArrayList<bidHistoryModel> bidHistoryModels;
    String className;

    public bidHistoryAdapter(String className, ArrayList<bidHistoryModel> bidHistoryModels) {
        this.bidHistoryModels = bidHistoryModels;
        this.className = className;

    }

    @NonNull
    @Override
    public bidHistoryView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.bid_hist_card, parent, false);
        return new bidHistoryView(v);
    }

    @Override
    public void onBindViewHolder(@NonNull bidHistoryView holder, int position) {


        bidHistoryModel bidHistoryModel = bidHistoryModels.get(position);
        String digits = bidHistoryModel.getSession().equals("OPEN")
                ? bidHistoryModel.getOpenDigits()
                : bidHistoryModel.getCloseDigits();

        holder.game_name.setText(bidHistoryModel.getGame_name());
        holder.closedigits.setText(bidHistoryModel.getPana() + ", "  + bidHistoryModel.getSession() +  ": " + digits);

        //for session details
//        String sessionDetails = bidHistoryModel.getSession();
//        if (!sessionDetails.equals("")) {
//           // holder.session.setText(sessionDetails);
//        } else {
//            //holder.sessionLayout.setVisibility(View.GONE);
//        }
//
//        if (sessionDetails.equals("Open")) {
//            // holder.close.setVisibility(View.GONE);
//            //holder.open.setVisibility(View.VISIBLE);
//            if (bidHistoryModel.getPana().equals("Single Digit")) {
//                // holder.open.setVisibility(View.VISIBLE);
//                if (className.equals("starLineGameBidHistory")) {
//                 //   holder.openDigitTxt.setText("Digit: ");
//                    // holder.sessionLayout.setVisibility(View.GONE);
//                } else {
//                 //   holder.openDigitTxt.setText("Open Digit: ");
//                    // holder.sessionLayout.setVisibility(View.VISIBLE);
//                }
//              //  holder.opendigits.setText(bidHistoryModel.getOpenDigits());
//            } else if (bidHistoryModel.getPana().equals("Single Pana") || bidHistoryModel.getPana().equals("Double Pana") || bidHistoryModel.getPana().equals("Triple Pana")) {
//                if (className.equals("starLineGameBidHistory")) {
//                 //   holder.openDigitTxt.setText("Pana: ");
//                    //  holder.sessionLayout.setVisibility(View.GONE);
//                } else {
//               //     holder.openDigitTxt.setText("Open Pana: ");
//                    //holder.sessionLayout.setVisibility(View.VISIBLE);
//                }
//              //  holder.opendigits.setText(bidHistoryModel.getOpenDigits());
//            } else if (bidHistoryModel.getPana().equals("Half Sangam")) {
//                // holder.close.setVisibility(View.VISIBLE);
//                //holder.open.setVisibility(View.VISIBLE);
//                //holder.sessionLayout.setVisibility(View.VISIBLE);
//               // holder.openDigitTxt.setText("Open Digit: ");
//               // holder.opendigits.setText(bidHistoryModel.getOpenDigits());
//             //   holder.closeDigitTxt.setText("Close Pana: ");
//                holder.closedigits.setText(bidHistoryModel.getCloseDigits());
//            } else if (bidHistoryModel.getPana().equals("Left Digit") || bidHistoryModel.getPana().equals("Right Digit") || bidHistoryModel.getPana().equals("Jodi Digit")) {
//                if (className.equals("galiDesawarGameBidHistory")) {
//                //    holder.openDigitTxt.setText("Digit: ");
//                    // holder.sessionLayout.setVisibility(View.GONE);
//                }
//               // holder.opendigits.setText(bidHistoryModel.getOpenDigits());
//            }
//
//        } else {
//            // holder.open.setVisibility(View.GONE);
//            // holder.close.setVisibility(View.VISIBLE);
//            if (bidHistoryModel.getPana().equals("Single Digit")) {
//                /// holder.close.setVisibility(View.VISIBLE);
//                // holder.sessionLayout.setVisibility(View.VISIBLE);
//               // holder.closeDigitTxt.setText("Close Digit: ");
//                holder.closedigits.setText(bidHistoryModel.getOpenDigits());
//            } else if (bidHistoryModel.getPana().equals("Single Pana") || bidHistoryModel.getPana().equals("Double Pana") || bidHistoryModel.getPana().equals("Triple Pana")) {
//                //holder.close.setVisibility(View.VISIBLE);
//                //  holder.sessionLayout.setVisibility(View.VISIBLE);
//              //  holder.closeDigitTxt.setText("Close Pana: ");
//                holder.closedigits.setText(bidHistoryModel.getOpenDigits());
//            } else if (bidHistoryModel.getPana().equals("Half Sangam")) {
//                //  holder.open.setVisibility(View.VISIBLE);
//                //  holder.sessionLayout.setVisibility(View.VISIBLE);
//               // holder.openDigitTxt.setText("Close Digit: ");
//               // holder.opendigits.setText(bidHistoryModel.getOpenDigits());
//               // holder.closeDigitTxt.setText("Open Pana: ");
//                holder.closedigits.setText(bidHistoryModel.getCloseDigits());
//            } else if (bidHistoryModel.getPana().equals("Full Sangam")) {
//                // holder.open.setVisibility(View.VISIBLE);
//                //  holder.close.setVisibility(View.VISIBLE);
//              //  holder.openDigitTxt.setText("Open Pana: ");
//              //  holder.closeDigitTxt.setText("Close Pana: ");
//               // holder.opendigits.setText(bidHistoryModel.getOpenDigits());
//                holder.closedigits.setText(bidHistoryModel.getCloseDigits());
//            } else {
//                // holder.sessionLayout.setVisibility(View.GONE);
//                //  holder.close.setVisibility(View.GONE);
//                // holder.open.setVisibility(View.VISIBLE);
//              //  holder.openDigitTxt.setText("Digit: ");
//                //holder.opendigits.setText(bidHistoryModel.getOpenDigits());
//            }
//        }


       // holder.pana.setText("(" + bidHistoryModel.getPana() + ")");
        holder.bid_date.setText(bidHistoryModel.getBid_date());
        holder.points.setText("₹"+bidHistoryModel.getPoints());

    }

    @Override
    public int getItemCount() {
        return bidHistoryModels.size();
    }

    public static class bidHistoryView extends RecyclerView.ViewHolder {
        // LinearLayout open, sessionLayout, close;
        TextView game_name, bid_date, closedigits, points;
        //TextView opendigits, session, pana, openDigitTxt, closeDigitTxt;

        public bidHistoryView(@NonNull View itemView) {
            super(itemView);

            //open = itemView.findViewById(R.id.open);
            // openDigitTxt = itemView.findViewById(R.id.openDigitTxt);
            // sessionLayout = itemView.findViewById(R.id.sessionLayout);
            // close = itemView.findViewById(R.id.close);
            //closeDigitTxt = itemView.findViewById(R.id.closeDigitTxt);
            game_name = itemView.findViewById(R.id.game_name);
            // opendigits = itemView.findViewById(R.id.opendigits);
            // session = itemView.findViewById(R.id.session);
            bid_date = itemView.findViewById(R.id.bid_date);
            closedigits = itemView.findViewById(R.id.closedigits);
            // pana = itemView.findViewById(R.id.pana);
            points = itemView.findViewById(R.id.points);
        }
    }




}
