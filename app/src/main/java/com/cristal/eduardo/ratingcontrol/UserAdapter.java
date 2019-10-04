package com.cristal.eduardo.ratingcontrol;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.HolderMaterias> {

    //Creamos una variable global para poder instanciarla
    private static final String IP_ELIMINAR = "http://www.electronica.umsa.bo/RatingControl/webservice/appeliminar.php";
    private List<UserAtributos> atributosList;
    private Context context;
    private RequestQueue mRequest;
    private VolleyRP volley;

    //Creamos la lista de la recycler view
    public UserAdapter(List<UserAtributos> atributosList, Context context){
        this.atributosList = atributosList;
        this.context = context;
    }

    @NonNull
    @Override
    public HolderMaterias onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_user,parent,false);
        return new UserAdapter.HolderMaterias(v);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderMaterias holderMaterias, final int position) {
        volley = VolleyRP.getInstance(this.context);
        mRequest = this.volley.getRequestQueue();
        holderMaterias.imageView.setImageResource(atributosList.get(position).getFoto());
        holderMaterias.nombre.setText(atributosList.get(position).getNombre());
        holderMaterias.parentezco.setText(atributosList.get(position).getParentezco());
        holderMaterias.age.setText(atributosList.get(position).getAge());

        holderMaterias.sexo.setText(atributosList.get(position).getSexo());
        holderMaterias.idmodificar.setText(atributosList.get(position).getIdmodificar());
        holderMaterias.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "La posicion de la tarjeta es "+atributosList.get(position).getNombre(), Toast.LENGTH_SHORT).show();

            }
        });

        holderMaterias.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                String var1 = Preferences.obtenerPreferenceString(UserAdapter.this.context, Preferences.PREFERENCE_USUARIO_LOGIN);
                String var2 = Preferences.obtenerPreferenceString(UserAdapter.this.context, Preferences.PREFERENCE_PASSWORD_LOGIN);
                Eliminar(atributosList.get(position).getNombre(),atributosList.get(position).getParentezco(),var1,var2);
                Intent i = new Intent(context,MyMember.class);
                context.startActivity(i);
                return true;
            }
        });
        holderMaterias.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String var1 = atributosList.get(position).getNombre();
                String var2 = atributosList.get(position).getParentezco();
                String var3 = atributosList.get(position).getAge();
                String var4 = atributosList.get(position).getSexo();
                String var5 = atributosList.get(position).getIdmodificar();

                Intent i = new Intent(context, Modificar.class);
                i.putExtra("nombre", var1);
                i.putExtra("parent", var2);
                i.putExtra("edad", var3);
                i.putExtra("sexo", var4);
                i.putExtra("idusuario", var5);
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return atributosList.size();
    }
    static class HolderMaterias extends RecyclerView.ViewHolder{

        CardView cardView;
        ImageView imageView;
        TextView nombre;
        TextView parentezco;
        TextView age;
        TextView sexo;
        TextView idmodificar;

        public HolderMaterias(@NonNull View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.CardViewUser);
            imageView = (ImageView) itemView.findViewById(R.id.fotouser);
            nombre = (TextView) itemView.findViewById(R.id.nombre_user);
            parentezco = (TextView) itemView.findViewById(R.id.idparentezco);
            age = (TextView) itemView.findViewById(R.id.idage);

            sexo = (TextView) itemView.findViewById(R.id.idsexo);
            idmodificar = (TextView) itemView.findViewById(R.id.idmodificar);
        }
    }

    public void Eliminar(String name, String parent, String family, String pass){
        HashMap<String, String> hashMapToken = new HashMap();
        hashMapToken.put("nombre", name);
        hashMapToken.put("parent", parent);
        hashMapToken.put("nfamilia", family);
        hashMapToken.put("pass", pass);
        final JsonObjectRequest solicitud = new JsonObjectRequest(Request.Method.POST,IP_ELIMINAR,new JSONObject(hashMapToken), new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject datos) {
                try{
                    String estado = datos.getString("respuesta");

                    if (estado.equals("eliminado")) {
                        Toast.makeText(context, "Se elimino correctamente", Toast.LENGTH_SHORT).show();
                        UserAdapter.this.context.startActivity(new Intent(UserAdapter.this.context, MyMember.class));
                    } else if (estado.equals("incorrecto")) {
                        Toast.makeText(context, "No se puedo eliminar intente mas tarde", Toast.LENGTH_SHORT).show();
                        //Toast.makeText(MateriasAdapter.this.context, "VERIGFIQUE NUEVAMENTE", 0).show();
                    }
                }catch(JSONException e){
                    //Toast.makeText(MyRegisterMember.this, "Problemas de error", Toast.LENGTH_SHORT).show();
                    Toast.makeText(context, "Problemas error", Toast.LENGTH_SHORT).show();
                }
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(MyRegisterMember.this, "Intente mas tarde", Toast.LENGTH_SHORT).show();
                //Toast.makeText(MyRegister.this,"No se pudo autentificar Internet Inestable",Toast.LENGTH_SHORT).show();
                Toast.makeText(context, "Problemas internos intente mas tarde", Toast.LENGTH_SHORT).show();
            }
        });
        VolleyRP.addToQueue(solicitud,mRequest,context,volley);
    }
}
