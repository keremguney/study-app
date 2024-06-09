package com.example.calismaasistani2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ActivitySetGoal extends AppCompatActivity {


    private DBHelper dbHelper;
    private SharedPreferences sharedPreferences;
    private NumberPicker numberPicker;
    private Button btnSetGoal;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_goal);

        dbHelper = new DBHelper(this);
        sharedPreferences = getSharedPreferences("SessionPrefs", MODE_PRIVATE);
        numberPicker = findViewById(R.id.goalPicker);
        btnSetGoal = findViewById(R.id.btnSetGoal);

        // Set the minimum and maximum values
        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(100);

        // Optionally, set the initial value
        numberPicker.setValue(50);

        // Optionally, set a formatter to display the numbers in a specific format
        numberPicker.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                return String.format("%02d", value); // Format numbers with leading zeros
            }
        });

        btnSetGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedGoal = numberPicker.getValue();
                String username = sharedPreferences.getString("username", "");

                if (!username.isEmpty()) {
                    boolean success = dbHelper.addStudyGoal(username, String.valueOf(selectedGoal));
                    if (success) {
                        Toast.makeText(ActivitySetGoal.this, "Study goal set successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ActivitySetGoal.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(ActivitySetGoal.this, "Error setting study goal", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ActivitySetGoal.this, "User not logged in. Please log in again.", Toast.LENGTH_SHORT).show();
                    // Optionally redirect to login activity
                    // startActivity(new Intent(ActivitySetGoal.this, ActivityLogin.class));
                    finish();
                }
            }
        });
    }
}
