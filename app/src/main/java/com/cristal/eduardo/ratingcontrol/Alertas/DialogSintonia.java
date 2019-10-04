package com.cristal.eduardo.ratingcontrol.Alertas;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;

import com.cristal.eduardo.ratingcontrol.R;

public class DialogSintonia extends DialogFragment {

    @Override
    public Dialog onCreateDialog( Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setIcon(R.mipmap.icon_usuario)
                .setTitle("Emision Correcta")
                .setMessage("Digite el canal en el que se encuentra el televisor, seguido de la tecla enter")
                .setPositiveButton("Entendido", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        return builder.create();
    }
}
