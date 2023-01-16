package com.gilberto.projetofirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class LoginScreen extends AppCompatActivity {

    private TextView btn_registrationScreen;
    private EditText loginForm_email, loginForm_pass;
    private Button btn_logIn;
    private ProgressBar progressBar;
    private View screen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_login);
        Objects.requireNonNull(getSupportActionBar()).hide();
        ComponentsInitializer();

        btn_registrationScreen.setOnClickListener(v -> GoToScreen(RegisterScreen.class));

        btn_logIn.setOnClickListener(v -> {
            hideKeyBoard(v);
            String mail = loginForm_email.getText().toString();
            String pass = loginForm_pass.getText().toString();

            if (mail.isEmpty() || pass.isEmpty()) {
                SendAlert(v, "Digite um email e senha!");
            } else {
                UserAuthenticate(v, mail, pass);
            }
        });

        screen.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                hideKeyBoard(v);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser loggedUser = FirebaseAuth.getInstance().getCurrentUser();

        if (loggedUser != null) {
            GoToScreen(ProfileScreen.class);
        }
    }

    private void ComponentsInitializer() {
        btn_registrationScreen = findViewById(R.id.btn_registrationScreen);
        loginForm_email = findViewById(R.id.loginForm_email);
        loginForm_pass = findViewById(R.id.loginForm_pass);
        btn_logIn = findViewById(R.id.btn_logIn);
        progressBar = findViewById(R.id.progressBar);
        screen = findViewById(R.id.loginScreen);
    }

    private void UserAuthenticate(View v, String mail, String pass) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(mail, pass).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                progressBar.setVisibility(View.VISIBLE);
                new Handler().postDelayed(this::GoToProfileScreen
                        , 3000);
            } else {
                String error;
                try {
                    throw Objects.requireNonNull(task.getException());
                } catch (FirebaseAuthInvalidUserException e) {
                    error = "Email ou senha incorretos.";
                } catch (FirebaseAuthInvalidCredentialsException e) {
                    error = "Email inválido!!";
                } catch (Exception e) {
//                    error = "Erro ao fazer log in. Por favor, tente novamente mais tarde!";
                    error = e.getMessage();
                }
                assert error != null;
                SendAlert(v, error);
            }
        });
    }

    private void GoToProfileScreen() {
        Intent intent = new Intent(LoginScreen.this, ProfileScreen.class);
        startActivity(intent);
        finish();
    }

    private void GoToScreen(Class<?> destination) {
        Intent intent = new Intent(LoginScreen.this, destination);
        startActivity(intent);
    }

    private void SendAlert(View v, @NonNull String message) {
        Snackbar snackbar = Snackbar.make(v, message, Snackbar.LENGTH_INDEFINITE);
        View view = snackbar.getView();
        TextView snackTextView = view.findViewById(com.google.android.material.R.id.snackbar_text);
        snackbar.setAction("Dismiss", new RegisterScreen.DismissAlert());
        snackbar.setMaxInlineActionWidth(100);
        snackbar.setActionTextColor(Color.BLACK);
        snackbar.setBackgroundTint(Color.WHITE);
        snackbar.setTextColor(Color.BLACK);
        snackTextView.setMaxLines(10);
        snackbar.show();
    }

    private void hideKeyBoard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}