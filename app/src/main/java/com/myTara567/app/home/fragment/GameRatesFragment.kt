package com.myTara567.app.home.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.gson.JsonObject
import com.myTara567.app.databinding.FragmentGameRatesBinding

class GameRatesFragment : Fragment() {

    private var _binding: FragmentGameRatesBinding? = null
    private val binding get() = _binding!! // Non-nullable binding reference
    private var appKey: String? = null

    override fun onCreateView(
        inflater: android.view.LayoutInflater,
        container: android.view.ViewGroup?,
        savedInstanceState: Bundle?
    ): android.view.View {
        _binding = FragmentGameRatesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: android.view.View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Retrieve app key from SharedPreferences
        appKey = activity?.getSharedPreferences("app_key", Context.MODE_PRIVATE)
            ?.getString("app_key", null)

        Log.d("GameRatesFragment", "AppKey: $appKey")



        // Fetch game rates
        fetchGameRates()
    }

    private fun fetchGameRates() {
        if (appKey.isNullOrEmpty()) { Toast.makeText(requireContext(), "App key is missing, unable to fetch game rates.", Toast.LENGTH_SHORT).show()
            return
        }

        // Prepare request body
        val js = JsonObject().apply {
            addProperty("env_type", "Prod")
            addProperty("app_key", appKey)
        }

      /*  // Make API call
        val call = controller.getInstance().api.game_rates(js)
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful && response.body() != null) {
                    val responseBody = response.body()!!
                    val status = responseBody["status"].asBoolean
                    if (status) {
                        val jsonArray = responseBody.getAsJsonArray("game_rates")
                        if (jsonArray.size() > 0) {
                            val jsonObject = jsonArray[0].asJsonObject

                            // Update UI with game rates using ViewBinding
                            _binding!!.singleDigitVal1.text =
                                jsonObject["single_digit_val_1"].asString
                            _binding!!.singleDigitVal2.text =
                                jsonObject["single_digit_val_2"].asString
                            _binding!!.jodiDigitVal1.text =
                                jsonObject["jodi_digit_val_1"].asString
                            _binding!!.jodiDigitVal2.text =
                                jsonObject["jodi_digit_val_2"].asString
                            _binding!!.singlePanaVal1.text =
                                jsonObject["single_pana_val_1"].asString
                            _binding!!.singlePanaVal2.text =
                                jsonObject["single_pana_val_2"].asString
                            _binding!!.doublePanaVal1.text =
                                jsonObject["double_pana_val_1"].asString
                            _binding!!.doublePanaVal2.text =
                                jsonObject["double_pana_val_2"].asString
                            _binding!!.tripplePanaVal1.text =
                                jsonObject["tripple_pana_val_1"].asString
                            _binding!!.tripplePanaVal2.text =
                                jsonObject["tripple_pana_val_2"].asString
                            _binding!!.halfSangamVal1.text =
                                jsonObject["half_sangam_val_1"].asString
                            _binding!!.halfSangamVal2.text =
                                jsonObject["half_sangam_val_2"].asString
                            _binding!!.fullSangamVal1.text =
                                jsonObject["full_sangam_val_1"].asString
                            _binding!!.fullSangamVal2.text =
                                jsonObject["full_sangam_val_2"].asString
                        }
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Failed to fetch game rates.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Server error: ${response.code()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Toast.makeText(
                    requireContext(),
                    "Something went wrong... Try again later",
                    Toast.LENGTH_SHORT
                ).show()
                Log.e("GameRatesFragment", "Error: ${t.message}")
            }
        })*/
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Clean up binding reference
    }
}
