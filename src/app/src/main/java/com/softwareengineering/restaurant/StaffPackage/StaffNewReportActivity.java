package com.softwareengineering.restaurant.StaffPackage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;
import com.softwareengineering.restaurant.LoginActivity;
import com.softwareengineering.restaurant.R;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class StaffNewReportActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private TextView topMenuName, title, content;
    private ImageView topMenuImg;
    private DrawerLayout drawerLayout;
    private RelativeLayout customers, menu, tables, reports, payment, account, logout;
    private Button confirm, save;
    Runnable runnable;
    private String g1_sender;
    private String g1_currentReportId = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_new_report);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        topMenuImg = findViewById(R.id.topMenuImg);
        drawerLayout = findViewById(R.id.staffsDrawerLayout);
        customers = findViewById(R.id.staffsCustomersDrawer);
        menu = findViewById(R.id.staffsMenuDrawer);
        tables = findViewById(R.id.staffsTablesDrawer);
        reports = findViewById(R.id.staffsReportsDrawer);
        payment = findViewById(R.id.staffsPaymentDrawer);
        account = findViewById(R.id.staffsAccountDrawer);
        logout = findViewById(R.id.staffsLogoutDrawer);
        topMenuName = findViewById(R.id.topMenuName);
        title = findViewById(R.id.title);

        content = findViewById(R.id.content);

        topMenuImg.setImageResource(R.drawable.back);


        confirm = (Button) findViewById(R.id.btn_confirm);
        save = (Button) findViewById(R.id.btn_save);


        topMenuName.setText("Write a report");

    }

}