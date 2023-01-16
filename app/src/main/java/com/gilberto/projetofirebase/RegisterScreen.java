package com.gilberto.projetofirebase;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RegisterScreen extends AppCompatActivity {

    private EditText cadastroForm_nome, cadastroForm_email, cadastroForm_pass;
    private Button btn_cadastrar;
    private View screen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_cadastro);
        Objects.requireNonNull(getSupportActionBar()).hide();
        ComponentsInitializer();

        btn_cadastrar.setOnClickListener(v -> {
            hideKeyBoard(v);
            String nome = cadastroForm_nome.getText().toString().trim();
            String mail = cadastroForm_email.getText().toString().trim();
            String pass = cadastroForm_pass.getText().toString().trim();

            if (nome.isEmpty() || mail.isEmpty() || pass.isEmpty()) {
                SendAlert(v, "Preencha todos os campos!");

            } else if (!nome.matches("^([a-zA-Z0-9]+){5,20}")) {
                SendAlert(v, String.format("O nome %s não é válido! O mesmo não pode ter símbolos especiais e deve ter mais do que 5 caracteres!", nome));

            } else if (!mail.matches("^([a-z0-9]+)([.]|[_])?[a-z0-9]+@([a-z]{3,8}.[a-z]{3})(.[a-z]{2})?")) {
                SendAlert(v, String.format("O email: %s não é válido! Ex.: exemplo@email.com", mail));

            } else if (!pass.matches("^(?=.*[}{,.^?~=+\\-_/*-+|@])(?=.*[a-zA-Z])(?=.*[0-9]).{8,}")) {
                SendAlert(v, "A senha inserida é inválida! São necessários no mínimo 8 " +
                        "caracteres contendo ao menos um carácter especial, um número e uma letra.");
            } else {
                RegisterUser(v, mail, pass);
            }
        });

        screen.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                hideKeyBoard(v);
            }
        });
    }

    private void ComponentsInitializer() {
        cadastroForm_nome = findViewById(R.id.cadastroForm_nome);
        cadastroForm_email = findViewById(R.id.cadastroForm_email);
        cadastroForm_pass = findViewById(R.id.cadastroForm_pass);
        btn_cadastrar = findViewById(R.id.btn_cadastrar);
        screen = findViewById(R.id.registerScreen);
    }

    private void RegisterUser(View v, String mail, String pass) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(mail, pass).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                SaveUserRegistrationDataOnDatabase();
                SendAlert(v, "Cadastro realizado com sucesso!");
            } else {
                String error;
                try {
                    throw Objects.requireNonNull(task.getException());
                } catch (FirebaseAuthWeakPasswordException e) {
                    error = "Digite uma senha de no mínimo 6 caracteres.";
                } catch (FirebaseAuthUserCollisionException e) {
                    error = "Esse email ja está em uso!";
                } catch (FirebaseAuthInvalidCredentialsException e) {
                    error = "Email inválido!!";
                } catch (Exception e) {
                    error = "Erro ao cadastrar. Por favor, tente novamente mais tarde!";
                }
                SendAlert(v, error);
            }
        });
    }

    private void GoToLoginScreen() {
        Intent intent = new Intent(RegisterScreen.this, LoginScreen.class);
        startActivity(intent);
        finish();
    }

    private void SaveUserRegistrationDataOnDatabase() {
        String nome = cadastroForm_nome.getText().toString();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> users = new HashMap<>();
        users.put("nome", nome);

        String userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        DocumentReference docRef = db.collection("usuarios").document(userId);
        docRef.set(users)
                .addOnSuccessListener(command -> new Handler().postDelayed(this::GoToLoginScreen
                        , 2000));
    }

    public static class DismissAlert implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            // Code to undo the user's last action
        }
    }

    private void SendAlert(View v, @NonNull String message) {
        Snackbar snackbar = Snackbar.make(v, message, Snackbar.LENGTH_INDEFINITE);
        View view = snackbar.getView();
        TextView snackTextView = view.findViewById(com.google.android.material.R.id.snackbar_text);
        snackbar.setAction("Dismiss", new DismissAlert());
        snackbar.setMaxInlineActionWidth(100);
        snackbar.setActionTextColor(Color.BLACK);
        snackbar.setBackgroundTint(Color.WHITE);
        snackbar.setTextColor(Color.BLACK);
        snackTextView.setMaxLines(4);
        snackbar.show();
    }

    private void hideKeyBoard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}