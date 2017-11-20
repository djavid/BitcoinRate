package com.djavid.bitcoinrate;

import android.util.Log;

import com.djavid.bitcoinrate.model.DataRepository;
import com.djavid.bitcoinrate.model.RestDataRepository;
import com.djavid.bitcoinrate.util.RxUtils;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import retrofit2.HttpException;
import retrofit2.Response;
import retrofit2.http.HTTP;

import static android.content.ContentValues.TAG;


public class FirebaseManager extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.

        sendTokenToServer(refreshedToken);

    }

    private void sendTokenToServer(String token) {
        DataRepository dataRepository = new RestDataRepository();

        long id;
        if (App.getAppInstance().getPrefencesWrapper().sharedPreferences.contains("token_id")) {
            id = App.getAppInstance().getPrefencesWrapper().sharedPreferences.getLong("token_id", 0);
        } else {
            id = 0;
        }

        dataRepository.registerToken(token, id)
                .compose(RxUtils.applySingleSchedulers())
                .subscribe(response -> {

                    if (response.error.isEmpty()) {

                        if (response.id != 0) {
                            App.getAppInstance()
                                    .getPrefencesWrapper()
                                    .sharedPreferences
                                    .edit()
                                    .putLong("token_id", response.id)
                                    .apply();

                            App.getAppInstance()
                                    .getPrefencesWrapper()
                                    .sharedPreferences
                                    .edit()
                                    .putString("token", token)
                                    .apply();
                        }
                    }
                }, error -> {

                });
    }
}
