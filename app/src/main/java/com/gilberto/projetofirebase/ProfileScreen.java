package com.gilberto.projetofirebase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class ProfileScreen extends AppCompatActivity {

    private TextView text_user_name, text_user_email;
    private Button btn_logOut;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_screen);
        Objects.requireNonNull(getSupportActionBar()).hide();
        ComponentsInitializer();

        btn_logOut.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            GoToLoggingOutScreen();
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        String email = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail();
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DocumentReference docRef = db.collection("usuarios").document(userId);
        docRef.addSnapshotListener((userData, error) -> {
            if(userData != null) {
                text_user_name.setText(userData.getString("nome"));
                text_user_email.setText(email);
            }
        });
    }

    private void ComponentsInitializer() {
        text_user_name = findViewById(R.id.text_user_name);
        text_user_email = findViewById(R.id.text_user_email);
        btn_logOut = findViewById(R.id.btn_logOut);
    }

    private void GoToLoggingOutScreen() {
        Intent intent = new Intent(ProfileScreen.this, LoggingOutScreen.class);
        startActivity(intent);
        finish();
    }
}