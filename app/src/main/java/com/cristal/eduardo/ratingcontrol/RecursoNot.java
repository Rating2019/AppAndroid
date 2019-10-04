package com.cristal.eduardo.ratingcontrol;

import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.cristal.eduardo.ratingcontrol.Alertas.DialogPermanecia;

public class RecursoNot extends AppCompatActivity {

    private Button botPer, botUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recurso_not);

        botPer = (Button)findViewById(R.id.buttonp);
        botUser = (Button)findViewById(R.id.buttonu);

        botPer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RecursoNot.this, MyControl.class);
                startActivity(i);
            }
        });

        botUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecursoNot.this, MyMember.class);
                startActivity(intent);

            }
        });


    }
}
