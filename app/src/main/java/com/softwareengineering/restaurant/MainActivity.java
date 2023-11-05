package com.softwareengineering.restaurant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import nl.joery.animatedbottombar.AnimatedBottomBar;

public class MainActivity extends AppCompatActivity {

    TextView mainMsg;
    FirebaseAuth auth;
    Button buttonLogout;
    FirebaseUser user;
    FirebaseFirestore db;
    Button addUser;
    Button getUser;
    AnimatedBottomBar bottomBar;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainMsg = findViewById(R.id.user_details);
        auth = FirebaseAuth.getInstance();
        buttonLogout = findViewById(R.id.logout);
        user = auth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        addUser = findViewById(R.id.addUserBtn);
        getUser = findViewById(R.id.getUserBtn);
        bottomBar = findViewById(R.id.bottom_bar);

        if(user == null){
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        }
        else{
            mainMsg.setText("Hello " + user.getDisplayName());
        }

        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });

        addUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user != null) {
                    // Create a new user with a name and role
                    String userName = "hieu"; // Replace with the user's name
                    String userRole = "admin"; // Replace with the user's role
                    String userEmail = user.getEmail(); // Replace with the user's email

                    Map<String, Object> userData = new HashMap<>();
                    userData.put("name", userName);
                    userData.put("email", userEmail);
                    userData.put("role", userRole);

                    db.collection("users")
                            .add(userData)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Toast.makeText(MainActivity.this, "Successfully added user to database", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(MainActivity.this, "Failed to add user to database", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });

        getUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user != null) {
                    String targetedRole = "admin";

                    db.collection("users")
                            .whereEqualTo("role", targetedRole)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            String userName = document.getString("name");
                                            String userEmail = document.getString("email");
                                            String userRole = document.getString("role");
                                            Toast.makeText(MainActivity.this, "Successfully get user's info", Toast.LENGTH_SHORT).show();
                                            Toast.makeText(MainActivity.this, "Admin's info: " + "Name: " + userName + ", Email: " + userEmail + ", Role: " + userRole, Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(MainActivity.this, "Failed to get user's info", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });

    }

    // Handle Fragment for Bottom Navigation Bar
    private void replace(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.bottomFrameLayout, fragment);
        fragmentTransaction.commit();
    }


}