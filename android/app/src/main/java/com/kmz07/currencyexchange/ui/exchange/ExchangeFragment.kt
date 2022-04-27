package com.kmz07.currencyexchange.ui.exchange

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.kmz07.currencyexchange.api.Authentication
import com.kmz07.currencyexchange.api.ExchangeService
import com.kmz07.currencyexchange.api.model.ExchangeRates
import com.kmz07.currencyexchange.databinding.FragmentExchangeBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ExchangeFragment : Fragment() {

    private var _binding: FragmentExchangeBinding? = null
    private val binding get() = _binding!!
    private var buyUsdTextView: TextView? = null
    private var sellUsdTextView: TextView? = null
    var exchangeHeader: TextView? = null

     override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fetchRates()
        _binding = FragmentExchangeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        buyUsdTextView = binding.txtBuyUsdRate
        sellUsdTextView = binding.txtSellUsdRate
        exchangeHeader = binding.exchangeHeader

        exchangeHeader!!.setOnClickListener{ view ->
            fetchRates()
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun fetchRates() {
        ExchangeService.exchangeApi().getExchangeRates().enqueue(object :
            Callback<ExchangeRates> {
            override fun onResponse(
                call: Call<ExchangeRates>, response:
                Response<ExchangeRates>
            ) {
                val responseBody: ExchangeRates? = response.body();
                buyUsdTextView?.text = responseBody?.lbpToUsd.toString();
                sellUsdTextView?.text = responseBody?.usdToLbp.toString();
            }

            override fun onFailure(call: Call<ExchangeRates>, t: Throwable) {
                Log.d("mytag", t.stackTraceToString())
                Snackbar.make(
                    view as View,
                    "Could not fetch rates.",
                    Snackbar.LENGTH_LONG
                )
                    .show()
                return;
            }
        })
    }

}