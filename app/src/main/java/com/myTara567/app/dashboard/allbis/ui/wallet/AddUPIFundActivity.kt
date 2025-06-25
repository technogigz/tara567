package com.myTara567.app.dashboard.allbis.ui.wallet

import android.app.Dialog
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.gson.JsonObject
import com.myTara567.app.R
import com.myTara567.app.databinding.ActivityAddUpifundBinding
import com.myTara567.app.databinding.UpiOtpVerificationBinding
import com.myTara567.app.serverApi.controller
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddUPIFundActivity : AppCompatActivity() {
    lateinit var binding: ActivityAddUpifundBinding
    lateinit var binding1: UpiOtpVerificationBinding
    var appKey: String? = null
    lateinit var spUnique_token: SharedPreferences
    var unique_token: String? = null
    private var mPhoneNumber = ""
    private var paymentMethod = ""

    var mainObject: JsonObject = JsonObject()

    var otp: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddUpifundBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }

    private fun initView() {

        val sp = applicationContext.getSharedPreferences("app_key", MODE_MULTI_PROCESS)
        appKey = sp.getString("app_key", null)
        binding.backBidHistImage!!.setOnClickListener(View.OnClickListener { onBackPressed() })


        spUnique_token = applicationContext.getSharedPreferences("unique_token", MODE_MULTI_PROCESS)
        unique_token = spUnique_token.getString("unique_token", null)
        mainObject.addProperty("env_type", "Prod")
        mainObject.addProperty("app_key", appKey)
        mainObject.addProperty("unique_token", unique_token)
        // Set up click listeners for each payment option
        binding.PhonePay.setOnClickListener {
            paymentMethod = "3"
            updateSelection(binding.PhonePay)

        }
        binding.GooglePay.setOnClickListener {
            paymentMethod = "2"
            updateSelection(binding.GooglePay)
        }
        binding.Paytym.setOnClickListener {
            paymentMethod = "1"
            updateSelection(binding.Paytym)
        }

        binding.UPIVerifiy.setOnClickListener {
//           showCustomDialog()
            sendOTP()
        }
    }

    private fun updateSelection(selectedLayout: ConstraintLayout) {
        // Set all layouts to the unselected background
        binding.PhonePay.setBackgroundResource(R.drawable.unselected_bg)
        binding.GooglePay.setBackgroundResource(R.drawable.unselected_bg)
        binding.Paytym.setBackgroundResource(R.drawable.unselected_bg)

        // Set the selected layout to the selected background
        selectedLayout.setBackgroundResource(R.drawable.select_payment_bg)
    }


    private fun sendOTP(){
        mPhoneNumber = binding.loginPhone.text.toString().trim()
        val js = JsonObject().apply {
            addProperty("env_type", "Prod")
            addProperty("app_key", appKey)
            addProperty("mobile", mPhoneNumber)
        }


        controller.getInstance().api.resendOtp(js)
            .enqueue(object : Callback<JsonObject> {
                override fun onResponse(
                    call: Call<JsonObject>,
                    response: Response<JsonObject>
                ) {
                    response.body()?.let {
                        otp = it["otp"].asString

                    }
                    Log.d("asdasd",otp.toString())

                    showCustomDialog(otp)
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Toast.makeText(
                        applicationContext,
                        "Something went wrong. Try again later.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    private fun showCustomDialog(otp: String?) {
        // Create the dialog
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.upi_otp_verification)

        // Correctly find the TextView from the dialog's layout
        val mtxt_title = dialog.findViewById<TextView>(R.id.mtxt_title)
        mtxt_title?.text = "Enter OTP Verification"

        // Make the dialog fullscreen
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // Set up close button
        val closeIcon = dialog.findViewById<ImageView>(R.id.closeIcon)
        closeIcon?.setOnClickListener {
            dialog.dismiss()
        }

        val btn_ok = dialog.findViewById<TextView>(R.id.btn_ok)
        val otpEdit = dialog.findViewById<TextView>(R.id.otp)
        btn_ok?.setOnClickListener {



            val enteredOtp = otpEdit.text?.toString()?.trim()

            if (enteredOtp.isNullOrEmpty() || enteredOtp.length != 4) {
                Toast.makeText(this, "Please enter a valid 4-digit OTP", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (enteredOtp == otp) {
                mainObject.addProperty("upi_type", paymentMethod)
                if (paymentMethod == "1") {
                    mainObject.addProperty("paytm_no", mPhoneNumber)
                    mainObject.addProperty("google_pay_no", "")
                    mainObject.addProperty("phon_pay_no", mPhoneNumber)
                } else if (paymentMethod == "2") {
                    mainObject.addProperty("paytm_no", "")
                    mainObject.addProperty("google_pay_no", mPhoneNumber)
                    mainObject.addProperty("phon_pay_no", "")
                } else if (paymentMethod == "3") {
                    mainObject.addProperty("paytm_no", "")
                    mainObject.addProperty("google_pay_no", "")
                    mainObject.addProperty("phon_pay_no", mPhoneNumber)
                }

                Log.d("asdasd",mainObject.toString())

                val call = controller.getInstance().api.addUserUpiDetails(mainObject)
                call.enqueue(object : Callback<JsonObject> {
                    override fun onResponse(
                        call: Call<JsonObject>,
                        response: Response<JsonObject>
                    ) {
                        Toast.makeText(
                            applicationContext,
                            response.body()!!["msg"].asString,
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    }

                    override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                        Log.d("asdasd",t.toString())
                        Toast.makeText(
                            applicationContext,
                            "Something went wrong.... Try again later",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
            } else {
                Toast.makeText(this, "Invalid OTP. Please try again.", Toast.LENGTH_SHORT).show()
            }
        }

        dialog.show()
    }

}

//9625128949