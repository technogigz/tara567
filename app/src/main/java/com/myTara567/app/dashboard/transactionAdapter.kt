package com.myTara567.app.dashboard

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Vibrator
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.myTara567.app.R

class transactionAdapter
    (private val context: Context,
     var gameDataModels: ArrayList<gameDataModel>) :
    RecyclerView.Adapter<transactionAdapter.transactionView>() {
    var vibe: Vibrator? = null



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): transactionView {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.game_card_updated, parent, false)
        return transactionView(v)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: transactionView, position: Int) {
        val gameDataModel = gameDataModels[position]

        //card background
        val msg_status = gameDataModel.getMsg_status()
        Log.d("msg_status", "onBindViewHolder: $msg_status") //
        if (msg_status != "2") {
            holder.market.setTextColor(Color.parseColor("#26B544"))
        } else {
            holder.market.setTextColor(Color.parseColor("#EC020A"))
        }
        holder.head.text = gameDataModel.getGame_name()
        holder.open.text = gameDataModel.getOpenTime()
        holder.close.text = gameDataModel.getCloseTime()
        holder.market.text = gameDataModel.getMarketMessage()
        val openResult = gameDataModel.getOpenResult()

        if (openResult == "") {
            holder.openResult.text = "XXX-X"
        } else {
            holder.openResult.text = openResult
        }

        val closeResult = gameDataModel.getCloseResult()
        if (closeResult == "") {
            holder.closeResult.text = "X-XXX"
        } else {
            holder.closeResult.text = closeResult
        }




       /* holder.mMaiLayout.setOnClickListener { v ->
            val url = gameDataModel.getWebChartUrl()
            val gameName = gameDataModel.getGame_name()

            val i = Intent(v.context, WebView::class.java)
            i.putExtra("url", url)
            i.putExtra("name", gameName)
            Log.d("url", "onCreate: $url")
            v.context.startActivity(i)
        }

        if (gameDataModel.getBetting_status() == "0") {
            holder.mMaiLayout.isEnabled = false
        } else {
            holder.mMaiLayout.setOnClickListener { v ->
                if (msg_status == "2") {
                    vibe =
                      v.context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                    vibe!!.vibrate(100)
                    holder.mMaiLayout.startAnimation(
                        AnimationUtils.loadAnimation(
                            v.context,
                            R.anim.shake
                        )
                    )
                } else {
                    val i = Intent(v.context, Games::class.java)
                    i.putExtra("gameDataModel", gameDataModel)
                    v.context.startActivity(i)
                }
            }
        }*/
    }

    override fun getItemCount(): Int {
        return gameDataModels.size
    }






    class transactionView(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var head: TextView = itemView.findViewById(R.id.textView17)
        var open: TextView = itemView.findViewById(R.id.tv_time_open_value)
        var close: TextView = itemView.findViewById(R.id.tv_time_close_value)
        var market: TextView = itemView.findViewById(R.id.market)
        var openResult: TextView = itemView.findViewById(R.id.openResult)
        var closeResult: TextView = itemView.findViewById(R.id.closeResult)
        var playIcon: ImageView = itemView.findViewById(R.id.imageView3)
        var simple_calender: ImageView = itemView.findViewById(R.id.mSimple_calender)
        var marketStatus: ImageView? = null
        var mMaiLayout: ConstraintLayout = itemView.findViewById(R.id.mMaiLayout)
        var view: View? = null
    }
}


