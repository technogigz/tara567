package com.myTara567.app.Wallet;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.myTara567.app.R;

import java.util.ArrayList;

public class add_fund_hist_adapter extends RecyclerView.Adapter<add_fund_hist_adapter.walletHistView> {
    ArrayList<wallet_withdrawal_hist_model> modelArrayList;

    public add_fund_hist_adapter(ArrayList<wallet_withdrawal_hist_model> modelArrayList) {
        this.modelArrayList = modelArrayList;
    }

    @NonNull
    @Override
    public walletHistView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.withdrawal_hist_card, parent, false);
        return new walletHistView(v);
    }

    @Override
    public void onBindViewHolder(@NonNull walletHistView holder, int position) {
        wallet_withdrawal_hist_model model = modelArrayList.get(position);

        holder.amount.setText(model.getRequest_amount());
        holder.id.setText(model.getRequest_number());
        holder.date.setText(model.getInsert_date());

        if (model.getRemark().equals("")) {
            holder.remarkLayout.setVisibility(View.GONE);
        } else {
            holder.remark.setText(model.getRemark());
        }

        if (model.getRequest_status().equals("0")) {
            holder.status.setText("Pending");
            holder.pointsTxt.setTextColor(Color.parseColor("#FFA500"));
            holder.status.setTextColor(Color.parseColor("#FFA500"));
            //holder.constraintLayout.setBackgroundResource(R.drawable.add_fund_orange_bg);
            holder.amount.setTextColor(Color.parseColor("#FFA500"));
        } else if (model.getRequest_status().equals("2")) {
            holder.status.setText("Rejected");
            holder.pointsTxt.setTextColor(Color.parseColor("#FF0000"));
            holder.status.setTextColor(Color.parseColor("#FF0000"));
            //holder.constraintLayout.setBackgroundResource(R.drawable.add_fund_red_bg);
            holder.amount.setTextColor(Color.parseColor("#FF0000"));
        } else if (model.getRequest_status().equals("1")) {
            holder.pointsTxt.setTextColor(Color.parseColor("#61CF32"));
            holder.status.setTextColor(Color.parseColor("#61CF32"));
            //holder.constraintLayout.setBackgroundResource(R.drawable.add_fund_green_bg);
            holder.amount.setTextColor(Color.parseColor("#61CF32"));

            if (model.getFund_status().equals("1"))
                holder.status.setText("Auto Approved");
            else {
                holder.status.setText("Approved");
            }

        }


        holder.paymentMethod.setText(model.getPayment_method());


    }

    @Override
    public int getItemCount() {
        return modelArrayList.size();
    }

    public static class walletHistView extends RecyclerView.ViewHolder {
        TextView id, paymentMethod, date, remark, amount, status, pointsTxt;
        CardView cardView;
        LinearLayout remarkLayout;
        ConstraintLayout constraintLayout;

        public walletHistView(@NonNull View itemView) {
            super(itemView);
            constraintLayout = itemView.findViewById(R.id.constraintLayout);
            id = itemView.findViewById(R.id.opendigits);
            paymentMethod = itemView.findViewById(R.id.session);
            date = itemView.findViewById(R.id.game_name);
            remark = itemView.findViewById(R.id.bid_date);
            amount = itemView.findViewById(R.id.points);
            status = itemView.findViewById(R.id.pana);
            cardView = itemView.findViewById(R.id.cardView);
            remarkLayout = itemView.findViewById(R.id.linearLayout1);
            pointsTxt = itemView.findViewById(R.id.pointsTxt);
        }
    }
}
