package com.myTara567.app

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.CountDownTimer
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.JsonObject
import com.myTara567.app.databinding.OtpBinding
import com.myTara567.app.home.HomePageActivity
import com.myTara567.app.serverApi.controller
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OtpVerification : AppCompatActivity() {
    lateinit var binding: OtpBinding

    var name: String? = null
    var user_name: String? = null
    var user_mpin: String? = null
    var phone: String? = null
    var password: String? = null
    var email: String? = null
    var appKey: String? = null
    var otp: String? = null
    var spUnique_token: SharedPreferences? = null
    var user_otp: String? = null
    var vibe: android.os.Vibrator? = null
    private var progressLayout: RelativeLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = OtpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sp = applicationContext.getSharedPreferences("app_key", MODE_MULTI_PROCESS)
        appKey = sp.getString("app_key", null)

        spUnique_token = getSharedPreferences("unique_token", MODE_MULTI_PROCESS)

        binding.mbackBtn.setOnClickListener {
            onBackPressed()
        }


        name = intent.getStringExtra("name")
        user_mpin = intent.getStringExtra("user_pin")
        phone = intent.getStringExtra("phone")
        password = intent.getStringExtra("password")
        appKey = intent.getStringExtra("appKey")
        otp = intent.getStringExtra("otp")
        user_name = intent.getStringExtra("user_name")
        progressLayout = findViewById(R.id.progressLayout)

        binding.otpTimer.visibility = View.VISIBLE
        binding.resendOtp.visibility = View.INVISIBLE

        // Start OTP Timer
        startTimer()

        binding.verifyOtp.setOnClickListener {

            val enteredOtp = binding.otp.text?.toString()?.trim()
            if (enteredOtp.isNullOrEmpty() || enteredOtp.length < 4) {
                Toast.makeText(this, "Please enter a valid 4-digit OTP", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            user_otp = enteredOtp
            progressLayout?.visibility = View.VISIBLE
            val js = JsonObject().apply {
                addProperty("env_type", "Prod")
                addProperty("app_key", appKey)
                addProperty("name", name)
                addProperty("mobile", phone)
                addProperty("security_pin", user_mpin)
                addProperty("user_name", user_name)
                addProperty("otp", user_otp)
                addProperty("device_id", Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID))
            }

            controller.getInstance().api.userRegistration(js)
                .enqueue(object : Callback<JsonObject> {
                    override fun onResponse(
                        call: Call<JsonObject>,
                        response: Response<JsonObject>
                    ) {
                        progressLayout?.visibility = View.GONE
                        response.body()?.let { body ->
                            val message = body["msg"].asString
                            Log.d("message", "onResponse: ${response.body()}")
                            if (body["status"].asBoolean) {
                                Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
                                startActivity(Intent(this@OtpVerification, HomePageActivity::class.java))
                                val uniqueToken = body["unique_token"].asString
                                spUnique_token?.edit()?.apply {
                                    putString("unique_token", uniqueToken)
                                    apply()
                                }
                                Log.d("unique_token", "Saved unique token: $uniqueToken")
                            } else {
                                Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                    }

                    override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                        progressLayout?.visibility = View.GONE
                        Log.e("registration", "onFailure: ${t.message}")
                        Toast.makeText(
                            applicationContext,
                            "Something went wrong. Try again later.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
        }

        binding.resendOtp.setOnClickListener {
            binding.resendOtp.visibility = View.INVISIBLE
            binding.otpTimer.visibility = View.VISIBLE
            progressLayout?.visibility = View.VISIBLE

            val js = JsonObject().apply {
                addProperty("env_type", "Prod")
                addProperty("app_key", appKey)
                addProperty("mobile", phone)
            }

            controller.getInstance().api.resendOtp(js)
                .enqueue(object : Callback<JsonObject> {
                    override fun onResponse(
                        call: Call<JsonObject>,
                        response: Response<JsonObject>
                    ) {
                        progressLayout?.visibility = View.GONE
                        response.body()?.let {
                            otp = it["otp"].asString

                        }
                        startTimer()
                    }

                    override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                        progressLayout?.visibility = View.GONE
                        Toast.makeText(
                            applicationContext,
                            "Something went wrong. Try again later.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
        }
    }

    private fun startTimer() {
        val timerDuration = 30000L // 30 seconds in milliseconds
        val countDownInterval = 1000L // 1 second in milliseconds

        object : CountDownTimer(timerDuration, countDownInterval) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsRemaining = millisUntilFinished / 1000
                binding.otpTimer.text = "$secondsRemaining seconds remaining..."
            }

            override fun onFinish() {
                binding.resendOtp.visibility = View.VISIBLE
                binding.otpTimer.visibility = View.INVISIBLE
                binding.otp.setText("")
            }
        }.start()
    }
}
