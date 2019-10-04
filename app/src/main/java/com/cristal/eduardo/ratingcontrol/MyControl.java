package com.cristal.eduardo.ratingcontrol;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.cristal.eduardo.ratingcontrol.Alertas.DialogApagado;
import com.cristal.eduardo.ratingcontrol.Alertas.DialogEncendido;
import com.cristal.eduardo.ratingcontrol.Alertas.DialogPermanecia;
import com.cristal.eduardo.ratingcontrol.Alertas.DialogSintonia;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.UUID;

public class  MyControl extends AppCompatActivity {


    //Datos para el manejo de la comunicacion con la base de datos
    private static final String IP_REGISTRO_DATOS = "http://www.electronica.umsa.bo/RatingControl/webservice/appdatos.php";
    private RequestQueue mRequest;
    private VolleyRP volley;

    //Datos para el manejo de la comunicacion bluetooth
    private Button btn0, btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9, btnpunt, btnprech, btnent, btnsalir;
    private ImageButton btnmas, btnmenos, btnup, btndown, btnpower, btnmute;
    private TextView IdCanal, IdDuracion;

    Handler bluetoothIn;
    final int handlerState = 0;
    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;
    private StringBuilder DataStringIN = new StringBuilder();
    private ConnectedThread MyConexionBT;

    // Identificador unico de servicio - SPP UUID
    private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    // String para la direccion MAC
    private static String address = null;
    //-------------------------------------------

