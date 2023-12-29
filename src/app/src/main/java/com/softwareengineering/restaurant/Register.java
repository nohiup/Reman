package com.softwareengineering.restaurant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    EditText editTextName, editTextEmail, editTextPhone, editTextPassword, editTextRetypePassword;
    RadioGroup radioGroupGender;
    Button buttonReg;
    FirebaseAuth mAuth;
    TextView textViewSignIn;
    FirebaseFirestore db;
    CheckBox checkBoxTOS;

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editTextName = findViewById(R.id.name);
        editTextEmail = findViewById(R.id.email);
        editTextPhone = findViewById(R.id.phone);
        radioGroupGender = findViewById(R.id.gender);
        editTextPassword = findViewById(R.id.password);
        editTextRetypePassword = findViewById(R.id.retypePassword);
        buttonReg = findViewById(R.id.btn_register);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        textViewSignIn = findViewById(R.id.loginNow);
        checkBoxTOS = findViewById(R.id.TOS);

        buttonReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name, email, phone, gender, password, retypePassword;
                name = String.valueOf(editTextName.getText());
                email = String.valueOf(editTextEmail.getText());
                phone = String.valueOf(editTextPhone.getText());
                int selectedGenderId = radioGroupGender.getCheckedRadioButtonId();
                password = String.valueOf(editTextPassword.getText());
                retypePassword = String.valueOf(editTextRetypePassword.getText());

                if (selectedGenderId == 0) {
                    gender = "Male";
                }
                else {
                    gender = "Female";
                }

                if (TextUtils.isEmpty(name)){
                    Toast.makeText(Register.this, "Enter name", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(email)){
                    Toast.makeText(Register.this, "Enter email", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(phone)){
                    Toast.makeText(Register.this, "Enter phone number", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(gender)){
                    Toast.makeText(Register.this, "Select gender", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)){
                    Toast.makeText(Register.this, "Enter password", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(retypePassword)){
                    Toast.makeText(Register.this, "Re-enter password", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!TextUtils.equals(password, retypePassword)) {
                    Toast.makeText(Register.this, "Password doesn't matched", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!checkBoxTOS.isChecked()) {
                    Toast.makeText(Register.this, "You must accept our Terms & Conditions in order to proceed", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser currentUser = mAuth.getCurrentUser();
                                    if (currentUser != null){
                                        currentUser.sendEmailVerification();
                                        Toast.makeText(Register.this, "Confirmation link has been sent to your registered email", Toast.LENGTH_SHORT).show();
                                        addUserToDatabase(currentUser.getUid(), name, email, gender, phone);
                                        Intent intent = new Intent(getApplicationContext(), Login.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                } else {
                                    Toast.makeText(Register.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        });

        textViewSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void addUserToDatabase(String uid, String name, String email, String gender, String phone) {
        Map<String, Object> userData = new HashMap<>();
        userData.put("email", email);
        userData.put("gender", gender);
        userData.put("name", name);
        userData.put("phone", phone);
        db.collection("users").document(uid).set(userData)
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d("Firestore", "DocumentSnapshot written with ID: " + uid);
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.w("Firestore", "Error adding document", e);
                }
            });
    }
}