package com.kmz07.currencyexchange.api

import android.content.Context
import android.content.SharedPreferences
import com.kmz07.currencyexchange.ui.calculator.CalculatorFragment
import com.kmz07.currencyexchange.ui.exchange.ExchangeFragment
import com.kmz07.currencyexchange.ui.login.LoginFragment
import com.kmz07.currencyexchange.ui.register.RegisterFragment
import com.kmz07.currencyexchange.ui.statistics.StatisticsFragment
import com.kmz07.currencyexchange.ui.transactions.TransactionsFragment

object Authentication {
    private var token: String? = null
    private var preferences: SharedPreferences? = null
    public fun initialize(context: Context) {
        preferences = context.getSharedPreferences(
            "SETTINGS",
            Context.MODE_PRIVATE
        )
        token = preferences?.getString("TOKEN", null)
    }

    public fun getToken(): String? {
        return token
    }

    public fun saveToken(token: String) {
        this.token = token
        preferences?.edit()?.putString("TOKEN", token)?.apply()
    }

    public fun clearToken() {
        this.token = null
        preferences?.edit()?.remove("TOKEN")?.apply()
    }
}