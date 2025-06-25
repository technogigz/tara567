package com.myTara567.app.dashboard;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.myTara567.app.R;

import java.util.ArrayList;

public class winHistoryAdapter extends RecyclerView.Adapter<winHistoryAdapter.winHistory> {
    ArrayList<winHistoryModel> winHistoryModels;

    public winHistoryAdapter(ArrayList<winHistoryModel> winHistoryModels) {
        this.winHistoryModels = winHistoryModels;
    }

    @NonNull
    @Override
    public winHistory onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.win_hist_card, parent, false);
        return new winHistory(v);
    }

    @Override
    public void onBindViewHolder(@NonNull winHistory holder, int position) {
        winHistoryModel winHistoryModel = winHistoryModels.get(position);
        holder.amount.setText(winHistoryModel.getAmount());
        holder.transaction_note.setText(winHistoryModel.getTransaction_note());
        holder.tx_request_number.setText(winHistoryModel.getTx_request_number());
        holder.wining_date.setText(winHistoryModel.getWining_date());
    }

    @Override
    public int getItemCount() {
        return winHistoryModels.size();
    }

    public static class winHistory extends RecyclerView.ViewHolder {
        TextView amount, transaction_note, tx_request_number, wining_date;

        public winHistory(@NonNull View itemView) {
            super(itemView);
            amount = itemView.findViewById(R.id.amount);
            transaction_note = itemView.findViewById(R.id.transaction_note);
            tx_request_number = itemView.findViewById(R.id.tx_request_number);
            wining_date = itemView.findViewById(R.id.wining_date);
        }
    }
}
