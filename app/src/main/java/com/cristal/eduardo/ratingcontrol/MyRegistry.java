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

public class MyRegistry extends AppCompatActivity {

    private static final String IP_LOGIN_FINAL = "http://www.electronica.umsa.bo/RatingControl/webservice/applogin.php";
    private static final String IP_REGISTRO = "http://www.electronica.umsa.bo/RatingControl/webservice/appregistro.php";
    private RequestQueue mRequest;
    private VolleyRP volley;
    private EditText txtDirec;
    private EditText txtFamilia;
    private EditText txtMail;
    private EditText txtNserie;
    private EditText txtServidor;
    private EditText txtPass;
    private EditText txtTel;
    private Button btnRegistrar;
    private Button btnSesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_registry);
        volley = VolleyRP.getInstance(this);
        mRequest = volley.getRequestQueue();

        txtFamilia = (EditText) findViewById(R.id.txtfamily);
        txtDirec = (EditText) findViewById(R.id.txtadress);
        txtTel = (EditText) findViewById(R.id.txtphone);
        txtNserie = (EditText) findViewById(R.id.txtserie);
        txtServidor = (EditText) findViewById(R.id.txtservidor);
        txtMail = (EditText) findViewById(R.id.txtmail);
        txtPass = (EditText) findViewById(R.id.txtpass);
        btnSesion = (Button) findViewById(R.id.btnsesion);
        btnRegistrar = (Button) findViewById(R.id.btnregistro);

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String var1 = txtFamilia.getText().toString();
                String var2 = txtDirec.getText().toString();
                String var3 = txtTel.getText().toString();
                String var4 = txtNserie.getText().toString();
                String var5 = txtServidor.getText().toString();
                String var6 = txtMail.getText().toString();
                String var7 = txtPass.getText().toString();
                Registrarfamilia(var1,var2,var3,var4,var5,var6, var7);

                Preferences.savePreferenceString(MyRegistry.this, var6, Preferences.PREFERENCE_EMAIL);
                Preferences.savePreferenceString(MyRegistry.this, var1, Preferences.PREFERENCE_USUARIO_LOGIN);
                Preferences.savePreferenceString(MyRegistry.this, var7, Preferences.PREFERENCE_PASSWORD_LOGIN);
                Preferences.savePreferenceString(MyRegistry.this, var5, Preferences.PREFERENCE_SERVIDOR);
            }
        });

    }

    public void Registrarfamilia(final String familia, String dir, String tel, String ns, String stv, final String user, final String password){
        HashMap<String, String> hashMapToken = new HashMap();
        hashMapToken.put("nfamilia", familia);
        hashMapToken.put("direc", dir);
        hashMapToken.put("tel", tel);
        hashMapToken.put("nserie", ns);
        hashMapToken.put("empresa", stv);
        hashMapToken.put("mail", user);
        hashMapToken.put("pass", password);
        final JsonObjectRequest solicitud = new JsonObjectRequest(Request.Method.POST,IP_REGISTRO,new JSONObject(hashMapToken), new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject datos) {
                try{
                    String estado = datos.getString("respuesta");
                    if (estado.equals("Registro correcto")) {
                        Toast.makeText(MyRegistry.this, "Familia registrada", Toast.LENGTH_SHORT).show();
                        String var1 = "true";
                        Intent i = new Intent(MyRegistry.this, MyRegisterMember.class);
                        i.putExtra("keybloqueador",var1);
                        startActivity(i);
                    } else if (estado.equals("incorrecto")) {
                        Toast.makeText(MyRegistry.this, "No registrado", Toast.LENGTH_SHORT).show();
                    }
                }catch(JSONException e){

                    Toast.makeText(MyRegistry.this, "Problema de Servicio Intente mas tarde", Toast.LENGTH_SHORT).show();
                }
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MyRegistry.this,"No se pudo autentificar Internet Inestable",Toast.LENGTH_SHORT).show();
            }
        });
        VolleyRP.addToQueue(solicitud,mRequest,this,volley);
    }
}
