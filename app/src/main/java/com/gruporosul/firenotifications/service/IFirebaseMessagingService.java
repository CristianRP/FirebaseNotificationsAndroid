package com.gruporosul.firenotifications.service;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.gruporosul.firenotifications.activity.MainActivity;
import com.gruporosul.firenotifications.app.Config;
import com.gruporosul.firenotifications.util.NotificationUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Cristian Ramírez on 6/10/2016.
 * Grupo Rosul
 * cristianramirezgt@gmail.com
 */


public class IFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = IFirebaseMessagingService.class.getSimpleName();

    private NotificationUtils mNotificationUtils;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage == null)
            return;

        // Verifica si el mensaje tiene un payload
        if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
            handleNotification(remoteMessage.getNotification().getBody());
        }

        // Verifica si el mensaje contiene datos en el payload
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());

            try {
                JSONObject json = new JSONObject(remoteMessage.getData().toString());
                handleDataMessage(json);
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }
    }

    private void handleNotification(String message) {
        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            // La app esta en foreground, transmite el mensaje
            Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
            pushNotification.putExtra("message", message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

            // reproducir sonito de notificacion
            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            notificationUtils.playNotificationSound();
        } else {
            // Si la app esta en background, firebase maneja por si mismo la notificacion
        }
    }

    private void handleDataMessage(JSONObject json) {
        Log.e(TAG, "push json: " + json.toString());

        try {
            JSONObject data = json.getJSONObject("data");

            String title = data.getString("title");
            String message = data.getString("message");
            boolean isBackground = data.getBoolean("is_background");
            String imageUrl = data.getString("image");
            String timestamp = data.getString("timestamp");
            JSONObject payload = data.getJSONObject("payload");

            Log.e(TAG, "title: " + title);
            Log.e(TAG, "message: " + message);
            Log.e(TAG, "isBackground: " + isBackground);
            Log.e(TAG, "payload: " + payload.toString());
            Log.e(TAG, "imageUrl: " + imageUrl);
            Log.e(TAG, "timestamp: " + timestamp);

            if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
                // La app esta en foreground, transmitir el mensaje
                Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
                pushNotification.putExtra("message", message);
                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

                //reproducir el sonido de notificaion
                NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                notificationUtils.playNotificationSound();
            } else {
                // La app esta en background, muestra la notificacion en la bandeja
                Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);
                resultIntent.putExtra("message", message);

                // verifica si tiene un adjunto de imagen
                if (TextUtils.isEmpty(imageUrl)) {
                    showNotificationMessage(getApplicationContext(), title,
                            message, timestamp, resultIntent);
                } else {
                    // si hay una imagen, muestra la notificacion con una imagen
                    showNotificationMessageWithBigImage(getApplicationContext(), title,
                            message, timestamp, resultIntent, imageUrl);
                }
            }
        } catch (JSONException je) {
            je.printStackTrace();
            Log.e(TAG, "JSON Exception: " + je.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.e(TAG, "Exception: " + ex.getMessage());
        }
    }

    /**
     * Muestra la notificación solo con texto
     */
    private void showNotificationMessage(Context context, String title,
                                         String message, String timeStamp, Intent intent) {
        mNotificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        mNotificationUtils.showNotificationMessage(title, message, timeStamp, intent);
    }

    /**
     * Muestra la notificación con texto e imagen
     */
    private void showNotificationMessageWithBigImage(Context context, String title,
                                                     String message, String timeStamp,
                                                     Intent intent, String imageUrl) {
        mNotificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        mNotificationUtils.showNotificationMessage(title, message, timeStamp, intent, imageUrl);
    }

}
