package com.example.calismaasistani2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ActivityEnterName extends AppCompatActivity {

    private EditText etName;
    private Button btnSubmit;
    private DBHelper dbHelper;
    private SharedPreferences sharedPreferences;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_name);

        // Initialize UI components
        etName = findViewById(R.id.etFullName);
        btnSubmit = findViewById(R.id.btnSubmitName);
        dbHelper = new DBHelper(this);
        sharedPreferences = getSharedPreferences("SessionPrefs", MODE_PRIVATE);

        // Check if username is properly set in SharedPreferences
        String username = sharedPreferences.getString("username", "");
        if (username.isEmpty()) {
            Log.e("ActivityEnterName", "Username not found in SharedPreferences.");
            Toast.makeText(this, "Error: Username not found. Please log in again.", Toast.LENGTH_SHORT).show();
            // Optionally redirect to login activity
            // startActivity(new Intent(ActivityEnterName.this, LoginActivity.class));
            finish();
        } else {
            Log.d("ActivityEnterName", "Username: " + username);
        }

        // Set click listener for submit button
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleSubmit();
            }
        });
    }

    private void handleSubmit() {
        String fullName = etName.getText().toString().trim();
        String username = sharedPreferences.getString("username", "");

        // Input validation
        if (fullName.isEmpty()) {
            Toast.makeText(ActivityEnterName.this, "Please enter your name", Toast.LENGTH_SHORT).show();
        } else {
            // Add or update the full name in the database
            boolean success = dbHelper.addFullName(username, fullName);
            if (success) {
                Toast.makeText(ActivityEnterName.this, "Name saved successfully", Toast.LENGTH_SHORT).show();
                // Proceed to the next activity
                Intent intent = new Intent(ActivityEnterName.this, ActivitySetGoal.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(ActivityEnterName.this, "Error adding/updating full name", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
