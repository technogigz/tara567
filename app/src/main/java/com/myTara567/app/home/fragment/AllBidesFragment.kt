package com.myTara567.app.home.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.myTara567.app.dashboard.allbis.ui.ApprovedDebitHistoryActivity
import com.myTara567.app.dashboard.allbis.ui.allbids.ApprovedCreditHistoryActivity
import com.myTara567.app.dashboard.allbis.ui.allbids.BidHistory
import com.myTara567.app.dashboard.allbis.ui.allbids.FundRequestHistoryActivity
import com.myTara567.app.databinding.FragmentAllBidesBinding

class AllBidesFragment : Fragment() {
    private var _binding: FragmentAllBidesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAllBidesBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listner()
    }


    fun listner() {
        binding.bidHistory.setOnClickListener {
            startActivity(Intent(requireActivity(), BidHistory::class.java))
        }

        binding.mFundHistory.setOnClickListener {
            val intent = Intent(requireActivity(), FundRequestHistoryActivity::class.java)
            intent.putExtra("screenName", "Fund Request History")
            startActivity(intent)
        }

        binding.mCreditHistory.setOnClickListener {
            startActivity(Intent(requireActivity(), ApprovedCreditHistoryActivity::class.java))
        }

        binding.mDebitHistory.setOnClickListener {
            startActivity(Intent(requireActivity(), ApprovedDebitHistoryActivity::class.java))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
