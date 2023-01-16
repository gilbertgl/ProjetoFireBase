package com.gilberto.projetofirebase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import java.util.Objects;

public class LoggingOutScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loggin_out_screen);
        Objects.requireNonNull(getSupportActionBar()).hide();

        new Handler().postDelayed(this::GoToScreen
                , 3000);
    }

    private void GoToScreen() {
        Intent intent = new Intent(LoggingOutScreen.this, LoginScreen.class);
        startActivity(intent);
    }
}