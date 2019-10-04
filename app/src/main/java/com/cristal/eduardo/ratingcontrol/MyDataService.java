package com.cristal.eduardo.ratingcontrol;


import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class MyDataService extends JobService {

    private static final String IP_REGISTRO_DATOS = "http://www.electronica.umsa.bo/RatingControl/webservice/appdatos.php";
    private RequestQueue mRequest;
    private VolleyRP volley;

    private static final String TAG = "MyJobService";
    private boolean jobCancelled = false;

    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            try {
                new Time().execute();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    };

    @Override
    public boolean onStartJob(JobParameters params) {
        volley = VolleyRP.getInstance(this);
        mRequest = volley.getRequestQueue();
        Log.d(TAG, "Job started");

        handler.removeCallbacks(runnable);
        new Time().execute();
        return true;
    }

    public class Time extends AsyncTask <Void, Integer, Boolean>{

        @Override
        protected Boolean doInBackground(Void... voids) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    handler.postDelayed(runnable,60000);
                    Log.d(TAG, "RUN");

                    String var_e = Preferences.obtenerPreferenceString(getApplication(), Preferences.PREFERENCE_EMAIL);
                    String var_s = Preferences.obtenerPreferenceString(getApplication(), Preferences.PREFERENCE_SERVIDOR);
                    String var_u = Preferences.obtenerPreferenceString(getApplication(), Preferences.PREFERENCE_USUARIOS_ACTIVOS);

                    String var1 = Preferences.obtenerPreferenceString(getApplication(), Preferences.PREFERENCE_CANAL);
                    String var2 = Preferences.obtenerPreferenceString(getApplication(), Preferences.PREFERENCE_PERMANENCIA);

                    Registrardatos(var_e, var_s, var_u, var1, var2);
                    if (jobCancelled) {
                        return;
                    }
                }
            }).start();
            return true;
        }
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        handler.removeCallbacks(runnable);
        Log.d(TAG, "Job cancelled before completion");
        jobCancelled = true;
        return true;
    }

    public void Registrardatos(String email, String emp, String usuarios, final String canal, final String duracion){
        HashMap<String, String> hashMapToken = new HashMap();
        hashMapToken.put("email", email);
        hashMapToken.put("empresa", emp);
        hashMapToken.put("nombre", usuarios);
        hashMapToken.put("canal", canal);
        hashMapToken.put("tiempo_perm", duracion);

        final JsonObjectRequest solicitud = new JsonObjectRequest(Request.Method.POST,IP_REGISTRO_DATOS,new JSONObject(hashMapToken), new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject datos) {
                try{
                    String estado = datos.getString("respuesta");
                    Toast.makeText(getApplicationContext(), estado, Toast.LENGTH_SHORT).show();
                    if (estado.equals("Dato Correcto")) {
                        Toast.makeText(getApplicationContext(), "Dato Registrado", Toast.LENGTH_SHORT).show();

                    } else if (estado.equals("incorrecto")) {
                        Toast.makeText(getApplicationContext(), "Dato No registrado", Toast.LENGTH_SHORT).show();
                    }
                }catch(JSONException e){

                    Toast.makeText(getApplicationContext(), "Problema de Servicio Intente mas tarde", Toast.LENGTH_SHORT).show();
                }
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"No se pudo autentificar Internet Inestable",Toast.LENGTH_SHORT).show();
            }
        });
        VolleyRP.addToQueue(solicitud,mRequest,this,volley);
    }
}
