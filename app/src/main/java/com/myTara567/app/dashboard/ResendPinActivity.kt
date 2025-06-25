package com.myTara567.app.dashboard

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.JsonObject
import com.myTara567.app.R
import com.myTara567.app.databinding.ActivityResendPinBinding
import com.myTara567.app.serverApi.controller
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ResendPinActivity : AppCompatActivity() {

    private var mPin: String = ""
    private var spUniqueToken: SharedPreferences? = null
    private var appKey: String? = null
    private var uniqueToken: String? = null
    private var loginMobile: String? = null
    private var mPhone = ""
    private lateinit var binding: ActivityResendPinBinding
    private var progressLayout: RelativeLayout? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResendPinBinding.inflate(layoutInflater)
        setContentView(binding.root)
        progressLayout = findViewById(R.id.progressLayout)
        initView()
    }

    private fun initView() {
        // Fetch app key

        mPhone = intent.getStringExtra("number").toString()
        appKey = getSharedPreferences("app_key", MODE_PRIVATE)
            .getString("app_key", null)
        Log.d("ResendPinActivity", "AppKey: $appKey")

        // Fetch unique token
        spUniqueToken = getSharedPreferences("unique_token", MODE_PRIVATE)
        uniqueToken = spUniqueToken?.getString("unique_token", "")
        Log.d("ResendPinActivity", "UniqueToken: $uniqueToken")

        // Fetch login mobile number
        loginMobile = getSharedPreferences("user_name", MODE_PRIVATE).getString("phone", "")
        Log.d("ResendPinActivity", "LoginMobile: $loginMobile")

        // Set click listener for the submit button
        binding.mbSubmit.setOnClickListener {
            if (validateInput()) {
                updatePin()
            }
        }
        binding.tvBackBtn.setOnClickListener {
            onBackPressed()
        }
    }

    private fun validateInput(): Boolean {
        mPin = binding.mPin.text.toString().trim()

        return when {
            mPin.isEmpty() -> {
                Toast.makeText(this, "Please create a new PIN!", Toast.LENGTH_SHORT).show()
                false
            }
            mPin.length != 4 -> {
                Toast.makeText(this, "PIN must be exactly 4 digits!", Toast.LENGTH_SHORT).show()
                false
            }

            else -> true
        }
    }

    private fun updatePin() {
        progressLayout!!.visibility = View.VISIBLE

        val jsonObject = JsonObject().apply {
            addProperty("env_type", "Prod")
            addProperty("app_key", appKey)
            addProperty("mobile", mPhone)
            addProperty("security_pin", mPin)
        }

        val call = controller.getInstance().api.updateMPIN(jsonObject)
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                progressLayout!!.visibility = View.GONE
                response.body()?.let {
                    val status = it["status"].asBoolean
                    val message = it["msg"].asString

                    Toast.makeText(this@ResendPinActivity, message, Toast.LENGTH_SHORT).show()
                    if (status) {
                        onBackPressed()
                    }
                } ?: run {
                    Toast.makeText(this@ResendPinActivity, "Invalid response from server.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.e("ResendPinActivity", "API call failed: $t")
             progressLayout!!.visibility = View.GONE
                Toast.makeText(
                    this@ResendPinActivity,
                    "Something went wrong. Please try again!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}