    private static final String TAG = "MainActivity";

    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_control);

        FragmentManager fragmentManager = getSupportFragmentManager();
        DialogFragment dialog = new DialogEncendido();
        dialog.show(fragmentManager, "tagEncendido");

        volley = VolleyRP.getInstance(this);
        mRequest = volley.getRequestQueue();

        //Enlaza los controles con sus respectivas vistas
        btn0 = (Button)findViewById(R.id.button_0);
        btn1 = (Button)findViewById(R.id.button_1);
        btn2 = (Button)findViewById(R.id.button_2);
        btn3 = (Button)findViewById(R.id.button_3);
        btn4 = (Button)findViewById(R.id.button_4);
        btn5 = (Button)findViewById(R.id.button_5);
        btn6 = (Button)findViewById(R.id.button_6);
        btn7 = (Button)findViewById(R.id.button_7);
        btn8 = (Button)findViewById(R.id.button_8);
        btn9 = (Button)findViewById(R.id.button_9);
        btnpunt = (Button)findViewById(R.id.button_punt);
        btnprech = (Button)findViewById(R.id.button_prech);
        btnmas = (ImageButton)findViewById(R.id.button_volmas);
        btnmenos = (ImageButton)findViewById(R.id.button_volmenos);
        btnup = (ImageButton) findViewById(R.id.button_chup);
        btndown = (ImageButton) findViewById(R.id.button_chdown);
        btnent = (Button)findViewById(R.id.button_ent);
        btnpower = (ImageButton)findViewById(R.id.button_power);
        btnmute = (ImageButton)findViewById(R.id.button_mute);
        btnsalir = (Button)findViewById(R.id.button_salir);
        final Vibrator vibrator =(Vibrator)this.getSystemService(Context.VIBRATOR_SERVICE);

        //Variables para recepcion y envio de datos
        IdCanal = (TextView) findViewById(R.id.idcanal);
        IdDuracion = (TextView) findViewById(R.id.idduracion);

        btAdapter = BluetoothAdapter.getDefaultAdapter(); // get Bluetooth adapter
        VerificarEstadoBT();

        // Configuracion onClick listeners para los botones
        // para indicar que se realizara cuando se detecte
        // el evento de Click
        btn0.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                MyConexionBT.write("I");
                vibrator.vibrate(100);
            }
        });

        btn1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MyConexionBT.write("J");
                vibrator.vibrate(100);
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                MyConexionBT.write("K");
                vibrator.vibrate(100);
            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MyConexionBT.write("L");
                vibrator.vibrate(100);
            }
        });

        btn4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                MyConexionBT.write("M");
                vibrator.vibrate(100);
            }
        });

        btn5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MyConexionBT.write("N");
                vibrator.vibrate(100);
            }
        });

        btn6.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MyConexionBT.write("O");
                vibrator.vibrate(100);
            }
        });

        btn7.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MyConexionBT.write("P");
                vibrator.vibrate(100);
            }
        });

        btn8.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                MyConexionBT.write("Q");
                vibrator.vibrate(100);
            }
        });

        btn9.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MyConexionBT.write("R");
                vibrator.vibrate(100);
            }
        });

        btnpunt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MyConexionBT.write("G");
                vibrator.vibrate(100);
            }
        });

        btnprech.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MyConexionBT.write("S");
                vibrator.vibrate(100);
            }
        });

        btnmas.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MyConexionBT.write("E");
                vibrator.vibrate(100);
            }
        });

        btnmenos.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MyConexionBT.write("F");
                vibrator.vibrate(100);
            }
        });

        btnup.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MyConexionBT.write("C");
                vibrator.vibrate(100);
                AlarmService();
            }
        });

        btndown.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MyConexionBT.write("D");
                vibrator.vibrate(100);
                AlarmService();
            }
        });

        btnent.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MyConexionBT.write("H");
                vibrator.vibrate(100);
                AlarmService();
            }
        });

        btnpower.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MyConexionBT.write("A");
                vibrator.vibrate(100);
                FragmentManager fragmentManager = getSupportFragmentManager();
                DialogFragment dialog = new DialogSintonia();
                dialog.show(fragmentManager, "tagEncendido");
            }
        });

        btnmute.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MyConexionBT.write("B");
                vibrator.vibrate(100);
            }
        });

        btnsalir.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MyConexionBT.write("Z");
                vibrator.vibrate(100);
                /*FragmentManager fragmentManager = getSupportFragmentManager();
                DialogFragment dialog = new DialogApagado();
                dialog.show(fragmentManager, "tagpermanencia");*/
                cancelJob();

                Toast.makeText(MyControl.this,"Finalizo", Toast.LENGTH_SHORT).show();
                if (btSocket!=null)
                {
                    try {btSocket.close();}
                    catch (IOException e)
                    { Toast.makeText(getBaseContext(), "Error", Toast.LENGTH_SHORT).show();;}
                }
                finish();
            }
        });

        //Hilo de ingreso de datos
        bluetoothIn = new Handler() {

            public void handleMessage(Message msg) {

                if (msg.what == handlerState) {
                    String readMessage = (String) msg.obj;
                    DataStringIN.append(readMessage);

                    int endOfLineIndexc = DataStringIN.indexOf("#");
                    int endOfLineIndexd = DataStringIN.indexOf("%");

                    if (endOfLineIndexc > 0 && endOfLineIndexd > 0) {

                        //Toma de dato del canal sintonizado
                        String dataInPrintc = DataStringIN.substring(0, endOfLineIndexc);
                        IdCanal.setText(dataInPrintc);

                        //Toma de dato de la duracion en en canal sintonizado
                        String dataInPrintd = DataStringIN.substring(endOfLineIndexc + 1, endOfLineIndexd);
                        IdDuracion.setText(dataInPrintd);

                        DataStringIN.delete(0, DataStringIN.length());

                        String var1 = IdCanal.getText().toString();
                        Preferences.savePreferenceString(MyControl.this, var1, Preferences.PREFERENCE_CANAL);
                        String var2 = IdDuracion.getText().toString();
                        Preferences.savePreferenceString(MyControl.this, var2, Preferences.PREFERENCE_PERMANENCIA);

                        jobService();

                    }

                }
            }
        };
    }

    public void  jobService (){
        ComponentName componentName = new ComponentName(this, MyDataService.class);
        JobInfo info = new JobInfo.Builder(1, componentName)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setPersisted(true)
                .build();

        JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        int resultCode = scheduler.schedule(info);
        if (resultCode == JobScheduler.RESULT_SUCCESS) {
            Log.d(TAG, "Job scheduled");
        } else {
            Log.d(TAG, "Job scheduling failed");
        }
    }

    public void  AlarmService (){
        ComponentName componentName = new ComponentName(this, MyAlarmService.class);
        JobInfo info = new JobInfo.Builder(2, componentName)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setPersisted(true)
                .build();

        JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        int resultCode = scheduler.schedule(info);
        if (resultCode == JobScheduler.RESULT_SUCCESS) {
            Log.d(TAG, "Job scheduled");
        } else {
            Log.d(TAG, "Job scheduling failed");
        }
    }

    public void cancelJob (){
        JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        scheduler.cancel(1);

        JobScheduler scheduler2 = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        scheduler2.cancel(2);
        Log.d(TAG, "Job cancelled");
    }

    //Bluetooth
    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException
    {
        //crea un conexion de salida segura para el dispositivo
        //usando el servicio UUID
        return device.createRfcommSocketToServiceRecord(BTMODULEUUID);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        //Consigue la direccion MAC desde DeviceListActivity via intent
        Intent intent = getIntent();
        //Consigue la direccion MAC desde DeviceListActivity via EXTRA
        address = intent.getStringExtra(MyAccess.EXTRA_ADDRESS);
        //Setea la direccion MAC
        BluetoothDevice device = btAdapter.getRemoteDevice(address);

        try
        {
            btSocket = createBluetoothSocket(device);
        } catch (IOException e) {
            Toast.makeText(getBaseContext(), "La conexion fallo", Toast.LENGTH_LONG).show();
        }
        // Establece la conexión con el socket Bluetooth.
        try
        {
            btSocket.connect();
        } catch (IOException e) {
            try {
                btSocket.close();
            } catch (IOException e2) {}
        }
        MyConexionBT = new ConnectedThread(btSocket);
        MyConexionBT.start();
    }

    @Override
    public void onPause()
    {
        super.onPause();
        try
        { // Cuando se sale de la aplicación esta parte permite
            // que no se deje abierto el socket
            btSocket.close();
        } catch (IOException e2) {}
    }

    //Comprueba que el dispositivo Bluetooth Bluetooth está disponible y solicita que se active si está desactivado
    private void VerificarEstadoBT() {

        if(btAdapter==null) {
            Toast.makeText(getBaseContext(), "El dispositivo no soporta bluetooth", Toast.LENGTH_LONG).show();
        } else {
            if (btAdapter.isEnabled()) {
            } else {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 1);
            }
        }
    }

    //Crea la clase que permite crear el evento de conexion
    private class ConnectedThread extends Thread
    {
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket)
        {
            InputStream tmpIn = null;
            OutputStream tmpOut = null;
            try
            {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) { }
            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run()
        {
            byte[] buffer = new byte[256];
            int bytes;

            // Se mantiene en modo escucha para determinar el ingreso de datos
            while (true) {
                try {
                    bytes = mmInStream.read(buffer);
                    String readMessage = new String(buffer, 0, bytes);
                    // Envia los datos obtenidos hacia el evento via handler
                    bluetoothIn.obtainMessage(handlerState, bytes, -1, readMessage).sendToTarget();
                } catch (IOException e) {
                    break;
                }
            }
        }
        //Envio de trama
        public void write(String input)
        {
            try {
                mmOutStream.write(input.getBytes());
            }
            catch (IOException e)
            {
                //si no es posible enviar datos se cierra la conexión
                Toast.makeText(getBaseContext(), "La Conexión fallo", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }
}
