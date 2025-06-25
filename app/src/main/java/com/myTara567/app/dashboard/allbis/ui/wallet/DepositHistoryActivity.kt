package com.myTara567.app.dashboard.allbis.ui.wallet

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonObject
import com.myTara567.app.R
import com.myTara567.app.Wallet.TransactionHistModel
import com.myTara567.app.dashboard.allbis.adapter.DepositHistoryAdapter
import com.myTara567.app.databinding.ActivityDepositHistoryBinding
import com.myTara567.app.serverApi.controller
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DepositHistoryActivity : AppCompatActivity() {
    lateinit var binding: ActivityDepositHistoryBinding
    private var withdrawalStatus: String? = null
    private var transferPointStatus: String? = null
    private var appKey: String? = null
    private var uniqueToken: String? = null
    private var mainObject: JsonObject? = null
    private lateinit var recyclerView: RecyclerView
    private val histModelsList = ArrayList<TransactionHistModel>()
    private var progressLayout: RelativeLayout? = null
    private var NoDataFundTitle: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityDepositHistoryBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        progressLayout = findViewById(R.id.progressLayout)
        NoDataFundTitle = findViewById(R.id.NoDataFundTitle)
        progressLayout?.visibility = View.VISIBLE

        initView()
    }

    private fun initView() {
        binding.backBidHistImage.setOnClickListener {
            onBackPressed()
        }

        withdrawalStatus = intent.getStringExtra("withdraw_status")
        transferPointStatus = intent.getStringExtra("transfer_point_status")

        appKey = getSharedPreferences("app_key", MODE_MULTI_PROCESS).getString("app_key", "")
        uniqueToken = applicationContext.getSharedPreferences("unique_token", MODE_MULTI_PROCESS)
            .getString("unique_token", "")

        mainObject = JsonObject().apply {
            addProperty("env_type", "Prod")
            addProperty("app_key", appKey)
            addProperty("unique_token", uniqueToken)
        }

        recyclerView = binding.rvDepositHistory
        setTransactionHist()
    }

    private fun setTransactionHist() {
        progressLayout?.visibility = View.VISIBLE

        val walletTransactionHistory = controller.getInstance().api.walletTransactionHistory(mainObject)

        walletTransactionHistory.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                progressLayout?.visibility = View.GONE
                NoDataFundTitle?.visibility = View.GONE
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        val transactionHist = responseBody.getAsJsonArray("transaction_history")
                        val status = responseBody["status"].asBoolean

                        if (status) {
                            if (transactionHist.size() == 0) {
                                recyclerView.visibility = View.INVISIBLE
                            } else {
                                histModelsList.clear()
                                for (i in 0 until transactionHist.size()) {
                                    val jsonObject = transactionHist[i].asJsonObject
                                    val transactionType = jsonObject["transaction_type"].asString
                                    val amount = jsonObject["amount"]?.asString ?: "0.0" // Handle null safely
                                    val transactionNote = jsonObject["transaction_note"].asString
                                    val insertDate = jsonObject["insert_date"].asString

                                    val model = TransactionHistModel()
                                        .apply {
                                        this.amount = amount
                                        this.transaction_note = transactionNote
                                        this.insert_date = insertDate
                                        this.transaction_type = transactionType
                                    }
                                    histModelsList.add(model)
                                }
                                recyclerView.layoutManager = LinearLayoutManager(this@DepositHistoryActivity)
                                recyclerView.adapter = DepositHistoryAdapter(histModelsList)
                            }
                        }
                    }
                } else {
                    showToast("Failed to fetch transaction history.")
                    showCustomDialog("No Deposit History Found")
                    NoDataFundTitle?.visibility = View.GONE
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                progressLayout?.visibility = View.GONE
                Log.e("history", "onFailure: ${t.localizedMessage}")
                showToast("Something went wrong. Try again later.")
            }
        })
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showCustomDialog(title: String) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.sorry_dailog)

        val mtxt_title = dialog.findViewById<TextView>(R.id.mtxt_title)
        mtxt_title?.text = title

        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val closeIcon = dialog.findViewById<ImageView>(R.id.closeIcon)
        closeIcon?.setOnClickListener { dialog.dismiss() }

        val btn_ok = dialog.findViewById<TextView>(R.id.btn_ok)
        btn_ok?.setOnClickListener { dialog.dismiss() }

        dialog.show()
    }
}
