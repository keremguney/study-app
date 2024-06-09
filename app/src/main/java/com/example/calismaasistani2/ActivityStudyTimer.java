package com.example.calismaasistani2;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

public class ActivityStudyTimer extends AppCompatActivity {

    private static final String TAG = "ActivityStudyTimer";
    private static final String KEY_TIME_LEFT = "keyTimeLeft";
    private static final String KEY_TIMER_RUNNING = "keyTimerRunning";

    private TextView textViewTimer;
    private Button buttonStart;
    private Button buttonPause;
    private Button buttonReset;
    private Button buttonStartMusic;
    private Button buttonStopMusic;

    private CountDownTimer countDownTimer;
    private long timeLeftInMillis;
    private boolean timerRunning;
    private MediaPlayer mediaPlayerRock;
    private MediaPlayer mediaPlayerPop;
    private MediaPlayer mediaPlayerJazz;
    private MediaPlayer mediaPlayerClassical;
    private MediaPlayer mediaPlayerHipHop;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_timer);

        textViewTimer = findViewById(R.id.textViewTimer);
        buttonStart = findViewById(R.id.buttonStart);
        buttonPause = findViewById(R.id.buttonPause);
        buttonReset = findViewById(R.id.buttonReset);
        buttonStartMusic = findViewById(R.id.buttonStartMusic); // Initialize buttonStartMusic
        buttonStopMusic = findViewById(R.id.buttonStopMusic); // Initialize buttonStopMusic

        if (savedInstanceState != null) {
            timeLeftInMillis = savedInstanceState.getLong(KEY_TIME_LEFT);
            timerRunning = savedInstanceState.getBoolean(KEY_TIMER_RUNNING);
            if (timerRunning) {
                startTimer();
            } else {
                updateTimerText();
            }
        } else {
            // Retrieve study goal from DBHelper or intent
            DBHelper dbHelper = new DBHelper(this);
            String[] profileData = dbHelper.loadProfileData();
            String studyGoalString = profileData[1];
            int studyGoal = Integer.parseInt(studyGoalString);

            // Convert study goal from minutes to milliseconds
            timeLeftInMillis = studyGoal * 60 * 1000;

            updateTimerText();
        }

        buttonStart.setOnClickListener(v -> startTimer());
        buttonPause.setOnClickListener(v -> pauseTimer());
        buttonReset.setOnClickListener(v -> resetTimer());
        buttonStartMusic.setOnClickListener(v -> startMusic()); // Set click listener for buttonStartMusic
        buttonStopMusic.setOnClickListener(v -> pauseMusic()); // Set click listener for buttonStopMusic

        // Retrieve selected music genres from intent or SharedPreferences
        Intent intent = getIntent();
        boolean playRock = intent.getBooleanExtra("RockSelected", false);
        boolean playPop = intent.getBooleanExtra("PopSelected", false);
        boolean playJazz = intent.getBooleanExtra("JazzSelected", false);
        boolean playClassical = intent.getBooleanExtra("ClassicalSelected", false);
        boolean playHipHop = intent.getBooleanExtra("HipHopSelected", false);
        Log.d(TAG, "Rock: " + playRock);
        Log.d(TAG, "Pop: " + playPop);
        Log.d(TAG, "Jazz: " + playJazz);
        Log.d(TAG, "Classical: " + playClassical);
        Log.d(TAG, "HipHop: " + playHipHop);

        // Initialize and start MediaPlayer for selected genres
        if (playRock) {
            mediaPlayerRock = MediaPlayer.create(this, R.raw.rock_music);
            mediaPlayerRock.start();
            mediaPlayerRock.setLooping(true);
        }
        if (playPop) {
            mediaPlayerPop = MediaPlayer.create(this, R.raw.pop_music);
            mediaPlayerPop.start();
            mediaPlayerPop.setLooping(true);
        }
        if (playJazz) {
            mediaPlayerJazz = MediaPlayer.create(this, R.raw.jazz_music);
            mediaPlayerJazz.start();
            mediaPlayerJazz.setLooping(true);
        }
        if (playClassical) {
            mediaPlayerClassical = MediaPlayer.create(this, R.raw.classical_music);
            mediaPlayerClassical.start();
            mediaPlayerClassical.setLooping(true);
        }
        if (playHipHop) {
            mediaPlayerHipHop = MediaPlayer.create(this, R.raw.hip_hop_music);
            mediaPlayerHipHop.start();
            mediaPlayerHipHop.setLooping(true);
        }

        startTimer(); // Start the timer
        startMusic(); // Start the music

        buttonPause.setEnabled(timerRunning);
        buttonStart.setEnabled(!timerRunning);
    }




    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(KEY_TIME_LEFT, timeLeftInMillis);
        outState.putBoolean(KEY_TIMER_RUNNING, timerRunning);
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateTimerText();
            }

            @Override
            public void onFinish() {
                timerRunning = false;
                buttonStart.setEnabled(true);
                buttonPause.setEnabled(false);
                sendNotification();
                pauseMusic();
                saveGoalReached(); // Save goal reached status when timer finishes
            }
        }.start();

        timerRunning = true;
        buttonStart.setEnabled(false);
        buttonPause.setEnabled(true);
    }

    private void pauseTimer() {
        countDownTimer.cancel();
        timerRunning = false;
        buttonStart.setEnabled(true);
        buttonPause.setEnabled(false);
    }

    private void resetTimer() {
        countDownTimer.cancel();
        DBHelper dbHelper = new DBHelper(this);
        String[] profileData = dbHelper.loadProfileData();
        String studyGoalString = profileData[1];
        int studyGoal = Integer.parseInt(studyGoalString);

        timeLeftInMillis = studyGoal * 60 * 1000;
        updateTimerText();
        buttonStart.setEnabled(true);
        buttonPause.setEnabled(false);
    }

    private void updateTimerText() {
        int hours = (int) (timeLeftInMillis / 1000) / 3600;
        int minutes = (int) ((timeLeftInMillis / 1000) % 3600) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;

        String timeLeftFormatted = String.format("%02d:%02d:%02d", hours, minutes, seconds);
        textViewTimer.setText(timeLeftFormatted);
    }

    private void sendNotification() {
        Log.d(TAG, "Sending notification");

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intent = new Intent(this, ActivityStudyTimer.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Notification notification = new NotificationCompat.Builder(this, MyApplication.CHANNEL_ID)
                .setSmallIcon(R.drawable.check_mark)
                .setContentTitle("Study Timer")
                .setContentText("Time's up! Good job!")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setSound(alarmSound)
                .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 }) // Vibrate pattern: wait 1 second, vibrate 1 second
                .build();

        if (notificationManager != null) {
            notificationManager.notify(1, notification);
        }
    }

    // New method to save goal reached status
    private void saveGoalReached() {
        DBHelper dbHelper = new DBHelper(this);
        dbHelper.saveGoalReached();
    }
    private void startMusic() {
        if (mediaPlayerRock != null) {
            mediaPlayerRock.start();
        }
        if (mediaPlayerPop != null) {
            mediaPlayerPop.start();
        }
        if (mediaPlayerJazz != null) {
            mediaPlayerJazz.start();
        }
        if (mediaPlayerClassical != null) {
            mediaPlayerClassical.start();
        }
        if (mediaPlayerHipHop != null) {
            mediaPlayerHipHop.start();
        }
    }

    private void pauseMusic() {
        if (mediaPlayerRock != null && mediaPlayerRock.isPlaying()) {
            mediaPlayerRock.pause();
        }
        if (mediaPlayerPop != null && mediaPlayerPop.isPlaying()) {
            mediaPlayerPop.pause();
        }
        if (mediaPlayerJazz != null && mediaPlayerJazz.isPlaying()) {
            mediaPlayerJazz.pause();
        }
        if (mediaPlayerClassical != null && mediaPlayerClassical.isPlaying()) {
            mediaPlayerClassical.pause();
        }
        if (mediaPlayerHipHop != null && mediaPlayerHipHop.isPlaying()) {
            mediaPlayerHipHop.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayerRock != null) {
            mediaPlayerRock.release();
            mediaPlayerRock = null;
        }
        if (mediaPlayerPop != null) {
            mediaPlayerPop.release();
            mediaPlayerPop = null;
        }
        if (mediaPlayerJazz != null) {
            mediaPlayerJazz.release();
            mediaPlayerJazz = null;
        }
        if (mediaPlayerClassical != null) {
            mediaPlayerClassical.release();
            mediaPlayerClassical = null;
        }
        if (mediaPlayerHipHop != null) {
            mediaPlayerHipHop.release();
            mediaPlayerHipHop = null;
        }
    }

}
