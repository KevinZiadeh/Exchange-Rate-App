package com.youssef12347.exchange.api.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Interactions {
    @SerializedName("give")
    public List<Transaction> giveList;
    @SerializedName("receive")
    public List<Transaction> receiveList;

}