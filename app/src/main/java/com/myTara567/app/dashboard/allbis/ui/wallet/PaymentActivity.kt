package com.myTara567.app.dashboard.allbis.ui.wallet

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonObject
import com.myTara567.app.R
import com.myTara567.app.Wallet.wallet_withdrawal_hist_model
import com.myTara567.app.databinding.ActivityPaymentBinding
import com.myTara567.app.serverApi.controller
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.UUID

class PaymentActivity : AppCompatActivity() {

    var number: EditText? = null
    var drawerLayout: ConstraintLayout? = null
    var VerifyBtn: TextView? = null
    var mainObject: JsonObject = JsonObject()
    var appKey: String? = null
    var unique: String? = null
    var min_amt: String = "0"
    var max_amt: String? = "0"
    var paymentMethod: String? = null
    var upi_payment_id: String? = null
    var google_upi_payment_id: String? = null
    var phonepay_upi_payment_id: String? = null
    var rdGooglePay: ConstraintLayout? = null
    var rdPhonePe: ConstraintLayout? = null
    var rdOthers: ConstraintLayout? = null
    var addFundHistRecycler: RecyclerView? = null
    var noResults: LinearLayout? = null
    var transLayout: LinearLayout? = null
    var method: Int = 0
    var UPI_REQUEST_CODE: Int = 123
    var url: Uri.Builder = Uri.Builder()
    var uuid: UUID = UUID.randomUUID()
    var modelArrayList: ArrayList<wallet_withdrawal_hist_model> = ArrayList()
    var addFund: Boolean = true
    private var progressLayout: RelativeLayout? = null

