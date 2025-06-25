package com.myTara567.app.home.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.myTara567.app.databinding.FragmentTeramConditionBinding

class TeramConditionFragment : Fragment() {

    private var _binding: FragmentTeramConditionBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout using ViewBinding
        _binding = FragmentTeramConditionBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

       /* whatsApp!!.setOnClickListener(View.OnClickListener {
            val url = "https://wa.me/$mobile_no"
            val i = Intent(Intent.ACTION_VIEW)
            i.setData(Uri.parse(url))
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            applicationContext.startActivity(i)
        })*/
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



}
