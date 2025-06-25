package com.myTara567.app.home.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.myTara567.app.dashboard.allbis.ui.wallet.DepositHistoryActivity
import com.myTara567.app.dashboard.allbis.ui.allbids.FundRequestHistoryActivity
import com.myTara567.app.dashboard.allbis.ui.wallet.ManualDepositUploadActivity
import com.myTara567.app.dashboard.allbis.ui.WithdrawFundActivity
import com.myTara567.app.dashboard.allbis.ui.wallet.AddBankDetailsActivity
import com.myTara567.app.dashboard.allbis.ui.wallet.AddUPIFundActivity
import com.myTara567.app.databinding.FragmentWalletBinding // Generated binding class
import com.myTara567.app.dashboard.allbis.ui.wallet.PaymentActivity

class WalletFragment : Fragment() {

    private var _binding: FragmentWalletBinding? = null
    private val binding get() = _binding!! // Ensures non-null access to binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWalletBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mInitView()
    }

    fun mInitView() {


        binding.mAddFoundLayout.setOnClickListener {
            startActivity(Intent(requireActivity(), PaymentActivity::class.java))
        }

        binding.mManualLayout.setOnClickListener {
            startActivity(Intent(requireActivity(), ManualDepositUploadActivity::class.java))
        }

        binding.mWithdrawFundsLayout.setOnClickListener {
            startActivity(Intent(requireActivity(), WithdrawFundActivity::class.java))
        }

        binding.mDepositHistoryLayout.setOnClickListener {
            startActivity(Intent(requireActivity(), DepositHistoryActivity::class.java))

        }
        binding.mAddUPILayout.setOnClickListener {
            startActivity(Intent(requireActivity(), AddUPIFundActivity::class.java))
        }

        binding.mWithdrawalHistoryLayout.setOnClickListener {
            val intent = Intent(requireActivity(), FundRequestHistoryActivity::class.java)
            intent.putExtra("screenName", "Withdrawal History") // Replace with your key-value pair
            startActivity(intent)
        }

        binding.mAddBankDetailsLayout.setOnClickListener {

            startActivity(Intent(requireActivity(), AddBankDetailsActivity::class.java))        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Prevent memory leaks
    }
}
