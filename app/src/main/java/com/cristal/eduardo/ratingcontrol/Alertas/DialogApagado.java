package com.cristal.eduardo.ratingcontrol.Alertas;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.widget.Toast;

import com.cristal.eduardo.ratingcontrol.MyConnection;
import com.cristal.eduardo.ratingcontrol.MyControl;
import com.cristal.eduardo.ratingcontrol.R;

public class DialogApagado extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setIcon(R.mipmap.icon_usuario)
                .setTitle("CONFIRMACION DE CIERRE")
                .setMessage("¿Confirma que desea salir del sistema?")
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getContext(), MyConnection.class);
                        startActivity(intent);
                        dialog.cancel();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getContext(), MyControl.class);
                        startActivity(intent);
                        Toast.makeText(getContext(), "Por favor, seleccione el canal sintonizado", Toast.LENGTH_LONG).show();
                        dialog.cancel();
                    }
                });
        return builder.create();
    }
}
