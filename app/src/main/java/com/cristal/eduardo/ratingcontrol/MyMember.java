package com.cristal.eduardo.ratingcontrol;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyMember extends AppCompatActivity {

    private RecyclerView rv;
    private List<UserAtributos> atributosList;
    private UserAdapter adapter;
    private RequestQueue mRequest;
    private VolleyRP volley;
    private Button ingresarrating;
    private Button anadirinterno;
    private Button listas;
    private String cadena = "";
    private static final String IP_FAMILIAS = "http://www.electronica.umsa.bo/RatingControl/webservice/applistas.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_member);

        atributosList = new ArrayList<>();
        rv=(RecyclerView) findViewById(R.id.userRecyclerView);
        LinearLayoutManager lm = new LinearLayoutManager(this);
        rv.setLayoutManager(lm);
        adapter = new UserAdapter(atributosList,this);
        rv.setAdapter(adapter);
        volley = VolleyRP.getInstance(this);
        mRequest = volley.getRequestQueue();
        anadirinterno = (Button) findViewById(R.id.anadirin);
        listas = (Button) findViewById(R.id.listas);
        ingresarrating = (Button) findViewById(R.id.btnrating) ;
        String variable1 = Preferences.obtenerPreferenceString(this, Preferences.PREFERENCE_USUARIO_LOGIN);
        String variable2 = Preferences.obtenerPreferenceString(this, Preferences.PREFERENCE_PASSWORD_LOGIN);
        Pedirtabla(variable1, variable2);
        /*for (int i=0 ; i<10;i++){
            agregarMaterias(R.drawable.fondoimagen,"materiasinscritas".toUpperCase().replace("_"," "),"materia","VER-CHAT","tipe","modi");
        }*/
        anadirinterno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String var1 = "false";
                Intent i = new Intent(MyMember.this, MyRegisterMember.class);
                i.putExtra("keybloqueador",var1);
                startActivity(i);
            }
        });

        listas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MyMember.this, cadena, Toast.LENGTH_SHORT).show();
            }
        });

        ingresarrating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MyMember.this, "Iniciemos", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(MyMember.this, MyAccess.class);
                startActivity(i);
            }
        });

    }

    public void agregarMaterias(int fotoDeMateria,String nombre,String ultimoMensaje,String hora,String tipo,String idmodificar){
        UserAtributos materiasAtributos= new UserAtributos();
        materiasAtributos.setFoto(fotoDeMateria);
        materiasAtributos.setNombre(nombre);
        materiasAtributos.setParentezco(ultimoMensaje);
        materiasAtributos.setAge(hora);
        materiasAtributos.setSexo(tipo);
        materiasAtributos.setIdmodificar(idmodificar);
        atributosList.add(materiasAtributos);
        adapter.notifyDataSetChanged();
    }


    public void Pedirtabla(String token, String pass){
        HashMap<String, String> hashMapToken = new HashMap();
        hashMapToken.put("namefamilia", token);
        hashMapToken.put("contrasena", pass);
        final JsonObjectRequest solicitud = new JsonObjectRequest(Request.Method.POST,IP_FAMILIAS,new JSONObject(hashMapToken), new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject datos) {
                try{
                    JSONArray jsonArray = new JSONArray(datos.getString("respuesta"));
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject js = jsonArray.getJSONObject(i);
                        agregarMaterias(R.mipmap.fondo, js.getString("nombre"), js.getString("parent"), js.getString("edad"), js.getString("sexo"), js.getString("id_usu"));
                        /*nombres[i] = js.getString("nombre");
                        MyMember myMember = MyMember.this;
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append(js.getString("nombre"));
                        stringBuilder.append(";");
                        stringBuilder.append(MyMember.this.cadena);
                        myMember.cadena = stringBuilder.toString();*/
                        cadena = cadena+js.getString("nombre")+";";
                    }
                    Preferences.savePreferenceString(MyMember.this, cadena, Preferences.PREFERENCE_USUARIOS_ACTIVOS);

                }catch(JSONException e){

                    Toast.makeText(MyMember.this, "Problema de Servicio Intente mas tarde", Toast.LENGTH_SHORT).show();
                }
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MyMember.this,"No se pudo autentificar Internet Inestable",Toast.LENGTH_SHORT).show();
            }
        });
        VolleyRP.addToQueue(solicitud,mRequest,this,volley);
    }

    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, MyConnection.class));
        finish();
    }
}
