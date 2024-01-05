package com.softwareengineering.restaurant;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class AdminLoginActivity extends AppCompatActivity {

    private EditText emailET, passwordET;
    private TextView forgotPasswordTV;
    private Button loginButton;
    private FirebaseAuth mAuth;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference usersRef = db.collection("users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        emailET = findViewById(R.id.adminUsername);
        passwordET = findViewById(R.id.adminPassword);
        loginButton = findViewById(R.id.adminLoginButton);
        forgotPasswordTV = findViewById(R.id.adminTxtForgotPassword);
        mAuth = FirebaseAuth.getInstance();

        forgotPasswordTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminLoginActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String adminName, adminPass;
                adminName = String.valueOf(emailET.getText());
                adminPass = String.valueOf(passwordET.getText());

                if (TextUtils.isEmpty(adminName)){
                    Toast.makeText(AdminLoginActivity.this, "Enter username", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(adminPass)){
                    Toast.makeText(AdminLoginActivity.this, "Enter password", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.signInWithEmailAndPassword(adminName, adminPass)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser currentUser = mAuth.getCurrentUser();
                                    assert currentUser != null;
                                    String uid = currentUser.getUid();
                                    getUserRoleFromFirestore(uid);
                                } else {
                                    Toast.makeText(AdminLoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        });

    }

    private void getUserRoleFromFirestore(String uid) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userRef = db.collection("users").document(uid);

        userRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    // Get user role
                    String userRole = document.getString("role");

                    if(Objects.equals(userRole, "admin")){
                        Toast.makeText(AdminLoginActivity.this, "Authentication successfully.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), AdminMainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } else {
                    // User document not found
                    Log.d("Auth Firestore Database", "No such document");
                }
            } else {
                Log.d("Auth Firestore Database", "get failed with ", task.getException());
            }
        });
    }
}