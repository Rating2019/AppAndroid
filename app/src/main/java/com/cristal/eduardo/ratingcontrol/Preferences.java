package com.cristal.eduardo.ratingcontrol;

import android.content.Context;
import android.content.SharedPreferences;

public class Preferences {


    public static final String PREFERENCE_EMAIL = "email.login";
    public static final String PREFERENCE_PASSWORD_LOGIN = "password.login";
    public static final String PREFERENCE_USUARIO_LOGIN = "usuario.login";
    public static final String PREFERENCE_USUARIOS_ACTIVOS = "usuarios.login";
    public static final String PREFERENCE_SERVIDOR = "servidor.login";
    public static final String PREFERENCE_CANAL = "Canal.login";
    public static final String PREFERENCE_PERMANENCIA = "permanencia.login";
    public static final String PREFERENCE_e = "e.login";
    public static final String PREFERENCE_f = "f.login";
    public static final String PREFERENCE_g = "g.login";
    public static final String PREFERENCE_h = "h.login";
    public static final String PREFERENCE_i = "i.login";
    public static final String PREFERENCE_j = "j.login";
    public static final String PREFERENCE_k = "k.login";
    public static final String STRING_PREFERENCES = "michattimereal.Mensajes.Mensajeria";

    public static void savePreferenceBoolean(Context c, boolean b,String key){
        SharedPreferences preferences = c.getSharedPreferences(STRING_PREFERENCES,c.MODE_PRIVATE);
        preferences.edit().putBoolean(key,b).apply();
    }

    public static void savePreferenceString(Context c, String b, String key){
        SharedPreferences preferences = c.getSharedPreferences(STRING_PREFERENCES,c.MODE_PRIVATE);
        preferences.edit().putString(key,b).apply();
    }

    public static boolean obtenerPreferenceBoolean(Context c,String key){
        SharedPreferences preferences = c.getSharedPreferences(STRING_PREFERENCES,c.MODE_PRIVATE);
        return preferences.getBoolean(key,false);//Si es que nunca se ha guardado nada en esta key pues retornara false
    }

    public static String obtenerPreferenceString(Context c,String key){
        SharedPreferences preferences = c.getSharedPreferences(STRING_PREFERENCES,c.MODE_PRIVATE);
        return preferences.getString(key,"");//Si es que nunca se ha guardado nada en esta key pues retornara una cadena vacia
    }

}