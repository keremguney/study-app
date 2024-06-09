package com.example.calismaasistani2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ActivitySelectStudy extends AppCompatActivity {

    private RadioGroup radioGroupSubjects;
    private Button buttonSelect;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_study);

        // Initialize views
        radioGroupSubjects = findViewById(R.id.radioGroupSubjects);
        buttonSelect = findViewById(R.id.buttonSelect);

        // Set click listener for the select button
        buttonSelect.setOnClickListener(v -> {
            // Get the selected radio button ID
            int selectedRadioButtonId = radioGroupSubjects.getCheckedRadioButtonId();

            // Check if any radio button is selected
            if (selectedRadioButtonId != -1) {
                // Find the selected radio button by ID
                RadioButton selectedRadioButton = findViewById(selectedRadioButtonId);

                // Get the text of the selected radio button
                String selectedSubject = selectedRadioButton.getText().toString();

                // Show a toast message with the selected subject
                Intent intent = new Intent(ActivitySelectStudy.this, ActivitySelectMusic.class);
                startActivity(intent);
            } else {
                // If no radio button is selected, show a toast message
                Toast.makeText(ActivitySelectStudy.this, "Please select a subject", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
