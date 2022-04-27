package com.kmz07.currencyexchange.ui.exchange_user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ExchangeUserViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is exchange with user Fragment"
    }
    val text: LiveData<String> = _text
}