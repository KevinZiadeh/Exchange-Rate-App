package com.kmz07.currencyexchange.api

import com.google.gson.GsonBuilder
import com.kmz07.currencyexchange.api.model.*
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

object ExchangeService {
    //    private const val API_URL: String = "http://192.168.1.204:5000"
    private const val API_URL: String = "http://10.0.2.2:5000"

    //    private const val API_URL: String = "http://192.168.193.162:5000"
    fun exchangeApi(): Exchange {
        val gson = GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create()
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(API_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        // Create an instance of our ExchangeUserFragment API interface.
        return retrofit.create(Exchange::class.java)
    }

    interface Exchange {
        @GET("/exchangeRate")
        fun getExchangeRates(): Call<ExchangeRates>

        @POST("/user")
        fun addUser(@Body credentials: User): Call<User>

        @POST("/authentication")
        fun authenticate(@Body credentials: User): Call<Token>

        @POST("/transaction")
        fun addTransaction(
            @Body transaction: Transaction,
            @Header("Authorization") authorization: String?
        ): Call<Any>

        @GET("/transaction")
        fun getTransactions(@Header("Authorization") authorization: String):
                Call<List<Transaction>>

        @GET("/graph")
        fun getGraphs(): Call<Graph>

        @GET("/statistics")
        fun getStatistics(): Call<Statistics>

        @GET("/exchangeuser")
        fun getExchangeUser(@Header("Authorization") authorization: String): Call<UserExchanges>

        @POST("/exchangeuser")
        fun addUserTransaction(
            @Body transaction: Transaction,
            @Header("Authorization") authorization: String?
        ): Call<Any>


    }
}


class retrofit {

}