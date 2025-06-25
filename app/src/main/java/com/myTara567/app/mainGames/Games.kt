package com.myTara567.app.mainGames

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.myTara567.app.R
import com.myTara567.app.dashboard.gameDataModel

class Games : AppCompatActivity() {
    var gameName: String? = null
    var gameId: String? = null
    var timeSrt: String? = null
    var gameTitle: TextView? = null
    var gameLayout: ConstraintLayout? = null
    var back: ImageView? = null
    var Single_Digit: ConstraintLayout? = null
    var Jodi_Digit: ConstraintLayout? = null
    var Single_Pana: ConstraintLayout? = null
    var Double_Pana: ConstraintLayout? = null
    var Triple_Pana: ConstraintLayout? = null
    var Half_Sangam: ConstraintLayout? = null
    var Full_Sangam: ConstraintLayout? = null
    var constraintLayout: ConstraintLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.games)

        val gameDataModel = intent.getSerializableExtra("gameDataModel") as gameDataModel?

        Log.d("game_id", "onCreate: " + gameDataModel!!.game_id)
        Log.d("game_id", "onCreate: " + gameDataModel.game_name)
        Log.d("game_id", "onCreate: " + gameDataModel.openTime)
        Log.d("game_id", "onCreate: " + gameDataModel.closeTime)
        Log.d("game_id", "onCreate: " + gameDataModel.closeResult)
        Log.d("game_id", "onCreate: " + gameDataModel.marketMessage)
        Log.d("game_id", "onCreate: " + gameDataModel.msg_status)

        gameName = gameDataModel.game_name
        gameId = gameDataModel.game_id
        timeSrt = gameDataModel.time_srt

        val sysTime = System.currentTimeMillis() / 1000
        constraintLayout = findViewById(R.id.constraintLayout14)
        //gameLayout = findViewById(R.id.gameLayout);
        back = findViewById(R.id.gamesBackImageView)
        Single_Digit = findViewById(R.id.Single_Digit)
        Jodi_Digit = findViewById(R.id.Jodi_Digit)
        Single_Pana = findViewById(R.id.Single_Pana)
        Double_Pana = findViewById(R.id.Double_Pana)
        Triple_Pana = findViewById(R.id.Triple_Pana)
        Half_Sangam = findViewById(R.id.Half_Sangam)
        Full_Sangam = findViewById(R.id.mSangam)
        gameTitle = findViewById(R.id.gameTitle)

        gameTitle!!.setText(gameName)

        // constraintLayout.setAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_up));
        back!!.setOnClickListener(View.OnClickListener { onBackPressed() })


        Single_Digit!!.setOnClickListener(View.OnClickListener {
            val i = Intent(applicationContext, bidPlacer::class.java)
            i.putExtra("Title", "Single Digit")
            i.putExtra("gamDataObject", gameDataModel)
            startActivity(i)
        })

        Jodi_Digit!!.setOnClickListener {
            if (sysTime <= timeSrt!!.toInt()) {
                val i = Intent(applicationContext, bidPlacer::class.java)
                i.putExtra("Title", "Jodi Digit")
                i.putExtra("gamDataObject", gameDataModel)
                startActivity(i)
            } else {
                Toast.makeText(applicationContext, "Session Closed for Now", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        Single_Pana!!.setOnClickListener(View.OnClickListener {
            val i = Intent(applicationContext, bidPlacer::class.java)
            i.putExtra("Title", "Single Pana")
            i.putExtra("gamDataObject", gameDataModel)
            startActivity(i)
        })

        Double_Pana!!.setOnClickListener(View.OnClickListener {
            val i = Intent(applicationContext, bidPlacer::class.java)
            i.putExtra("Title", "Double Pana")
            i.putExtra("gamDataObject", gameDataModel)
            startActivity(i)
        })

        Triple_Pana!!.setOnClickListener(View.OnClickListener {
            val i = Intent(applicationContext, bidPlacer::class.java)
            i.putExtra("Title", "Triple Pana")
            i.putExtra("gamDataObject", gameDataModel)
            startActivity(i)
        })

        Half_Sangam!!.setOnClickListener(View.OnClickListener {
            if (sysTime <= timeSrt!!.toInt()) {
                val i = Intent(applicationContext, bidPlacer::class.java)
                i.putExtra("Title", "Half Sangam")
                i.putExtra("gamDataObject", gameDataModel)
                startActivity(i)
            } else {
                Toast.makeText(applicationContext, "Session Closed for Now", Toast.LENGTH_SHORT)
                    .show()
            }
        })

        Full_Sangam!!.setOnClickListener(View.OnClickListener {
            if (sysTime <= timeSrt!!.toInt()) {
                val i = Intent(applicationContext, bidPlacer::class.java)
                i.putExtra("Title", "Full Sangam")

                i.putExtra("gamDataObject", gameDataModel)
                startActivity(i)
            } else {
                Toast.makeText(applicationContext, "Session Closed for Now", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }
}