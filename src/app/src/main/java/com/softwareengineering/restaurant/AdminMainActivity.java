package com.softwareengineering.restaurant;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.softwareengineering.restaurant.databinding.ActivityAdminMainBinding;

public class AdminMainActivity extends AppCompatActivity {

    ActivityAdminMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        int[] itemImage = {
                R.drawable.staffs, R.drawable.customers, R.drawable.menu, R.drawable.tables,
                R.drawable.reports, R.drawable.sales, R.drawable.account
        };

        String[] itemName = {
                "Staffs", "Customers", "Menu", "Tables",
                "Reports", "Sales", "Account"
        };


        AdminGridAdapter adminGridAdapter = new AdminGridAdapter(AdminMainActivity.this, itemImage, itemName);
        binding.adminMainActGridView.setAdapter(adminGridAdapter);

        binding.adminMainActGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(AdminMainActivity.this, "You choose " + position, Toast.LENGTH_SHORT).show();
                //startItemActivity(id);
            }
        });

    }

    private void startItemActivity(int position) {
        Intent intent = new Intent(this, AdminMainActivity.class);
    }
}