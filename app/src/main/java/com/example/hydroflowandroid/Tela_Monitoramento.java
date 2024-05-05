package com.example.hydroflowandroid;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class Tela_Monitoramento extends AppCompatActivity {
    private BluetoothDevice dispositivoBluetooth;
    private BluetoothAdapter bluetoothAdapter = null;
    private BluetoothSocket bluetoothSocket = null;
    private InputStream inputStream = null;
    private EditText edit_litros, edit_gasto, edit_metros, edit_valor;
    private Button botao_iniciar, botaoEncerrar;
    static final UUID bUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_monitoramento);
        edit_litros = findViewById(R.id.editlitros1);
        edit_gasto = findViewById(R.id.editgasto1);
        edit_metros = findViewById(R.id.editmetros1);
        edit_valor = findViewById(R.id.editvalor1);
        botao_iniciar = findViewById(R.id.botaoIniciar1);
        botaoEncerrar = findViewById(R.id.botaoEncerrar1);
        OnBackPressedCallback callback=new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent intent=new Intent(Tela_Monitoramento.this,Tela_Principal.class);
                startActivity(intent);
                finish();
            }
        };
        Tela_Monitoramento.this.getOnBackPressedDispatcher().addCallback(this,callback);
        botao_iniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                conexao(view);
                Timer timer = new Timer();
                Task task = new Task();

                timer.schedule(task, 1000, 1000);


            }
            class Task extends TimerTask {

                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                byte[] buffer = new byte[1024];
                                int bytes;
                                InputStream inputStream = bluetoothSocket.getInputStream();
                                bytes = inputStream.read(buffer);
                                String readmessage = new String(buffer, 0, bytes);
                                System.out.println(readmessage);
                                String p=readmessage;
                                String c="LM";
                                int posiL=p.indexOf(c);
                                c="GT";
                                int posiG=p.indexOf(c);
                                String litros=p.substring(posiL+2,posiG);
                                c="MC";
                                int posiM=p.indexOf(c);
                                String gasto=p.substring(posiG+2,posiM);
                                c="RS";
                                int posiR=p.indexOf(c);
                                String metros=p.substring(posiM+2,posiR);
                                c="LT";
                                String valor=p.substring(posiR+2,p.indexOf(c));

                                edit_litros.setText("");
                                edit_gasto.setText("");
                                edit_metros.setText("");
                                edit_valor.setText("");

                                edit_litros.setText("Litros/Min: "+litros);
                                edit_gasto.setText( "Gasto Total: "+gasto);
                                edit_metros.setText("Metros Cúbicos: "+metros);
                                edit_valor.setText("Valor Total R$ "+valor);

                            }catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }

        });
        botaoEncerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                encerrarMedicao(view);
            }
        });

    }

    public void encerrarMedicao(View view) {
        try {
            bluetoothSocket.close();
            Snackbar snackbar = Snackbar.make(view, "Dispositivo Desconectado", Snackbar.LENGTH_SHORT);
            snackbar.setBackgroundTint(Color.WHITE);
            snackbar.setTextColor(Color.rgb(64, 1, 1));
            snackbar.show();
            edit_litros.setText("");
            edit_gasto.setText("");
            edit_metros.setText("");
            edit_valor.setText("");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void conexao(View view) {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        dispositivoBluetooth = bluetoothAdapter.getRemoteDevice("98:DA:60:03:AB:10");
        try {
            bluetoothSocket = dispositivoBluetooth.createRfcommSocketToServiceRecord(bUUID);
            bluetoothSocket.connect();
            Snackbar snackbar = Snackbar.make(view, "Dispositivo conectado", Snackbar.LENGTH_SHORT);
            snackbar.setBackgroundTint(Color.WHITE);
            snackbar.setTextColor(Color.rgb(64, 1, 1));
            System.out.println("conectado");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }};