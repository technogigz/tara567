package com.myTara567.app.dashboard

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import androidx.viewpager.widget.ViewPager
import com.google.android.material.navigation.NavigationView
import com.google.gson.JsonObject
import com.myTara567.app.Login
import com.myTara567.app.Maintenance
import com.myTara567.app.ManageGooglePayActivity
import com.myTara567.app.ManagePaytmActivity
import com.myTara567.app.ManagePhonePeActivity
import com.myTara567.app.R
import com.myTara567.app.Wallet.Wallet
import com.myTara567.app.Wallet.Wallet_withdrawal
import com.myTara567.app.dashboard.allbis.ui.allbids.BidHistory
import com.myTara567.app.serverApi.controller
import com.myTara567.app.starline.StarLine
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Timer
import java.util.TimerTask


class Dashboard : AppCompatActivity() {

    var spUnique_token: SharedPreferences? = null
    var js: JsonObject? = null
    var unique_token: String? = null
    var appKey: String? = null
    var userName: String? = null
    var mobile: String? = null
    var mobile_no: String? = null
    var telegram_id: String? = null
    var unique: String? = null
    var images: ArrayList<slider_model> = ArrayList()
    var gameDataModels: ArrayList<gameDataModel> = ArrayList()
    var recycler: RecyclerView? = null
    var navigationView: NavigationView? = null
    var drawerLayout: DrawerLayout? = null
    var navIcon: ImageView? = null
    var betting_status: String? = null
    var account_block_status: String? = null
    var withdraw_status: String? = null
    var transfer_point_status: String? = null
    var app_marquee_msg: String? = null
    var textViewHeader: TextView? = null
    var textViewMobile: TextView? = null
    var biddingStatus: Int = 0
    var profileText: TextView? = null
    var username: TextView? = null
    var userMobile: TextView? = null
    var whatsApp: TextView? = null
    var telegram: TextView? = null
    var phone: TextView? = null
    var addPointsWallet: TextView? = null
    var mTitle: TextView? = null
    var starline: LinearLayout? = null
    var swipeRefresh: SwipeRefreshLayout? = null

    var currentPage: Int = 0
    var timer: Timer? = null
    val DELAY_MS: Long = 2500 //delay in milliseconds before task is to be executed
    val PERIOD_MS: Long = 2500
    var app_link: String? = null
    var share_msg: String? = null
    var maintainence_msg_status: String? = null
    var user_current_version: String? = null
    var user_minimum_version: String? = null
    var otherGames: LinearLayout? = null
    var walletLayout: LinearLayout? = null
    var notificationLayout: LinearLayout? = null
    var llDepositOnline: LinearLayout? = null
    var profileImg: ImageView? = null
    var walletBalance: TextView? = null
    var tvMarque: TextView? = null
    var notification_counter: TextView? = null
    var androidId: String? = null
    var message: String? = null
    var versionName: String? = null
    var device_Id_list: ArrayList<String?> = ArrayList()
    var update_status: Boolean = true
    var action_btn_text: String? = null
    var link_btn_text: String? = null
    var view: View? = null