    lateinit var binding:ActivityPaymentBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityPaymentBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        number = findViewById<EditText>(R.id.Id)
        progressLayout = findViewById(R.id.progressLayout)
        progressLayout?.visibility = View.VISIBLE
        VerifyBtn = findViewById<Button>(R.id.VerifyBtn)
        mInitView()
    }

    fun mInitView(){
        appKey = getSharedPreferences("app_key", MODE_MULTI_PROCESS).getString("app_key", "")
        Log.d("login appkey", "onCreate: $appKey")

        unique = applicationContext.getSharedPreferences("unique_token", MODE_MULTI_PROCESS)
            .getString("unique_token", "")
        Log.d("signUp editor", "home: $unique")


        mainObject.addProperty("env_type", "Prod")
        mainObject.addProperty("app_key", appKey)
        mainObject.addProperty("unique_token", unique)
        progressLayout?.visibility = View.VISIBLE
        //get min and max amount
        val call = controller.getInstance().api.lastFundRequestDetail(mainObject)
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                progressLayout?.visibility = View.GONE
                min_amt = response.body()!!["min_amt"].asString
                max_amt = response.body()!!["max_amt"].asString
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                progressLayout?.visibility = View.GONE
                Toast.makeText(applicationContext, "Something went wrong.... Try again later", Toast.LENGTH_SHORT).show()
            }
        })


        //get admin bank details
        val adminObj = controller.getInstance().api.adminBankDetails(mainObject)
        adminObj.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.body()!!["status"].asBoolean) {
                    val bank_details = response.body()!!.getAsJsonArray("bank_details")
                    for (i in 0 until bank_details.size()) {
                        val jsonObject = bank_details[i].asJsonObject
                        upi_payment_id = jsonObject["upi_payment_id"].asString
                        google_upi_payment_id = jsonObject["google_upi_payment_id"].asString
                        phonepay_upi_payment_id = jsonObject["phonepay_upi_payment_id"].asString
                    }
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Toast.makeText(applicationContext, "Something went wrong.... Try again later", Toast.LENGTH_SHORT).show()
            }
        })


        //set transaction history
        setAddFundTransactionHist()

      /*  val swipeRefresh = findViewById<SwipeRefreshLayout>(R.id.swipeRefresh)
        swipeRefresh.setOnRefreshListener {
            setAddFundTransactionHist()
            setWalletBalance()
            swipeRefresh.isRefreshing = false
        }*/


        val backImg = findViewById<ImageView>(R.id.backBidHistImage)
        backImg.setOnClickListener { onBackPressed() }


        rdGooglePay = findViewById(R.id.rdGooglePay)
        rdPhonePe = findViewById(R.id.rdPhonePe)
        rdOthers = findViewById(R.id.rdOthers)
        val gpayImg = findViewById<ImageView>(R.id.imageView15)
        val phonePeImg = findViewById<ImageView>(R.id.imageView16)
        val othersImg = findViewById<ImageView>(R.id.imageView19)
        rdGooglePay!!.setOnClickListener(View.OnClickListener { v: View? ->
            method = 1
            gpayImg.setImageResource(R.drawable.add_fund_checked)
            phonePeImg.setImageResource(R.drawable.add_find_uncheckd)
            othersImg.setImageResource(R.drawable.add_find_uncheckd)
        })
        rdPhonePe!!.setOnClickListener(View.OnClickListener { v: View? ->
            method = 2
            gpayImg.setImageResource(R.drawable.add_find_uncheckd)
            phonePeImg.setImageResource(R.drawable.add_fund_checked)
            othersImg.setImageResource(R.drawable.add_find_uncheckd)
        })
        rdOthers!!.setOnClickListener(View.OnClickListener { v: View? ->
            method = 3
            gpayImg.setImageResource(R.drawable.add_find_uncheckd)
            phonePeImg.setImageResource(R.drawable.add_find_uncheckd)
            othersImg.setImageResource(R.drawable.add_fund_checked)
        })

        VerifyBtn!!.setOnClickListener(View.OnClickListener {
            if (number!!.getText().toString().trim { it <= ' ' } == "") {
                Toast.makeText(applicationContext, "Please Enter some amount", Toast.LENGTH_SHORT)
                    .show()
            } else if (number!!.getText().toString().trim { it <= ' ' }
                    .contains(".") || number!!.getText().toString().trim { it <= ' ' }
                    .contains(",") ||
                number!!.getText().toString().trim { it <= ' ' }.contains("-")) {
                Toast.makeText(applicationContext, "please enter valid amount", Toast.LENGTH_SHORT)
                    .show()
            } else if (min_amt.toInt() > number!!.getText().toString().trim { it <= ' ' }.toInt()) {
                Toast.makeText(
                    applicationContext,
                    "Minimum amount is: $min_amt",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (max_amt!!.toInt() < number!!.getText().toString().trim { it <= ' ' }.toInt()) {
                Toast.makeText(
                    applicationContext,
                    "Maximum amount is: $max_amt",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                if (addFund) {
                    Log.d("UUID", "onClick: $uuid")

                    //12b1bf50-9d94-447f-a37a-018722838ec8
                    Log.d("UUID", uuid.toString().replace("-", ""))

                    //12b1bf509d94447fa37a018722838ec8
                    url.scheme("upi")
                    url.authority("pay")
                    url.appendQueryParameter("pn", "Matka")
                    url.appendQueryParameter("tr", uuid.toString().replace("-", ""))
                    url.appendQueryParameter("tn", "testing")
                    url.appendQueryParameter("am", number!!.getText().toString().trim { it <= ' ' })
                    url.appendQueryParameter("cu", "INR")

                    //                    Uri uri =
                    //                            Uri.parse("upi://pay")
                    //                                    .buildUpon()
                    //                                    .appendQueryParameter("pn", "Matka")
                    //                                    .appendQueryParameter("tr", "123113123")
                    //                                    .appendQueryParameter("tn", "testing")
                    //                                    .appendQueryParameter("am", "1")
                    //                                    .appendQueryParameter("cu", "INR")
                    //                                    .build();
                    if (method == 1) {
                        paymentMethod = "1"

                        val GOOGLE_PAY_PACKAGE_NAME = "com.google.android.apps.nbu.paisa.user"

                        url.appendQueryParameter("pa", google_upi_payment_id)
                        url.build()

                        Log.d("Url", "onClick: $url")
                        val intent = Intent(Intent.ACTION_VIEW)
                        intent.setData(Uri.parse(url.toString()))
                        intent.setPackage(GOOGLE_PAY_PACKAGE_NAME)
                        try {
                            startActivityForResult(intent, UPI_REQUEST_CODE)
                        } catch (e: java.lang.Exception) {
                            Toast.makeText(
                                applicationContext,
                                "Google Pay not found.",
                                Toast.LENGTH_SHORT
                            ).show()
                            e.printStackTrace()
                        }
                    } else if (method == 2) {
                        paymentMethod = "2"

                        val PHONE_PE_PACKAGE_NAME = "com.phonepe.app"

                        /**Log.d("phone pe uri", "onClick: " + uri); */

                        /**Log.d("phone pe uri", "onClick: " + uri); */
                        url.appendQueryParameter("pa", phonepay_upi_payment_id)
                        url.build()

                        val intent = Intent(Intent.ACTION_VIEW)
                        intent.setData(Uri.parse(url.toString()))
                        intent.setPackage(PHONE_PE_PACKAGE_NAME)
                        try {
                            Log.d("package", isPackageInstalled(PHONE_PE_PACKAGE_NAME).toString())
                            startActivityForResult(intent, UPI_REQUEST_CODE)
                        } catch (e: java.lang.Exception) {
                            Toast.makeText(
                                applicationContext,
                                "PhonePe not found.",
                                Toast.LENGTH_SHORT
                            ).show()
                            e.printStackTrace()
                            Log.d("Phone Pe", "onClick: $e")
                        }
                    } else if (method == 3) {
                        paymentMethod = "3"

                        url.appendQueryParameter("pa", upi_payment_id)
                        url.build()

                        val intent = Intent(Intent.ACTION_VIEW)
                        intent.setData(Uri.parse(url.toString()))
                        try {
                            startActivityForResult(intent, UPI_REQUEST_CODE)
                        } catch (e: java.lang.Exception) {
                            Toast.makeText(
                                applicationContext,
                                "Others payment mode not found.",
                                Toast.LENGTH_SHORT
                            ).show()
                            e.printStackTrace()
                            Log.d("Phone Pe", "onClick: $e")
                        }
                    } else {
                        Toast.makeText(
                            applicationContext,
                            "Please select one method to add fund.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    addFund = false
                }
            }
        })
    }

    private fun setWalletBalance() {
       val walletBalance = findViewById<TextView>(R.id.walletBalance)
        val call1 = controller.getInstance().api.getWalletBalance(mainObject)
        call1.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                val walletAmount = response.body()!!["wallet_amt"].asInt.toString()
                walletBalance.text = "₹$walletAmount.00"
                binding.mWalletBalance.text = "₹$walletAmount.00"

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

    private fun setAddFundTransactionHist() {
        addFundHistRecycler = findViewById(R.id.addFundHistRecycler)
        noResults = findViewById(R.id.noResults)
        transLayout = findViewById(R.id.linearLayout21)
        val call2 = controller.getInstance().api.getAutoDepositList(mainObject)
        call2.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.body()!!["status"].asBoolean) {
                    val HistArray = response.body()!!.getAsJsonArray("result")

                    if (HistArray.size() == 0) {
                       // addFundHistRecycler!!.setVisibility(View.INVISIBLE)
                       // noResults!!.setVisibility(View.VISIBLE)
                       // transLayout!!.setVisibility(View.GONE)
                    } else {
                       // noResults!!.setVisibility(View.GONE)
                       // transLayout!!.setVisibility(View.VISIBLE)
                        //modelArrayList.clear()
                        for (i in 0 until HistArray.size()) {
                            val jsonObject = HistArray[i].asJsonObject

                            val request_number = jsonObject["txn_id"].asString
                            val request_amount = jsonObject["amount"].asString
                            val request_status = jsonObject["fund_status"].asString
                            val payment_method = jsonObject["payment_method"].asString
                            val deposit_type = jsonObject["deposit_type"].asString
                            val remark = jsonObject["reject_remark"].asString
                            val insert_date = jsonObject["insert_date"].asString


                          /*  val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(
                                applicationContext,
                                RecyclerView.VERTICAL, false
                            )*/

                           // addFundHistRecycler.setLayoutManager(layoutManager)

                            val model = wallet_withdrawal_hist_model()

                            model.request_number = request_number
                            model.request_amount = request_amount
                            model.insert_date = insert_date
                            model.request_status = request_status
                            model.remark = remark
                            model.payment_method = payment_method
                            model.fund_status = deposit_type


                            modelArrayList.add(model)
                        }

                        /*val adapter = add_fund_hist_adapter(modelArrayList)
                        addFundHistRecycler.setAdapter(adapter)*/
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        Log.d("Activity result", "onActivityResult: $requestCode")
        Log.d("Activity result", "onActivityResult: $resultCode")

        if (requestCode == UPI_REQUEST_CODE && resultCode == RESULT_OK && data!!.extras != null) {
            //Toast.makeText(getApplicationContext(), "Payment Success", Toast.LENGTH_SHORT).show();
            addFundTransaction(data!!.extras, paymentMethod!!)
        } else {
            Toast.makeText(applicationContext, "Payment failed", Toast.LENGTH_SHORT).show()
        }
    }

    private fun addFundTransaction(extras: Bundle?, paymentMethod: String) {
//        Log.d("success", "addFundTransaction " + "Status: " + extras.get("Status").toString());
//        Log.d("success", "addFundTransaction " + "txnRef: " + extras.get("txnRef").toString());
//        Log.d("success", "addFundTransaction " + "txnId: " + extras.get("txnId").toString());

        mainObject.addProperty("amount", number!!.text.toString().trim { it <= ' ' })
        try {
            mainObject.addProperty("txn_id", extras!!["txnId"].toString())
            mainObject.addProperty("txn_ref", extras!!["txnRef"].toString())
        } catch (e: Exception) {
            //Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }

        if (paymentMethod == "1") {
            mainObject.addProperty("upigpay", "1")
            mainObject.addProperty("upiphonepe", "0")
            mainObject.addProperty("otherupi", "0")
        } else if (paymentMethod == "2") {
            mainObject.addProperty("upigpay", "0")
            mainObject.addProperty("upiphonepe", "1")
            mainObject.addProperty("otherupi", "0")
        } else {
            mainObject.addProperty("upigpay", "0")
            mainObject.addProperty("upiphonepe", "0")
            mainObject.addProperty("otherupi", "1")
        }
        Log.d("JsonObject", "addFundTransaction $mainObject")

        if (extras!!["Status"].toString().equals("SUCCESS", ignoreCase = true)) {
            val call = controller.getInstance().api.addMoneyViaUpi(mainObject)
            call.enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.body()!!["status"].asBoolean) {
                        Toast.makeText(
                            applicationContext,
                            response.body()!!["msg"].asString,
                            Toast.LENGTH_SHORT
                        ).show()
                        //Log.d("addFundResponse", "onResponse: " + response.body().getAsJsonArray());
//                        "result": [
//                        {
//                            "id": "5",
//                                "user_id": "4",
//                                "amount": "100",
//                                "tx_request_number": "2048306",
//                                "txn_id": "PTM0a1d340bf2c7474fb864dc6a07c1340b",
//                                "txn_ref": "84e648ca97c841719ce1e7c860268623",
//                                "insert_date": "2021-11-19 14:44:37",
//                                "payment_method": "Others",
//                                "paid_upi": "",
//                                "fund_status": "0",
//                                "deposit_type": "0",
//                                "reject_remark": ""
//                        },
//                        {
//                            "id": "4",
//                                "user_id": "4",
//                                "amount": "1",
//                                "tx_request_number": "2140259",
//                                "txn_id": "83e3954e34e34a9a9b9f925cefbf27fa",
//                                "txn_ref": "PTM5538f2442a7a4909a176c49e35ebd975",
//                                "insert_date": "2021-11-19 14:27:12",
//                                "payment_method": "Others",
//                                "paid_upi": "",
//                                "fund_status": "0",
//                                "deposit_type": "0",
//                                "reject_remark": ""
//                        }
//    ]
                        setWalletBalance()
                        setAddFundTransactionHist()
                        number!!.text.clear()
                    } else {
                        Toast.makeText(
                            applicationContext,
                            response.body()!!["msg"].asString,
                            Toast.LENGTH_SHORT
                        ).show()
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
        } else {
            Toast.makeText(applicationContext, "Payment failed", Toast.LENGTH_SHORT).show()
        }
    }

    private fun isPackageInstalled(packageName: String): Boolean {
        val packageManager = packageManager
        try {
            packageManager.getPackageInfo(packageName, 0)
            return true
        } catch (e: PackageManager.NameNotFoundException) {
            return false
        }
    }

    override fun onResume() {
        super.onResume()
        setWalletBalance()
        setAddFundTransactionHist()
        addFund = true
    }
}