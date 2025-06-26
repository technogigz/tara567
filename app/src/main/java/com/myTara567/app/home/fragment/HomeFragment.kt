package com.myTara567.app.home.fragment

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.gson.JsonObject
import com.myTara567.app.Maintenance
import com.myTara567.app.R
import com.myTara567.app.WebView
import com.myTara567.app.dashboard.NewTransactionAdapter
import com.myTara567.app.dashboard.gameDataModel
import com.myTara567.app.dashboard.slider_model
import com.myTara567.app.mainGames.Games
import com.myTara567.app.serverApi.controller
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import androidx.core.net.toUri

class HomeFragment : Fragment() {
    private var spUniqueToken: SharedPreferences? = null
    var js: JsonObject? = null
    var unique_token: String? = null
    var appKey: String? = null
    var mobile: String? = null
    var unique: String? = null
    var gameDataModels: ArrayList<gameDataModel> = ArrayList()
    var recycler: RecyclerView? = null
    var drawerLayout: DrawerLayout? = null
    var betting_status: String? = null
    var account_block_status: String? = null
    var withdraw_status: String? = null
    var transfer_point_status: String? = null
    var app_marquee_msg: String? = null

    var phone: TextView? = null
    var starline: LinearLayout? = null
    var swipeRefresh: SwipeRefreshLayout? = null
    var maintainence_msg_status: String? = null
    var user_current_version: String? = null
    var user_minimum_version: String? = null
    var walletBalance: TextView? = null
    var mMarquu_title:TextView?=null
    var mWhatAppNumber:TextView?=null
    var whatsApp_a:TextView?=null
    var layoutA:ConstraintLayout?=null
    var imageA:ImageView?=null
    var imageB:ImageView?=null
    var mLayout:ConstraintLayout?=null



    var accountActivationStatus:String = ""