    @SuppressLint("HardwareIds")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dashboard)
        // Hide blocked UI if account is already blocked
        val pref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        account_block_status = pref.getString("account_block_status", "")

        if (account_block_status == "0") {
            hideBlockedUI()
        }


        mTitle = findViewById(R.id.mTitle)
        val tarText = SpannableString("TAR")
        tarText.setSpan(
            ForegroundColorSpan(Color.parseColor("#007AFF")),
            0,
            tarText.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        // Create a SpannableString for "567"
        val numberText = SpannableString("567")
        numberText.setSpan(
            ForegroundColorSpan(Color.parseColor("#000000")),
            0,
            numberText.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        // Combine both parts using SpannableStringBuilder
        val spannable = SpannableStringBuilder()
        spannable.append(tarText)
        spannable.append(numberText)
        mTitle!!.setText(spannable)


        androidId = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
        Log.d("device Id", "onCreate: $androidId")
        try {
            versionName = this.packageManager.getPackageInfo(this.packageName, 0).versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }


        appKey = getSharedPreferences("app_key", MODE_MULTI_PROCESS).getString("app_key", "")
        Log.d("login appkey", "onCreate: $appKey")

        spUnique_token = applicationContext.getSharedPreferences("unique_token", MODE_MULTI_PROCESS)
        unique = spUnique_token!!.getString("unique_token", "")
        Log.d("signUp editor", "home: $unique")




        // llDepositOnline = findViewById(R.id.ll_depositOnline);
        //notificationLayout = findViewById(R.id.notificationLayout);

        notification_counter = findViewById(R.id.notification_counter)

        starline = findViewById(R.id.starline)
        phone = findViewById(R.id.phone)
                //telegram = findViewById(R.id.telegram);
        whatsApp = findViewById(R.id.whatsApp)
        navigationView = findViewById(R.id.navigationView)
        drawerLayout = findViewById(R.id.drawerLayout)
        navIcon = findViewById(R.id.nav_icon)
        recycler = findViewById(R.id.home_recycler)
        //walletLayout = findViewById(R.id.walletLayout);
        //profileImg = findViewById(R.id.profileImg);
        walletBalance = findViewById(R.id.walletBalanceTxt)
        view = navigationView!!.getHeaderView(0)
        textViewHeader = view!!.findViewById(R.id.headerName)
        textViewMobile = view!!.findViewById(R.id.textView3)
        profileText = view!!.findViewById(R.id.nameStart)
        otherGames = findViewById(R.id.otherGames)
        phone?.visibility = View.GONE



        /* tvMarque = findViewById(R.id.tv_marque);
        tvMarque.setSelected(true);*/
        //addPointsWallet = findViewById(R.id.addPoints);


//9955332211
        // username = findViewById(R.id.username);
        //userMobile = findViewById(R.id.userMobile);
        navIcon!!.setOnClickListener(View.OnClickListener {
            drawerLayout!!.openDrawer(
                GravityCompat.START
            )
        })


        js = JsonObject()
        js!!.addProperty("env_type", "Prod")
        js!!.addProperty("app_key", appKey)
        js!!.addProperty("unique_token", unique)

        //sliderImage
        setSliderImage()

        //recycler view
        setRecyclerViewData()

        /*  walletLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent wallet = new Intent(getApplicationContext(), Wallet.class);
                wallet.putExtra("withdraw_status", withdraw_status);
                wallet.putExtra("transfer_point_status", transfer_point_status);
                startActivity(wallet);
            }
        });*/
        swipeRefresh = findViewById(R.id.swipeRefresh)

        swipeRefresh!!.setOnRefreshListener(OnRefreshListener {
            setRecyclerViewData()
            swipeRefresh!!.setRefreshing(false)
        })

        /* llDepositOnline.setOnClickListener(view -> {
            startActivity(new Intent(Dashboard.this, DepositOnlineActivity.class));
        });*/
        whatsApp!!.setOnClickListener(View.OnClickListener {
            val url = "https://wa.me/$mobile_no"
            val i = Intent(Intent.ACTION_VIEW)
            i.setData(Uri.parse(url))
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            applicationContext.startActivity(i)
        })

        /* telegram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://telegram.me/" + telegram_id;
                Intent telegramIntent = new Intent(Intent.ACTION_VIEW);
                telegramIntent.setData(Uri.parse(url));
                startActivity(telegramIntent);
            }
        });*/
        phone!!.setOnClickListener(View.OnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.setData(Uri.parse("tel:$mobile_no"))
            startActivity(intent)
        })

        starline!!.setOnClickListener(View.OnClickListener {
            startActivity(
                Intent(
                    applicationContext,
                    StarLine::class.java
                )
            )
        })

        //for home nav
        val home = view!!.findViewById<LinearLayout>(R.id.linearLayout10)
        home.setOnClickListener {
            drawerLayout!!.closeDrawer(
                GravityCompat.START
            )
        }

        //for profile nav
        val profile = view!!.findViewById<LinearLayout>(R.id.linearLayout11)
        profile.setOnClickListener { view1: View? ->
            drawerLayout!!.closeDrawer(GravityCompat.START)
            startActivity(Intent(applicationContext, User_profile::class.java))
        }

        //for wallet nav
        val wallet = view!!.findViewById<ConstraintLayout>(R.id.myWallet)
        wallet.setOnClickListener {
            drawerLayout!!.closeDrawer(GravityCompat.START)
            val wallet = Intent(applicationContext, Wallet::class.java)
            wallet.putExtra("withdraw_status", withdraw_status)
            wallet.putExtra("transfer_point_status", transfer_point_status)
            startActivity(wallet)
        }

        /* addPointsWallet.setOnClickListener(v -> {
     //Intent k = new Intent(getApplicationContext(), AddFund.class);
     Intent k = new Intent(getApplicationContext(), DepositOnlineActivity.class);
     k.putExtra("Title", "Add Fund");
     startActivity(k);
 });*/

        //for add fund nav
        val addFund = view!!.findViewById<ConstraintLayout>(R.id.addFund)
        addFund.setOnClickListener {
            drawerLayout!!.closeDrawer(GravityCompat.START)
            //Intent k = new Intent(getApplicationContext(), AddFund.class);
            val k = Intent(applicationContext, DepositOnlineActivity::class.java)
            k.putExtra("Title", "Add Fund")
            startActivity(k)
        }

        //for withdraw fund nav
        val withdrawFund = view!!.findViewById<ConstraintLayout>(R.id.withdrawFund)
        withdrawFund.setOnClickListener {
            drawerLayout!!.closeDrawer(GravityCompat.START)
            val j = Intent(applicationContext, Wallet_withdrawal::class.java)
            j.putExtra("Title", "Withdraw Fund")
            j.putExtra("withdraw_status", withdraw_status)
            startActivity(j)
        }

        //for share nav
        val share = view!!.findViewById<LinearLayout>(R.id.linearLayout23)
        share.setOnClickListener {
            drawerLayout!!.closeDrawer(GravityCompat.START)
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.setType("text/plain")
            var shareMessage = """
                
                $share_msg
                
                
                """.trimIndent()
            shareMessage = shareMessage + app_link + "\n\n"
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
            startActivity(Intent.createChooser(shareIntent, "Choose One"))
        }

        //for rating nav
        val rating = view!!.findViewById<LinearLayout>(R.id.linearLayout25)
        rating.setOnClickListener {
            drawerLayout!!.closeDrawer(GravityCompat.START)
            val i = Intent(Intent.ACTION_VIEW)
            i.setData(Uri.parse(app_link))
            startActivity(i)
        }

        //for bid history nav
        val bid_hist = view!!.findViewById<LinearLayout>(R.id.linearLayout21)
        bid_hist.setOnClickListener {
            drawerLayout!!.closeDrawer(GravityCompat.START)
            val i = Intent(applicationContext, BidHistory::class.java)
            startActivity(i)
        }

        //for HowToPlay nav
        val HowToPlay = view!!.findViewById<LinearLayout>(R.id.howToPlay)
        HowToPlay.setOnClickListener {
            drawerLayout!!.closeDrawer(GravityCompat.START)
            val i = Intent(applicationContext, HowToPlay::class.java)
            startActivity(i)
        }


        //for win history nav
        val win_hist = view!!.findViewById<LinearLayout>(R.id.linearLayout20)
        win_hist.setOnClickListener {
            drawerLayout!!.closeDrawer(GravityCompat.START)
            val i = Intent(applicationContext, Win_history::class.java)
            startActivity(i)
        }

        //for game rates nav
        val game_rates = view!!.findViewById<LinearLayout>(R.id.linearLayout22)
        game_rates.setOnClickListener {
            drawerLayout!!.closeDrawer(GravityCompat.START)
            val i = Intent(applicationContext, Game_rates::class.java)
            startActivity(i)
        }

        // for change password nav
        val change_password = view!!.findViewById<LinearLayout>(R.id.linearLayout27)
        change_password.setOnClickListener { view2: View? ->
            drawerLayout!!.closeDrawer(GravityCompat.START)
            startActivity(Intent(applicationContext, Change_password::class.java))
        }

        // for contact us nav
        val contact_us = view!!.findViewById<LinearLayout>(R.id.linearLayout26)
        contact_us.setOnClickListener { view3: View? ->
            drawerLayout!!.closeDrawer(GravityCompat.START)
            startActivity(Intent(applicationContext, Contact_us::class.java))
        }

        val phonePe = view!!.findViewById<ConstraintLayout>(R.id.phonePe)
        phonePe.setOnClickListener { v: View? ->
            drawerLayout!!.closeDrawer(GravityCompat.START)
            startActivity(Intent(this@Dashboard, ManagePhonePeActivity::class.java))
        }

        val googlePe = view!!.findViewById<ConstraintLayout>(R.id.googlePay)
        googlePe.setOnClickListener { v: View? ->
            drawerLayout!!.closeDrawer(GravityCompat.START)
            startActivity(Intent(this@Dashboard, ManageGooglePayActivity::class.java))
        }

        val paytm = view!!.findViewById<ConstraintLayout>(R.id.payTm)
        paytm.setOnClickListener { v: View? ->
            drawerLayout!!.closeDrawer(GravityCompat.START)
            startActivity(Intent(this@Dashboard, ManagePaytmActivity::class.java))
        }

        /*notificationLayout.setOnClickListener(v -> {`
    startActivity(new Intent(Dashboard.this, Notification.class));
});*/

        //for Logout nav
        val logout = view!!.findViewById<LinearLayout>(R.id.linearLayout29)
        logout.setOnClickListener {
            drawerLayout!!.closeDrawer(GravityCompat.START)
            val v = LayoutInflater.from(this@Dashboard)
                .inflate(R.layout.logout_dialog_box, null)
            val alertDialogBuilder = AlertDialog.Builder(this@Dashboard)

            alertDialogBuilder.setView(v)
            alertDialogBuilder.setCancelable(false)
            val alertDialog = alertDialogBuilder.create()
            alertDialog.show()
            alertDialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)

            val cancel = v.findViewById<Button>(R.id.cancel_button)
            cancel.setOnClickListener { alertDialog.dismiss() }
            val submit = v.findViewById<Button>(R.id.submitBtn)
            submit.setOnClickListener {
                userLogout()
                alertDialog.cancel()
            }
            drawerLayout!!.closeDrawer(GravityCompat.START)
        }
    }

    private fun hideBlockedUI() {
        findViewById<View>(R.id.starline)?.visibility = View.GONE
        findViewById<View>(R.id.walletBalanceTxt)?.visibility = View.GONE
        findViewById<View>(R.id.otherGames)?.visibility = View.GONE

        // If view is null, avoid crash (safety check)
        view?.findViewById<LinearLayout>(R.id.linearLayout21)?.visibility = View.GONE // Bid History
        view?.findViewById<ConstraintLayout>(R.id.withdrawFund)?.visibility = View.GONE
        view?.findViewById<ConstraintLayout>(R.id.myWallet)?.visibility = View.GONE
        view?.findViewById<LinearLayout>(R.id.linearLayout22)?.visibility = View.GONE // Game Rates
    }


    override fun onBackPressed() {
        super.onBackPressed()
        val v = LayoutInflater.from(this@Dashboard).inflate(R.layout.exit_dialog_box, null)
        val alertDialogBuilder = AlertDialog.Builder(this@Dashboard)

        alertDialogBuilder.setView(v)
        alertDialogBuilder.setCancelable(false)
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
        alertDialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)

        val cancel = v.findViewById<Button>(R.id.cancel_button)
        cancel.setOnClickListener { alertDialog.dismiss() }
        val submit = v.findViewById<Button>(R.id.submitBtn)
        submit.setOnClickListener {
            finish()
            alertDialog.cancel()
        }
    }

    private fun setSliderImage() {
        //Image Slider

        val mViewPager = findViewById<View>(R.id.slider) as ViewPager

        val data = controller.getInstance().api.sliderImage(js)
        data.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                Log.d("Dashboard", "call: $call")
                Log.d("Dashboard", "onResponse: " + response.body().toString())
                val sliderData = response.body()!!["sliderdata"].asJsonArray
                val status = response.body()!!["status"].asBoolean
                Log.d("result", "onResponse: $sliderData")

                //JsonObject jsonResponse = new JsonObject();
                //JsonArray jsonArray = jsonResponse.getAsJsonArray("sliderdata");
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

                        images.add(slider_model(id, imgUrl))
                    }


                    //        mViewPager.setPadding(120, 0, 120, 0);
                    /*ImageAdapter adapterView = new ImageAdapter(Dashboard.this, images);
                    mViewPager.setAdapter(adapterView);*/


                    /*After setting the adapter use the timer */
                    val handler = Handler()
                    val Update = Runnable {
                        mViewPager.setCurrentItem(currentPage++, true)
                        if (currentPage == images.size) {
                            currentPage = 0
                        }
                    }

                    timer = Timer() // This will create a new Thread
                    timer!!.schedule(object : TimerTask() {
                        // task to be scheduled
                        override fun run() {
                            handler.post(Update)
                        }
                    }, DELAY_MS, PERIOD_MS)
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.d("Dashboard", "fail: $t")
            }
        })
    }

    override fun onResume() {
        super.onResume()

        val pref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        if (pref.getString("account_block_status", "") == "0") {
            hideBlockedUI()
        }
        setRecyclerViewData()

    }

    private fun setRecyclerViewData() {
        val data1 = controller.getInstance().api.dashBoard(js)
        data1.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                Log.d("Dashboard", "onResponse: " + response.body().toString())
                val gameData = response.body()!!["result"].asJsonArray
                val status = response.body()!!["status"].asBoolean
                betting_status = response.body()!!["betting_status"].asString
                account_block_status = response.body()!!["account_block_status"].asString


                withdraw_status = response.body()!!["withdraw_status"].asString
                transfer_point_status = response.body()!!["transfer_point_status"].asString
                maintainence_msg_status = response.body()!!["maintainence_msg_status"].asString
                app_marquee_msg = response.body()!!["app_marquee_msg"].asString
                user_current_version = response.body()!!["user_current_version"].asString
                user_minimum_version = response.body()!!["user_minimum_version"].asString
                action_btn_text = response.body()!!["action_btn_text"].asString
                link_btn_text = response.body()!!["link_btn_text"].asString
                message = response.body()!!["message"].asString

                val device_result_arr = response.body()!!.getAsJsonArray("device_result")
                biddingStatus = response.body()!!["betting_status"].asInt
                //walletBalance.setText(response.body().get("wallet_amt").getAsString());
                Log.d("result", "onResponse: $betting_status")

                mobile_no = response.body()!!["mobile_no"].asString
                telegram_id = response.body()!!["telegram_no"].asString
                app_link = response.body()!!["app_link"].asString
                share_msg = response.body()!!["share_msg"].asString
                textViewHeader!!.text = response.body()!!["user_name"].asString
                //username.setText(response.body().get("user_name").getAsString());
                textViewMobile!!.text = response.body()!!["mobile"].asString


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
                    profileText!!.text = ""
                } else {
                    val s = StringBuilder()
                    for (j in 0..0) {
                        s.append(textViewHeader!!.text[0])
                    }
                    profileText!!.text = s
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
                        val time_srt = jsonArray1["time_srt"].asString
                        val msg_status = jsonArray1["msg_status"].asString
                        val game_id = jsonArray1["game_id"].asString

                        //Log.d("id", "onResponse: "+id);
                        Log.d("jsonArray", "onResponse: $jsonArray1")


                        val layoutManager: RecyclerView.LayoutManager =
                            LinearLayoutManager(
                                applicationContext,
                                LinearLayoutManager.VERTICAL,
                                false
                            )

                        recycler!!.layoutManager = layoutManager

                        val model = gameDataModel()

                        model.setGame_name(game_name)
                        model.setOpenTime(open_time)
                        model.setCloseTime(close_time)
                        model.setWebChartUrl(game_url)
                        model.setOpenResult(openResult)
                        model.setCloseResult(closeResult)
                        model.setMarketMessage(marketMessage)
                        model.setTime_srt(time_srt)
                        model.setMsg_status(msg_status)
                        model.setGame_id(game_id)
                        model.setBetting_status(betting_status)

                        gameDataModels.add(model)
                    }
                   /* val adapter = NewTransactionAdapter(applicationContext,gameDataModels,object :NewTransactionAdapter.mMyGameController{
                        override fun mChartInfo(id: String) {
                            Toast.makeText(this@Dashboard, "chartOpne", Toast.LENGTH_SHORT).show()

                        }

                        override fun mChartInfo(list: gameDataModel) {
                            TODO("Not yet implemented")
                        }

                        override fun mAllGameClick(list: gameDataModel) {
                            val intent = Intent(this@Dashboard,Games::class.java)
                            intent.putExtra("gameDataModel",list)
                            startActivity(intent)
                        }
                    })*/
                    /*recycler!!.adapter = adapter
                    adapter.notifyDataSetChanged()*/

                    if (betting_status == "0") {
                        view!!.findViewById<View>(R.id.linearLayout19).visibility =
                            View.GONE
                        view!!.findViewById<View>(R.id.gameDetails).visibility =
                            View.GONE
                        view!!.findViewById<View>(R.id.addFund).visibility =
                            View.GONE
                        view!!.findViewById<View>(R.id.withdrawFund).visibility =
                            View.GONE
                        findViewById<View>(R.id.ll_add_funds).visibility =
                            View.GONE
                        findViewById<View>(R.id.view_top).visibility =
                            View.GONE
                        findViewById<View>(R.id.ll_homeddd).visibility =
                            View.GONE
                        // walletLayout.setVisibility(View.GONE);
                        otherGames!!.visibility = View.GONE
                    } else {
                        view!!.findViewById<View>(R.id.linearLayout19).visibility =
                            View.VISIBLE
                        view!!.findViewById<View>(R.id.gameDetails).visibility =
                            View.VISIBLE
                        view!!.findViewById<View>(R.id.addFund).visibility =
                            View.VISIBLE
                        view!!.findViewById<View>(R.id.withdrawFund).visibility =
                            View.VISIBLE
                        findViewById<View>(R.id.ll_add_funds).visibility =
                            View.VISIBLE
                        findViewById<View>(R.id.view_top).visibility =
                            View.VISIBLE
                        findViewById<View>(R.id.ll_homeddd).visibility =
                            View.VISIBLE
                        // walletLayout.setVisibility(View.VISIBLE);
                        otherGames!!.visibility = View.VISIBLE
                    }

                    if (account_block_status == "0") {
                        userLogout()
                    } else {
                        if (device_Id_list.size != 0) {
                            if (!device_Id_list.contains(androidId)) {
                                userLogout()
                            } else {
                                for (i in 0 until device_result_arr.size()) {
                                    val jsObj = device_result_arr[i].asJsonObject
                                    Log.d("jsOBJ", "onResponse: $jsObj")
                                    device_Id_list.add(jsObj["device_id"].asString)
                                    if (androidId == jsObj["device_id"].asString) {
                                        if (jsObj["logout_status"].asString == "1" || jsObj["security_pin_status"].asString == "1") {
                                            userLogout()
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
                                        userLogout()
                                        break
                                    }
                                    break
                                }
                            }
                        }
                    }

                    if (maintainence_msg_status == "1") {
                        val maintenance = Intent(this@Dashboard, Maintenance::class.java)
                        maintenance.putExtra(
                            "app_maintainence_msg",
                            response.body()!!["app_maintainence_msg"].asString
                        )
                        startActivity(maintenance)
                        finish()
                    }

                    if (update_status) {
                        if (versionName!!.replace(".", "")
                                .toInt() < user_current_version!!.replace(".", "").toInt()
                        ) {
                            //  Typeface face = Typeface.createFromAsset(getAssets(), "font/roboto_bold.xml");

                            val v = LayoutInflater.from(applicationContext)
                                .inflate(R.layout.check_version_dialog_box, null)
                            val alertDialogBuilder = AlertDialog.Builder(this@Dashboard)

                            val messageTxt = v.findViewById<TextView>(R.id.updateMessage)
                            // messageTxt.setTypeface(face);
                            messageTxt.text = message

                            alertDialogBuilder.setView(v)
                            alertDialogBuilder.setCancelable(false)
                            val alertDialog = alertDialogBuilder.create()
                            alertDialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)

                            val cancel = v.findViewById<Button>(R.id.cancel_button)
                            if (versionName!!.replace(".", "")
                                    .toInt() < user_minimum_version!!.replace(".", "").toInt()
                            ) {
                                cancel.visibility = View.GONE
                            } else {
                                cancel.text = action_btn_text
                                cancel.setPadding(0, 30, 0, 30)
                                cancel.setOnClickListener { alertDialog.dismiss() }
                            }

                            val submit = v.findViewById<Button>(R.id.submitBtn)
                            submit.text = link_btn_text
                            submit.setPadding(0, 30, 0, 30)
                            submit.setOnClickListener {
                                val i = Intent(Intent.ACTION_VIEW)
                                i.setData(Uri.parse(app_link))
                                startActivity(i)
                                // alertDialog.cancel();
                            }
                            alertDialog.show()
                            update_status = false
                        } else {
                            update_status = false
                        }
                    }
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Toast.makeText(
                    applicationContext,
                    "Something went wrong.... Try again later",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun userLogout() {
        Log.d("signUp unique", "home: $unique_token")
        val editor = spUnique_token!!.edit()
        editor.remove("unique_token")
        editor.apply()
        Log.d("signUp editor", "home: $unique")
        startActivity(Intent(applicationContext, Login::class.java))
        finish()
    }



    /*fun mSubscriptionDailog() {
        val dialogLayout = LayoutInflater.from(this).inflate(R.layout.dailog_subscription_successfully, null)
        val myBuilder = android.app.AlertDialog.Builder(this, 0).create()
        myBuilder.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        myBuilder.apply {
            setView(dialogLayout)
            setCancelable(false)
        }.show()
        Handler().postDelayed({
            startActivity(Intent(this, HomeActivity::class.java).apply {
                putExtra("AddCard", "AddCard")
            })
        }, 3000)
    }*/
}