package com.example.task61d;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context) {
        super(context, "UserData.db", null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase DB) {
        // Table for user login and registration details
        DB.execSQL("CREATE TABLE Users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT UNIQUE, " +
                "email TEXT, " +
                "password TEXT, " +
                "phone_number TEXT, " +
                "interests TEXT)");

        // Table for question and answers
        DB.execSQL("CREATE TABLE Questions (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "user_id INTEGER, " +
                "question TEXT, " +
                "answer TEXT, " +
                "FOREIGN KEY(user_id) REFERENCES Users(id))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase DB, int oldVersion, int newVersion) {
        DB.execSQL("DROP TABLE IF EXISTS Questions");
        DB.execSQL("DROP TABLE IF EXISTS Users");
        onCreate(DB);
    }

    // ✅ Register new user
    public boolean registerUser(String username, String email, String password, String phone, String interests) {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("email", email);
        values.put("password", password);
        values.put("phone_number", phone);
        values.put("interests", interests);

        long result = DB.insert("Users", null, values);
        DB.close();
        return result != -1;
    }

    // ✅ Check login credentials
    public boolean loginUser(String username, String password) {
        SQLiteDatabase DB = this.getReadableDatabase();
        Cursor cursor = DB.rawQuery("SELECT * FROM Users WHERE username=? AND password=?", new String[]{username, password});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        DB.close();
        return exists;
    }
    // ✅ Update user's interests
    public boolean updateUserInterests(int userId, String interests) {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("interests", interests);

        int rows = DB.update("Users", values, "id=?", new String[]{String.valueOf(userId)});
        DB.close();
        return rows > 0;
    }

    public User getUserDetails(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        User user = null;

        try {
            cursor = db.rawQuery("SELECT * FROM Users WHERE username = ?", new String[]{username});
            if (cursor.moveToFirst()) {
                String email = cursor.getString(cursor.getColumnIndexOrThrow("email"));
                String password = cursor.getString(cursor.getColumnIndexOrThrow("password"));
                String phone = cursor.getString(cursor.getColumnIndexOrThrow("phone_number"));
                String interests=cursor.getString(cursor.getColumnIndexOrThrow("interests"));
                user = new User(username, email, password, phone,interests);

            }
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }
User.setCurrentUser(user);
        return user;
    }



    // ✅ Get user ID from username
    public int getUserId(String username) {
        SQLiteDatabase DB = this.getReadableDatabase();
        Cursor cursor = DB.rawQuery("SELECT id FROM Users WHERE username=?", new String[]{username});
        if (cursor.moveToFirst()) {
            int id = cursor.getInt(0);
            cursor.close();
            return id;
        }
        cursor.close();
        return -1;
    }

    // ✅ Insert question and answer
    public boolean insertQuestionAnswer(int userId, String question, String answer) {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("user_id", userId);
        values.put("question", question);
        values.put("answer", answer);

        long result = DB.insert("Questions", null, values);
        DB.close();
        return result != -1;
    }

    // ✅ Get all Q&A for a user
    public List<String> getUserQnA(int userId) {
        List<String> qnaList = new ArrayList<>();
        SQLiteDatabase DB = this.getReadableDatabase();
        Cursor cursor = DB.rawQuery("SELECT question, answer FROM Questions WHERE user_id=?", new String[]{String.valueOf(userId)});

        if (cursor.moveToFirst()) {
            do {
                String qna = "Q: " + cursor.getString(0) + "\nA: " + cursor.getString(1);
                qnaList.add(qna);
            } while (cursor.moveToNext());
        }

        cursor.close();
        DB.close();
        return qnaList;
    }

    // ✅ Delete user and all associated data
    public boolean deleteUser(int userId) {
        SQLiteDatabase DB = this.getWritableDatabase();
        DB.delete("Questions", "user_id=?", new String[]{String.valueOf(userId)});
        int rows = DB.delete("Users", "id=?", new String[]{String.valueOf(userId)});
        DB.close();
        return rows > 0;
    }
}
