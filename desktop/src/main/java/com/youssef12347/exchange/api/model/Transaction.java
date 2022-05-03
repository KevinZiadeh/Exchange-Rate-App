package com.youssef12347.exchange.api.model;

import com.google.gson.annotations.SerializedName;

public class Transaction {
    @SerializedName("usd_amount")
    Float usdAmount;
    @SerializedName("lbp_amount")
    Float lbpAmount;
    @SerializedName("usd_to_lbp")
    Boolean usdToLbp;
    @SerializedName("id")
    Integer id;
    @SerializedName("added_date")
    String addedDate;
    @SerializedName("user_name")
    String userName;
    @SerializedName("receiver_name")
    String receiverName;

    public Transaction(Float usdAmount, Float lbpAmount, Boolean usdToLbp, String receiverName) {
        this.usdAmount = usdAmount;
        this.lbpAmount = lbpAmount;
        this.usdToLbp = usdToLbp;
        this.receiverName = receiverName;
    }

    public Float getUsdAmount() {
        return usdAmount;
    }

    public Float getLbpAmount() {
        return lbpAmount;
    }

    public Boolean getUsdToLbp() {
        return usdToLbp;
    }

    public Integer getId() {
        return id;
    }

    public String getAddedDate() {
        return addedDate;
    }


    public String getUserName() {
        return userName;
    }

    public String getReceiverName() {
        return receiverName;
    }
}
