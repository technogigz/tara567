package com.myTara567.app.dashboard.allbis.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.myTara567.app.R
import com.myTara567.app.Wallet.TransactionHistModel

class FundRequestHistoryAdapter(var hist_modelArrayList: ArrayList<TransactionHistModel>) :
    RecyclerView.Adapter<FundRequestHistoryAdapter.wallet_transHist>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): wallet_transHist {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.fund_request_history_item, parent, false)
        return wallet_transHist(v)
    }

    override fun onBindViewHolder(holder: wallet_transHist, position: Int) {
        val hist_model_list = hist_modelArrayList[position]

        holder.amount.text = "₹" + hist_model_list.amount
        holder.date.text = hist_model_list.insert_date
        holder.note.text = hist_model_list.transaction_note

        if (hist_model_list.transaction_type == "1") {
            //  holder.symbol.setText("+");
            holder.amount.setTextColor(Color.parseColor("#0179FF"))
            //  holder.symbol.setTextColor(Color.parseColor("#2ac200"));
            // holder.show.setImageResource(R.drawable.ic_downward_green);
        } else if (hist_model_list.transaction_type == "2") {
            //  holder.symbol.setText("-");
            holder.amount.setTextColor(Color.parseColor("#0179FF"))
            // holder.symbol.setTextColor(Color.parseColor("#f71000"));
            // holder.show.setImageResource(R.drawable.ic_upward_red);
        } else if (hist_model_list.transaction_type == "3") {
            // holder.symbol.setText("+");
            holder.amount.setTextColor(Color.parseColor("#0179FF"))
            // holder.symbol.setTextColor(Color.parseColor("#2ac200"));
            // holder.show.setImageResource(R.drawable.ic_downward_green);
        } else {
            //  holder.symbol.setText("");
        }
    }

    override fun getItemCount(): Int {
        return hist_modelArrayList.size
    }

    class wallet_transHist(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var date: TextView = itemView.findViewById(R.id.tranDate)
        var note: TextView = itemView.findViewById(R.id.tranNote)
        var amount: TextView = itemView.findViewById(R.id.tranPoints)
        var symbol: TextView? = null

        //ImageView show;
        init {
            // symbol = itemView.findViewById(R.id.tranSymbol);
            // show = itemView.findViewById(R.id.imageView20);
        }
    }
}
