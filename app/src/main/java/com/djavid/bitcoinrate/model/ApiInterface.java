package com.djavid.bitcoinrate.model;

import com.djavid.bitcoinrate.model.dto.BlockchainModel;
import com.djavid.bitcoinrate.model.dto.CryptonatorTicker;
import com.djavid.bitcoinrate.model.dto.CurrenciesModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by djavid on 05.08.17.
 */

public interface ApiInterface {

    //blockchain

    @GET("https://blockchain.info/ru/charts/market-price")
    Call<BlockchainModel> getChartValues(@Query("timespan") String timespan,
                                         @Query("sampled") boolean sampled,
                                         @Query("format") String format);

    //cryptonator

    @GET("https://api.cryptonator.com/api/full/{curr1}-{curr2}")
    Call<CryptonatorTicker> getRate(@Path("curr1") String curr1, @Path("curr2") String curr2);

    @GET("https://www.cryptonator.com/api/currencies")
    Call<CurrenciesModel> getCurrencies();

}