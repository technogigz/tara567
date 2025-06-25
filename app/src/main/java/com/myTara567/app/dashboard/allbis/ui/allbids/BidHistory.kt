package com.myTara567.app.dashboard.allbis.ui.allbids

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.google.gson.JsonObject
import com.myTara567.app.R
import com.myTara567.app.dashboard.allbis.adapter.bidHistoryAdapter
import com.myTara567.app.dashboard.bidHistoryModel
import com.myTara567.app.databinding.BidHistoryBinding
import com.myTara567.app.serverApi.controller
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar

class BidHistory : AppCompatActivity() {
    lateinit var binding: BidHistoryBinding
    var backBidHistImage: ImageView? = null
    var bid_from: LinearLayout? = null
    var bid_to: LinearLayout? = null
   // var noResults: LinearLayout? = null
    var mYear: Int = 0
    var mMonth: Int = 0
    var mDay: Int = 0
    var dateFromText: TextView? = null
    var dateToText: TextView? = null
    var mDateFrom: String? = null
    var mDateTo: String? = null
    var title: TextView? = null
    var submitBtn: Button? = null
    var appKey: String? = null
    var unique_token: String? = null
    var bidHistRecycler: RecyclerView? = null
    var swipeRefresh: SwipeRefreshLayout? = null

    private var NoDataFundTitle: TextView? = null
    var bidHistoryModels: ArrayList<bidHistoryModel> = ArrayList()
    var bidHistLayout: ConstraintLayout? = null
    private var progressLayout: RelativeLayout? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = BidHistoryBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        progressLayout = findViewById(R.id.progressLayout)
        NoDataFundTitle = findViewById(R.id.NoDataFundTitle)
        progressLayout?.visibility = View.VISIBLE
        //app key
        appKey = applicationContext.getSharedPreferences("app_key", MODE_MULTI_PROCESS)
            .getString("app_key", null)
        Log.d("signUp appkey", "onCreate: $appKey")

        //unique token
        unique_token = applicationContext.getSharedPreferences("unique_token", MODE_MULTI_PROCESS)
            .getString("unique_token", "")
        Log.d("signUp editor", "home: $unique_token")


        val label = getTitle().toString()
        Log.d("title", "onCreate: $label")
        title = findViewById(R.id.title)
        title!!.setText(label)



        backBidHistImage = findViewById(R.id.backBidHistImage)
        bid_from = findViewById(R.id.bid_from)
        bid_to = findViewById(R.id.bid_to)
        // dateFromText = findViewById(R.id.date_from_text)
        // dateToText = findViewById(R.id.date_to_text)
       // noResults = findViewById(R.id.noResults)
        submitBtn = findViewById(R.id.submitBtn)
        bidHistRecycler = findViewById(R.id.bidHistRecycler)
        bidHistLayout = findViewById(R.id.bidHistLayout)


        // bidHistLayout.setAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_up));
        val c = Calendar.getInstance()
        mYear = c[Calendar.YEAR]
        Log.d("date", "onCreate: $mYear")
        mMonth = c[Calendar.MONTH]
        Log.d("date", "onCreate: $mMonth")
        mDay = c[Calendar.DAY_OF_MONTH]
        Log.d("date", "onCreate: $mDay")

        val currentDate = StringBuilder()
        currentDate.append(mDay).append("-").append(mMonth + 1).append("-").append(mYear)
        mDateFrom = (currentDate.toString())
        mDateTo = (currentDate).toString()

        backBidHistImage!!.setOnClickListener(View.OnClickListener { onBackPressed() })

        /*  bid_from!!.setOnClickListener(View.OnClickListener {
              val datePickerDialog = DatePickerDialog(
                  this@Bid_history,
                  { view, year, monthOfYear, dayOfMonth -> dateFromText!!.setText(dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year) },
                  mYear,
                  mMonth,
                  mDay
              )
              datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
              datePickerDialog.show()
          })

          bid_to!!.setOnClickListener(View.OnClickListener {
              val datePickerDialog = DatePickerDialog(
                  this@Bid_history,
                  { view, year, monthOfYear, dayOfMonth -> dateToText!!.setText(dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year) },
                  mYear,
                  mMonth,
                  mDay
              )
              datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
              datePickerDialog.show()
          })*/

        showBidHistory()

        swipeRefresh = findViewById(R.id.swipeRefresh)
        swipeRefresh!!.setOnRefreshListener(OnRefreshListener {
            showBidHistory()
            swipeRefresh!!.setRefreshing(false)
        })

        submitBtn!!.setOnClickListener(View.OnClickListener { showBidHistory() })
    }

    private fun showBidHistory() {
        progressLayout?.visibility = View.VISIBLE
        val onClickDateFromText =mDateFrom
        val onClickDateToText =  mDateTo


        val js = JsonObject()
        js.addProperty("env_type", "Prod")
        js.addProperty("app_key", appKey)
        js.addProperty("unique_token", unique_token)
        js.addProperty("bid_from", onClickDateFromText)
        js.addProperty("bid_to", onClickDateToText)

        Log.d("js", "onClick: $js")
        val call = controller.getInstance().api.bid_history(js)
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                Log.d("bidHistory", "onResponse: " + response.body().toString())
                progressLayout?.visibility = View.GONE
                NoDataFundTitle?.visibility = View.GONE
                val win_data = response.body()!!["bid_data"].asJsonArray

               // val msg = response.body()!!["msg"].asString

                val status = response.body()!!["status"].asBoolean

                if (status) {
                    bidHistoryModels.clear()
                   // noResults!!.visibility = View.INVISIBLE
                    bidHistRecycler!!.visibility = View.VISIBLE
                    for (i in 0 until win_data.size()) {
                        val jsonObject = win_data[i].asJsonObject

                        val game_name = jsonObject["game_name"].asString
                        val pana = jsonObject["pana"].asString
                        val session = jsonObject["session"].asString
                        val openDigits = jsonObject["digits"].asString
                        val closeDigits = jsonObject["closedigits"].asString
                        val points = jsonObject["points"].asString
                        val bid_date = jsonObject["bid_date"].asString

                        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)

                        bidHistRecycler!!.layoutManager = layoutManager

                        val model = bidHistoryModel()
                        model.setGame_name(game_name)
                        model.setPana(pana)
                        model.setSession(session)
                        model.setOpenDigits(openDigits)
                        model.setCloseDigits(closeDigits)
                        model.setPoints(points)
                        model.setBid_date(bid_date)

                        bidHistoryModels.add(bidHistoryModel(game_name, pana, session, openDigits, closeDigits, points, bid_date))
                        val adapter = bidHistoryAdapter("bid_history", bidHistoryModels)
                        bidHistRecycler!!.adapter = adapter
                    }
                } else {
                   // Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT).show()
                    showCustomDialog("No Bid History Found")
                    bidHistRecycler!!.visibility = View.INVISIBLE
                    NoDataFundTitle?.visibility = View.VISIBLE
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                progressLayout?.visibility = View.GONE
                Toast.makeText(
                    applicationContext,
                    "Something went wrong.... Try again later",
                    Toast.LENGTH_SHORT
                ).show()
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