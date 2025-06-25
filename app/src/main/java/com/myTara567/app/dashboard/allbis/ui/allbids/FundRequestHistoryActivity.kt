package com.myTara567.app.dashboard.allbis.ui.allbids

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
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonObject
import com.myTara567.app.R
import com.myTara567.app.Wallet.TransactionHistModel
import com.myTara567.app.Wallet.wallet_withdrawal_hist_model
import com.myTara567.app.dashboard.allbis.adapter.FundRequestHistoryAdapter
import com.myTara567.app.dashboard.allbis.adapter.WithdrawalHistoryAdapter
import com.myTara567.app.databinding.ActivityFundRequestHistoryBinding
import com.myTara567.app.serverApi.controller
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FundRequestHistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFundRequestHistoryBinding
    private var withdrawalStatus: String? = null
    private var transferPointStatus: String? = null
    private var appKey: String? = null
    private var uniqueToken: String? = null
    private var mainObject: JsonObject? = null

    private lateinit var recyclerView: RecyclerView
    private val histModelsList = ArrayList<TransactionHistModel>()
    private val witdhistModelsList = ArrayList<wallet_withdrawal_hist_model>()
    private var progressLayout: RelativeLayout? = null
    private var NoDataFundTitle: TextView? = null
    private var title: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFundRequestHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize the progress layout and set it to visible initially
        progressLayout = findViewById(R.id.progressLayout)
        progressLayout?.visibility = View.VISIBLE
        NoDataFundTitle = findViewById(R.id.NoDataFundTitle)
        title = findViewById(R.id.title)


        mInitView()
    }

    private fun mInitView() {
        // Retrieve data passed via Intent
        binding.backBidHistImage.setOnClickListener {
            onBackPressed()
        }


        withdrawalStatus = intent.getStringExtra("withdraw_status")
        transferPointStatus = intent.getStringExtra("transfer_point_status")

        // Fetch shared preferences
        appKey = getSharedPreferences("app_key", MODE_MULTI_PROCESS).getString("app_key", "")
        Log.d("login appkey", "App Key: $appKey")

        uniqueToken = applicationContext.getSharedPreferences("unique_token", MODE_MULTI_PROCESS)
            .getString("unique_token", "")
        Log.d("signUp editor", "Unique Token: $uniqueToken")

        // Prepare the main JSON object for API request
        mainObject = JsonObject().apply {
            addProperty("env_type", "Prod")
            addProperty("app_key", appKey)
            addProperty("unique_token", uniqueToken)
        }


        val receivedValue = intent.getStringExtra("screenName")
        title?.setText(receivedValue)
        // Initialize RecyclerView and No Results View
        recyclerView = binding.histRecycler

        // Call function to fetch and display transaction history
        setTransactionHist()
    }

    private fun setTransactionHist() {
        progressLayout?.visibility = View.VISIBLE

        if (title?.text == "Withdrawal History") {
            val walletTransactionHistory = controller.getInstance().api.withdrawTransactionHist(mainObject)
            walletTransactionHistory.enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    progressLayout?.visibility = View.GONE
                    NoDataFundTitle?.visibility = View.GONE
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        Log.d("history123", "Transaction History: $responseBody")

                        if (responseBody != null) {
                            val withdrawDataHist = responseBody.getAsJsonArray("withdrawdata")
                            val status = responseBody["status"].asBoolean

                            Log.d("history", "Transaction History: $withdrawDataHist")

                            if (withdrawDataHist == null) {
                                binding.NoDataFundTitle.visibility
                                NoDataFundTitle?.visibility = View.VISIBLE
                            }
                            if (status) {
                                if (withdrawDataHist.size() == 0) {
                                    recyclerView.visibility = View.INVISIBLE

                                } else {
                                    // Transaction history found
                                    witdhistModelsList.clear()

                                    for (i in 0 until withdrawDataHist.size()) {
                                        val jsonObject = withdrawDataHist[i].asJsonObject

                                        val request_number = jsonObject["request_number"].asString
                                        val request_amount = jsonObject["request_amount"]?.asString
                                            ?: "0.0"
                                        val insertDate = jsonObject["insert_date"].asString
                                        val model = wallet_withdrawal_hist_model().apply {
                                            this.request_number = request_number
                                            this.request_amount = request_amount
                                            this.insert_date = insertDate
                                        }
                                        witdhistModelsList.add(model)
                                    }

                                    // Setup RecyclerView and Adapter
                                    recyclerView.layoutManager =
                                        LinearLayoutManager(this@FundRequestHistoryActivity)
                                    recyclerView.adapter = WithdrawalHistoryAdapter(
                                        witdhistModelsList
                                    )
                                }
                            }
                        }
                    } else {
                        Log.e("history", "Failed Response: ${response.errorBody()?.string()}")
                        showCustomDialog("No Fund Request History")
                        binding.NoDataFundTitle.visibility
                        NoDataFundTitle?.visibility = View.VISIBLE
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    // Hide the progress layout in case of failure
                    progressLayout?.visibility = View.GONE

                    Log.e("history", "onFailure: ${t.localizedMessage}")

                }
            })
        } else {
            val walletTransactionHistory =
                controller.getInstance().api.fundRequestHistory(mainObject)
            walletTransactionHistory.enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    progressLayout?.visibility = View.GONE
                    NoDataFundTitle?.visibility = View.GONE
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        Log.d("history123", "Transaction History: $responseBody")

                        if (responseBody != null) {
                            val transactionHist = responseBody.getAsJsonArray("transaction_history")
                            val status = responseBody["status"].asBoolean

                            Log.d("history", "Transaction History: $transactionHist")

                            if (transactionHist == null) {
                                binding.NoDataFundTitle.visibility
                                NoDataFundTitle?.visibility = View.VISIBLE
                            }
                            if (status) {
                                if (transactionHist.size() == 0) {
                                    // No transaction history found
                                    recyclerView.visibility = View.INVISIBLE

                                } else {
                                    // Transaction history found
                                    histModelsList.clear()

                                    for (i in 0 until transactionHist.size()) {
                                        val jsonObject = transactionHist[i].asJsonObject

                                        val transactionType =
                                            jsonObject["transaction_type"].asString
                                        val transactionNote =
                                            jsonObject["transaction_note"].asString
                                        val amount = jsonObject["amount"]?.asString
                                            ?: "0.0" // Handle null safely
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

                                    // Setup RecyclerView and Adapter
                                    recyclerView.layoutManager =
                                        LinearLayoutManager(this@FundRequestHistoryActivity)
                                    recyclerView.adapter = FundRequestHistoryAdapter(
                                        histModelsList
                                    )
                                }
                            }
                        }
                    } else {
                        Log.e("history", "Failed Response: ${response.errorBody()?.string()}")
                        showCustomDialog("No Fund Request History")
                        binding.NoDataFundTitle.visibility
                        NoDataFundTitle?.visibility = View.VISIBLE
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    // Hide the progress layout in case of failure
                    progressLayout?.visibility = View.GONE

                    Log.e("history", "onFailure: ${t.localizedMessage}")

                }
            })
        }


    }

    private fun showCustomDialog(title: String) {
        // Create the dialog
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.sorry_dailog)

        // Correctly find the TextView from the dialog's layout
        val mtxt_title = dialog.findViewById<TextView>(R.id.mtxt_title)
        mtxt_title?.text = title

        // Make the dialog fullscreen
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // Set up close button
        val closeIcon = dialog.findViewById<ImageView>(R.id.closeIcon)
        closeIcon?.setOnClickListener {
            dialog.dismiss()
        }

        val btn_ok = dialog.findViewById<TextView>(R.id.btn_ok)
        btn_ok?.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }
}
