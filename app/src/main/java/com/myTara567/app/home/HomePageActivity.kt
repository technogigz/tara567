package com.myTara567.app.home

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.gson.JsonObject
import com.myTara567.app.Notification
import com.myTara567.app.R
import com.myTara567.app.home.fragment.PassBookActivity
import com.myTara567.app.dashboard.gameDataModel
import com.myTara567.app.databinding.ActivityHomePageBinding
import com.myTara567.app.home.fragment.AllBidesFragment
import com.myTara567.app.home.fragment.ChatFragment
import com.myTara567.app.home.fragment.GameRatesFragment
import com.myTara567.app.home.fragment.HomeFragment
import com.myTara567.app.home.fragment.TeramConditionFragment
import com.myTara567.app.home.fragment.WalletFragment
import com.myTara567.app.new_ui_update.EnterMPINActivity
import com.myTara567.app.dashboard.allbis.ui.wallet.PaymentActivity
import com.myTara567.app.serverApi.controller
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomePageActivity : AppCompatActivity(){
    lateinit var binding: ActivityHomePageBinding
    private var spUniqueToken: SharedPreferences? = null
    var js: JsonObject? = null
    var unique_token: String? = null
    var appKey: String? = null
    var unique: String? = null
    private lateinit var tarText: SpannableString
    private lateinit var numberText: SpannableString

    val preference: SharedPreferences? = null

    var gameDataModels: ArrayList<gameDataModel> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityHomePageBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        tarText = SpannableString("TARA").apply {
            setSpan(
                ForegroundColorSpan(
                    ContextCompat.getColor(
                        this@HomePageActivity,
                        R.color.blue_color
                    )
                ),
                0, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

        numberText = SpannableString("567").apply {
            setSpan(
                ForegroundColorSpan(ContextCompat.getColor(this@HomePageActivity, R.color.black)),
                0, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

        setupDrawerLayout()
        maSpannableText()
        mLoadFragment("Home")
        mClickListener()

        val bottomNavigationView = binding.ivBottomSheep

        ViewCompat.setOnApplyWindowInsetsListener(bottomNavigationView) { view, insets ->
            val systemBarInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())

            val layoutParams = view.layoutParams as ViewGroup.MarginLayoutParams
            layoutParams.bottomMargin = systemBarInsets.bottom
            view.layoutParams = layoutParams

            // Return insets so that other views can also consume them if needed
            insets
        }


        // Get the appKey from SharedPreferences
        appKey = getSharedPreferences("app_key", Context.MODE_PRIVATE)?.getString("app_key", "")
        Log.d("login appkey", "onCreate: $appKey")

        // Get the unique token from SharedPreferences
        spUniqueToken = getSharedPreferences("unique_token", Context.MODE_PRIVATE)
        unique = spUniqueToken?.getString("unique_token", "")
        Log.d("signUp editor", "home: $unique")

        // Prepare the JsonObject for API request
        js = JsonObject().apply {
            addProperty("env_type", "Prod")
            addProperty("app_key", appKey)
            addProperty("unique_token", unique)
        }

        // Set RecyclerView data
        setRecyclerViewData()
    }

    private fun maSpannableText() {

        binding.mTitle.text = SpannableStringBuilder().apply {
            append(tarText).append(numberText)
        }
    }

    private fun setupDrawerLayout() {
        // Open the Drawer when clicking the nav icon
        binding.navIcon.setOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }

        // Access the header layout in the NavigationView
        val headerView = binding.navigationView.getHeaderView(0)
        val userlogout = headerView.findViewById<TextView>(R.id.mLogout)
        val side_cancle = headerView.findViewById<ImageView>(R.id.side_cancle)
        val mGmaeRating = headerView.findViewById<TextView>(R.id.mGame_rate)
        val mTerms_Conditions = headerView.findViewById<TextView>(R.id.mTerms_Conditions)
        val mVideos = headerView.findViewById<TextView>(R.id.mVideos)
        val mAccount_Statement = headerView.findViewById<TextView>(R.id.mAccount_Statement)

        side_cancle.setOnClickListener {
            if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                binding.drawerLayout.closeDrawer(GravityCompat.START)
            }
        }

        mAccount_Statement.setOnClickListener {
            if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                binding.drawerLayout.closeDrawer(GravityCompat.START)
                startActivity(Intent(this, PassBookActivity::class.java).apply {
                    putExtra("title", "Account Statement")
                })
            }
        }



        mTerms_Conditions.setOnClickListener {
            mLoadFragment("TeramCondition")
            if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                binding.drawerLayout.closeDrawer(GravityCompat.START)

            }
        }


        mVideos.setOnClickListener {
            if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                binding.drawerLayout.closeDrawer(GravityCompat.START)
                Toast.makeText(this, "under development", Toast.LENGTH_SHORT).show()
            }
        }

        mGmaeRating.setOnClickListener {
            mLoadFragment("GameRating")
            if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                binding.drawerLayout.closeDrawer(GravityCompat.START)
            }
        }

        userlogout.setOnClickListener {
            startActivity(Intent(this, EnterMPINActivity::class.java).apply {
                putExtra("checkActivity", "homepageactivity")
            })
        }

        binding.Notification.setOnClickListener {
            startActivity(Intent(this, Notification::class.java))
        }

        binding.mWallet.setOnClickListener {
            startActivity(Intent(this, PaymentActivity::class.java))
        }

        // Handle NavigationView item clicks
        binding.navigationView.setNavigationItemSelectedListener { menuItem: MenuItem ->
            when (menuItem.itemId) {
                R.id.mHomeFragment -> mLoadFragment("Home")
                R.id.mAllBidsFragment -> mLoadFragment("AllBids")
                R.id.mWalletFragment -> mLoadFragment("Wallet")
                R.id.mChatFragment -> mLoadFragment("Chat")
            }
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
    }

    private fun mClickListener() {
        binding.mHistoryFragment.setOnClickListener {
            startActivity(Intent(this, PassBookActivity::class.java).apply {
                putExtra("title", "PassBook")

            })
        }


        binding.mAllBidsFragment.setOnClickListener {
            mLoadFragment("AllBids")
            binding.mTitle.text = "All Bids"
            binding.mTitle.setTextColor(ContextCompat.getColor(this, R.color.black))
        }
        binding.mHomeFragment.setOnClickListener {
            mLoadFragment("Home")
            maSpannableText()
        }
        binding.mWalletFragment.setOnClickListener {

            binding.mTitle.text = "Wallet"
            mLoadFragment("Wallet")
        }
        binding.mChatFragment.setOnClickListener {
            binding.mTitle.text = "Chat"
            mLoadFragment("Chat")
        }
    }

    private fun mLoadFragment(mFragmentType: String) {
        val transaction = supportFragmentManager.beginTransaction()

        when (mFragmentType) {
            "AllBids" -> {
                transaction.replace(R.id.fl, AllBidesFragment())
                supportActionBar?.title = "All Bids"
                binding.mTitle.text = "All Bids"
            }
            "Home" -> {
                transaction.replace(R.id.fl, HomeFragment())
                supportActionBar?.title = "Home"
                binding.mTitle.text = SpannableStringBuilder().apply {
                    append(tarText).append(numberText)
                }
            }
            "Wallet" -> {
                transaction.replace(R.id.fl, WalletFragment())
                supportActionBar?.title = "My Wallet"
                binding.mTitle.text = "My Wallet"
            }
            "Chat" -> {
                transaction.replace(R.id.fl, ChatFragment())
                supportActionBar?.title = "Chat"
                binding.mTitle.text = "Chat"
            }
            "GameRating" -> {
                transaction.replace(R.id.fl, GameRatesFragment())
                supportActionBar?.title = "Game Ratings"
                binding.mTitle.text = "Game Ratings"
            }
            "TeramCondition" -> {
                transaction.replace(R.id.fl, TeramConditionFragment())
                supportActionBar?.title = "Terms & Conditions"
                binding.mTitle.text = "Terms & Conditions"
            }
            else -> {
                transaction.replace(R.id.fl, HomeFragment())
                supportActionBar?.title = "Home"
                binding.mTitle.text = SpannableStringBuilder().apply {
                    append(tarText).append(numberText)
                }
            }
        }

        transaction.addToBackStack(null).commit()
    }



