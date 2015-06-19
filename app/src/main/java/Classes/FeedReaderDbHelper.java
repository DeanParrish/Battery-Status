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
    public static final String DATABASE_NAME = "Battery_Statuses";

    public FeedReaderDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
          final String SQL_CREATE_BATTERY =
                "CREATE TABLE batteries ( " +
                        "name TEXT PRIMARY KEY, " +
                        "cells TEXT, " +
                        "mah TEXT, " +
                        "cycles TEXT, " +
                        "type TEXT )";

        final String SQL_CREATE_ENTRY =
                "CREATE TABLE entries ( " +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "name TEXT, " +
                        "time LONG, " + //changed from INT
                        "start INT, " +
                        "end INT )";


        db.execSQL(SQL_CREATE_BATTERY);
        db.execSQL(SQL_CREATE_ENTRY);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS batteries");
        this.onCreate(db);
    }

/*    public String insert(ContentValues values){

    }*/
}

