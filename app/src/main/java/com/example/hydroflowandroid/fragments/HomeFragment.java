package com.example.hydroflowandroid.fragments;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hydroflowandroid.R;
import com.example.hydroflowandroid.Tela_Principal;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;


public class HomeFragment extends Fragment {
    View view;
    private BluetoothDevice dispositivoBluetooth;
    private BluetoothAdapter bluetoothAdapter = null;
    private BluetoothSocket bluetoothSocket = null;
    private InputStream inputStream = null;
    private EditText edit_litros, edit_gasto, edit_metros, edit_valor;
    private Button botao_iniciar,botao_encerrar;
    public int contador = 0;
    static final UUID bUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_home, container, false);
        edit_litros = view.findViewById(R.id.editlitros);
        edit_gasto = view.findViewById(R.id.editgasto);
        edit_metros = view.findViewById(R.id.editmetros);
        edit_valor = view.findViewById(R.id.editvalor);
        botao_iniciar = view.findViewById(R.id.botaoIniciar);
        botao_encerrar=view.findViewById(R.id.botaoEncerrar);
        botao_iniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contador += 1;
                if (contador >= 1) {
                    conexao();
                    IniciarMedicao();
                }

            }

        });
        botao_encerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    bluetoothSocket.close();
                    Snackbar snackbar=Snackbar.make(view,"Dispositivo Desconectado",Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.rgb(64,1,1));
                    snackbar.show();
                    edit_litros.setText("");
                    edit_gasto.setText("");
                    edit_metros.setText("");
                    edit_valor.setText("");

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        return view;

    }

    private void IniciarMedicao() {
        try {
            byte[] buffer = new byte[1024];
            int bytes;
            InputStream inputStream = bluetoothSocket.getInputStream();
            bytes = inputStream.read(buffer);
            String readmessage = new String(buffer, 0, bytes);
            System.out.println(readmessage);
            String p=readmessage;
            String c="L";
            int posiL=p.indexOf(c);
            c="G";
            int posiG=p.indexOf(c);
            String litros=p.substring(posiL,posiG);
            c="M";
            int posiM=p.indexOf(c);
            String gasto=p.substring(posiG,posiM);
            c="R";
            int posiR=p.indexOf(c);
            String metros=p.substring(posiM,posiR);
            c="F";
            String valor=p.substring(posiR,p.indexOf(c));

            edit_litros.setText("");
            edit_gasto.setText("");
            edit_metros.setText("");
            edit_valor.setText("");

            edit_litros.setText(litros);
            edit_gasto.setText(gasto);
            edit_metros.setText(metros);
            edit_valor.setText(valor);

        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void conexao() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        dispositivoBluetooth = bluetoothAdapter.getRemoteDevice("98:DA:60:03:AB:10");
        try {
            bluetoothSocket = dispositivoBluetooth.createRfcommSocketToServiceRecord(bUUID);
            bluetoothSocket.connect();
            System.out.println("conectado");
            Snackbar snackbar=Snackbar.make(view,"Dispositivo Conectado",Snackbar.LENGTH_SHORT);
            snackbar.setBackgroundTint(Color.WHITE);
            snackbar.setTextColor(Color.rgb(64,1,1));
            snackbar.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
