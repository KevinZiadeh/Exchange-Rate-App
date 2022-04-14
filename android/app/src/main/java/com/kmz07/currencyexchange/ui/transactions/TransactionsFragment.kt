package com.kmz07.currencyexchange.ui.transactions

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
import com.kmz07.currencyexchange.R
import com.kmz07.currencyexchange.api.Authentication
import com.kmz07.currencyexchange.api.ExchangeService
import com.kmz07.currencyexchange.api.model.Transaction
import com.kmz07.currencyexchange.databinding.FragmentTransactionsBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class TransactionsFragment : Fragment() {

    private var _binding: FragmentTransactionsBinding? = null
    private var listview: ListView? = null
    private var transactions: ArrayList<Transaction>? = ArrayList()
    private var adapter: TransactionAdapter? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentTransactionsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        fetchTransactions()

        listview = binding.listview
        adapter =
            TransactionAdapter(layoutInflater, transactions!!)
        listview?.adapter = adapter

        return root
    }

    private fun fetchTransactions() {
        if (Authentication.getToken() != null) {
            ExchangeService.exchangeApi()
                .getTransactions("Bearer ${Authentication.getToken()}")
                .enqueue(object : Callback<List<Transaction>> {
                    override fun onFailure(
                        call: Call<List<Transaction>>,
                        t: Throwable
                    ) {
                        Log.d("mytag", t.stackTraceToString())
                        Snackbar.make(
                            view as View,
                            "Fetching Transactions Failed.",
                            Snackbar.LENGTH_LONG
                        )
                            .show()
                    }

                    override fun onResponse(
                        call: Call<List<Transaction>>,
                        response: Response<List<Transaction>>
                    ) {
                        transactions?.clear()
                        transactions?.addAll(response.body()!!)
                        adapter!!.notifyDataSetChanged()
                    }
                })
        }
    }

    class TransactionAdapter(
        private val inflater: LayoutInflater,
        private val dataSource: List<Transaction>
    ) : BaseAdapter() {
        override fun getView(
            position: Int, convertView: View?, parent:
            ViewGroup?
        ): View {
            val view: View = inflater.inflate(
                R.layout.item_transaction,
                parent, false
            )

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