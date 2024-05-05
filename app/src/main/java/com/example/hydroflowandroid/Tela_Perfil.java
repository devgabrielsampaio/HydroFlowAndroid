package com.example.hydroflowandroid;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class Tela_Perfil extends AppCompatActivity {
    private TextView nomeUsuario,emailUsuario;
    private Button bt_Deslogar;
    FirebaseFirestore db= FirebaseFirestore.getInstance();
    String UsuarioID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_perfil);
        nomeUsuario = findViewById(R.id.textNomeUsuario);
        emailUsuario= findViewById(R.id.textEmailUsuario);
        bt_Deslogar=  findViewById(R.id.btnDeslogar);
        OnBackPressedCallback callback=new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent intent=new Intent(Tela_Perfil.this,Tela_Principal.class);
                startActivity(intent);
                finish();
            }
        };
        Tela_Perfil.this.getOnBackPressedDispatcher().addCallback(this,callback);
        bt_Deslogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                FragmentActivity act= Tela_Perfil.this;
                if(act!=null){
                    startActivity(new Intent(act, Tela_Login.class));
                }
            }
        });
    }
    @Override
    public void onStart(){
        super.onStart();
        String email= FirebaseAuth.getInstance().getCurrentUser().getEmail();
        UsuarioID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DocumentReference documentReference = db.collection("Usuarios").document(UsuarioID);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if(value!=null){
                    nomeUsuario.setText(value.getString("nome"));
                    emailUsuario.setText(email);
                }
                else{
                    nomeUsuario.setText("vazio");
                    emailUsuario.setText("vazio");
                }
            }
        });
    }
}