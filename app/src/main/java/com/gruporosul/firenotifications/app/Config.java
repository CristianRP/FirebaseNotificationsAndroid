package com.gruporosul.firenotifications.app;

/**
 * Created by Cristian Ramírez on 6/10/2016.
 * Grupo Rosul
 * cristianramirezgt@gmail.com
 */


public class Config {
    // tema globar para recibir notificaciones
    public static final String TOPIC_GLOBAL = "global";

    // broadcast receiver intent filters
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final String PUSH_NOTIFICATION = "pushNotification";

    // id para manejar la notificación en la bandeja
    public static final int NOTIFICATION_ID = 100;
    public static final int NOTIFICATION_ID_BIG_IMAGE = 101;

    public static final String SHARED_PREF = "fire_notifications";
}
