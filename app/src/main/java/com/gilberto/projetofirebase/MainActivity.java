package com.gilberto.projetofirebase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).hide();

        new Handler().postDelayed(this::HasLoggedUser
                , 1000);
    }

    private void HasLoggedUser() {
        FirebaseUser loggedUser = FirebaseAuth.getInstance().getCurrentUser();

        if (loggedUser != null) {
            GoToScreen(ProfileScreen.class);
        } else {
            GoToScreen(LoginScreen.class);
        }
    }

    private void GoToScreen(Class<?> destination) {
        Intent intent = new Intent(MainActivity.this, destination);
        startActivity(intent);
    }
}