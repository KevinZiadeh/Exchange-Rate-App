package com.kmz07.currencyexchange.ui.statistics

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.kmz07.currencyexchange.MainActivity
import com.kmz07.currencyexchange.api.Authentication
import com.kmz07.currencyexchange.api.ExchangeService
import com.kmz07.currencyexchange.api.model.Token
import com.kmz07.currencyexchange.api.model.User
import com.kmz07.currencyexchange.databinding.FragmentStatisticsBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class StatisticsFragment : Fragment() {

    private var _binding: FragmentStatisticsBinding? = null
    private var usernameEditText: TextInputLayout? = null
    private var passwordEditText: TextInputLayout? = null
    private var submitButton: Button? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentStatisticsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}