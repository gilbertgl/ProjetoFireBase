package com.gilberto.projetofirebase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class FormLogin extends AppCompatActivity {

    private TextView tela_cadastro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_login);
        getSupportActionBar().hide();
        IniciarComponentes();

        tela_cadastro.setOnClickListener(v -> {
                    Intent intent = new Intent(FormLogin.this, FormCadastro.class);
                    startActivity(intent);
                }
        );
    }

    private void IniciarComponentes() {
        tela_cadastro = findViewById(R.id.tela_cadastro);
    }
}