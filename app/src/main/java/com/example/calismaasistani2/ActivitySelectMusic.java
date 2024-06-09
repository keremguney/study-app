package com.example.calismaasistani2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ActivitySelectMusic extends AppCompatActivity {
    RadioButton radioButtonRock, radioButtonPop, radioButtonJazz, radioButtonClassical, radioButtonHipHop;

    private Button buttonBack;
    private Button buttonSkip;
    private Button buttonContinue;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_music);

        radioButtonRock = findViewById(R.id.radioButtonRock);
        radioButtonPop = findViewById(R.id.radioButtonPop);
        radioButtonJazz = findViewById(R.id.radioButtonJazz);
        radioButtonClassical = findViewById(R.id.radioButtonClassical);
        radioButtonHipHop = findViewById(R.id.radioButtonHipHop);
        buttonBack = findViewById(R.id.buttonBack);
        buttonContinue = findViewById(R.id.buttonContinue);

        buttonBack.setOnClickListener(v -> {
            // Handle the back button logic here
            finish(); // Just finishes the current activity and goes back to the previous one
        });


        buttonContinue.setOnClickListener(v -> {
            // Handle the continue button logic here
            StringBuilder selectedGenres = new StringBuilder("Selected Genres:\n");
            if (radioButtonRock.isChecked()) {
                selectedGenres.append("Rock\n");
            }
            if (radioButtonPop.isChecked()) {
                selectedGenres.append("Pop\n");
            }
            if (radioButtonJazz.isChecked()) {
                selectedGenres.append("Jazz\n");
            }
            if (radioButtonClassical.isChecked()) {
                selectedGenres.append("Classical\n");
            }
            if (radioButtonHipHop.isChecked()) {
                selectedGenres.append("Hip-Hop\n");
            }

            Intent intent = new Intent(ActivitySelectMusic.this, ActivityStudyTimer.class);
            intent.putExtra("RockSelected", radioButtonRock.isChecked());
            intent.putExtra("PopSelected", radioButtonPop.isChecked());
            intent.putExtra("JazzSelected", radioButtonJazz.isChecked());
            intent.putExtra("ClassicalSelected", radioButtonClassical.isChecked());
            intent.putExtra("HipHopSelected", radioButtonHipHop.isChecked());
            startActivity(intent);
        });
    }
}
