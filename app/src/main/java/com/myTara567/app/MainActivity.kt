package com.myTara567.app

import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.gson.JsonObject
import com.myTara567.app.new_ui_update.EnterMPINActivity
import com.myTara567.app.serverApi.controller
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    lateinit var sp: SharedPreferences
    lateinit var spUnique_token: SharedPreferences
    var unique_token: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        // Corrected Glide loading with ImageView
        val splashImage = findViewById<ImageView>(R.id.splashImage)

// Set width and height programmatically to 40dp
        val layoutParams = splashImage.layoutParams
        val density = resources.displayMetrics.density
        layoutParams.width = (50 * density).toInt() // Convert 40dp to pixels
        layoutParams.height = (50 * density).toInt() // Convert 40dp to pixels
        splashImage.layoutParams = layoutParams

// Load GIF into the ImageView
        Glide.with(this)
            .asGif()
            .load(R.drawable.iv_gif_) // Replace with your GIF file
            .into(splashImage)

        sp = getSharedPreferences("app_key", MODE_MULTI_PROCESS)
        spUnique_token = applicationContext.getSharedPreferences("unique_token", MODE_MULTI_PROCESS)
        unique_token = spUnique_token.getString("unique_token", null)
        Log.e("unique validate", "onCreate: $unique_token")

        if (isNetworkConnected) {
            val js = JsonObject()
            js.addProperty("env_type", "Prod")
            Log.d("TAG", "onCreate: $js")
            val call = controller.getInstance().api.getAppKey(js)
            call.enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    Log.e("response ", "onResponse: " + response.body().toString())
                    sp = getSharedPreferences("app_key", MODE_MULTI_PROCESS)
                    val app_key = response.body()!!["app_key"].asString
                    Log.e("response ", "onResponse: $app_key")
                    Encrypt().cryptop(app_key)
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Toast.makeText(
                        applicationContext,
                        "Something went wrong.... Try again later",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })

            if (unique_token == null) {
                Handler().postDelayed({
                    val i = Intent(this@MainActivity, Login::class.java)
                    startActivity(i)
                    finish() // Close this activity
                }, 1000) // 1-second delay
            } else {
                Handler().postDelayed({
                    val i = Intent(this@MainActivity, EnterMPINActivity::class.java)
                    i.putExtra("checkActivity", "mainActivity")
                    startActivity(i)
                    finish() // Close this activity
                }, 4000) // 4-second delay
            }
        } else {
            val handler = Handler()
            handler.postDelayed({
                val toast = Toast.makeText(
                    applicationContext,
                    "Please Check Your Internet Connection",
                    Toast.LENGTH_LONG
                )
                toast.show()
                finishAffinity() // Close all activities
            }, 4000) // 4-second delay
        }
    }

    private val isNetworkConnected: Boolean
        get() {
            val cm = applicationContext.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
            val ni = cm.activeNetworkInfo
            return ni != null && ni.isConnectedOrConnecting
        }

    private inner class Encrypt {
        fun cryptop(args: String) {
            val str = args
            val k = -3
            encode(str, k)
        }

        fun encode(str: String, k: Int): String {
            var encodedString = ""
            var nothingencoded = ""

            for (i in 0 until 15) {
                val char = str[i]
                val charCode = char.code
                val newChar2 = charCode.toChar()
                nothingencoded += newChar2
            }

            for (i in 15 until str.length) {
                val char = str[i]
                val charCode = char.code
                val newCharCode = charCode + k
                val newChar = newCharCode.toChar()

                if (newCharCode > 122) {
                    val newCharCode2 = newCharCode + 26
                    val newChar2 = newCharCode2.toChar()
                    encodedString += newChar2
                } else if (newCharCode > 90 && newCharCode < 97) {
                    val newCharCode2 = newCharCode + 26
                    val newChar2 = newCharCode2.toChar()
                    encodedString += newChar2
                } else if (newCharCode < 65) {
                    val newCharCode2 = newCharCode + 26
                    val newChar2 = newCharCode2.toChar()
                    encodedString += newChar2
                } else {
                    encodedString += newChar
                }
            }
            Log.e("Encrypted_app_key", nothingencoded)
            Log.e("Encrypted_app_key_2", encodedString)
            Log.e("response ", "onResponse: ${nothingencoded + encodedString}")

            val editor = sp.edit()
            editor.putString("app_key", nothingencoded + encodedString)
            editor.commit()

            return nothingencoded + encodedString
        }
    }
}
