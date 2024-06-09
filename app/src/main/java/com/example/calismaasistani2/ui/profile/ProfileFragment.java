package com.example.calismaasistani2.ui.profile;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.calismaasistani2.DBHelper;
import com.example.calismaasistani2.R;

public class ProfileFragment extends Fragment {

    private EditText editTextName;
    private TextView textViewNameLabel;
    private TextView textViewStudyGoalLabel;
    private NumberPicker numberPickerStudyGoal;
    private Button buttonSave;
    private DBHelper dbHelper;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        editTextName = root.findViewById(R.id.editTextName);
        textViewNameLabel = root.findViewById(R.id.textViewNameLabel);
        textViewStudyGoalLabel = root.findViewById(R.id.textViewStudyGoalLabel);
        numberPickerStudyGoal = root.findViewById(R.id.numberPickerStudyGoal);
        buttonSave = root.findViewById(R.id.buttonSave);
        dbHelper = new DBHelper(getActivity());

        // Set up the NumberPicker
        numberPickerStudyGoal.setMinValue(0);
        numberPickerStudyGoal.setMaxValue(100);
        numberPickerStudyGoal.setValue(0);

        // Load saved data every time the fragment is created
        loadProfileData();

        buttonSave.setOnClickListener(v -> {
            saveProfileData();
            Toast.makeText(getActivity(), "Profile saved", Toast.LENGTH_SHORT).show();
        });

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Reload profile data every time the fragment is resumed
        loadProfileData();
    }

    private void loadProfileData() {
        String[] profileData = dbHelper.loadProfileData();
        String name = profileData[0];
        String studyGoal = profileData[1];

        editTextName.setText(name);
        textViewNameLabel.setText("Name: " + name);
        textViewStudyGoalLabel.setText("Study Goal: " + studyGoal);
        numberPickerStudyGoal.setValue(Integer.parseInt(studyGoal));
    }

    private void saveProfileData() {
        String name = editTextName.getText().toString();
        String studyGoal = String.valueOf(numberPickerStudyGoal.getValue());
        dbHelper.saveProfileData(name, studyGoal);
        loadProfileData(); // Reload the profile data after saving
    }

}
