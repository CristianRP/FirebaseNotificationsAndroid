package com.gruporosul.firenotifications.service;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.gruporosul.firenotifications.app.Config;

/**
 * Created by Cristian Ramírez on 6/10/2016.
 * Grupo Rosul
 * cristianramirezgt@gmail.com
 */


public class IFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = IFirebaseInstanceIDService.class.getSimpleName();

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        storeRegIdInPrefManager(refreshedToken);

        sendRegIdToServer(refreshedToken);

        /**
         * Notificar al UI, que el registro esta completado,
         * entonces la progressbar se esconde
         */
        Intent registrationComplete = new Intent(Config.REGISTRATION_COMPLETE);
        registrationComplete.putExtra("token", refreshedToken);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    /**
     * Enviar el reg id al servidor
     * @param token
     */
    private void sendRegIdToServer(final String token) {
        Log.e(TAG, "sendRegistrationToServer: " + token);
    }

    /**
     * Almacenar el reg id en el {@link android.content.SharedPreferences}
     * @param token
     */
    private void storeRegIdInPrefManager(String token) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("regId", token);
        editor.commit();
    }
}
