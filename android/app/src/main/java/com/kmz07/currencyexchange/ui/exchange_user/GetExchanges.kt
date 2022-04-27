package com.kmz07.currencyexchange.ui.exchange_user

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.internal.LinkedTreeMap
import com.kmz07.currencyexchange.R
import com.kmz07.currencyexchange.api.Authentication
import com.kmz07.currencyexchange.api.ExchangeService
import com.kmz07.currencyexchange.api.model.Transaction
import com.kmz07.currencyexchange.api.model.UserExchanges
import com.kmz07.currencyexchange.databinding.FragmentGetExchangesBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class GetExchanges : Fragment() {

    private var _binding: FragmentGetExchangesBinding? = null
    private var listview: ListView? = null
    private var exchangesG: ArrayList<Transaction>? = ArrayList()
    private var exchangesR: ArrayList<Transaction>? = ArrayList()
    private var adapterG: ExchangesAdapter? = null
    private var adapterR: ExchangesAdapter? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGetExchangesBinding.inflate(inflater, container, false)
        val root: View = binding.root
        fetchExchanges()

        listview = binding.listviewG
        adapterG =
            ExchangesAdapter(layoutInflater, exchangesG!!)
        listview?.adapter = adapterG

        listview = binding.listviewR
        adapterR =
            ExchangesAdapter(layoutInflater, exchangesR!!)
        listview?.adapter = adapterR

        return root
    }

    private fun fetchExchanges() {
        if (Authentication.getToken() != null) {
            ExchangeService.exchangeApi()
                .getExchangeUser("Bearer ${Authentication.getToken()}")
                .enqueue(object : Callback<UserExchanges> {
                    override fun onFailure(
                        call: Call<UserExchanges>,
                        t: Throwable
                    ) {
                        Snackbar.make(
                            view as View,
                            "Fetching Exchanges Failed.",
                            Snackbar.LENGTH_LONG
                        )
                            .show()
                    }

                    override fun onResponse(
                        call: Call<UserExchanges>,
                        response: Response<UserExchanges>
                    ) {
                        exchangesG?.clear()
                        exchangesR?.clear()

                        exchangesG?.addAll(response.body()!!.giveList!!)
                        exchangesR?.addAll(response.body()!!.receiveList!!)

                        adapterG!!.notifyDataSetChanged()
                        adapterR!!.notifyDataSetChanged()
                    }
                })
        }
    }

    class ExchangesAdapter(
        private val inflater: LayoutInflater,
        private val dataSource: List<Transaction>
    ) : BaseAdapter() {
        override fun getView(
            position: Int, convertView: View?, parent:
            ViewGroup?
        ): View {
            val view: View = inflater.inflate(
                R.layout.item_exchanges,
                parent, false
            )

            view.findViewById<TextView>(R.id.receiverName).text =
                dataSource[position].receiverId.toString()
//                dataSource[position].receiverUsername.toString()
            view.findViewById<TextView>(R.id.usdAmount).text =
                dataSource[position].usdAmount.toString()
            view.findViewById<TextView>(R.id.lbpAmount).text =
                dataSource[position].lbpAmount.toString()
            view.findViewById<TextView>(R.id.usdToLbp).text =
                dataSource[position].usdToLbp.toString()
            view.findViewById<TextView>(R.id.date).text =
                dataSource[position].addedDate.toString()
            return view
        }

        override fun getItem(position: Int): Any {
            return dataSource[position]
        }

        override fun getItemId(position: Int): Long {
            return dataSource[position].id?.toLong() ?: 0
        }

        override fun getCount(): Int {
            return dataSource.size
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}