package com.myTara567.app.new_ui_update

import BiometricHelper
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricPrompt
import com.google.gson.JsonObject
import com.myTara567.app.OtpVerification
import com.myTara567.app.R
import com.myTara567.app.dashboard.ResendPinActivity
import com.myTara567.app.databinding.ActivityEnterMpinactivityBinding
import com.myTara567.app.home.HomePageActivity
import com.myTara567.app.serverApi.controller
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EnterMPINActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEnterMpinactivityBinding
    private var progressLayout: RelativeLayout? = null
    private var appKey: String? = null
    private var phone: String? = null
    private var name: String? = null
    private var userName: String? = null
    private lateinit var biometricHelper: BiometricHelper
    private var userMpin: String? = null
    private var userOtp: String? = null
    private var checkActivity: String? = null
    private var wrongAttemptsRemaining = 3
    private lateinit var sp: SharedPreferences
    private lateinit var spMpin: SharedPreferences
    lateinit var spUniqueToken: SharedPreferences
    private var uniqueToken: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEnterMpinactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initExtras()
        setupUiBasedOnActivity()

        initView()
    }

    private fun initExtras() {
        phone = intent.getStringExtra("phone")
        name = intent.getStringExtra("name")
        userName = intent.getStringExtra("user_name")
        uniqueToken = intent.getStringExtra("unique_token")
        checkActivity = intent.getStringExtra("checkActivity")

        progressLayout = findViewById(R.id.progressLayout)
        sp = getSharedPreferences("app_key", MODE_PRIVATE)
        appKey = sp.getString("app_key", null)

        spUniqueToken = applicationContext.getSharedPreferences("unique_token", MODE_MULTI_PROCESS)
        uniqueToken = spUniqueToken.getString("unique_token", null)

        biometricHelper = BiometricHelper(this)

        spMpin = applicationContext.getSharedPreferences("smPIN", MODE_PRIVATE)


        binding.fingerprint.setOnClickListener{
            if (biometricHelper.isBiometricAvailable()) {
                biometricHelper.authenticate(
                    object : BiometricPrompt.AuthenticationCallback() {
                        override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                            super.onAuthenticationSucceeded(result)
                            userMpin = spMpin.getString("smPIN", null)
                            if (!userMpin.isNullOrEmpty()) {
                                checkMPin()
                            } else {
                                Toast.makeText(applicationContext, "MPIN Required", Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onAuthenticationFailed() {
                            super.onAuthenticationFailed()

                            Toast.makeText(applicationContext, "Authentication Failed", Toast.LENGTH_SHORT).show()
                        }

                        override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                            super.onAuthenticationError(errorCode, errString)

                            Toast.makeText(applicationContext, "Authentication Error: $errString", Toast.LENGTH_SHORT).show()
                        }
                    }
                )
            }
        }
    }

    fun initView() {
        binding.mForgotMPIN.setOnClickListener {
            startActivity(Intent(this, ResendPinActivity::class.java).apply {
                putExtra("number",phone)
            })

        }
    }

    private fun setupUiBasedOnActivity() {
        when (checkActivity) {
            "RegisterActivity" -> setupForRegistration()
            "mainActivity", "homepageactivity"-> setupForLogin()
            "LoginActivity" ->setupUserAllredyregister()
            else -> showToast("Invalid activity source")
        }
    }

    private fun setupUserAllredyregister(){
        binding.apply {
            mConfrimMpinTitle.visibility = View.GONE
            mConfirmMPINLayout.visibility = View.GONE
            mEnterMpinTitle.visibility = View.INVISIBLE
            mForgotMPIN.visibility = View.VISIBLE
            mLinearLayout.visibility = View.GONE
            loginButton.text = "MPIN LOGIN"
            EnterNumberTitle.text = "MPIN"
            EnterNumberTitle.text = "MPIN LOGIN"
            mTxtTitle.text = "Login with your MPIN"
        }
        setupSubmitButtonListenerForLogin()
    }

    private fun setupForRegistration() {
        binding.apply {
            mConfrimMpinTitle.visibility = View.VISIBLE
            mConfirmMPINLayout.visibility = View.VISIBLE
            mEnterMpinTitle.visibility = View.VISIBLE
            mForgotMPIN.visibility = View.GONE
            mLinearLayout.visibility = View.VISIBLE
            EnterNumberTitle.text = "MPIN"
            mForgotMPIN.text = "MPIN"
            loginButton.text = "GENERATE MPIN"
            mTxtTitle.text = "Create MPIN for login"
        }

        setupSubmitButtonListener()
    }

    private fun setupForLogin() {
        binding.apply {
            mConfrimMpinTitle.visibility = View.GONE
            mConfirmMPINLayout.visibility = View.GONE
            mEnterMpinTitle.visibility = View.INVISIBLE
            mForgotMPIN.visibility = View.VISIBLE
            mLinearLayout.visibility = View.GONE
            loginButton.text = "MPIN LOGIN"
            EnterNumberTitle.text = "MPIN"
            EnterNumberTitle.text = "MPIN LOGIN"
            mTxtTitle.text = "Login with your MPIN"
        }

        setupSubmitButtonListenerForLogin()
    }

    private fun setupSubmitButtonListener() {
        binding.loginButton.setOnClickListener {
            userMpin = binding.mEnterMPIN.text.toString().trim()
            val confirmMpin = binding.mEnterConfirmMPIN.text.toString().trim()

            when {
                userMpin.isNullOrEmpty() || userMpin!!.length != 4 -> showToast("Please enter a valid 4-digit MPIN")
                confirmMpin.isEmpty() || confirmMpin.length != 4 -> showToast("Please enter a valid 4-digit Confirm MPIN")
                userMpin != confirmMpin -> showToast("MPIN and Confirm MPIN do not match")
                else -> validateUser(phone ?: "")
            }
        }
    }

    private fun setupSubmitButtonListenerForLogin() {
        binding.loginButton.setOnClickListener {
            userMpin = binding.mEnterMPIN.text.toString().trim()

            if (userMpin.isNullOrEmpty() || userMpin!!.length != 4) {
                showToast("Please enter a valid 4-digit MPIN")
            } else {
                checkMPin()
            }
        }
    }

    private fun validateUser(phone: String) {
        if (appKey.isNullOrEmpty()) {
            showToast("AppKey is missing")
            return
        }
        sendOtp(phone, appKey!!)
    }

    private fun sendOtp(mobile: String, appKey: String) {
        toggleProgress(true)

        val requestJson = JsonObject().apply {
            addProperty("env_type", "Prod")
            addProperty("app_key", appKey)
            addProperty("mobile", mobile)
        }

        controller.getInstance().api.sendotp(requestJson).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                toggleProgress(false)
                response.body()?.let {
                    userOtp = it.get("otp")?.asString ?: "No OTP found"
                    Log.d("fail message", "onResponse: ${response.body()}")
                    navigateToOtpScreen()
                } ?: showToast("Response is empty")
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                toggleProgress(false)
                showToast("Something went wrong... Try again later.")
                Log.e("TAG", "sendOtp: Failure", t)
            }
        })
    }

    private fun checkMPin() {

        val requestJson = JsonObject().apply {
            addProperty("env_type", "Prod")
            addProperty("app_key", appKey)
            addProperty("unique_token", uniqueToken)
            addProperty("security_pin", userMpin)
        }



        controller.getInstance().api.checkMPin(requestJson).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                val status = response.body()?.get("status")?.asBoolean ?: false
                val message = response.body()?.get("msg")?.asString ?: "Unknown error"

                if (status) {
                    val editor = spMpin.edit()
                    editor.putString("smPIN", userMpin)
                    editor.apply()
                    startActivity(Intent(applicationContext, HomePageActivity::class.java))
                    finish()
                } else {
                    handleIncorrectPin(message)
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                showToast("Something went wrong... Try again later.")
                Log.e("TAG", "checkMPin: Failure", t)
            }
        })
    }

    private fun handleIncorrectPin(message: String) {
        if (wrongAttemptsRemaining <= 1) {
            finish()
        } else {
            wrongAttemptsRemaining--
            showToast("$message. ${wrongAttemptsRemaining} attempts left.")
        }
    }

    private fun toggleProgress(show: Boolean) {
        progressLayout?.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun navigateToOtpScreen() {
        val intent = Intent(this, OtpVerification::class.java).apply {
            putExtra("name", name)
            putExtra("phone", phone)
            putExtra("user_name", userName)
            putExtra("user_pin", userMpin)
            putExtra("user_otp", userOtp)
            putExtra("appKey", appKey)
        }
        startActivity(intent)
    }
}
