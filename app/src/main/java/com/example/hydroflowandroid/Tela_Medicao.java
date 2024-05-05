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
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class Tela_Medicao extends AppCompatActivity {
    ImageView nivelbaixo, nivelmedio, nivelcheio;
    double nivel = 0;
    EditText litrostotais;
    Button botao_iniciar_monitoramento, botao_encerrar_monitoramento;
    private BluetoothDevice dispositivoBluetooth;
    private BluetoothAdapter bluetoothAdapter = null;
    private BluetoothSocket bluetoothSocket = null;
    private InputStream inputStream = null;
    static final UUID bUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_medicao);
        litrostotais = findViewById(R.id.editlitrostotais1);
        nivelbaixo = findViewById(R.id.nivelbaixo1);
        nivelmedio = findViewById(R.id.nivelmedio);
        nivelcheio = findViewById(R.id.nivelcheio);
        botao_iniciar_monitoramento = findViewById(R.id.botaoIniciarMonitoramento1);
        botao_encerrar_monitoramento = findViewById(R.id.botaoEncerrarMonitoramento1);
        nivelbaixo.setVisibility(View.INVISIBLE);
        nivelmedio.setVisibility(View.INVISIBLE);
        nivelcheio.setVisibility(View.INVISIBLE);
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent intent = new Intent(Tela_Medicao.this, Tela_Principal.class);
                startActivity(intent);
                finish();
            }
        };
        Tela_Medicao.this.getOnBackPressedDispatcher().addCallback(this, callback);
        botao_iniciar_monitoramento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                conexao(view);
                Timer timer = new Timer();
                Task task = new Task();
                timer.schedule(task, 4000, 4000);
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
                                String p = readmessage;
                                String c = "LT";
                                int posiLT = p.indexOf(c);
                                String f = "F";
                                String valor = p.substring(posiLT + 2, p.indexOf(f));
                                litrostotais.setText("");
                                litrostotais.setText("Litros Totais: " + valor);
                                if ((Double.parseDouble(valor) >= 0) && Double.parseDouble(valor) <= 3) {
                                    nivelbaixo.setVisibility(View.VISIBLE);
                                    Glide.with(Tela_Medicao.this).load(R.drawable.nivelbaixo).into(nivelbaixo);
                                }
                                if (Double.parseDouble(valor) > 3 && Double.parseDouble(valor) <= 6) {
                                    nivelmedio.setVisibility(View.VISIBLE);
                                    Glide.with(Tela_Medicao.this).load(R.drawable.nivelmedio).into(nivelmedio);
                                }
                                if (Double.parseDouble(valor) > 6 && Double.parseDouble(valor) <= 8) {
                                    nivelcheio.setVisibility(View.VISIBLE);
                                    Glide.with(Tela_Medicao.this).load(R.drawable.nivelcheio).into(nivelcheio);
                                }

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }

        });
        botao_encerrar_monitoramento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                encerrarMedicao(view);
            }
        });


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
    }
    public void encerrarMedicao(View view) {
        try {
            bluetoothSocket.close();
            Snackbar snackbar = Snackbar.make(view, "Dispositivo Desconectado", Snackbar.LENGTH_SHORT);
            snackbar.setBackgroundTint(Color.WHITE);
            snackbar.setTextColor(Color.rgb(64, 1, 1));
            snackbar.show();
            litrostotais.setText("");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}