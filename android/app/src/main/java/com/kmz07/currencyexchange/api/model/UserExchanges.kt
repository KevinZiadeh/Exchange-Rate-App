package com.kmz07.currencyexchange.api.model

import com.google.gson.annotations.SerializedName
class UserExchanges {
    @SerializedName("give")
    var giveList: List<Transaction>? = null
    @SerializedName("receive")
    var receiveList: List<Transaction>? = null
}
