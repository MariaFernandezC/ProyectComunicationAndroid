package com.example.proyectcomunicationandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Set;

public class DispositivosBT extends AppCompatActivity {

    public static String EXTRA_DEVICE_ADDRESS = "device_address";
    ListView listView;
    private static final String TAG = "DispositivosBT";

    private BluetoothAdapter mbtAdapter;
    private ArrayAdapter<String> mPairedDevicesArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dispositivos_bt);
    }

    @Override
    protected void onResume() {
        super.onResume();
        VerificarEstadoBT();
        mPairedDevicesArrayAdapter = new ArrayAdapter<>(this, R.layout.nombre_dispositivos);
        listView = (ListView) findViewById(R.id.idlista);
        listView.setAdapter(mPairedDevicesArrayAdapter);
        listView.setOnItemClickListener(mDeviceClickListener);
        mbtAdapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> pairedDevices = mbtAdapter.getBondedDevices();
        if(pairedDevices.size()> 0)
        {
            for(BluetoothDevice device : pairedDevices){
                mPairedDevicesArrayAdapter.add(device.getName()+"\n" + device.getAddress());
            }
        }


    }
     //Configurar un click
     private AdapterView.OnItemClickListener mDeviceClickListener = new AdapterView.OnItemClickListener() {
         public void onItemClick(AdapterView av, View v, int arg2, long arg3) {

             // Obtener la dirección MAC del dispositivo, que son los últimos 17 caracteres en la vista
             String info = ((TextView) v).getText().toString();
             String address = info.substring(info.length() - 17);

             // Realiza un intent para iniciar la siguiente actividad
             // mientras toma un EXTRA_DEVICE_ADDRESS que es la dirección MAC.
             Intent i = new Intent(DispositivosBT.this, User_Interface.class);//<-<- PARTE A MODIFICAR >->->
             i.putExtra(EXTRA_DEVICE_ADDRESS, address);
             startActivity(i);
         }
     };
    private void VerificarEstadoBT() {
        // Comprueba que el dispositivo tiene Bluetooth y que está encendido.
        mbtAdapter= BluetoothAdapter.getDefaultAdapter();
        if(mbtAdapter==null) {
            Toast.makeText(getBaseContext(), "El dispositivo no soporta Bluetooth", Toast.LENGTH_SHORT).show();
        } else {
            if (mbtAdapter.isEnabled()) {
                Log.d(TAG, "...Bluetooth Activado...");
            } else {
                //Solicita al usuario que active Bluetooth
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 1);

            }
        }
    }
}
