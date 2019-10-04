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

public class MyRegisterMember extends AppCompatActivity {

    private static final String IP_LOGIN_FAMILIA = "http://www.electronica.umsa.bo/RatingControl/webservice/appusuarios.php";
    private EditText correo;
    private EditText edad;
    private Button inisesion;
    private EditText nombre;
    private EditText parentezco;
    private Button regintegrante;
    private EditText sexo;

    private RequestQueue mRequest;
    private VolleyRP volley;
    private String variablebloqueo = "true";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_register_member);

        volley = VolleyRP.getInstance(this);
        mRequest = volley.getRequestQueue();

        correo = (EditText) findViewById(R.id.txtmail1);
        nombre = (EditText) findViewById(R.id.txtname1);
        parentezco = (EditText) findViewById(R.id.txtparent1);
        edad = (EditText) findViewById(R.id.txtage1);
        sexo = (EditText) findViewById(R.id.txtsexo1);
        inisesion = (Button) findViewById(R.id.btnsesion1);
        regintegrante = (Button) findViewById(R.id.btnintegrante1) ;
        String variable3 = Preferences.obtenerPreferenceString(MyRegisterMember.this, Preferences.PREFERENCE_EMAIL);
        correo.setText(variable3);
        correo.setFocusable(false);
        variablebloqueo = getIntent().getStringExtra("keybloqueador");
        //Toast.makeText(this, variablebloqueo, Toast.LENGTH_SHORT).show();
        if (variablebloqueo.equals("false")) {
            inisesion.setVisibility(View.GONE);
        }
        regintegrante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String variable1 = Preferences.obtenerPreferenceString(MyRegisterMember.this, Preferences.PREFERENCE_USUARIO_LOGIN);
                String variable2 = Preferences.obtenerPreferenceString(MyRegisterMember.this, Preferences.PREFERENCE_PASSWORD_LOGIN);
                String variable3 = Preferences.obtenerPreferenceString(MyRegisterMember.this, Preferences.PREFERENCE_EMAIL);


                String var2 =nombre.getText().toString();
                String var3 = parentezco.getText().toString();
                String var4 = edad.getText().toString();
                String var5 = sexo.getText().toString();
                Registrarfamilia(variable3, var2, var3, var4, var5, variable1, variable2);

            }
        });
        inisesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MyRegisterMember.this, MyConnection.class);
                startActivity(i);
            }
        });
    }

    public void Registrarfamilia(String email, String dir, String tel, String ns, String user, String password, String contra){
        HashMap<String, String> hashMapToken = new HashMap();
        hashMapToken.put("email", email);
        hashMapToken.put("nombre", dir);
        hashMapToken.put("parent", tel);
        hashMapToken.put("edad", ns);
        hashMapToken.put("sexo", user);
        hashMapToken.put("namefamilia", password);
        hashMapToken.put("contrasena", contra);
        final JsonObjectRequest solicitud = new JsonObjectRequest(Request.Method.POST,IP_LOGIN_FAMILIA,new JSONObject(hashMapToken), new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject datos) {
                try{
                    String estado = datos.getString("respuesta");
                    if (estado.equals("Usuario Registrado")) {
                        Toast.makeText(MyRegisterMember.this, "Se registro correctamente", Toast.LENGTH_SHORT).show();
                        borrar();
                        if (variablebloqueo.equals("false")) {
                            Intent i = new Intent(MyRegisterMember.this, MyMember.class);
                            MyRegisterMember.this.startActivity(i);
                        }
                        Toast.makeText(MyRegisterMember.this, "Introduzca nuevo usuario", Toast.LENGTH_SHORT).show();
                    } else if (estado.equals("incorrecto")) {
                        Toast.makeText(MyRegisterMember.this, "Datos incorrectos", Toast.LENGTH_SHORT).show();
                    }
                }catch(JSONException e){
                    Toast.makeText(MyRegisterMember.this, "Problemas de error", Toast.LENGTH_SHORT).show();

                }
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MyRegisterMember.this, "Intente mas tarde", Toast.LENGTH_SHORT).show();
                //Toast.makeText(MyRegister.this,"No se pudo autentificar Internet Inestable",Toast.LENGTH_SHORT).show();
            }
        });
        VolleyRP.addToQueue(solicitud,mRequest,this,volley);
    }

    public void borrar() {
        nombre.setText("");
        parentezco.setText("");
        edad.setText("");
        sexo.setText("");
    }
}
