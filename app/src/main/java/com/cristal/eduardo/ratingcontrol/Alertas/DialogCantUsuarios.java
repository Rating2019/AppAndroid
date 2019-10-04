package com.cristal.eduardo.ratingcontrol.Alertas;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.widget.Toast;

import com.cristal.eduardo.ratingcontrol.MyControl;
import com.cristal.eduardo.ratingcontrol.MyMember;
import com.cristal.eduardo.ratingcontrol.R;

public class DialogCantUsuarios extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setIcon(R.mipmap.icon_usuario)
                .setTitle("CONFIRMACION DE USUARIOS ACTIVOS")
                .setMessage("¿Siguen viendo el contenido, los mismos usuarios?")
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getContext(), MyControl.class);
                        startActivity(intent);
                        Toast.makeText(getContext(), "Gracias, siga disfrutando de la señal", Toast.LENGTH_LONG).show();
                        dialog.cancel();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        Toast.makeText(getContext(), "Porfavor elimine los integrantes que no se encuentren presentes", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getContext(), MyMember.class);
                        startActivity(intent);
                    }
                });
        return builder.create();
    }
}
