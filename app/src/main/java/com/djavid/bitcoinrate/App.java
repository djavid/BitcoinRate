package com.djavid.bitcoinrate;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatDelegate;

import com.djavid.bitcoinrate.model.ApiInterface;
import com.djavid.bitcoinrate.domain.PreferencesWrapper;
import com.djavid.bitcoinrate.domain.PresenterProvider;

import net.danlew.android.joda.JodaTimeAndroid;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


public class App extends Application {

    private static App appInstance;

    private ApiInterface apiInterface;
    private PresenterProvider presenterProvider;
    private SharedPreferences sharedPreferences;

    private static String SHARED_PREFERENCES_CODE = "bitcoin_rate_app";

    @Override
    public void onCreate() {
        super.onCreate();

        getPresenterProvider();
        getPrefencesWrapper();
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        JodaTimeAndroid.init(this);

        appInstance = (App) getApplicationContext();
    }

    public static Context getContext() {
        return appInstance.getApplicationContext();
    }

    public static App getAppInstance() {
        return appInstance;
    }

    public ApiInterface getApiInterface() {
        if (apiInterface == null)
            apiInterface = buildApiInterface();
        return apiInterface;
    }

    public PresenterProvider getPresenterProvider() {
        if (presenterProvider == null)
            presenterProvider = new PresenterProvider();

        return presenterProvider;
    }

    public SharedPreferences getRawPreferences() {
        return getSharedPreferences(SHARED_PREFERENCES_CODE, MODE_PRIVATE);
    }

    public PreferencesWrapper getPrefencesWrapper() {
        return new PreferencesWrapper(getRawPreferences());
    }

    private ApiInterface buildApiInterface() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClient.addInterceptor(logging);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://blockchain.info/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(httpClient.build())
                .build();

        return retrofit.create(ApiInterface.class);
    }

}