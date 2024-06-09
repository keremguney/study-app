package com.example.calismaasistani2.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.calismaasistani2.ActivitySelectStudy;
import com.example.calismaasistani2.DBHelper;
import com.example.calismaasistani2.R;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private DBHelper dbHelper;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        dbHelper = new DBHelper(context); // Initialize DBHelper
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        final TextView textView = root.findViewById(R.id.text_home);

        // Retrieve full_name from DBHelper
        String fullName = dbHelper.getLoggedInFullName();
        String welcomeMessage = fullName != null ? "Welcome, " + fullName + "!" : "Welcome, Guest";

        // Update the textView with the welcome message
        homeViewModel.getText().observe(getViewLifecycleOwner(), s -> textView.setText(welcomeMessage));

        Button buttonHome = root.findViewById(R.id.button_home);
        buttonHome.setOnClickListener(v -> {
            // Create an intent to start ActivitySelectStudy
            Intent intent = new Intent(getActivity(), ActivitySelectStudy.class);

            // Start the ActivitySelectStudy activity
            startActivity(intent);
        });

        return root;
    }
}
