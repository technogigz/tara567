package com.myTara567.app

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.JsonObject
import com.myTara567.app.new_ui_update.EnterMPINActivity
import com.myTara567.app.serverApi.controller
import com.myTara567.app.databinding.LoginBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Login : AppCompatActivity() {

    private lateinit var binding: LoginBinding
    private var mPhoneNumber = ""
    var spUnique_token: SharedPreferences? = null

    private var appKey: String? = null
    private lateinit var progressLayout: RelativeLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize SharedPreferences and retrieve appKey
        val sp = getSharedPreferences("app_key", MODE_PRIVATE)
        appKey = sp.getString("app_key", "").orEmpty()
        Log.d("Login", "AppKey: $appKey")

        // Progress layout reference
        progressLayout = findViewById(R.id.progressLayout)

        spUnique_token = getSharedPreferences("unique_token", MODE_MULTI_PROCESS)

        // Set click listener for login button
        binding.mloginButton.setOnClickListener {
            validateUser()
        }

        // Add TextWatcher to remove unnecessary formatting
        binding.loginPhone.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                binding.loginPhone.error = null // Clear error on text change
            }
        })
    }

    private fun validateUser() {
        mPhoneNumber = binding.loginPhone.text.toString().trim()

        if (mPhoneNumber.isEmpty()) {
            binding.loginPhone.error = "Please enter your phone number"
            return
        }

        // Input check: Disallow invalid characters
        if (mPhoneNumber.contains(".") || mPhoneNumber.contains(",") || mPhoneNumber.contains("-")) {
            binding.loginPhone.error = "Please enter a valid phone number"
            return
        }

        if (appKey.isNullOrEmpty()) {
            Toast.makeText(this, "App key is missing", Toast.LENGTH_SHORT).show()
        } else {
            mobileCheck(mPhoneNumber, appKey!!)
        }
    }
    private fun mobileCheck(mobile: String, appKey: String) {
        progressLayout.visibility = View.VISIBLE

        val jsonObject = JsonObject().apply {
            addProperty("env_type", "Prod")
            addProperty("app_key", appKey)
            addProperty("mobile", mobile)
        }

        controller.getInstance().api.mobileCheck(jsonObject)
            .enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    progressLayout.visibility = View.GONE

                    // Log the raw response for debugging
                    val rawResponse = response.errorBody()?.string() ?: "Empty Body"
                    Log.d("Login", "Raw Response: $rawResponse")

                    if (response.isSuccessful) {
                        response.body()?.let { responseBody ->
                            val registered = responseBody["status"].asBoolean

                            Log.d("sadasd",responseBody.toString())

                            if (registered) {
                                navigateToOtpScreen(mobile, appKey)
                            } else {
                                val uniqueToken = responseBody["unique_token"].asString
                                if (!uniqueToken.isNullOrEmpty()) {
                                    spUnique_token?.edit()?.apply {
                                        putString("unique_token", uniqueToken)
                                        apply()
                                    }
                                    navigateToEnterMPIN(mobile)
                                }
                            }
                        }
                    } else {
                        Log.e("Login", "Server Response Error: ${response.errorBody()?.string()}")
                        Toast.makeText(this@Login, "Server error. Please try again.", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    progressLayout.visibility = View.GONE
                    Log.e("Login", "API Failure: ${t.message}")
                    Toast.makeText(
                        this@Login,
                        "Something went wrong. Please try again.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    /**
     * Navigate to OTP verification screen.
     */
    private fun navigateToOtpScreen(mobile: String, appKey: String) {
        Intent(this, Register::class.java).apply {
            putExtra("appKey", appKey)
            putExtra("phone", mobile)
            startActivity(this)
        }
    }

    /**
     * Navigate to MPIN entry screen.
     */
    private fun navigateToEnterMPIN(mobile: String) {
        Intent(this, EnterMPINActivity::class.java).apply {
            putExtra("phone", mobile)
            putExtra("checkActivity", "LoginActivity")
            startActivity(this)
        }
    }
}
