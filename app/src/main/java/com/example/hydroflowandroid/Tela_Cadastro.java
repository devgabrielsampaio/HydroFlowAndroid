package com.example.hydroflowandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Tela_Cadastro extends AppCompatActivity {
    private EditText edit_nome, edit_email, edit_senha;
    private Button botaoCadastrar;
    String[] mensagens={"Preencha Todos os Campos","Cadastro Realizado com Sucesso"};
    String UsuarioID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_cadastro);
        IniciarComponentes();
        botaoCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nome= edit_nome.getText().toString();
                String email= edit_email.getText().toString();
                String senha=edit_senha.getText().toString();
                if(nome.equals("")||email.equals("")||senha.equals("")){
                    Snackbar snackbar=Snackbar.make(view,mensagens[0],Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.rgb(64,1,1));
                    snackbar.show();

                }
                else{
                    CadastrarUsuario(view);
                }
            }
        });
    }
    private void IniciarComponentes(){
        edit_nome= findViewById(R.id.editnomeCadastro);
        edit_email= findViewById(R.id.editloginCadastro);
        edit_senha=findViewById(R.id.editsenhaCadastro);
        botaoCadastrar=findViewById(R.id.botaocadastrar);
    }
    private void SalvarDadosUsuario(){
        String nome= edit_nome.getText().toString();
        FirebaseFirestore db= FirebaseFirestore.getInstance();
        Map<String,Object> usuarios= new HashMap<>();
        usuarios.put("nome",nome);
        UsuarioID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DocumentReference documentReference = db.collection("Usuarios").document(UsuarioID);
        documentReference.set(usuarios).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("db","Sucesso ao Salvar os Dados");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("db Error","Erro ao Salvar os Dados" + e.toString());
                    }
                });
    }
    private void CadastrarUsuario(View view) {
        String email = edit_email.getText().toString();
        String senha = edit_senha.getText().toString();
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    SalvarDadosUsuario();
                    Snackbar snackbar = Snackbar.make(view, mensagens[1], Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.rgb(64, 1, 1));
                    snackbar.show();
                    edit_nome.setText("");
                    edit_email.setText("");
                    edit_senha.setText("");
                } else {
                    String erro;
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthWeakPasswordException e) {
                        erro = "Digite uma Senha com no mínimo 6 caracteres";
                    } catch (FirebaseAuthUserCollisionException e) {
                        erro = "Esta Conta já foi Cadastrada";
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        erro = "E-mail Inválido";
                    } catch (Exception e) {
                        erro = "Erro ao Cadastrar Usuário";
                    }
                    Snackbar snackbar = Snackbar.make(view, erro, Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.rgb(64, 1, 1));
                    snackbar.show();
                }
            }
        });
    }
}