package com.softwareengineering.restaurant;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class EditStaffsActivity extends AppCompatActivity {

    private ImageView topMenuImg;
    private TextView topMenuName;
    private RadioButton editStaffsMale, editStaffsFemale;
    private EditText nameET, emailET, phoneET, roleET, usernameET;
    private Button btnEditStaffDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_staffs);

        editStaffsMale = findViewById(R.id.editStaffsMale);
        editStaffsFemale = findViewById(R.id.editStaffsFemale);
        nameET = findViewById(R.id.editStaffsName);
        emailET = findViewById(R.id.editStaffsEmail);
        phoneET = findViewById(R.id.editStaffsPhone);
        roleET = findViewById(R.id.editStaffsRole);
        usernameET = findViewById(R.id.editStaffsUsername);
        btnEditStaffDone = findViewById(R.id.btn_edit_staff_done);

        topMenuImg = findViewById(R.id.topMenuImg);
        topMenuName = findViewById(R.id.topMenuName);

        topMenuImg.setImageResource(R.drawable.back);
        topMenuImg.setColorFilter(ContextCompat.getColor(this, R.color.white));
        topMenuName.setText(R.string.edit_account);

        // Get info from Details
        Staffs existingStaff = (Staffs) getIntent().getParcelableExtra("existedStaffs");
        if (existingStaff != null) {
            nameET.setText(existingStaff.getName());
            emailET.setText(existingStaff.getEmail());
            phoneET.setText(existingStaff.getPhone());
            roleET.setText(existingStaff.getRole());
            usernameET.setText(existingStaff.getUsername());

            if (Objects.equals(existingStaff.getGender(), "Male")) {
                editStaffsMale.setChecked(true);
                editStaffsFemale.setChecked(false);
            }
            else {
                editStaffsMale.setChecked(false);
                editStaffsFemale.setChecked(true);
            }
        }

        // Handle Edit Staffs Info
        // Also change info on database
        btnEditStaffDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}