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
    @SerializedName("receiver_id")
    Integer receiverId;
    @SerializedName("receiver_username")
    String receiverUsername;

    public Transaction(Float usdAmount, Float lbpAmount, Boolean usdToLbp, String receiverUsername)
    {
        this.usdAmount = usdAmount;
        this.lbpAmount = lbpAmount;
        this.usdToLbp = usdToLbp;
        this.receiverUsername = receiverUsername;
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


    public Integer getReceiverId() {
        return receiverId;
    }

    public String getReceiverUsername() {
        return receiverUsername;
    }
}
