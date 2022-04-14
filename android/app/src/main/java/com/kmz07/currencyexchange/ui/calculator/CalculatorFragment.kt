package com.kmz07.currencyexchange.ui.calculator

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.kmz07.currencyexchange.api.ExchangeService
import com.kmz07.currencyexchange.api.model.ConvertObject
import com.kmz07.currencyexchange.api.model.ExchangeRates
import com.kmz07.currencyexchange.databinding.FragmentCalculatorBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CalculatorFragment : Fragment() {

    private var _binding: FragmentCalculatorBinding? = null
    private var usdAmountConv: EditText? = null
    private var lbpAmountConv: EditText? = null
    private var usdToLbpConv: Switch? = null
    private var convertBtn: Button? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val galleryViewModel =
            ViewModelProvider(this).get(CalculatorViewModel::class.java)

        _binding = FragmentCalculatorBinding.inflate(inflater, container, false)
        val root: View = binding.root

        usdAmountConv = binding.usdAmountConv
        lbpAmountConv = binding.lbpAmountConv
        usdToLbpConv = binding.usdToLbpConv
        convertBtn = binding.convertBtn
        convertBtn?.setOnClickListener { view ->
            convert()
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun convert(){
        ExchangeService.exchangeApi().getExchangeRates().enqueue(object :
            Callback<ExchangeRates> {
            override fun onResponse(
                call: Call<ExchangeRates>, response:
                Response<ExchangeRates>
            ) {
                val responseBody: ExchangeRates? = response.body();
                try {
                    if (usdToLbpConv!!.isChecked) {
                        lbpAmountConv?.setText(
                            (usdAmountConv?.text.toString().toFloat() *
                                    responseBody!!.usdToLbp!!).toString()
                        )
                    } else {
                        usdAmountConv?.setText(
                            (lbpAmountConv?.text.toString().toFloat() /
                                    responseBody!!.lbpToUsd!!).toString()
                        )
                    }
                } catch (ex: Exception){
                    Log.d("mytag", ex.stackTraceToString())
                    val imm: InputMethodManager =
                        activity!!.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(view!!.windowToken, 0)
                    Snackbar.make(view as View, "Conversion Failed.",
                        Snackbar.LENGTH_LONG)
                        .show()
                }
            }

            override fun onFailure(call: Call<ExchangeRates>, t: Throwable) {
                Log.d("mytag", t.stackTraceToString())
                Snackbar.make(
                    convertBtn as View,
                    "Conversion Failed",
                    Snackbar.LENGTH_LONG
                )
                    .show()
                return;
            }
        })
    }

}