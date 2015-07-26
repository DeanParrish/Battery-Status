package Classes;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Parrish on 6/16/2015.
 */
public class FeedReaderDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Battery_Status";

    public FeedReaderDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
          final String SQL_CREATE_BATTERY =
                "CREATE TABLE batteries ( " +
                        "userid INTEGER NOT NULL, " +
                        "name TEXT NOT NULL, " +
                        "cells TEXT, " +
                        "mah TEXT, " +
                        "cycles TEXT, " +
                        "type TEXT, " +
                        "PRIMARY KEY ( userid, name ), " +
                        "FOREIGN KEY(userid) REFERENCES users(id) )";

        final String SQL_CREATE_ENTRY =
                "CREATE TABLE entries ( " +
                        "userid INTEGER NOT NULL, " +
                        "id INTEGER, " +
                        "name TEXT, " +
                        "time LONG, " + //changed from INT
                        "start INT, " +
                        "end INT, " +
                        "date TEXT, " +
                        "dateTime TEXT, " +
                        "editDate TEXT, " +
                        "editTime TEXT, " +
                        "notes TEXT, "  +
                        "PRIMARY KEY ( userid, id ), " +
                        "FOREIGN KEY (userid) REFERENCES users(id) )";

        final String SQL_CREATE_USERS =
                "CREATE TABLE users ( " +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "email TEXT, " +
                        "password TEXT, " +
                        "question1 TEXT, " +
                        "answer1 TEXT, " +
                        "question2 TEXT, " +
                        "answer2 TEXT, " +
                        "question3 TEXT, " +
                        "answer3 TEXT, " +
                        "active TEXT, " +
                        "recent TEXT, " +
                        "createDate TEXT, " +
                        "loginDate TEXT )";

        db.execSQL(SQL_CREATE_BATTERY);
        db.execSQL(SQL_CREATE_ENTRY);
        db.execSQL(SQL_CREATE_USERS);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS batteries");
        db.execSQL("DROP TABLE IF EXISTS entries");
        db.execSQL("DROP TABLE IF EXISTS users");
        this.onCreate(db);
    }
}

