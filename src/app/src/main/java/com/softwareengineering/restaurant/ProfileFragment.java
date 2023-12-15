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
import android.widget.ImageView;
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
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class ProfileFragment extends Fragment {

    ImageView avatar;
    TextView textViewUsername, textViewEmail;
    FirebaseAuth auth;
    Button buttonLogout;
    FirebaseUser user;
    FirebaseFirestore db;

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        avatar = view.findViewById(R.id.avatar);
        textViewUsername = view.findViewById(R.id.displayUsername);
        textViewEmail = view.findViewById(R.id.displayEmail);
        auth = FirebaseAuth.getInstance();
        buttonLogout = view.findViewById(R.id.logout);
        user = auth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        if(user == null){
            Intent intent = new Intent(getActivity(), Login.class);
            startActivity(intent);
        }

        Picasso.get().load(String.valueOf(user.getPhotoUrl())).into(avatar);
        textViewUsername.setText(String.valueOf(user.getDisplayName()));
        textViewEmail.setText(String.valueOf(user.getEmail()));

        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(), Login.class);
                startActivity(intent);
            }
        });

        return view;
    }

}