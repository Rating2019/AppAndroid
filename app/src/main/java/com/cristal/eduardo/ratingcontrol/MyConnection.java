package com.cristal.eduardo.ratingcontrol;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class MyConnection extends AppCompatActivity {

    private static final String IP_LOGIN = "http://www.electronica.umsa.bo/RatingControl/webservice/applogin.php";
    private Button btnRegistrar;
    private Button btnSesion;
    private RequestQueue mRequest;
    private VolleyRP volley;
    private EditText txtFamilia;
    private EditText txtPass;
    private String var1;
    private String var2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_connection);
        txtFamilia = (EditText) findViewById(R.id.txtfamily);
        txtPass = (EditText) findViewById(R.id.txtpass);
        btnSesion = (Button) findViewById(R.id.btnsesion);
        btnRegistrar = (Button) findViewById(R.id.btnregistro);
        volley = VolleyRP.getInstance(this);
        mRequest = volley.getRequestQueue();

        btnSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String var1=txtFamilia.getText().toString();
                String var2=txtPass.getText().toString();
                Logfinal(var1,var2);
            }
        });

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MyConnection.this, MyRegistry.class);
                startActivity(i);
            }
        });
    }


    public void Logfinal(final String user, final String password){
        HashMap<String, String> hashMapToken = new HashMap();
        hashMapToken.put("nfamilia", user);
        hashMapToken.put("pass", password);
        final JsonObjectRequest solicitud = new JsonObjectRequest(Request.Method.POST,IP_LOGIN,new JSONObject(hashMapToken), new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject datos) {
                try{
                    String estado = datos.getString("respuesta");
                    String correo = datos.getString("email");
                    if (estado.equals("incorrecto")) {
                        Toast.makeText(MyConnection.this, "Verifique nuevamente", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(MyConnection.this, "Bienvenido", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(MyConnection.this, MyMember.class);
                        i.putExtra("familia", user);
                        i.putExtra("pass", password);
                        i.putExtra("recargar", "false");
                        Preferences.savePreferenceString(MyConnection.this, correo, Preferences.PREFERENCE_EMAIL);
                        Preferences.savePreferenceString(MyConnection.this, user, Preferences.PREFERENCE_USUARIO_LOGIN);
                        Preferences.savePreferenceString(MyConnection.this, password, Preferences.PREFERENCE_PASSWORD_LOGIN);
                        MyConnection.this.startActivity(i);
                    }
                }catch(JSONException e){

                    Toast.makeText(MyConnection.this, "Problema de Servicio Intente mas tarde", Toast.LENGTH_SHORT).show();
                }
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MyConnection.this,"No se pudo autentificar Internet Inestable",Toast.LENGTH_SHORT).show();
            }
        });
        VolleyRP.addToQueue(solicitud,mRequest,this,volley);
    }

    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
