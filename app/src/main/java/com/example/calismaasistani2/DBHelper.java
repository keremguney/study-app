package com.example.calismaasistani2;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "Register.db";
    private static final String TABLE_USERS = "users";
    private static final String TABLE_GOAL_ACHIEVEMENTS = "goal_achievements";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_FULL_NAME = "full_name";
    private static final String COLUMN_STUDY_GOAL = "study_goal";
    private static final String COLUMN_DATE = "date";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private static final String PREF_NAME = "SessionPrefs";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_FULL_NAME = "full_name";

    public DBHelper(@Nullable Context context) {
        super(context, DB_NAME, null, 1);
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createUsersTable = "CREATE TABLE " + TABLE_USERS + " (" +
                COLUMN_USERNAME + " TEXT PRIMARY KEY, " +
                COLUMN_PASSWORD + " TEXT, " +
                COLUMN_FULL_NAME + " TEXT, " +
                COLUMN_STUDY_GOAL + " TEXT)";
        db.execSQL(createUsersTable);

        String createGoalsTable = "CREATE TABLE " + TABLE_GOAL_ACHIEVEMENTS + " (" +
                COLUMN_DATE + " TEXT PRIMARY KEY)";
        db.execSQL(createGoalsTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GOAL_ACHIEVEMENTS);
        onCreate(db);
    }

    public boolean insertData(String username, String password, Context context) {
        if (username.length() < 4 || password.length() < 4) {
            Toast.makeText(context, "Username and password must be at least 4 characters long.", Toast.LENGTH_SHORT).show();
            return false;
        }

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_USERNAME, username);
        contentValues.put(COLUMN_PASSWORD, password);

        long result = db.insert(TABLE_USERS, null, contentValues);
        return result != -1;
    }

    public boolean checkUsername(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_USERNAME + " = ?", new String[]{username});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public boolean checkFullName(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + COLUMN_FULL_NAME + " FROM " + TABLE_USERS + " WHERE " + COLUMN_USERNAME + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{username});
        boolean hasFullName = false;
        if (cursor.moveToFirst()) {
            @SuppressLint("Range") String fullName = cursor.getString(cursor.getColumnIndex(COLUMN_FULL_NAME));
            hasFullName = fullName != null && !fullName.isEmpty();
        }
        cursor.close();
        return hasFullName;
    }

    public boolean checkStudyGoal(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + COLUMN_STUDY_GOAL + " FROM " + TABLE_USERS + " WHERE " + COLUMN_USERNAME + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{username});
        boolean hasStudyGoal = false;
        if (cursor.moveToFirst()) {
            @SuppressLint("Range") String studyGoal = cursor.getString(cursor.getColumnIndex(COLUMN_STUDY_GOAL));
            hasStudyGoal = studyGoal != null && !studyGoal.isEmpty();
        }
        cursor.close();
        return hasStudyGoal;
    }

    public boolean addFullName(String username, String fullName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_FULL_NAME, fullName);

        String query = "SELECT " + COLUMN_USERNAME + " FROM " + TABLE_USERS + " WHERE " + COLUMN_USERNAME + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{username});
        boolean userExists = cursor.moveToFirst();
        cursor.close();

        long result;
        if (userExists) {
            result = db.update(TABLE_USERS, values, COLUMN_USERNAME + " = ?", new String[]{username});
        } else {
            values.put(COLUMN_USERNAME, username);
            result = db.insert(TABLE_USERS, null, values);
        }
        return result != -1;
    }

    public boolean addStudyGoal(String username, String studyGoal) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_STUDY_GOAL, studyGoal);

        String query = "SELECT " + COLUMN_USERNAME + " FROM " + TABLE_USERS + " WHERE " + COLUMN_USERNAME + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{username});
        boolean userExists = cursor.moveToFirst();
        cursor.close();

        long result;
        if (userExists) {
            result = db.update(TABLE_USERS, values, COLUMN_USERNAME + " = ?", new String[]{username});
        } else {
            values.put(COLUMN_USERNAME, username);
            result = db.insert(TABLE_USERS, null, values);
        }
        return result != -1;
    }

    public boolean checkUser(String username, String pwd) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_USERNAME + " = ? AND " + COLUMN_PASSWORD + " = ?", new String[]{username, pwd});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    // Session management methods

    public void setLoginSession(String username) {
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.putString(KEY_USERNAME, username);
        editor.apply();
    }

    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public String getLoggedInUsername() {
        return sharedPreferences.getString(KEY_USERNAME, null);
    }

    public String getLoggedInFullName() {
        String username = getLoggedInUsername();
        if (username == null) {
            return null;
        }
        SQLiteDatabase db = this.getReadableDatabase();
        String fullName = null;
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_FULL_NAME + " FROM " + TABLE_USERS + " WHERE " + COLUMN_USERNAME + " = ?", new String[]{username});
        if (cursor.moveToFirst()) {
            fullName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FULL_NAME));
        }
        cursor.close();
        return fullName;
    }

    public void saveProfileData(String name, String studyGoal) {
        String username = getLoggedInUsername();
        if (username == null) {
            return;
        }
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_FULL_NAME, name);
        values.put(COLUMN_STUDY_GOAL, studyGoal);

        db.update(TABLE_USERS, values, COLUMN_USERNAME + " = ?", new String[]{username});
    }

    @SuppressLint("Range")
    public String[] loadProfileData() {
        String username = getLoggedInUsername();
        if (username == null) {
            return new String[]{"", "0"};
        }
        SQLiteDatabase db = this.getReadableDatabase();
        String[] profileData = new String[2];

        Cursor cursor = db.rawQuery("SELECT " + COLUMN_FULL_NAME + ", " + COLUMN_STUDY_GOAL + " FROM " + TABLE_USERS + " WHERE " + COLUMN_USERNAME + " = ?", new String[]{username});
        if (cursor.moveToFirst()) {
            profileData[0] = cursor.getString(cursor.getColumnIndex(COLUMN_FULL_NAME));
            profileData[1] = cursor.getString(cursor.getColumnIndex(COLUMN_STUDY_GOAL));
        } else {
            profileData[0] = "";
            profileData[1] = "0";
        }
        cursor.close();
        return profileData;
    }

    public void logout() {
        editor.clear();
        editor.apply();
    }

    // New method to save goal reached date
    public void saveGoalReached() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        values.put(COLUMN_DATE, currentDate);

        db.insert(TABLE_GOAL_ACHIEVEMENTS, null, values);
    }

    // New method to get all goal reached dates
    @SuppressLint("Range")
    public String[] getGoalReachedDates() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_DATE + " FROM " + TABLE_GOAL_ACHIEVEMENTS, null);
        String[] dates = new String[cursor.getCount()];
        int i = 0;
        if (cursor.moveToFirst()) {
            do {
                dates[i] = cursor.getString(cursor.getColumnIndex(COLUMN_DATE));
                i++;
            } while (cursor.moveToNext());
        }
        cursor.close();
        return dates;
    }
}
