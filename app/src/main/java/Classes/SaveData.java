package Classes;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Dean Parrish on 6/15/2015.
 */
public class SaveData {

    private Context context;
    private SQLiteDatabase db;
    private static String batteryTableName = "batteries";
    private static String batteryName = "name";
    private static String batteryCell = "cells";
    private static String batteryMah = "mah";
    private static String batteryCycles = "cycles";
    private static String batteryType = "type";
    private static String entryTableName = "entries";
    private static String chargeTime = "time";
    private static String chargeStart = "start";
    private static String chargeEnd = "end";
    private static String entryDate = "date";
    private static String entryTime = "dateTime";
    private static String entryNotes = "notes";
    private static String entryEditDate = "editDate";
    private static String entryEditTime = "editTime";

    public SaveData() {

    }

    public SaveData(Context con) {
        context = con;
        //dbconnn = new FeedReaderDbHelper(con);
    }

    public void addBattery(String name, int cells, int mah, int cycles, String type) {
        FeedReaderDbHelper dbcon = new FeedReaderDbHelper(context);
        ContentValues values = new ContentValues();

        //add values to be inserted
        values.put(batteryName, name);
        values.put(batteryCell, cells);
        values.put(batteryMah, mah);
        values.put(batteryCycles, cycles);
        values.put(batteryType, type);

        db = dbcon.getWritableDatabase();

        db.insert(batteryTableName, //table
                null,        //column hack
                values);     //column and values; populated above

        db.close();
    }

    public Battery getBattery(String name) {
        FeedReaderDbHelper dbcon = new FeedReaderDbHelper(context);
        String[] columns = {batteryName, batteryCell, batteryMah, batteryCycles, batteryType};
        Battery battery = new Battery();

        db = dbcon.getReadableDatabase();

        Cursor cursor =
                db.query(batteryTableName,                                           //table name
                        columns,                                              //column names
                        " name = ?",                                            //selections
                        new String[]{String.valueOf(name)},                 //selection value
                        null,                                                 //group by
                        null,                                                 //having
                        null,                                                 //order by
                        null);                                                //limit

        if (cursor != null) {
            cursor.moveToFirst();
            battery.setName(cursor.getString(0));
            battery.setCells(Integer.parseInt(cursor.getString(1)));
            battery.setMah(Integer.parseInt(cursor.getString(2)));
            battery.setCycles(Integer.parseInt(cursor.getString(3)));
            battery.setType(cursor.getString(4));
        }
        return battery;
    }

