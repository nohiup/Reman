package com.softwareengineering.restaurant;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class HomeFragment extends Fragment {

    TextView mainMsg;
    FirebaseAuth auth;
    Button buttonLogout;
    FirebaseUser user;
    FirebaseFirestore db;
    Button addUser;
    Button getUser;

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mainMsg = view.findViewById(R.id.user_details);
        auth = FirebaseAuth.getInstance();
        buttonLogout = view.findViewById(R.id.logout);
        user = auth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        addUser = view.findViewById(R.id.addUserBtn);
        getUser = view.findViewById(R.id.getUserBtn);

        if(user == null){
            Intent intent = new Intent(getActivity(), Login.class);
            startActivity(intent);
        }

        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(), Login.class);
                startActivity(intent);
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
                    mainMsg.setText("Hello " + userName);

                    db.collection("users")
                            .add(userData)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Toast.makeText(getActivity(), "Successfully added user to database", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getActivity(), "Failed to add user to database", Toast.LENGTH_SHORT).show();
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
                                            Toast.makeText(getActivity(), "Successfully get user's info", Toast.LENGTH_SHORT).show();
                                            Toast.makeText(getActivity(), "Admin's info: " + "Name: " + userName + ", Email: " + userEmail + ", Role: " + userRole, Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(getActivity(), "Failed to get user's info", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });

        return view;
    }

}