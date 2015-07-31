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
    private static String batteryUserID = "userid";
    private static String batteryName = "name";
    private static String batteryCell = "cells";
    private static String batteryMah = "mah";
    private static String batteryCycles = "cycles";
    private static String batteryType = "type";
    private static String entryTableName = "entries";
    private static String entryUserID = "userid";
    private static String entryID = "id";
    private static String chargeTime = "time";
    private static String chargeStart = "start";
    private static String chargeEnd = "end";
    private static String entryDate = "date";
    private static String entryTime = "dateTime";
    private static String entryNotes = "notes";
    private static String entryEditDate = "editDate";
    private static String entryEditTime = "editTime";
    private static String usersTableName = "users";
    private static String userEmail = "email";
    private static String userpassword = "password";
    private static String userQuestion1 = "question1";
    private static String userAnswer1 = "answer1";
    private static String userQuestion2 = "question2";
    private static String userAnswer2 = "answer2";
    private static String userQuestion3 = "question3";
    private static String userAnswer3 = "answer3";
    private static String userActive = "active";
    private static String userRecent = "recent";
    private static String userCreateDate = "createDate";
    private static String userLoginDate = "loginDate";
    public SaveData() {

    }

    public SaveData(Context con) {
        context = con;
    }

    public void addBattery(Integer userID, String name, int cells, int mah, int cycles, String type) {
        FeedReaderDbHelper dbcon = new FeedReaderDbHelper(context);
        ContentValues values = new ContentValues();

        //add values to be inserted
        if (userID == null){
            values.putNull(batteryUserID);
        } else {
            values.put(batteryUserID, userID);
        }
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

    public Battery getBattery(Integer userID, String name) {
        FeedReaderDbHelper dbcon = new FeedReaderDbHelper(context);
        String[] columns = {batteryName, batteryCell, batteryMah, batteryCycles, batteryType};
        Battery battery = new Battery();

        db = dbcon.getReadableDatabase();
        if (userID != null) {
            Cursor cursor =
                    db.query(batteryTableName,                                    //table name
                            columns,                                              //column names
                            "userid = ? AND name = ?",                            //selections
                            new String[]{Integer.toString(userID),
                                    String.valueOf(name)},                        //selection value
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
        } else {
            Cursor cursor =
                    db.query(batteryTableName,                                    //table name
                            columns,                                              //column names
                            "userid IS NULL AND name = ?",                        //selections
                            new String[]{String.valueOf(name)},                   //selection value
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
                battery.setUserID(cursor.getInt(0));
                battery.setName(cursor.getString(1));
                battery.setCells(Integer.parseInt(cursor.getString(2)));
                battery.setMah(Integer.parseInt(cursor.getString(3)));
                battery.setCycles(Integer.parseInt(cursor.getString(4)));
                battery.setType(cursor.getString(5));

                batteries.add(battery);
            } while (cursor.moveToNext());
        }
        return batteries;
    }

    public List<Battery> getAllBatteriesUser(Integer userid){
        List<Battery> batteries = new LinkedList<>();
        String[] columns = {batteryUserID, batteryName, batteryCell, batteryMah, batteryCycles, batteryType};
        FeedReaderDbHelper dbcon = new FeedReaderDbHelper(context);
        Battery battery;

        db = dbcon.getReadableDatabase();

        if (userid != null) {
            Cursor cursor =
                    db.query(batteryTableName,                                           //table name
                            columns,                                              //column names
                            " userid = ?",                                            //selections
                            new String[]{Integer.toString(userid)},                 //selection value
                            null,                                                 //group by
                            null,                                                 //having
                            null,                                                 //order by
                            null);                                                //limit

            if (cursor.moveToFirst()) {
                do {
                    battery = new Battery();
                    battery.setUserID(cursor.getInt(0));
                    battery.setName(cursor.getString(1));
                    battery.setCells(Integer.parseInt(cursor.getString(2)));
                    battery.setMah(Integer.parseInt(cursor.getString(3)));
                    battery.setCycles(Integer.parseInt(cursor.getString(4)));
                    battery.setType(cursor.getString(5));

                    batteries.add(battery);
                } while (cursor.moveToNext());
            }
        } else {
            Cursor cursor =
                    db.query(batteryTableName,                                    //table name
                            columns,                                              //column names
                            "userid IS NULL",                                     //selections
                            null,                                                 //selection value
                            null,                                                 //group by
                            null,                                                 //having
                            null,                                                 //order by
                            null);                                                //limit

            if (cursor.moveToFirst()) {
                do {
                    battery = new Battery();
                    battery.setUserID(cursor.getInt(0));
                    battery.setName(cursor.getString(1));
                    battery.setCells(Integer.parseInt(cursor.getString(2)));
                    battery.setMah(Integer.parseInt(cursor.getString(3)));
                    battery.setCycles(Integer.parseInt(cursor.getString(4)));
                    battery.setType(cursor.getString(5));

                    batteries.add(battery);
                } while (cursor.moveToNext());
            }
        }
        return batteries;
    }

    public void update(){
        FeedReaderDbHelper dbcon = new FeedReaderDbHelper(context);

        db = dbcon.getWritableDatabase();

        dbcon.onUpgrade(db, 1, 2);

        db.close();
    }

    public void deleteBattery(Integer userID, String name){
        FeedReaderDbHelper dbcon = new FeedReaderDbHelper(context);


        db = dbcon.getWritableDatabase();
        if (userID != null) {
            String[] whereArgs = new String[] {
                    Integer.toString(userID),
                    name,
            };
            db.delete(batteryTableName, "userid = ? AND name = ?", whereArgs);
            db.delete(entryTableName, "userid = ? AND name = ?", whereArgs);
        } else {
            String[] whereArgs = new String[] {
                    name,
            };
            db.delete(batteryTableName, "userid IS NULL AND name = ?", whereArgs);
            db.delete(entryTableName, "userid IS NULL AND name = ?", whereArgs);
        }

        db.close();
    }
    public void updateBattery(Integer userID, String name, int cells, int mah, int cycles, String type){
        FeedReaderDbHelper dbcon = new FeedReaderDbHelper(context);

        ContentValues values = new ContentValues();

        values.put(batteryCell, cells);
        values.put(batteryMah, mah);
        values.put(batteryCycles, cycles);
        values.put(batteryType, type);

        if (userID != null) {
            try {
                String[] whereArgs = new String[] {
                        Integer.toString(userID),
                        name,
                };
                db = dbcon.getWritableDatabase();
                db.update(batteryTableName, values, "userid = ? AND name = ?", whereArgs);
                db.close();
            } catch (SQLiteException e) {
                Log.e("Update Battery", e.toString());
            }
        } else {
            String[] whereArgs = new String[] {
                    name,
            };
            db = dbcon.getWritableDatabase();
            db.update(batteryTableName, values, "userid IS NULL AND name = ?", whereArgs);
            db.close();
        }
    }

    public Integer getLastEntryId(Integer userID){
        FeedReaderDbHelper dbcon = new FeedReaderDbHelper(context);
        List<Integer> integerList = new LinkedList<>();
        String query = "Select id FROM " + entryTableName + " WHERE userid = ?";
        String[] whereArgs = new String[] {
                Integer.toString(userID),
        };

        db = dbcon.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, whereArgs);

        if (cursor.moveToLast()){
            return cursor.getInt(0) + 1;
        } else {
            return 0;
        }
    }

    public void addEntry(Integer userId, Integer id, String name, long time, int start, int end, String notes) {
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
        if (userId == null){
            values.putNull(entryUserID);
        } else {
            values.put(entryUserID, userId);
        }
        values.put(entryID, id);
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

    public Entry getEntry(Integer userID, Integer id) {
        Entry entry;
        FeedReaderDbHelper dbcon = new FeedReaderDbHelper(context);

        if (userID != null) {
            String query = "Select * FROM " + entryTableName + " WHERE userid = ? AND id = ?";
            String[] whereArgs = new String[]{
                    Integer.toString(userID),
                    Integer.toString(id),
            };

            db = dbcon.getReadableDatabase();

            Cursor cursor = db.rawQuery(query, whereArgs);
            entry = new Entry();
            if (cursor.moveToFirst()) {
                entry.setUserID(cursor.getInt(0));
                entry.setId(cursor.getInt(1));
                entry.setBatteryName(cursor.getString(2));
                entry.setRunTime(cursor.getInt(3));
                entry.setStartCharge(Integer.parseInt(cursor.getString(4)));
                entry.setEndCharge(Integer.parseInt(cursor.getString(5)));
                entry.setEntryDate(cursor.getString(6));
                entry.setEntryTime(cursor.getString(7));
                entry.setEditDate(cursor.getString(8));
                entry.setEditDate(cursor.getString(9));
                entry.setNotes(cursor.getString(10));
            }
        } else {
            String query = "Select * FROM " + entryTableName + " WHERE userid IS NULL AND id = ?";
            String[] whereArgs = new String[]{
                    Integer.toString(id),
            };

            db = dbcon.getReadableDatabase();

            Cursor cursor = db.rawQuery(query, whereArgs);
            entry = new Entry();
            if (cursor.moveToFirst()) {
                entry.setUserID(cursor.getInt(0));
                entry.setId(cursor.getInt(1));
                entry.setBatteryName(cursor.getString(2));
                entry.setRunTime(cursor.getInt(3));
                entry.setStartCharge(Integer.parseInt(cursor.getString(4)));
                entry.setEndCharge(Integer.parseInt(cursor.getString(5)));
                entry.setEntryDate(cursor.getString(6));
                entry.setEntryTime(cursor.getString(7));
                entry.setEditDate(cursor.getString(8));
                entry.setEditDate(cursor.getString(9));
                entry.setNotes(cursor.getString(10));
            }
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
                entry.setUserID(cursor.getInt(0));
                entry.setId(cursor.getInt(1));
                entry.setBatteryName(cursor.getString(2));
                entry.setRunTime(cursor.getInt(3));
                entry.setStartCharge(Integer.parseInt(cursor.getString(4)));
                entry.setEndCharge(Integer.parseInt(cursor.getString(5)));
                entry.setEntryDate(cursor.getString(6));
                entry.setEntryTime(cursor.getString(7));
                entry.setEditDate(cursor.getString(8));
                entry.setEditDate(cursor.getString(9));
                entry.setNotes(cursor.getString(10));

                entries.add(entry);
            } while (cursor.moveToNext());
        }

        return entries;
    }
    public List<Entry> getAllEntriesForBattery(Integer userID, String name) {
        List<Entry> entries = new LinkedList<>();
        FeedReaderDbHelper dbcon = new FeedReaderDbHelper(context);

        if (userID != null) {
            String query = "SELECT * FROM " + entryTableName + " WHERE userid = ? AND name = ?";
            String[] whereArgs = new String[]{
                    Integer.toString(userID),
                    name,
            };
            db = dbcon.getReadableDatabase();

            Cursor cursor = db.rawQuery(query, whereArgs);

            Entry entry;
            if (cursor.moveToFirst()) {
                do {
                    entry = new Entry();
                    entry.setUserID(cursor.getInt(0));
                    entry.setId(cursor.getInt(1));
                    entry.setBatteryName(cursor.getString(2));
                    entry.setRunTime(cursor.getInt(3));
                    entry.setStartCharge(Integer.parseInt(cursor.getString(4)));
                    entry.setEndCharge(Integer.parseInt(cursor.getString(5)));
                    entry.setEntryDate(cursor.getString(6));
                    entry.setEntryTime(cursor.getString(7));
                    entry.setEditDate(cursor.getString(8));
                    entry.setEditTime(cursor.getString(9));
                    entry.setNotes(cursor.getString(10));
                    entry.setSynced(cursor.getString(11));

                    entries.add(entry);
                } while (cursor.moveToNext());
            }
        } else {
            String query = "SELECT * FROM " + entryTableName + " WHERE userid IS NULL AND name = ?";
            String[] whereArgs = new String[]{
                    name,
            };
            db = dbcon.getReadableDatabase();

            Cursor cursor = db.rawQuery(query, whereArgs);

            Entry entry;
            if (cursor.moveToFirst()) {
                do {
                    entry = new Entry();
                    entry.setUserID(cursor.getInt(0));
                    entry.setId(cursor.getInt(1));
                    entry.setBatteryName(cursor.getString(2));
                    entry.setRunTime(cursor.getInt(3));
                    entry.setStartCharge(Integer.parseInt(cursor.getString(4)));
                    entry.setEndCharge(Integer.parseInt(cursor.getString(5)));
                    entry.setEntryDate(cursor.getString(6));
                    entry.setEntryTime(cursor.getString(7));
                    entry.setEditDate(cursor.getString(8));
                    entry.setEditTime(cursor.getString(9));
                    entry.setNotes(cursor.getString(10));
                    entry.setSynced(cursor.getString(11));

                    entries.add(entry);
                } while (cursor.moveToNext());
            }
        }
        return entries;
    }

    public void deleteEntry(Integer userID, Integer id) {
        FeedReaderDbHelper dbcon = new FeedReaderDbHelper(context);

        if (userID != null) {
            String[] whereArgs = new String[]{
                    Integer.toString(userID),
                    Integer.toString(id),
            };

            db = dbcon.getWritableDatabase();

            db.delete(entryTableName, "userid = ? AND id = ?", whereArgs);
        } else {
            String[] whereArgs = new String[]{
                    Integer.toString(id),
            };

            db = dbcon.getWritableDatabase();

            db.delete(entryTableName, "userid IS NULL AND id = ?", whereArgs);
        }
        db.close();

    }

    public void updateEntry(Integer userid, int id, String name, long time, int start, int end, String notes){
        FeedReaderDbHelper dbcon = new FeedReaderDbHelper(context);
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        String date = dateFormat.format(calendar.getTime());
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        String dateTime = timeFormat.format(calendar.getTime());

        String[] whereArgs = new String[] {
                Integer.toString(userid),
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

        db.update(entryTableName, values, "userid = ? AND id = ?", whereArgs);

        db.close();
    }

    public void addUser(String email, String password, String question1, String answer1, String question2, String answer2,
                        String question3, String answer3){
        FeedReaderDbHelper dbcon = new FeedReaderDbHelper(context);
        ContentValues values = new ContentValues();
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        String date = dateFormat.format(calendar.getTime());

        values.put(userEmail, email);
        values.put(userpassword, password);
        values.put(userQuestion1, question1);
        values.put(userAnswer1, answer1.toLowerCase());
        values.put(userQuestion2, question2);
        values.put(userAnswer2, answer2.toLowerCase());
        values.put(userQuestion3, question3);
        values.put(userAnswer3, answer3.toLowerCase());
        values.put(userActive, "X");
        values.put(userRecent, "X");
        values.put(userCreateDate, date);
        values.put(userLoginDate, date);

        db = dbcon.getWritableDatabase();

        db.insert(usersTableName, null, values);

        db.close();
    }

    public User getUser(int id){
        FeedReaderDbHelper dbcon = new FeedReaderDbHelper(context);

        String query = "Select * FROM " + usersTableName + " WHERE id = ?";
        String[] whereArgs = new String[] {
                Integer.toString(id),
        };

        db = dbcon.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, whereArgs);
        User user = new User();
        if (cursor.moveToFirst()){
            user.setID(cursor.getInt(0));
            user.setEmail(cursor.getString(1));
            user.setPassword(cursor.getString(2));
            user.setQuestion1(cursor.getString(3));
            user.setAnswer1(cursor.getString(4));
            user.setQuestion2(cursor.getString(5));
            user.setAnswer2(cursor.getString(6));
            user.setQuestion3(cursor.getString(7));
            user.setAnswer3(cursor.getString(8));
            user.setActive(cursor.getString(9));
            user.setRecent(cursor.getString(10));
            user.setCreateDate(cursor.getString(11));
            user.setLoginDate(cursor.getString(12));
        }

        return user;
    }

    public User getUserByEmail(String email){
        FeedReaderDbHelper dbcon = new FeedReaderDbHelper(context);
        String query;
        Cursor cursor;
        db = dbcon.getReadableDatabase();
        if (email == null){
            query = "SELECT * FROM " + usersTableName + " WHERE email IS NULL";
            cursor = db.rawQuery(query, null);
        } else {
            query = "Select * FROM " + usersTableName + " WHERE email = ?";
            String[] whereArgs = new String[]{
                    email,
            };
            cursor = db.rawQuery(query, whereArgs);
        }

        User user = new User();
        if (cursor.moveToFirst()){
            user.setID(cursor.getInt(0));
            user.setEmail(cursor.getString(1));
            user.setPassword(cursor.getString(2));
            user.setQuestion1(cursor.getString(3));
            user.setAnswer1(cursor.getString(4));
            user.setQuestion2(cursor.getString(5));
            user.setAnswer2(cursor.getString(6));
            user.setQuestion3(cursor.getString(7));
            user.setAnswer3(cursor.getString(8));
            user.setActive(cursor.getString(9));
            user.setRecent(cursor.getString(10));
            user.setCreateDate(cursor.getString(11));
            user.setLoginDate(cursor.getString(12));
        }
;
        return user;
    }

    public List<String> getAllUsersEmail(){
        List<String> listEmail = new LinkedList<>();
        FeedReaderDbHelper dbcon = new FeedReaderDbHelper(context);

        String query = "Select " + userEmail + " FROM " + usersTableName;

        db = dbcon.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        while (cursor.moveToNext()){
            listEmail.add(cursor.getString(0));
        }

        return listEmail;
    }

    public List<User> getAllUsers(){
        List<User> listUsers = new LinkedList<>();
        FeedReaderDbHelper dbcon = new FeedReaderDbHelper(context);

        String query = "SELECT * FROM " + usersTableName;

        db = dbcon.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);
        User user;

        while (cursor.moveToNext()){
            user = new User();
            user.setID(cursor.getInt(0));
            user.setEmail(cursor.getString(1));
            user.setPassword(cursor.getString(2));
            user.setQuestion1(cursor.getString(3));
            user.setAnswer1(cursor.getString(4));
            user.setQuestion2(cursor.getString(5));
            user.setAnswer2(cursor.getString(6));
            user.setQuestion3(cursor.getString(7));
            user.setAnswer3(cursor.getString(8));
            user.setActive(cursor.getString(9));
            user.setRecent(cursor.getString(10));
            user.setCreateDate(cursor.getString(11));
            user.setLoginDate(cursor.getString(12));
            listUsers.add(user);
        }
        return listUsers;
    }

    public void updateUser(Integer userId, String email, String pass, String question1, String answer1,
                           String question2, String answer2, String question3, String answer3,
                           String recent, String active, String loginDate) {
        FeedReaderDbHelper dbcon = new FeedReaderDbHelper(context);

        String[] whereArgs = new String[] {
                Integer.toString(userId),
        };
        ContentValues values = new ContentValues();
        values.put(userEmail, email);
        values.put(userpassword, pass);
        values.put(userQuestion1, question1);
        values.put(userAnswer1, answer1);
        values.put(userQuestion2, question2);
        values.put(userAnswer2, answer2);
        values.put(userQuestion3, question3);
        values.put(userAnswer3, answer3);
        values.put(userRecent, recent);
        values.put(userActive, active);
        values.put(userLoginDate, loginDate);

        db = dbcon.getWritableDatabase();

        db.update(usersTableName, values, "id = ?", whereArgs);

        db.close();
    }

    public boolean validatePassword(Integer id, String enteredPassword){
        FeedReaderDbHelper dbcon = new FeedReaderDbHelper(context);
        String pass;
        String query = "SELECT " + userpassword + " FROM " + usersTableName + " WHERE id = ?";
        String[] whereArgs = new String[]{
                Integer.toString(id)
        };
        db = dbcon.getWritableDatabase();
       Cursor cursor = db.rawQuery(query, whereArgs);

        if (cursor.moveToFirst()){
            pass = cursor.getString(0);
        } else {
            pass = "";
        }

        if (enteredPassword.equals(pass)){
            return true;
        }else {
            return false;
        }
    }

    public void upgrade(int oldVer, int newVer){
        FeedReaderDbHelper dbcon = new FeedReaderDbHelper(context);
        db = dbcon.getWritableDatabase();
        dbcon.onUpgrade(db, oldVer, newVer);
        db.close();
    }
}




