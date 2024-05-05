package com.example.hydroflowandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Tela_Login extends AppCompatActivity {
    private TextView text_tela_cadastro;
    private EditText edit_email,edit_senha;
    private Button botao_entrar;
    private ProgressBar progressBar;
    String[] mensagens={"Preencha Todos os Campos","Login Efetuado com Sucesso"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_login);
        IniciarComponentes();
        text_tela_cadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Tela_Login.this,Tela_Cadastro.class);
                startActivity(intent);
            }
        });
        botao_entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email=edit_email.getText().toString();
                String senha= edit_senha.getText().toString();
                if(email.equals("")|| senha.equals("")){
                    Snackbar snackbar=Snackbar.make(view,mensagens[0],Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.rgb(64,1,1));
                    snackbar.show();
                }else{
                    AutenticarUsuario(view);
                }
            }
        });
    }
    private void AutenticarUsuario(View v){
        String email=edit_email.getText().toString();
        String senha= edit_senha.getText().toString();
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email,senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    progressBar.setVisibility(View.VISIBLE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            gotoTelaPrincipal();
                        }
                    },3000);
                }
                else{
                    String erro;
                    try{
                        throw task.getException();
                    }catch(Exception e){
                        erro="Usuário Ou Senha Inválidos";
                    }
                    Snackbar snackbar=Snackbar.make(v,erro,Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.rgb(64,1,1));
                    snackbar.show();
                }

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser usuarioAtual = FirebaseAuth.getInstance().getCurrentUser();
        if(usuarioAtual != null){
            gotoTelaPrincipal();
        }

    }

    private void gotoTelaPrincipal(){
        Intent intent= new Intent(Tela_Login.this,Tela_Principal.class);
        startActivity(intent);
        finish();
    }
    private void IniciarComponentes(){
        text_tela_cadastro= findViewById(R.id.textcadastro);
        edit_email= findViewById(R.id.editlogin);
        edit_senha= findViewById(R.id.editsenha);
        botao_entrar=findViewById(R.id.botaologar);
        progressBar=findViewById(R.id.progressbar);
    }
}