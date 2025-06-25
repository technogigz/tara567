package com.myTara567.app

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.myTara567.app.databinding.RegisterBinding
import com.myTara567.app.new_ui_update.EnterMPINActivity

class Register : AppCompatActivity() {

    lateinit var binding: RegisterBinding

    private var appKey: String? = null
    private var phone: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = RegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get phone from intent safely
        phone = intent.getStringExtra("phone") ?: ""

        // Get appKey from shared preferences
        val sp = getSharedPreferences("app_key", MODE_PRIVATE)
        appKey = sp.getString("app_key", null)

        if (appKey.isNullOrEmpty()) {
            Toast.makeText(this, "App key is missing!", Toast.LENGTH_SHORT).show()
            return
        }

        binding.signUpBtn.setOnClickListener {
            val fullName = binding.mFullName.text.toString().trim()
            val userName = binding.mUserName.text.toString().trim()

            when {
                fullName.isEmpty() -> {
                    Toast.makeText(this, "Please enter your full name", Toast.LENGTH_SHORT).show()
                }
                userName.isEmpty() -> {
                    Toast.makeText(this, "Please enter your username", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    // ✅ Store account_block_status as "0" (blocked initially)
                    val sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                    sharedPref.edit().putString("account_block_status", "0").apply()

                    navigateToOtpScreen(fullName, phone, appKey!!, userName)
                }
            }
        }
    }

    private fun navigateToOtpScreen(name: String, mobile: String, appKey: String, username: String) {
        val intent = Intent(this, EnterMPINActivity::class.java).apply {
            putExtra("name", name)
            putExtra("user_name", username)
            putExtra("phone", mobile)
            putExtra("appKey", appKey)
            putExtra("checkActivity", "RegisterActivity")
        }
        startActivity(intent)
    }
}
