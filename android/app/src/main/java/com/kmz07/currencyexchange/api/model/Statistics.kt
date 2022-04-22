package com.kmz07.currencyexchange.api.model

import com.google.gson.annotations.SerializedName

class Statistics {
    @SerializedName("max_lbp_to_usd")
    var max_lbp_to_usd: Float? = null
    @SerializedName("max_usd_to_lbp")
    var max_usd_to_lbp: Float? = null
    @SerializedName("median_lbp_to_usd")
    var median_lbp_to_usd: Float? = null
    @SerializedName("median_usd_to_lbp")
    var median_usd_to_lbp: Float? = null
    @SerializedName("min_lbp_to_usd")
    var min_lbp_to_usd: Float? = null
    @SerializedName("min_usd_to_lbp")
    var min_usd_to_lbp: Float? = null
    @SerializedName("mode_lbp_to_usd")
    var mode_lbp_to_usd: Float? = null
    @SerializedName("mode_usd_to_lbp")
    var mode_usd_to_lbp: Float? = null
    @SerializedName("predicted_lbp_to_usd")
    var predicted_lbp_to_usd: Float? = null
    @SerializedName("predicted_usd_to_lbp")
    var predicted_usd_to_lbp: Float? = null
    @SerializedName("std_lbp_to_usd")
    var std_lbp_to_usd: Float? = null
    @SerializedName("std_usd_to_lbp")
    var std_usd_to_lbp: Float? = null

}