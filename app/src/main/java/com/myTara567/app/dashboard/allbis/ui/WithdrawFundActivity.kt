package com.myTara567.app.dashboard.allbis.ui

import android.R
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.view.Window
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.myTara567.app.Wallet.wallet_withdrawal_hist_model
import com.myTara567.app.databinding.ActivityWithdrawFundBinding
import com.myTara567.app.serverApi.controller
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WithdrawFundActivity : AppCompatActivity() {
    lateinit var binding:ActivityWithdrawFundBinding
    val spinnerList = ArrayList<String>()
    private lateinit var dataAdapter: ArrayAdapter<String>
    private var item: Array<String> = arrayOf()

    var appKey: String? = null
    var unique: String? = null
    var mainObject: JsonObject? = null
    var withdraw_open_time: String? = null
    var withdraw_close_time: String? = null
    var min_amt: String = "0"
    var max_amt: String? = "0"
    var paymentMethod: String? = "0"
    var index: Int = 0
    var checkmPin: Boolean = true
    var last_request_status: String? = null
    var modelArrayList: ArrayList<wallet_withdrawal_hist_model> = ArrayList()
    var withdraw: Boolean = true
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityWithdrawFundBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        mInitView()
    }


    fun mInitView(){
        appKey = getSharedPreferences("app_key", MODE_MULTI_PROCESS).getString("app_key", "")
        Log.d("login appkey", "onCreate: $appKey")
        unique = applicationContext.getSharedPreferences("unique_token", MODE_MULTI_PROCESS)
            .getString("unique_token", "")
        Log.d("signUp editor", "home: $unique")

        mainObject = JsonObject()
        mainObject!!.addProperty("env_type", "Prod")
        mainObject!!.addProperty("app_key", appKey)
        mainObject!!.addProperty("unique_token", unique)

        //show withdraw transaction
         getPaymentMethod("", "")



        binding.withdrawalBtn.setOnClickListener {
            val withdrawStatus = intent.getStringExtra("withdraw_status")
            if (withdrawStatus == "0") {
                Toast.makeText(applicationContext, "You can withdraw between $withdraw_open_time - $withdraw_close_time", Toast.LENGTH_SHORT).show()
            } else {
                val amount = binding.points.text.toString()

                if (amount.isEmpty()) {
                    Toast.makeText(applicationContext, "Please enter some amount", Toast.LENGTH_SHORT).show()
                } else {
                    val amountInt = amount.toIntOrNull()
                    val minAmtInt = min_amt.toIntOrNull()
                    val maxAmtInt = max_amt?.toIntOrNull()

                    if (amountInt == null) {
                        Toast.makeText(applicationContext, "Please enter a valid amount", Toast.LENGTH_SHORT).show()
                    } else if (amount.contains(".") || amount.contains(",") || amount.contains("-")) {
                        Toast.makeText(applicationContext, "Please enter a valid amount", Toast.LENGTH_SHORT).show()
                    } else if (minAmtInt != null && amountInt < minAmtInt) {
                        Toast.makeText(applicationContext, "Minimum Amount required: $min_amt", Toast.LENGTH_SHORT).show()
                    } else if (maxAmtInt != null && amountInt > maxAmtInt) {
                        Toast.makeText(applicationContext, "Maximum Amount required: $max_amt", Toast.LENGTH_SHORT).show()
                    } else {
                        val call = controller.getInstance().getApi().lastFundRequestDetail(mainObject)
                        call.enqueue(object : Callback<JsonObject> {
                            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {

                                if (withdraw) {
                                    if (last_request_status == "0") {
                                        Toast.makeText(applicationContext, "You already have a pending request.", Toast.LENGTH_SHORT).show()
                                    } else {
                                        when (index) {
                                            0, 1, 2 -> {
                                                getPaymentMethod(index.toString(), amount)
                                                withdraw = false
                                            }
                                        }
                                    }
                                }
                            }

                            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                                Toast.makeText(applicationContext, "Something went wrong.... Try again later", Toast.LENGTH_SHORT).show()
                            }
                        })
                    }
                }
            }
        }


