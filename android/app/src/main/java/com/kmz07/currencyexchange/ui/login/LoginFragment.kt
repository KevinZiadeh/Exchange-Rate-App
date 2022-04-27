package com.kmz07.currencyexchange.ui.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.kmz07.currencyexchange.MainActivity
import com.kmz07.currencyexchange.api.Authentication
import com.kmz07.currencyexchange.api.ExchangeService
import com.kmz07.currencyexchange.api.model.Token
import com.kmz07.currencyexchange.api.model.User
import com.kmz07.currencyexchange.databinding.FragmentLoginBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
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
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val root: View = binding.root
        usernameEditText = binding.txtInputUsername
        passwordEditText = binding.txtInputPassword
        submitButton = binding.btnSubmit
        submitButton?.setOnClickListener {
            loginUser()
        }

        return root
    }

    private fun loginUser() {
        val user = User()
        user.username = usernameEditText?.editText?.text.toString()
        user.password = passwordEditText?.editText?.text.toString()

        ExchangeService.exchangeApi().authenticate(user).enqueue(object : Callback<Token> {
            override fun onFailure(call: Call<Token>, t: Throwable) {
                Log.d("mytag", t.stackTraceToString())
                val imm: InputMethodManager =
                    activity!!.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view!!.windowToken, 0)
                Snackbar.make(
                    view as View,
                    "Login Failed.",
                    Snackbar.LENGTH_LONG
                )
                    .show()
            }

            override fun onResponse(
                call: Call<Token>, response:
                Response<Token>
            ) {
                if (response.code() != 200) {
                    val imm: InputMethodManager =
                        activity!!.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(view!!.windowToken, 0)
                    Snackbar.make(
                        submitButton as View,
                        "Login Failed.",
                        Snackbar.LENGTH_LONG
                    )
                        .show()
                } else {
                    Snackbar.make(
                        submitButton as View,
                        "Login Successful.",
                        Snackbar.LENGTH_LONG
                    )
                        .show()
                    response.body()?.token?.let {
                        Authentication.saveToken(it)
                    }
                    onCompleted()
                }
            }
        })
    }

    private fun onCompleted() {
        val intent = Intent(activity, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}