package com.kmz07.currencyexchange.api.model

import com.google.gson.annotations.SerializedName

class Graph {
    @SerializedName("date")
    var graph_dates: ArrayList<String>? = null
    @SerializedName("mean_rate_lbp_to_usd")
    var mean_rate_lbp_to_usd: ArrayList<Float>? = null
    @SerializedName("mean_rate_usd_to_lbp")
    var mean_rate_usd_to_lbp: ArrayList<Float>? = null
}