//        binding.withdrawalBtn.setOnClickListener(View.OnClickListener {
//            if (intent.getStringExtra("withdraw_status") == "0") {
//                Toast.makeText(applicationContext, "You can withdraw between $withdraw_open_time-$withdraw_close_time", Toast.LENGTH_SHORT).show()
//            } else {
//                val amount: String = binding.points.text.toString()
//                val call = controller.getInstance().api.lastFundRequestDetail(mainObject)
//                call.enqueue(object : Callback<JsonObject?> {
//                    override fun onResponse(
//                        call: Call<JsonObject?>,
//                        response: Response<JsonObject?>
//                    ) {
//                        if (amount == "") {
//                            Toast.makeText(
//                                applicationContext, "Please enter some amount", Toast.LENGTH_SHORT).show()
//                        } else if (binding.points.getText().toString().trim { it <= ' ' }
//                                .contains(".") || binding.points.text.toString()
//                                .contains(",") ||
//                            binding.points.getText().toString().trim { it <= ' ' }.contains("-")) {
//                            Toast.makeText(
//                                applicationContext,
//                                "please enter valid amount",
//                                Toast.LENGTH_SHORT
//                            ).show()
//                        } else if (amount.toInt() < min_amt.toInt()) {
//                            Toast.makeText(
//                                applicationContext,
//                                "Minimum Amount required: $min_amt", Toast.LENGTH_SHORT
//                            ).show()
//                        } else if (amount.toInt() > max_amt!!.toInt()) {
//                            Toast.makeText(
//                                applicationContext,
//                                "Maximum Amount required: $max_amt", Toast.LENGTH_SHORT
//                            ).show()
//                        } else {
//                            if (withdraw) {
//                                if (last_request_status == "0") {
//                                    Toast.makeText(
//                                        applicationContext,
//                                        "You already have a pending request.",
//                                        Toast.LENGTH_SHORT
//                                    ).show()
//                                } else {
//                                    if (index == 0) {
//                                       getPaymentMethod(index.toString(), amount)
//                                        withdraw = false
//                                    } else if (index == 1) {
//                                         getPaymentMethod(index.toString(), amount)
//                                        withdraw = false
//                                    } else if (index == 2) {
//                                         getPaymentMethod(index.toString(), amount)
//                                        withdraw = false
//                                    }
//
//                                    return
//                                }
//                            }
//                        }
//                    }
//
//                    override fun onFailure(call: Call<JsonObject?>, t: Throwable) {
//                        Toast.makeText(
//                            applicationContext,
//                            "Something went wrong.... Try again later",
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    }
//                })
//            }
//        })
       binding.backBidHistImage.setOnClickListener {
           onBackPressed()
       }

        getWalletBalance()

    }


    private fun getPaymentMethod(s: String, amount: String) {
        val call = controller.getInstance().getApi().userPaymentMethodList(mainObject)
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                val status = response.body()?.get("status")?.asBoolean ?: false
                val jsonArray = response.body()?.get("result")?.asJsonArray ?: JsonArray()
                spinnerList.clear()

                if (status) {
                    if (jsonArray.size() == 0) {
                        Toast.makeText(applicationContext, response.body()?.get("msg")?.asString, Toast.LENGTH_SHORT).show()
                    } else {
                        for (i in 0 until jsonArray.size()) {
                            val paymentObj = jsonArray[i].asJsonObject
                            paymentMethod = paymentObj.get("type").asString
                            val value = paymentObj.get("value").asString
                            val name = paymentObj.get("name").asString

                            spinnerList.add("$name ($value)")
                        }

                        when (s) {
                            "0", "2", "1" -> {
                                val jsonObject = jsonArray[s.toInt()].asJsonObject
                                showCustomDialog(jsonObject.get("type").asString, amount)

                            }
                        }
                    }
                } else {
                    spinnerList.add(response.body()?.get("msg")?.asString ?: "Unknown Error")
                }

                item = spinnerList.toTypedArray()
                Log.d("item", "onCreate: ${item.size}")

                if (item.isEmpty()) {
                    item = arrayOf(/* no-op */)
                    dataAdapter = ArrayAdapter(this@WithdrawFundActivity, R.layout.simple_spinner_item, item)
                } else {
                    dataAdapter = ArrayAdapter(this@WithdrawFundActivity, R.layout.simple_spinner_item, item)
                }

                // Drop down layout style - list view with radio button
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                // Attaching data adapter to spinner
                binding.spinner.adapter = dataAdapter
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Toast.makeText(applicationContext, "Something went wrong.... Try again later", Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun showCustomDialog(paymentMethod: String, amount: String) {
        // Create the dialog
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(com.myTara567.app.R.layout.upi_otp_verification)

        // Correctly find the TextView from the dialog's layout
        val mtxt_title = dialog.findViewById<TextView>(com.myTara567.app.R.id.mtxt_title)
        mtxt_title?.text = "Enter MPIN"

        // Make the dialog fullscreen
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

    // Set up close button
        val closeIcon = dialog.findViewById<ImageView>(com.myTara567.app.R.id.closeIcon)
        closeIcon?.setOnClickListener {
            dialog.dismiss()
        }


        val btn_ok = dialog.findViewById<TextView>(com.myTara567.app.R.id.btn_ok)
        val mPINEdit = dialog.findViewById<TextView>(com.myTara567.app.R.id.otp)
        btn_ok?.setOnClickListener {
            checkmPIn(paymentMethod, amount, mPINEdit.text.toString())
        }

        dialog.show()
    }


//    private fun checkSecurityPin(paymentMethod: String, amount: String) {
//        val intent = Intent(this@WithdrawFundActivity, Security::class.java).apply {
//            putExtra("checkActivity", "wallet_withdrawal")
//            putExtra("payment_method", paymentMethod)
//            putExtra("request_amount", amount)
//        }
//        startActivity(intent)
//         binding.points.text?.clear()
//    }


    private fun checkmPIn(paymentMethod: String, amount: String, mPINEdit: String) {
        mainObject!!.addProperty("security_pin", mPINEdit)

        Log.d("xxasxas",mainObject.toString())
        val call = controller.getInstance().getApi().checkMPin(mainObject)
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                val responseBody = response.body()
                val status = responseBody?.get("status")?.asBoolean ?: false
                val message = responseBody?.get("msg")?.asString.orEmpty()
                Log.d("mPin", "onResponse: $responseBody")

                if (status) {
                    when ("wallet_withdrawal") {

                        "wallet_withdrawal" -> {
                            mainObject!!.addProperty("payment_method", paymentMethod)
                            mainObject!!.addProperty("request_amount", amount)
                            Log.d("message", "onResponse: $mainObject")
                            if (checkmPin) {
                                val walletCall = controller.getInstance().getApi().walletWithdrawalRequest(mainObject)
                                walletCall.enqueue(object : Callback<JsonObject> {
                                    override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                                        val walletResponse = response.body()

                                        Log.d("roushan", "$walletResponse  ")
                                        Log.d("message", "$walletResponse  ")

                                        if (walletResponse?.get("status")?.asBoolean == true) {
                                            Log.d("TAG", "onResponse: $walletResponse")
                                            Toast.makeText(applicationContext, walletResponse.get("msg")?.asString.orEmpty(), Toast.LENGTH_SHORT).show()
                                            checkmPin = false
                                            finish()
                                        } else {
                                            Toast.makeText(applicationContext, walletResponse?.get("msg")?.asString.orEmpty(), Toast.LENGTH_SHORT).show()
                                            finish()
                                        }
                                    }

                                    override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                                        Log.d("fail response", "onFailure: $t")
                                        Toast.makeText(applicationContext, "Something went wrong.... Try again later", Toast.LENGTH_SHORT).show()
                                    }
                                })
                                return
                            }
                        }
                    }
                } else {
//                    if (wrongSecurity < 1) {
//                        vibe.vibrate(100)
//                        when (checkActivity) {
//                            "mainActivity", "Login" -> userLogout()
//                            else -> finish()
//                        }
//                    } else {
//                        wrongSecurity--
//                        Log.d("else message", "onResponse: $message")
//                        vibe.vibrate(100)
//                        Toast.makeText(applicationContext, "$message $attempt attempt left", Toast.LENGTH_SHORT).show()
//                        attempt--
//                    }
                }
//                login = false
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.d("fail message", "onResponse: $t")
            }
        })
    }


    private fun getWalletBalance() {
        val call1 = controller.getInstance().api.getWalletBalance(mainObject)
        call1.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
               val walletBalance = response.body()!!["wallet_amt"].asInt
                min_amt = response.body()!!["min_withdrawal"].asInt.toString()
                max_amt = response.body()!!["max_withdrawal"].asInt.toString()
                binding. mWalletBalance.text = "₹" +walletBalance.toString()
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


    override fun onResume() {
        super.onResume()
        getWalletBalance()
      //  getWalletWithdrawHistory()
        withdraw = true
    }
}