//    private fun mLoadFragment(mFragmentType: String) {
//        val transaction = supportFragmentManager.beginTransaction()
//        when (mFragmentType) {
//            "AllBids" -> transaction.replace(R.id.fl, AllBidesFragment())
//            "Home" -> transaction.replace(R.id.fl, HomeFragment())
//            "Wallet" -> transaction.replace(R.id.fl, WalletFragment())
//            "Chat" -> transaction.replace(R.id.fl, ChatFragment())
//            "GameRating" -> transaction.replace(R.id.fl, GameRatesFragment())
//            "TeramCondition" -> transaction.replace(R.id.fl, TeramConditionFragment())
//            else -> transaction.replace(R.id.fl, HomeFragment())
//        }
//        transaction.addToBackStack(null).commit()
//    }

    private fun setRecyclerViewData() {
        val data1 = controller.getInstance().api.dashBoard(js)
        data1.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    response.body()?.let { body ->

                        val hiddenUI = response.body()?.get("account_block_status")?.asString


//                        val hiddenUI = preference?.getString("account_block_status","").toString()
                        /// bro hiddenUI blank aa rha hia thik se check kro value - run k ye sahi code hai yhi se hide hoga - koi ek single text hide kro idhar
                        // run krke btawo
                        if (hiddenUI == "0"){
                            Log.d("Asads","yha pe hide wala code likho condition lagage    " + hiddenUI)
                            binding.mHistoryFragment.visibility = View.GONE
                            binding.mWalletFragment.visibility = View.GONE
                            binding.mHistoryFragment.visibility = View.GONE
                            binding.mAllBidsFragment.visibility = View.GONE
                            binding.walletLayout.visibility = View.GONE

                            binding.navigationView.findViewById<View>(R.id.mAccount_Statement)?.visibility = View.GONE
                            binding.navigationView.findViewById<View>(R.id.mAccount_image)?.visibility = View.GONE
                            binding.navigationView.findViewById<View>(R.id.mGame_image)?.visibility = View.GONE
                            binding.navigationView.findViewById<View>(R.id.mGame_rate)?.visibility = View.GONE
                            binding.navigationView.findViewById<View>(R.id.mTerms_Conditions)?.visibility = View.GONE
                            binding.navigationView.findViewById<View>(R.id.mTerms_image)?.visibility = View.GONE
                            binding.navigationView.findViewById<View>(R.id.mVideos_image)?.visibility = View.GONE
                            binding.navigationView.findViewById<View>(R.id.mVideos)?.visibility = View.GONE


                        }

                        val gameData = body["result"].asJsonArray
                        val status = body["status"].asBoolean
                        binding.walletAmount.text = "₹${body.get("wallet_amt").asString}"
                        val userName = body.get("user_name").asString
                        val userPhone = body.get("mobile").asString

                        val headerView = binding.navigationView.getHeaderView(0)
                        val mUserNameTextView = headerView.findViewById<TextView>(R.id.muserName)
                        val mUserPhoneNumberTextView =
                            headerView.findViewById<TextView>(R.id.muserPhoneNumber)

                        mUserNameTextView.text = userName
                        mUserPhoneNumberTextView.text = userPhone
                    }
                } else {
                    Toast.makeText(
                        this@HomePageActivity,
                        "Error: ${response.code()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Toast.makeText(
                    this@HomePageActivity,
                    "Something went wrong... Try again later",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    override fun onResume() {
        super.onResume()
        val call1 = controller.getInstance().api.getWalletBalance(js)
        call1.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                val walletAmount = response.body()!!["wallet_amt"].asInt.toString()
                binding.walletAmount.text = "₹$walletAmount"
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

}
