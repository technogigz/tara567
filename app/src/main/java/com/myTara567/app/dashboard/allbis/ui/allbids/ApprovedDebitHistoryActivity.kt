package com.myTara567.app.dashboard.allbis.ui

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
import com.myTara567.app.dashboard.allbis.adapter.ApprovedDebitHistoryHistoryAdapter
import com.myTara567.app.databinding.ActivityApprovedDebitHistoryBinding
import com.myTara567.app.serverApi.controller
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApprovedDebitHistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityApprovedDebitHistoryBinding
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
        super.onCreate(savedInstanceState)
        binding = ActivityApprovedDebitHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize the progress layout and set it to visible initially
        progressLayout = findViewById(R.id.progressLayout)
        NoDataFundTitle = findViewById(R.id.NoDataFundTitle)
        progressLayout?.visibility = View.VISIBLE

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

        uniqueToken = applicationContext.getSharedPreferences("unique_token", MODE_MULTI_PROCESS).getString("unique_token", "")
        Log.d("signUp editor", "Unique Token: $uniqueToken")

        // Prepare the main JSON object for API request
        mainObject = JsonObject().apply {
            addProperty("env_type", "Prod")
            addProperty("app_key", appKey)
            addProperty("unique_token", uniqueToken)
        }

        // Initialize RecyclerView and No Results View
        recyclerView = binding.rvApprovedHistory

        // Call function to fetch and display transaction history
        setTransactionHist()
    }

    private fun setTransactionHist() {
        // Show the progress layout while waiting for the response
        progressLayout?.visibility = View.VISIBLE

        val walletTransactionHistory = controller.getInstance().api.walletTransactionHistory(mainObject)

        walletTransactionHistory.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                // Hide the progress layout once the response is received
                progressLayout?.visibility = View.GONE
                NoDataFundTitle?.visibility = View.GONE

                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        val transactionHist = responseBody.getAsJsonArray("out_history")
                        val status = responseBody["status"].asBoolean

                        Log.d("history", "Transaction History: $transactionHist")

                        if (status) {
                            if (transactionHist.size() == 0) {
                                showCustomDialog("No Approved Debit History ")
                                // No transaction history found
                               // recyclerView.visibility = View.INVISIBLE
                                NoDataFundTitle?.visibility = View.VISIBLE
                            } else {
                                // Transaction history found
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

                                // Setup RecyclerView and Adapter
                                recyclerView.layoutManager = LinearLayoutManager(this@ApprovedDebitHistoryActivity)
                                recyclerView.adapter = ApprovedDebitHistoryHistoryAdapter(histModelsList)
                            }
                        }
                    }
                } else {
                    Log.e("history", "Failed Response: ${response.errorBody()?.string()}")
                    NoDataFundTitle?.visibility = View.VISIBLE
                    showCustomDialog("No Approved Debit History ")
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                // Hide the progress layout in case of failure
                progressLayout?.visibility = View.GONE
                Log.e("history", "onFailure: ${t.localizedMessage}")
            }
        })
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
