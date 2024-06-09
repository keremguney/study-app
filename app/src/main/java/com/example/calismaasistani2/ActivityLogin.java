package com.example.calismaasistani2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ActivityLogin extends AppCompatActivity {
    DBHelper dbHelper;
    Button btnLogin;
    Button btnRegister;
    EditText etUsername, etPwd;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        dbHelper = new DBHelper(this);
        etUsername = findViewById(R.id.etUsername);
        etPwd = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        sharedPreferences = getSharedPreferences("SessionPrefs", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = etUsername.getText().toString().trim();
                String password = etPwd.getText().toString().trim();
                boolean isLoggedIn = dbHelper.checkUser(username, password);

                if (isLoggedIn) {
                    // Store username in SharedPreferences
                    editor.putString("username", username);
                    editor.apply();

                    // Check if the user has entered their full name
                    boolean hasName = dbHelper.checkFullName(username);
                    // Check if the user has entered their study goal
                    boolean hasStudyGoal = dbHelper.checkStudyGoal(username);

                    if (!hasName) {
                        // Direct the user to enter their name
                        Intent intent = new Intent(ActivityLogin.this, ActivityEnterName.class);
                        startActivity(intent);
                        finish();
                    } else if (!hasStudyGoal) {
                        // Direct the user to set their study goal
                        Intent intent = new Intent(ActivityLogin.this, ActivitySetGoal.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // Proceed to the main activity if both name and study goal are set
                        Intent intent = new Intent(ActivityLogin.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } else {
                    Toast.makeText(ActivityLogin.this, "Login Failed", Toast.LENGTH_LONG).show();
                }
            }
        });

        btnRegister = findViewById(R.id.btnGoToRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityLogin.this, ActivityRegister.class);
                startActivity(intent);
            }
        });
    }
}
