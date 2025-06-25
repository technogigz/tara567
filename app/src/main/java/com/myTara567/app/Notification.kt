package com.myTara567.app

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.google.gson.JsonObject
import com.myTara567.app.serverApi.controller
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Notification : AppCompatActivity() {
    var mainObj: JsonObject? = null
    var notice_list: ArrayList<Notice_Model> = ArrayList()
    var recycler: RecyclerView? = null
    var noResults: TextView? = null
    var swipeRefresh: SwipeRefreshLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)

        val appKey = getSharedPreferences("app_key", MODE_MULTI_PROCESS).getString("app_key", "")!!
        Log.d("TAG", "onCreate: app key $appKey")
        val uniqueToken =
            applicationContext.getSharedPreferences("unique_token", MODE_MULTI_PROCESS)
                .getString("unique_token", "")!!
        Log.d("TAG", "onCreate: $uniqueToken")

        recycler = findViewById(R.id.recycler)
        noResults = findViewById(R.id.noResults)
        swipeRefresh = findViewById(R.id.swipeRefresh)

        val backPassImage = findViewById<ImageView>(R.id.backBidHistImage)
        backPassImage.setOnClickListener { v: View? ->
            onBackPressed()
        }

        mainObj = JsonObject()
        mainObj!!.addProperty("env_type", "Prod")
        mainObj!!.addProperty("app_key", appKey)
        mainObj!!.addProperty("unique_token", uniqueToken)

        Log.d("sdfsdfsdf", mainObj.toString())

        notificationData

        swipeRefresh!!.setOnRefreshListener(object : OnRefreshListener {
            override fun onRefresh() {
                swipeRefresh!!.setRefreshing(false)
            }
        })
    }

    private val notificationData: Unit
        get() {
            val call = controller.getInstance().api.getNotification(mainObj)
            call.enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    /*Log.d(
                        "sdfsdfsdf",
                        "onResponse: status " + response.body().toString()
                    )
                    Log.d(
                        "sdfsdfsdf",
                        "onResponse: status " + response.body()!!["status"].asBoolean
                    )*/

                    if (response.body()!!["status"].asBoolean) {
                        noResults!!.visibility = View.GONE
                        val notices =
                            response.body()!!.getAsJsonArray("notification").asJsonArray
                        notice_list.clear()
                        for (i in 0 until notices.size()) {
                            val notice_obj = notices[i].asJsonObject

                            val layoutManager: RecyclerView.LayoutManager =
                                LinearLayoutManager(
                                    applicationContext,
                                    LinearLayoutManager.VERTICAL,
                                    false
                                )

                            recycler!!.layoutManager = layoutManager

                            val notice_model = Notice_Model()
                            Log.d(
                                "TAG",
                                "onResponse: " + notice_obj["msg"].asString
                            )
                            notice_model.setNotice_msg(notice_obj["msg"].asString)
                            //notice_model.setNotice_title(notice_obj.get("notice_title").getAsString());
                            notice_model.setNotice_title("")
                            notice_model.setNotice_date(notice_obj["insert_date"].asString)

                            notice_list.add(notice_model)
                        }

                        val adapter =
                            NoticeAdapter(this@Notification, notice_list)
                        recycler!!.adapter = adapter
                        adapter.notifyDataSetChanged()
                    } else {
                        showCustomDialog("No Notification Found")
                        noResults!!.visibility = View.VISIBLE
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    showCustomDialog("No Notification Found")
                    Toast.makeText(applicationContext, "Something went wrong.... Try again later", Toast.LENGTH_SHORT).show()
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

