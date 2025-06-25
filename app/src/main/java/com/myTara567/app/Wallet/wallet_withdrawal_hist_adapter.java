package com.myTara567.app.Wallet;

import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.myTara567.app.R;
import com.myTara567.app.WebView;

import java.util.ArrayList;

public class wallet_withdrawal_hist_adapter extends RecyclerView.Adapter<wallet_withdrawal_hist_adapter.walletHistView> {
    ArrayList<wallet_withdrawal_hist_model> modelArrayList;

    public wallet_withdrawal_hist_adapter(ArrayList<wallet_withdrawal_hist_model> modelArrayList) {
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


        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String payment_receipt = model.getPayment_receipt();
                if (payment_receipt.equals("http://ommatka.club/uploads/file/")) {
                    Toast.makeText(v.getContext(), "No receipt available", Toast.LENGTH_SHORT).show();
                } else {
                    Intent i = new Intent(v.getContext(), WebView.class);
                    i.putExtra("url", payment_receipt);
                    i.putExtra("name", "Withdrawal Receipt");
                    v.getContext().startActivity(i);

                }
            }
        });


        if (model.getRequest_status().equals("0")) {
            holder.status.setText("Pending");
            //holder.constraintLayout.setBackgroundResource(R.drawable.add_fund_orange_bg);
            holder.status.setTextColor(Color.parseColor("#FFA500"));
            holder.pointsTxt.setTextColor(Color.parseColor("#FFA500"));
            holder.amount.setTextColor(Color.parseColor("#FFA500"));
        } else if (model.getRequest_status().equals("1")) {
            holder.status.setText("Rejected");
            //holder.constraintLayout.setBackgroundResource(R.drawable.add_fund_red_bg);
            holder.status.setTextColor(Color.parseColor("#FF0000"));
            holder.pointsTxt.setTextColor(Color.parseColor("#FF0000"));
            holder.amount.setTextColor(Color.parseColor("#FF0000"));
        } else {
            holder.status.setText("Approved");
            //holder.constraintLayout.setBackgroundResource(R.drawable.add_fund_green_bg);
            holder.status.setTextColor(Color.parseColor("#61CF32"));
            holder.pointsTxt.setTextColor(Color.parseColor("#61CF32"));
            holder.amount.setTextColor(Color.parseColor("#61CF32"));
        }

        if (model.getPayment_method().equals("2")) {
            holder.paymentMethod.setText("PayTM");
        } else if (model.getPayment_method().equals("3")) {
            holder.paymentMethod.setText("Google Pay");
        } else if (model.getPayment_method().equals("4")) {
            holder.paymentMethod.setText("PhonePe");
        } else {
            holder.paymentMethod.setText("Others");
        }

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
            pointsTxt = itemView.findViewById(R.id.pointsTxt);
            status = itemView.findViewById(R.id.pana);
            cardView = itemView.findViewById(R.id.cardView);
            remarkLayout = itemView.findViewById(R.id.linearLayout1);
        }
    }
}
