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

public class Modificar extends AppCompatActivity {

    private String IP_MODIFICAR = "http://www.electronica.umsa.bo/RatingControl/webservice/appeditar.php";
    private EditText correo;
    private EditText edad;
    private RequestQueue mRequest;
    private Button modificar;
    private EditText nombre;
    private EditText parentezco;
    private EditText sexo;
    private VolleyRP volley;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar);
        modificar = (Button) findViewById(R.id.btnmodificar);
        correo = (EditText) findViewById(R.id.txtmail11);
        nombre = (EditText) findViewById(R.id.txtname111);
        edad = (EditText) findViewById(R.id.txtage111);
        sexo = (EditText) findViewById(R.id.txtsexo111);
        parentezco = (EditText) findViewById(R.id.txtparent111);
        volley = VolleyRP.getInstance(this);
        mRequest = volley.getRequestQueue();

        String var1 = Preferences.obtenerPreferenceString(Modificar.this, Preferences.PREFERENCE_USUARIO_LOGIN);
        String var2 = Preferences.obtenerPreferenceString(Modificar.this, Preferences.PREFERENCE_PASSWORD_LOGIN);
        String var3 = Preferences.obtenerPreferenceString(Modificar.this, Preferences.PREFERENCE_EMAIL);

        String varname = getIntent().getStringExtra("nombre");
        String varparent = getIntent().getStringExtra("parent");
        String varedad = getIntent().getStringExtra("edad");
        String varsexo = getIntent().getStringExtra("sexo");
        String varid = getIntent().getStringExtra("idusuario");
        correo.setText(var3);
        correo.setFocusable(false);
        parentezco.setText(varparent);
        nombre.setText(varname);
        edad.setText(varedad);
        sexo.setText(varsexo);

        modificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String var1 = Preferences.obtenerPreferenceString(Modificar.this, Preferences.PREFERENCE_USUARIO_LOGIN);
                String var2 = Preferences.obtenerPreferenceString(Modificar.this, Preferences.PREFERENCE_PASSWORD_LOGIN);
                String var3 = Preferences.obtenerPreferenceString(Modificar.this, Preferences.PREFERENCE_EMAIL);

                modificarfamilia(var1,var2,var3,nombre.getText().toString(),parentezco.getText().toString(),edad.getText().toString(),sexo.getText().toString(),getIntent().getStringExtra("idusuario"));
            }
        });

    }

    public void modificarfamilia(String user, String password, String email, String nombre, String parent, String edad, String sexo, String idusuario){
        HashMap<String, String> hashMapToken = new HashMap();
        hashMapToken.put("user", user);
        hashMapToken.put("pass", password);
        hashMapToken.put("email", email);
        hashMapToken.put("name", nombre);
        hashMapToken.put("parent", parent);
        hashMapToken.put("edad", edad);
        hashMapToken.put("sexo", sexo);
        hashMapToken.put("id_usu", idusuario);
        final JsonObjectRequest solicitud = new JsonObjectRequest(Request.Method.POST,IP_MODIFICAR,new JSONObject(hashMapToken), new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject datos) {
                try{
                    String estado = datos.getString("respuesta");
                    if (estado.equals("correcto")) {
                        Toast.makeText(Modificar.this, "Se modifico correctamente", Toast.LENGTH_SHORT).show();
                        Modificar.this.startActivity(new Intent(Modificar.this, MyMember.class));
                    } else if (estado.equals("incorrecto")) {
                        Toast.makeText(Modificar.this, "Error al modificar", Toast.LENGTH_SHORT).show();
                        Modificar.this.startActivity(new Intent(Modificar.this, MyMember.class));
                    }
                }catch(JSONException e){
                    Toast.makeText(Modificar.this, "Problemas de sevicio intente mas tarde", Toast.LENGTH_SHORT).show();
                }
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Modificar.this, "No se puede autentificar intente mas tarde", Toast.LENGTH_SHORT).show();
            }
        });
        VolleyRP.addToQueue(solicitud,mRequest,this,volley);
    }
}
