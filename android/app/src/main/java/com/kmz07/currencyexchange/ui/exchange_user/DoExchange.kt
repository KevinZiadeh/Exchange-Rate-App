package com.kmz07.currencyexchange.ui.exchange_user

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.RadioButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.kmz07.currencyexchange.R
import com.kmz07.currencyexchange.api.Authentication
import com.kmz07.currencyexchange.api.ExchangeService
import com.kmz07.currencyexchange.api.model.Transaction
import com.kmz07.currencyexchange.ui.exchange.ExchangeFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DoExchange : Fragment() {
    private var myview: View? = null
    private var submitBtn: Button? = null
    private var usdAmountRef: TextInputLayout? = null
    private var lbpAmountRef: TextInputLayout? = null
    private var receiverNameRef: TextInputLayout? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        myview = inflater.inflate(
            R.layout.fragment_do_exchange,
            container, false
        )

        usdAmountRef = myview?.findViewById(R.id.txtInputUsdAmount)
        lbpAmountRef = myview?.findViewById(R.id.txtInputLbpAmount)
        receiverNameRef = myview?.findViewById(R.id.txtInputReceiverName)
        submitBtn = myview?.findViewById(R.id.btnExchangeSubmit)
        submitBtn?.setOnClickListener { myview ->
            doTransaction()
        }
        return myview
    }

    private fun doTransaction() {
        if (Authentication.getToken() == null){
            throw Exception("Exchanging without token")
        }

        val usdAmount = usdAmountRef?.editText?.text.toString();
        val lbpAmount = lbpAmountRef?.editText?.text.toString();
        val receiverName = receiverNameRef?.editText?.text.toString();

        val usdToLbp = myview?.findViewById<RadioButton>(R.id.rdBtnSellUsd)?.isChecked() == true
        val lbpToUbp = myview?.findViewById<RadioButton>(R.id.rdBtnBuyUsd)?.isChecked() == true
        try {
            val transaction = Transaction();
            transaction.lbpAmount = lbpAmount.toFloat();
            transaction.usdAmount = usdAmount.toFloat();
            transaction.receiverName = receiverName
            if (!usdToLbp && !lbpToUbp) {
                throw Exception();
            }
            transaction.usdToLbp = usdToLbp;
            addUserTransaction(transaction);

        } catch (e: Exception) {
            Snackbar.make(
                myview as View, "Could not add transaction.",
                Snackbar.LENGTH_LONG
            )
                .show()
        }
    }

    private fun addUserTransaction(transaction: Transaction) {
        ExchangeService.exchangeApi().addUserTransaction(
            transaction,
            "Bearer ${Authentication.getToken()}"
        ).enqueue(object :
            Callback<Any> {
            override fun onResponse(
                call: Call<Any>, response:
                Response<Any>
            ) {
                if (response.code() != 200) {
                    val imm: InputMethodManager =
                        activity!!.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(view!!.windowToken, 0)
                    Snackbar.make(
                        myview as View, "Could not add transaction.",
                        Snackbar.LENGTH_LONG
                    )
                        .show()
                } else {
                    Snackbar.make(
                        myview as View, "Transaction added!",
                        Snackbar.LENGTH_LONG
                    )
                        .show()
                    usdAmountRef?.editText?.setText("")
                    lbpAmountRef?.editText?.setText("")
                    receiverNameRef?.editText?.setText("")
                    receiverNameRef?.editText?.clearFocus()
                }
            }

            override fun onFailure(call: Call<Any>, t: Throwable) {
                Snackbar.make(
                    myview as View, "Could not add transaction.",
                    Snackbar.LENGTH_LONG
                )
                    .show()
            }
        })
    }

}
