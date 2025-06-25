package com.myTara567.app.dashboard.allbis.ui.wallet

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.myTara567.app.databinding.ActivityManualDepositUploadBinding

class ManualDepositUploadActivity : AppCompatActivity() {
    lateinit var binding: ActivityManualDepositUploadBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityManualDepositUploadBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.backBidHistImage.setOnClickListener {
            onBackPressed()
        }
    }
}