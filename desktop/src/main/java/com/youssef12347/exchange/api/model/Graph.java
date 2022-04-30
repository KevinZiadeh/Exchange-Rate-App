package com.youssef12347.exchange.api.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Graph {
    @SerializedName("date")
    public List date;
    @SerializedName("mean_rate_lbp_to_usd")
    public List mean_rate_lbp_to_usd;
    @SerializedName("mean_rate_usd_to_lbp")
    public List mean_rate_usd_to_lbp;
}
