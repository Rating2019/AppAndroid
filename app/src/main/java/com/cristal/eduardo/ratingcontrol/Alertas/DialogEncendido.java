package com.cristal.eduardo.ratingcontrol.Alertas;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.widget.Toast;

import com.cristal.eduardo.ratingcontrol.R;

public class DialogEncendido extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setIcon(R.mipmap.icon_usuario)
                .setTitle("ESTADO DEL TELEVISOR")
                .setMessage("¿Se encuentra encendido el televisor?")
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FragmentManager fragmentManager = getFragmentManager();
                        DialogFragment dialogFragment = new DialogSintonia();
                        dialogFragment.show(fragmentManager, "tagSintonia");
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getContext(), "Porfavor encienda el telvisor", Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                    }
                });
        return builder.create();
    }
}
