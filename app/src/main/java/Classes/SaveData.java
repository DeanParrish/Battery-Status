package Classes;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

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
    private static String entryID = "id";
    private static String chanrgeTime = "time";
    private static String chargeStart = "start";
    private static String chargeEnd = "end";
    private FeedReaderDbHelper dbconnn;

    public SaveData() {

    }

    public SaveData(Context con) {
        context = con;
        dbconnn = new FeedReaderDbHelper(con);
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
                        new String[] { String.valueOf(name)},                 //selection value
                        null,                                                 //group by
                        null,                                                 //having
                        null,                                                 //order by
                        null);                                                //limit

        if (cursor != null){
            cursor.moveToFirst();
        }

        battery.setName(cursor.getString(0));
        battery.setCells(Integer.parseInt(cursor.getString(1)));
        battery.setMah(Integer.parseInt(cursor.getString(2)));
        battery.setCycles(Integer.parseInt(cursor.getString(3)));
        battery.setType(cursor.getString(4));

        return battery;
    }

    public List<Battery> getAllBatteries(){
        List<Battery> batteries = new LinkedList<Battery>();
        FeedReaderDbHelper dbcon = new FeedReaderDbHelper(context);
        Battery battery;
        String query = "SELECT * FROM " + batteryTableName;

        db = dbcon.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()){
            do {
                battery = new Battery();
                battery.setName(cursor.getString(0));
                battery.setCells(Integer.parseInt(cursor.getString(1)));
                battery.setMah(Integer.parseInt(cursor.getString(2)));
                battery.setCycles(Integer.parseInt(cursor.getString(3)));
                battery.setType(cursor.getString(4));

                batteries.add(battery);
            }while (cursor.moveToNext());
        }
        return batteries;
    }

    public void addEntry(String name, long time, int start, int end) {
        FeedReaderDbHelper dbcon = new FeedReaderDbHelper(context);
        ContentValues values = new ContentValues();

        //add values to be inserted
        values.put(batteryName, name);
        values.put(chanrgeTime, time);
        values.put(chargeStart, start);
        values.put(chargeEnd, end);

        db = dbcon.getWritableDatabase();

        db.insert(entryTableName,    //table name
                null,           //column hack
                values);        //column and value

        db.close();
    }

    public List<Entry> getAllEntries(){
        List<Entry> entries = new LinkedList<Entry>();
        //FeedReaderDbHelper dbcon = new FeedReaderDbHelper(context);

        String query = "SELECT * FROM " + entryTableName;

        db = dbconnn.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        Entry entry;
        if (cursor.moveToFirst()){
            do {
                entry = new Entry();
                entry.setBatteryName(cursor.getString(0));
                entry.setRunTime(Long.parseLong(cursor.getString(1)));
                entry.setStartCharge(Integer.parseInt(cursor.getString(2)));
                entry.setEndCharge(Integer.parseInt(cursor.getString(3)));

                entries.add(entry);
            } while (cursor.moveToNext());
        }

        return entries;
    }
}




