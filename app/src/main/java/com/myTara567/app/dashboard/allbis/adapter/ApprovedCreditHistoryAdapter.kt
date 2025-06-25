package com.myTara567.app.dashboard.allbis.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.myTara567.app.R
import com.myTara567.app.Wallet.TransactionHistModel

class ApprovedCreditHistoryAdapter(private var hist_modelArrayList: ArrayList<TransactionHistModel>) :
    RecyclerView.Adapter<ApprovedCreditHistoryAdapter.wallet_transHist>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): wallet_transHist {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.approved_credit_history_item, parent, false)
        return wallet_transHist(v)
    }

    override fun onBindViewHolder(holder: wallet_transHist, position: Int) {
        val hist_model_list = hist_modelArrayList[position]
        holder.amount.text = "₹" + hist_model_list.amount
        holder.date.text = hist_model_list.insert_date
        holder.note.text = hist_model_list.transaction_note
        if (hist_model_list.transaction_type == "1") {
            holder.amount.setTextColor(Color.parseColor("#0179FF"))
        } else if (hist_model_list.transaction_type == "2") {
            holder.amount.setTextColor(Color.parseColor("#0179FF"))
        } else if (hist_model_list.transaction_type == "3") {
            holder.amount.setTextColor(Color.parseColor("#0179FF"))
        } else {
        }
    }

    override fun getItemCount(): Int {
        return hist_modelArrayList.size
    }

    class wallet_transHist(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var date: TextView = itemView.findViewById(R.id.tranDate)
        var note: TextView = itemView.findViewById(R.id.tranNote)
        var closedigits: TextView = itemView.findViewById(R.id.closedigits)
        var amount: TextView = itemView.findViewById(R.id.tranPoints)
        var symbol: TextView? = null

        //ImageView show;
        init {
            // symbol = itemView.findViewById(R.id.tranSymbol);
            // show = itemView.findViewById(R.id.imageView20);
        }
    }
}
