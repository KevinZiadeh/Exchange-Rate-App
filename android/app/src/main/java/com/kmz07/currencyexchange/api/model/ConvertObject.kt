package com.kmz07.currencyexchange.api.model

import com.google.gson.annotations.SerializedName
import java.sql.Date
import java.sql.Timestamp
import java.text.DateFormat
import java.util.*

class ConvertObject {
    @SerializedName("usd_amount")
    var usdAmount: Float? = null
    @SerializedName("lbp_amount")
    var lbpAmount: Float? = null
    @SerializedName("usd_to_lbp")
    var usdToLbp: Boolean? = null

    constructor(usdAmount: Float?, lbpAmount: Float?, usdToLbp: Boolean){
        this.usdAmount = usdAmount
        this.lbpAmount = lbpAmount
        this.usdToLbp = usdToLbp
    }

}