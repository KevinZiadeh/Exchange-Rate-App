package com.youssef12347.exchange.api.model;

import com.google.gson.annotations.SerializedName;
public class Statistics {
    @SerializedName("max_lbp_to_usd")
    public Float max_lbp_to_usd;
    @SerializedName("max_usd_to_lbp")
    public Float max_usd_to_lbp;
    @SerializedName("median_lbp_to_usd")
    public Float median_lbp_to_usd;
    @SerializedName("median_usd_to_lbp")
    public Float median_usd_to_lbp;
    @SerializedName("min_lbp_to_usd")
    public Float min_lbp_to_usd;
    @SerializedName("min_usd_to_lbp")
    public Float min_usd_to_lbp;
    @SerializedName("mode_lbp_to_usd")
    public Float mode_lbp_to_usd;
    @SerializedName("mode_usd_to_lbp")
    public Float mode_usd_to_lbp;
    @SerializedName("predicted_lbp_to_usd")
    public Float predicted_lbp_to_usd;
    @SerializedName("predicted_usd_to_lbp")
    public Float predicted_usd_to_lbp;
    @SerializedName("std_lbp_to_usd")
    public Float std_lbp_to_usd;
    @SerializedName("std_usd_to_lbp")
    public Float std_usd_to_lbp;
}