    public List<Battery> getAllBatteries() {
        List<Battery> batteries = new LinkedList<>();
        FeedReaderDbHelper dbcon = new FeedReaderDbHelper(context);
        Battery battery;
        String query = "SELECT * FROM " + batteryTableName;

        db = dbcon.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                battery = new Battery();
                battery.setName(cursor.getString(0));
                battery.setCells(Integer.parseInt(cursor.getString(1)));
                battery.setMah(Integer.parseInt(cursor.getString(2)));
                battery.setCycles(Integer.parseInt(cursor.getString(3)));
                battery.setType(cursor.getString(4));

                batteries.add(battery);
            } while (cursor.moveToNext());
        }
        return batteries;
    }

    public void update(){
        FeedReaderDbHelper dbcon = new FeedReaderDbHelper(context);

        db = dbcon.getWritableDatabase();

        dbcon.onUpgrade(db, 1, 2);

        db.close();
    }

    public void deleteBattery(String name){
        FeedReaderDbHelper dbcon = new FeedReaderDbHelper(context);
        String[] whereArgs = new String[] {
                name,
        };

        db = dbcon.getWritableDatabase();

        db.delete(batteryTableName, "name = ?", whereArgs);

        db.delete(entryTableName, "name = ?", whereArgs);

        db.close();
    }
    public void updateBattery(String name, int cells, int mah, int cycles, String type){
        FeedReaderDbHelper dbcon = new FeedReaderDbHelper(context);
        String[] whereArgs = new String[] {
                name,
        };
        ContentValues values = new ContentValues();

        values.put(batteryCell, cells);
        values.put(batteryMah, mah);
        values.put(batteryCycles, cycles);
        values.put(batteryType, type);

        try {
            db = dbcon.getWritableDatabase();
            db.update(batteryTableName, values, "name = ?", whereArgs);
            db.close();
        } catch (SQLiteException e){
            Log.e("Update Battery", e.toString());
        }
    }

    public void addEntry(String name, long time, int start, int end, String notes) {
        FeedReaderDbHelper dbcon = new FeedReaderDbHelper(context);
        ContentValues values = new ContentValues();
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        String date = dateFormat.format(calendar.getTime());
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        String dateTime = timeFormat.format(calendar.getTime());

        int runTime;

        runTime = Integer.parseInt(Long.toString(time));

        //add values to be inserted
        values.put(batteryName, name);
        values.put(chargeTime, runTime);
        values.put(chargeStart, start);
        values.put(chargeEnd, end);
        values.put(entryDate, date);
        values.put(entryTime, dateTime);
        values.put(entryEditDate, "");
        values.put(entryEditTime, "");
        values.put(entryNotes, notes);


        db = dbcon.getWritableDatabase();

        db.insert(entryTableName,    //table name
                null,           //column hack
                values);        //column and value

        db.close();
    }

    public Entry getEntry(int id) {
        Entry entry;
        FeedReaderDbHelper dbcon = new FeedReaderDbHelper(context);

        String query = "Select * FROM " + entryTableName + " WHERE id = ?";
        String[] whereArgs = new String[] {
                Integer.toString(id),
        };

        db = dbcon.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, whereArgs);
        entry = new Entry();
        if (cursor.moveToFirst()){
            entry.setBatteryName(cursor.getString(1));
            entry.setRunTime(cursor.getInt(2));
            entry.setStartCharge(Integer.parseInt(cursor.getString(3)));
            entry.setEndCharge(Integer.parseInt(cursor.getString(4)));
            entry.setEntryDate(cursor.getString(5));
            entry.setEntryTime(cursor.getString(6));
            entry.setEditDate(cursor.getString(7));
            entry.setEditDate(cursor.getString(8));
            entry.setNotes(cursor.getString(9));
        }

        return entry;
    }

    public List<Entry> getAllEntries() {
        List<Entry> entries = new LinkedList<>();
        FeedReaderDbHelper dbcon = new FeedReaderDbHelper(context);

        String query = "SELECT * FROM " + entryTableName;

        db = dbcon.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        Entry entry;
        if (cursor.moveToFirst()) {
            do {
                entry = new Entry();
                entry.setBatteryName(cursor.getString(1));
                entry.setRunTime(cursor.getInt(2));
                entry.setStartCharge(Integer.parseInt(cursor.getString(3)));
                entry.setEndCharge(Integer.parseInt(cursor.getString(4)));
                entry.setEntryDate(cursor.getString(5));
                entry.setEntryTime(cursor.getString(6));
                entry.setEditDate(cursor.getString(7));
                entry.setEditTime(cursor.getString(8));
                entry.setNotes(cursor.getString(9));

                entries.add(entry);
            } while (cursor.moveToNext());
        }

        return entries;
    }
    public List<Entry> getAllEntriesForBattery(String name) {
        List<Entry> entries = new LinkedList<>();
        FeedReaderDbHelper dbcon = new FeedReaderDbHelper(context);
        int runTime;

        String query = "SELECT * FROM " + entryTableName + " WHERE name = ?";
        String[] whereArgs = new String[] {
                name,
        };
        db = dbcon.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, whereArgs);

        Entry entry;
        if (cursor.moveToFirst()) {
            do {
                entry = new Entry();
                entry.setId(Integer.parseInt(cursor.getString(0)));
                entry.setBatteryName(cursor.getString(1));
                entry.setRunTime(cursor.getInt(2));
                entry.setStartCharge(Integer.parseInt(cursor.getString(3)));
                entry.setEndCharge(Integer.parseInt(cursor.getString(4)));
                entry.setEntryDate(cursor.getString(5));
                entry.setEntryTime(cursor.getString(6));
                entry.setEditDate(cursor.getString(7));
                entry.setEditTime(cursor.getString(8));
                entry.setNotes(cursor.getString(9));

                entries.add(entry);
            } while (cursor.moveToNext());
        }

        return entries;
    }

    public void deleteEntry(int id) {
        FeedReaderDbHelper dbcon = new FeedReaderDbHelper(context);
        String[] whereArgs = new String[] {
                Integer.toString(id),
        };

        db = dbcon.getWritableDatabase();

        db.delete(entryTableName, "id = ?", whereArgs);
        db.close();

    }

    public void updateEntry(int id, String name, String time, int start, int end, String notes){
        FeedReaderDbHelper dbcon = new FeedReaderDbHelper(context);
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        String date = dateFormat.format(calendar.getTime());
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        String dateTime = timeFormat.format(calendar.getTime());

        String[] whereArgs = new String[] {
                Integer.toString(id),
        };
        ContentValues values = new ContentValues();

        values.put(batteryName, name);
        values.put(chargeTime, time);
        values.put(chargeStart, start);
        values.put(chargeEnd, end);
        values.put(entryEditDate, date);
        values.put(entryEditTime, dateTime);
        values.put(entryNotes, notes);

        db = dbcon.getWritableDatabase();

        db.update(entryTableName, values, "id = ?", whereArgs);

        db.close();
    }

    public void upgrade(int oldVer, int newVer){
        FeedReaderDbHelper dbcon = new FeedReaderDbHelper(context);
        db = dbcon.getWritableDatabase();
        dbcon.onUpgrade(db, oldVer, newVer);
        db.close();
    }
}




