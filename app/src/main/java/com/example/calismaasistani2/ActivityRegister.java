package com.example.calismaasistani2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ActivityRegister extends AppCompatActivity {
    EditText etUser, etPwd, etRepwd;
    Button btnRegister, btnGoToLogin;
    DBHelper dbHelper;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        etUser = findViewById(R.id.etUsername);
        etPwd = findViewById(R.id.etPassword);
        etRepwd = findViewById(R.id.etRePassword);
        btnRegister = findViewById(R.id.btnRegister);
        dbHelper = new DBHelper(this);
        btnRegister.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String user, pwd, rePwd;
                user = etUser.getText().toString();
                pwd = etPwd.getText().toString();
                rePwd = etRepwd.getText().toString();
                if (user.equals("") || pwd.equals("") || rePwd.equals("")) {
                    Toast.makeText(ActivityRegister.this, "Please fill all the fields", Toast.LENGTH_LONG).show();
                } else {
                    if (pwd.equals(rePwd)) {
                        if (dbHelper.checkUsername(user)) {
                            Toast.makeText(ActivityRegister.this, "User alredy exists", Toast.LENGTH_LONG).show();
                            return;
                        }
                        // Proceed with registiration
                        boolean registeredSuccess = dbHelper.insertData(user, pwd, ActivityRegister.this);
                        if (registeredSuccess)
                            Toast.makeText(ActivityRegister.this, "User registered successfully", Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(ActivityRegister.this, "User registration failed", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(ActivityRegister.this, "Passwords do not match", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        btnGoToLogin = findViewById(R.id.btnLogin);
        btnGoToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityRegister.this, ActivityLogin.class);
                startActivity(intent);
            }
        });
    }
}