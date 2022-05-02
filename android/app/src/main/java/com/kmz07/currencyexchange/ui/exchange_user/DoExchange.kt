package com.kmz07.currencyexchange.ui.exchange_user

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        myview = inflater.inflate(
            R.layout.fragment_do_exchange,
            container, false
        )

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

        val usdAmountRef = myview?.findViewById<TextInputLayout>(R.id.txtInputUsdAmount)
        val usdAmount = usdAmountRef?.editText?.text.toString();
        val lbpAmountRef = myview?.findViewById<TextInputLayout>(R.id.txtInputLbpAmount)
        val lbpAmount = lbpAmountRef?.editText?.text.toString();
        val receiverNameRef = myview?.findViewById<TextInputLayout>(R.id.txtInputReceiverName)
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
            usdAmountRef?.editText?.setText("")
            lbpAmountRef?.editText?.setText("")
            receiverNameRef?.editText?.setText("")
            receiverNameRef?.editText?.clearFocus()

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
                Snackbar.make(
                    myview as View, "Transaction added!",
                    Snackbar.LENGTH_LONG
                )
                    .show()
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
