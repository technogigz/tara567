package com.myTara567.app.home.fragment

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.gson.JsonObject
import com.myTara567.app.R
import com.myTara567.app.Wallet.TransactionHistModel
import com.myTara567.app.dashboard.allbis.adapter.ApprovedCreditHistoryAdapter
import com.myTara567.app.dashboard.bidHistoryModel
import com.myTara567.app.databinding.ActivityPassBookBinding
import com.myTara567.app.serverApi.controller
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PassBookActivity : AppCompatActivity() {
    lateinit var binding: ActivityPassBookBinding
    var backBidHistImage: ImageView? = null
    var bid_from: LinearLayout? = null
    var bid_to: LinearLayout? = null
    var mYear: Int = 0
    var mMonth: Int = 0
    var mDay: Int = 0
    var dateFromText: TextView? = null
    var dateToText: TextView? = null
    var mDateFrom: String? = null
    var mDateTo: String? = null
    var title: TextView? = null
    private val histModelsList = ArrayList<TransactionHistModel>()
    private var uniqueToken: String? = null
    private var mainObject: JsonObject? = null
    var submitBtn: Button? = null
    var appKey: String? = null
    var unique_token: String? = null
    var pass_book_rv: RecyclerView? = null
    var swipeRefresh: SwipeRefreshLayout? = null
    private var withdrawalStatus: String? = null
    private var NoDataFundTitle: TextView? = null
    var bidHistoryModels: ArrayList<bidHistoryModel> = ArrayList()
    var bidHistLayout: ConstraintLayout? = null
    private var progressLayout: RelativeLayout? = null
    private var transferPointStatus: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPassBookBinding.inflate(layoutInflater)
        setContentView(binding.root)



        initView()


        // Initialize views
        progressLayout = findViewById(R.id.progressLayout)
        NoDataFundTitle = findViewById(R.id.NoDataFundTitle)
        backBidHistImage = findViewById(R.id.backBidHistImage)
        bid_from = findViewById(R.id.bid_from)
        bid_to = findViewById(R.id.bid_to)
        submitBtn = findViewById(R.id.submitBtn)
        pass_book_rv = findViewById(R.id.pass_book_rv)
        bidHistLayout = findViewById(R.id.bidHistLayout)

        // Set up shared preferences
        appKey = applicationContext.getSharedPreferences("app_key", MODE_MULTI_PROCESS)
            .getString("app_key", null)
        unique_token = applicationContext.getSharedPreferences("unique_token", MODE_MULTI_PROCESS)
            .getString("unique_token", "")

    }

    private fun initView() {
        val walletTitle = intent.getStringExtra("title") ?: ""

        val updatedTitle = when (walletTitle) {
            "AccountStatement " -> "Account Statement"
            "PassBook" -> "PassBook"
            else -> walletTitle // Default to original title if source isn't recognized
        }

        binding.title.text = updatedTitle


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

        // Initialize RecyclerView and No Results View
        pass_book_rv = binding.passBookRv

        // Call function to fetch and display transaction history
        setTransactionHist()
    }

    private fun setTransactionHist() {
        // Show the progress layout while waiting for the response
        progressLayout?.visibility = View.VISIBLE

        val walletTransactionHistory =
            controller.getInstance().api.walletTransactionHistory(mainObject)

        walletTransactionHistory.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                // Hide the progress layout once the response is received
                progressLayout?.visibility = View.GONE
                NoDataFundTitle?.visibility = View.GONE
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        val transactionHist = responseBody.getAsJsonArray("transaction_history")
                        val status = responseBody["status"].asBoolean

                        Log.d("history", "Transaction History: $transactionHist")

                        if (status) {
                            if (transactionHist.size() == 0) {
                                // No transaction history found
                                pass_book_rv!!.visibility = View.INVISIBLE

                            } else {
                                // Transaction history found
                                histModelsList.clear()

                                for (i in 0 until transactionHist.size()) {
                                    val jsonObject = transactionHist[i].asJsonObject

                                    val transactionType = jsonObject["transaction_type"].asString
                                    val transactionNote = jsonObject["transaction_note"].asString
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
                                pass_book_rv!!.layoutManager =
                                    LinearLayoutManager(this@PassBookActivity)
                                pass_book_rv!!.adapter =
                                    ApprovedCreditHistoryAdapter(histModelsList)

                            }
                        }
                    }
                } else {
                    Log.e("history", "Failed Response: ${response.errorBody()?.string()}")
                    showToast("Failed to fetch transaction history.")
                    showCustomDialog("No Approved Credit History")
                    NoDataFundTitle?.visibility = View.GONE
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                // Hide the progress layout in case of failure
                progressLayout?.visibility = View.GONE

                Log.e("history", "onFailure: ${t.localizedMessage}")
                showToast("Something went wrong.... Try again later")
            }
        })
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
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