    var androidId: String? = null
    var message: String? = null
    var device_Id_list: ArrayList<String?> = ArrayList()
    var action_btn_text: String? = null
    var link_btn_text: String? = null



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    private val preferenceChangeListener = SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, key ->
        if (key == "account_block_status") {
            val status = sharedPreferences.getString("account_block_status", "")
            updateAccountActivationUI(status)
        }
    }

    private fun updateAccountActivationUI(status: String?) {
        if (status == "0") {
            layoutA?.visibility = View.GONE
            imageA?.visibility = View.GONE
            imageB?.visibility = View.GONE
            mWhatAppNumber?.visibility = View.GONE
            whatsApp_a?.visibility = View.GONE
            mLayout?.visibility = View.GONE


        } else {
            layoutA?.visibility = View.VISIBLE
            imageA?.visibility = View.VISIBLE
            imageB?.visibility = View.VISIBLE
            mWhatAppNumber?.visibility = View.VISIBLE
            whatsApp_a?.visibility = View.VISIBLE
            mLayout?.visibility = View.VISIBLE
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recycler = view.findViewById(R.id.home_recycler)
        swipeRefresh = view.findViewById(R.id.swipeRefresh)
        mMarquu_title = view.findViewById(R.id.marquu_title)
        mWhatAppNumber = view.findViewById(R.id.Whataap_number)
        whatsApp_a = view.findViewById(R.id.whatsApp_a)
        imageA = view.findViewById(R.id.image_a)
        imageB = view.findViewById(R.id.image_b)
        layoutA = view.findViewById(R.id.mLayout)
        mLayout = view.findViewById(R.id.mLayout)

        mWhatAppNumber?.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = ("https://wa.me/919956770567" ).toUri()
            startActivity(intent)
        }

        whatsApp_a?.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = ("https://wa.me/919956770567" ).toUri()
            startActivity(intent)
        }

        val sharedPref = context?.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        sharedPref?.registerOnSharedPreferenceChangeListener(preferenceChangeListener)

        // Optional: call manually to set initial state
        updateAccountActivationUI(sharedPref?.getString("account_block_status", ""))

        mIntView()
    }

    fun mIntView() {
        appKey = activity?.getSharedPreferences("app_key", Context.MODE_PRIVATE)
            ?.getString("app_key", "")
        Log.d("login appkey", "onCreate: $appKey")

        // Get the unique token from SharedPreferences
        spUniqueToken = activity?.getSharedPreferences("unique_token", Context.MODE_PRIVATE)
        unique = spUniqueToken?.getString("unique_token", "")
        Log.d("signUp editor", "home: $unique")

        js = JsonObject()
        js!!.addProperty("env_type", "Prod")
        js!!.addProperty("app_key", appKey)
        js!!.addProperty("unique_token", unique)

        swipeRefresh!!.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            setRecyclerViewData()
            swipeRefresh!!.setRefreshing(false)
        })
        //sliderImage
        setSliderImage()

        //recycler view
        setRecyclerViewData()



    }

    override fun onResume() {
        super.onResume()
        val sharedPref = context?.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        sharedPref?.registerOnSharedPreferenceChangeListener(preferenceChangeListener)

        // Optional: call manually to set initial state
        updateAccountActivationUI(sharedPref?.getString("account_block_status", ""))
    }

    override fun onPause() {
        super.onPause()
        val sharedPref = context?.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        sharedPref?.unregisterOnSharedPreferenceChangeListener(preferenceChangeListener)
    }


    private fun setSliderImage() {
        val data = controller.getInstance().api.sliderImage(js)
        data.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                Log.d("Dashboard", "call: $call")
                Log.d("Dashboard", "onResponse: " + response.body().toString())
                val sliderData = response.body()!!["sliderdata"].asJsonArray
                val status = response.body()!!["status"].asBoolean
                Log.d("result", "onResponse: $sliderData")

                if (status) {
                    for (i in 0 until sliderData.size()) {
                        val jsonArray1 = sliderData[i].asJsonObject
                        val id = jsonArray1["image_id"].asString
                        val imgUrl = jsonArray1["slider_image"].asString
                        //Log.d("id", "onResponse: "+id);
                        Log.d("jsonArray", "onResponse: $jsonArray1")
                        val model = slider_model()
                        model.setSlider_image(imgUrl)
                        model.setImage_id(id)
                    }

                }

            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.d("Dashboard", "fail: $t")
            }
        })
    }


    private fun setRecyclerViewData() {
        val data1 = controller.getInstance().api.dashBoard(js)
        data1.enqueue(object : Callback<JsonObject> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                Log.d("onResponse1233", "onResponse1233: " + response.body().toString())
                mWhatAppNumber!!.text = response.body()!!["mobile_no"].asString
                whatsApp_a!!.text = response.body()!!["telegram_no"].asString
                account_block_status = response.body()!!["account_block_status"].asString

                if (accountActivationStatus == "0"){
                    mWhatAppNumber!!.visibility = View.GONE
                    whatsApp_a!!.visibility = View.GONE
                }

                val gameData = response.body()!!["result"].asJsonArray
                val status = response.body()!!["status"].asBoolean
                mMarquu_title!!.text = response.body()!!["app_marquee_msg"].toString()

                betting_status = response.body()!!["betting_status"].asString
                account_block_status = response.body()!!["account_block_status"].asString

                val sharedPref = context?.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                if (sharedPref != null) {
                    sharedPref.edit().putString("account_block_status", account_block_status).apply()
                }


                withdraw_status = response.body()!!["withdraw_status"].asString
                transfer_point_status = response.body()!!["transfer_point_status"].asString
                maintainence_msg_status = response.body()!!["maintainence_msg_status"].asString

                user_current_version = response.body()!!["user_current_version"].asString
                user_minimum_version = response.body()!!["user_minimum_version"].asString
                action_btn_text = response.body()!!["action_btn_text"].asString
                link_btn_text = response.body()!!["link_btn_text"].asString
                message = response.body()!!["message"].asString



                val device_result_arr = response.body()!!.getAsJsonArray("device_result")
                Log.d("result", "onResponse: $betting_status")


                // mobile_no = response.body()!!["mobile_no"].asString
                //telegram_id = response.body()!!["telegram_no"].asString
                // app_link = response.body()!!["app_link"].asString
                // share_msg = response.body()!!["share_msg"].asString
                // textViewHeader!!.text = response.body()!!["user_name"].asString
                //username.setText(response.body().get("user_name").getAsString());
                // textViewMobile!!.text = response.body()!!["mobile"].asString
               // marquu_title!!.text =unique
                //userMobile.setText(response.body().get("mobile").getAsString());

                //tvMarque.setText(app_marquee_msg);

//                TextView whatsAppNum = findViewById(R.id.whatsAppNum);
//                whatsAppNum.setText(mobile_no);
//
//                TextView telegramNum = findViewById(R.id.telegramNum);
//                telegramNum.setText(telegram_id);
//
//                TextView mobileNum = findViewById(R.id.mobileNum);
//                mobileNum.setText(mobile_no);
                if (response.body()!!["user_name"].asString == "") {
                    // profileText!!.text = ""
                } else {
                    val s = StringBuilder()
                    for (j in 0..0) {
                        // s.append(textViewHeader!!.text[0])
                    }
                    // profileText!!.text = s
                }
                if (status) {
                    gameDataModels.clear()
                    for (i in 0 until gameData.size()) {
                        val jsonArray1 = gameData[i].asJsonObject
                        val game_name = jsonArray1["game_name"].asString
                        val open_time = jsonArray1["open_time"].asString
                        val close_time = jsonArray1["close_time"].asString
                        val openResult = jsonArray1["open_result"].asString
                        val closeResult = jsonArray1["close_result"].asString
                        val marketMessage = jsonArray1["msg"].asString
                        val game_url = jsonArray1["web_chart_url"].asString
                        val game_url_pana = jsonArray1["web_chart_url_pana"].asString
                        val time_srt = jsonArray1["time_srt"].asString
                        val msg_status = jsonArray1["msg_status"].asString
                        val game_id = jsonArray1["game_id"].asString


                        //Log.d("id", "onResponse: "+id);
                        Log.d("jsonArray", "onResponse: $jsonArray1")


                        context?.let { safeContext ->
                            val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(
                                safeContext,
                                LinearLayoutManager.VERTICAL,
                                false
                            )
                            recycler!!.layoutManager = layoutManager
                        }


                        val model = gameDataModel()

                        model.setGame_name(game_name)
                        model.setOpenTime(open_time)
                        model.setCloseTime(close_time)
                        model.setWebChartUrl(game_url)
                        model.setWebChartUrlPana(game_url_pana)
                        model.setOpenResult(openResult)
                        model.setCloseResult(closeResult)
                        model.setMarketMessage(marketMessage)
                        model.setTime_srt(time_srt)
                        model.setMsg_status(msg_status)
                        model.setGame_id(game_id)
                        model.setBetting_status(betting_status)

                        gameDataModels.add(model)
                    }
                    val adapter = NewTransactionAdapter(
                        context!!,
                        gameDataModels,
                        object : NewTransactionAdapter.mMyGameController {

                            override fun mChartInfo(list: gameDataModel) {
                                val urlChart = list.webChartUrl
                                val urlPanaChart = list.webChartUrlPana
                                val gameName = list.game_name
                                showCustomDialog(urlChart, gameName,urlPanaChart)
                            }

                            override fun mAllGameClick(list: gameDataModel) {
                                val intent = Intent(requireActivity(), Games::class.java)
                                intent.putExtra("gameDataModel", list)
                                startActivity(intent)
                            }
                        })
                    recycler!!.adapter = adapter
                    adapter.notifyDataSetChanged()
                    if (account_block_status == "0") {
                        //userLogout()
                    } else {
                        if (device_Id_list.size != 0) {
                            if (!device_Id_list.contains(androidId)) {
                                // userLogout()
                            } else {
                                for (i in 0 until device_result_arr.size()) {
                                    val jsObj = device_result_arr[i].asJsonObject
                                    Log.d("jsOBJ", "onResponse: $jsObj")
                                    device_Id_list.add(jsObj["device_id"].asString)
                                    if (androidId == jsObj["device_id"].asString) {
                                        if (jsObj["logout_status"].asString == "1" || jsObj["security_pin_status"].asString == "1") {
                                            //userLogout()
                                            break
                                        }
                                        break
                                    }
                                }
                            }
                        } else {
                            for (i in 0 until device_result_arr.size()) {
                                val jsObj = device_result_arr[i].asJsonObject
                                Log.d("jsOBJ", "onResponse: $jsObj")
                                device_Id_list.add(jsObj["device_id"].asString)
                                if (androidId == jsObj["device_id"].asString) {
                                    if (jsObj["logout_status"].asString == "1" || jsObj["security_pin_status"].asString == "1") {
                                        // userLogout()
                                        break
                                    }
                                    break
                                }
                            }
                        }
                    }

                    if (maintainence_msg_status == "1") {
                        val maintenance = Intent(requireActivity(), Maintenance::class.java)
                        maintenance.putExtra(
                            "app_maintainence_msg",
                            response.body()!!["app_maintainence_msg"].asString
                        )
                        startActivity(maintenance)
                    }
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Toast.makeText(
                    requireActivity(),
                    "Something went wrong.... Try again later",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }


    private fun showCustomDialog(urlChart: String, mGameName: String, urlPanaChart: String?) {
        // Create the dialog
        val dialog = Dialog(requireActivity())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.chart_info)

        // Make the dialog fullscreen
        if (dialog.window != null) {
            dialog.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        // Set up close button
        val closeIcon = dialog.findViewById<ImageView>(R.id.closeIcon)
        closeIcon?.setOnClickListener {
            dialog.dismiss()
        }

        // Set up Open Kamal Day button
        val mJodiChart = dialog.findViewById<TextView>(R.id.JodiChart)
        mJodiChart?.setOnClickListener {
            startActivity(Intent(requireActivity(), WebView::class.java).apply {
                putExtra("url",urlChart )
                putExtra("name", mGameName)
            })
            dialog.dismiss()
        }

        // Set up Close Kamal Day button
        val mPanaChart = dialog.findViewById<TextView>(R.id.PanaChart)
        mPanaChart?.setOnClickListener {
            if (urlPanaChart != null) {
                startActivity(Intent(requireActivity(), WebView::class.java).apply {
                    putExtra("url", urlPanaChart)
                    putExtra("name", mGameName)
                })
            } else {
                Toast.makeText(requireContext(), "URL not available", Toast.LENGTH_SHORT).show()
            }
            dialog.dismiss()
        }

        dialog.show()
    }

}
