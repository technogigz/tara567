package com.myTara567.app.dashboard

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.myTara567.app.R
import com.myTara567.app.databinding.GameCardUpdatedBinding

class NewTransactionAdapter(
    private val context: Context,
    var arrayList: ArrayList<gameDataModel>,
    private var mListener: mMyGameController
) : RecyclerView.Adapter<NewTransactionAdapter.ViewHolder>() {

    // ViewHolder class
    class ViewHolder(val binding: GameCardUpdatedBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = GameCardUpdatedBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = arrayList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val gameDataModel = arrayList[position]
        var accountActivationStatus:String = ""
        // ✅ Get betting_status from SharedPreferences
        val sharedPref = context?.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        accountActivationStatus = sharedPref?.getString("account_block_status", "").toString()


        if (accountActivationStatus == "0"){
            holder.binding.imageView3.visibility = View.GONE
        }


        holder.binding.apply {
            textView17.text = gameDataModel.getGame_name()
            tvTimeOpenValue.text = gameDataModel.getOpenTime()
            tvTimeCloseValue.text = gameDataModel.getCloseTime()

            // Set openResult with default if empty
            val openResultValue = gameDataModel.getOpenResult()
            openResult.text = if (openResultValue.isNullOrEmpty()) "***-*" else openResultValue

            // Set closeResult with default if empty
            val closeResultValue = gameDataModel.getCloseResult()
            closeResult.text = if (closeResultValue.isNullOrEmpty()) "*-***" else closeResultValue

            // Set market message and change color based on msg_status
            market.text = gameDataModel.getMarketMessage()
            market.setTextColor(
                if (gameDataModel.getMsg_status() != "2") Color.parseColor("#26B544")
                else Color.parseColor("#EC020A")
            )

            // Enable or disable the cardView based on betting_status
            val isBettingEnabled = gameDataModel.getBetting_status() == "1"
            cardView.isEnabled = isBettingEnabled

            holder.binding.imageView3.setOnClickListener(View.OnClickListener {
                if (gameDataModel.getMsg_status() == "2") {
                        val message = "Betting is closed for today. Please come next day to play."
                        showCustomDialog(context, message)
                    } else {
                        mListener.mAllGameClick(gameDataModel)
                    }
            })

            holder.binding.mSimpleCalender.isClickable = true
            holder.binding.mSimpleCalender.isFocusable = true
            mSimpleCalender.setOnClickListener {

                mListener.mChartInfo(gameDataModel)

            }

            if (isBettingEnabled == false) {
                holder.binding.imageView3.visibility = View.GONE
                holder.binding.imageView3.setOnClickListener {
                    if (gameDataModel.getMsg_status() == "2") {
                        val message = "Betting is closed for today. Please come next day to play."

                        showCustomDialog(context, message)
                    }
                }
                // Handle card click
//                cardView.setOnClickListener {
//                    if (gameDataModel.getMsg_status() == "2") {
//                        val message = "Betting is closed for today. Please come next day to play."
//                        showCustomDialog(context, message)
//                    } else {
//                        mListener.mAllGameClick(gameDataModel)
//                    }
//                }


                // Handle simple calendar click

            }
        }
    }

    // Listener interface
    interface mMyGameController {
        fun mChartInfo(list: gameDataModel)
        fun mAllGameClick(list: gameDataModel)
    }

    // Custom Dialog
    private fun showCustomDialog(context: Context, message: String) {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.sorry_dailog)

        val title = dialog.findViewById<TextView>(R.id.mtxt_title)
        title.text = message

        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val closeIcon = dialog.findViewById<ImageView>(R.id.closeIcon)
        closeIcon?.setOnClickListener { dialog.dismiss() }

        val btnOk = dialog.findViewById<TextView>(R.id.btn_ok)
        btnOk?.setOnClickListener { dialog.dismiss() }

        dialog.show()
    }
}
