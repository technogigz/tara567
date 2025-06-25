package com.myTara567.app.dashboard.allbis.ui.wallet

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.JsonObject
import com.myTara567.app.R
import com.myTara567.app.databinding.ActivityAddBankDetailsBinding
import com.myTara567.app.serverApi.controller
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddBankDetailsActivity : AppCompatActivity() {
    lateinit var binding:ActivityAddBankDetailsBinding

    private var mPhoneNumber = ""
    private var appKey: String? = null
    private lateinit var progressLayout: RelativeLayout
    lateinit var spUnique_token: SharedPreferences
    var unique_token: String? = null
    var ifscCode:String? = null
    var accountNumber:String? = null
    var bankName:String? = null
    var holderName:String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityAddBankDetailsBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
       initView()
    }


    fun initView(){
        val sp = getSharedPreferences("app_key", MODE_PRIVATE)
        appKey = sp.getString("app_key", "").orEmpty()
        Log.d("Login", "AppKey: $appKey")
        spUnique_token = applicationContext.getSharedPreferences("unique_token", MODE_MULTI_PROCESS)
        unique_token = spUnique_token.getString("unique_token", null)
        // Progress layout reference
        progressLayout = findViewById(R.id.progressLayout)


        binding.backBidHistImage.setOnClickListener {
            onBackPressed()
        }

        binding.mloginButton.setOnClickListener {
            validateBankDetails()
        }
    }

    private fun validateBankDetails() {
         ifscCode = binding.tvIFSCCode.text.toString().trim()
         accountNumber = binding.tvAccountNumber.text.toString().trim()
         bankName = binding.tvBankName.text.toString().trim()
         holderName = binding.tvHolderName.text.toString().trim()

        if (ifscCode!!.isEmpty()) {
            binding.tvIFSCCode.error = "Please enter the IFSC code"
            return
        }

        if (accountNumber!!.isEmpty()) {
            binding.tvAccountNumber.error = "Please enter the account number"
            return
        }

        if (!accountNumber!!.matches("^[0-9]{9,18}$".toRegex())) {
            binding.tvAccountNumber.error = "Please enter a valid account number"
            return
        }

        if (bankName!!.isEmpty()) {
            binding.tvBankName.error = "Please enter the bank name"
            return
        }

        if (holderName!!.isEmpty()) {
            binding.tvHolderName.error = "Please enter the account holder name"
            return
        }

        // Process the bank details
        saveBankDetails(ifscCode!!, accountNumber!!, bankName!!, holderName!!)
    }

    private fun saveBankDetails(ifscCode: String, accountNumber: String, bankName: String, holderName: String) {
        val jsonObject = JsonObject().apply {
            addProperty("env_type", "Prod")
            addProperty("app_key", appKey)
            addProperty("unique_token", unique_token)
            addProperty("user_id", "")
            addProperty("bank_name", bankName)
            addProperty("branch_address", "")
            addProperty("ac_holder_name", holderName)
            addProperty("ac_number", accountNumber)
            addProperty("ifsc_code", ifscCode)
            addProperty("insert_date", "")
        }
        controller.getInstance().api.addBankDetails(jsonObject)
            .enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    progressLayout.visibility = View.GONE
                    val rawResponse = response.errorBody()?.string() ?: "Empty Body"
                    Log.d("Login", "Raw Response: $rawResponse")
                    if (response.isSuccessful) {
                        response.body()?.let { responseBody ->
                            val registered = responseBody["status"].asBoolean
                            Toast.makeText(this@AddBankDetailsActivity, response.message(), Toast.LENGTH_SHORT).show()
                            onBackPressed()
                        }
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    progressLayout.visibility = View.GONE
                    Log.e("Login", "API Failure: ${t.message}")
                    Toast.makeText(this@AddBankDetailsActivity, "Something went wrong. Please try again.", Toast.LENGTH_SHORT).show()
                }
            })

    }



}