package com.example.parrish.test;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

import Classes.Battery;
import Classes.SaveData;
import Classes.User;

public class MainActivity extends Activity {

    final Context context = this;
    Button btn_battery_create;
    Button btn_battery_archive;
    Button btn_entry_create;
    Button btn_view_stats;
    Button btn_settings;
    SaveData save;
    CharSequence toastText = "Please create a battery first!";
    int duration = Toast.LENGTH_LONG;
    String userEmail;
    Integer userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        save = new SaveData(getApplicationContext());
        userID = getIntent().getExtras().getInt("userID");
        if (userID == 0){
            if (getIntent().getExtras().getString("userID").equals("")){
                userID = null;
            }
        }
        boolean nullBatteries = getIntent().getExtras().getBoolean("nullBatteries");

        if (nullBatteries == true){
            save.setUserIDOfNull(userID, MainActivity.this);
        }
        onResume();
        addListenerOnButton();
        addListenerOnButton2();
        addListenerOnButton3();
        addListenerOnButton5();
        addListenerOnButton6();
        addListenerOnButton7();

        // hides shadow from action bar
        ActionBar actionBar = getActionBar();
        actionBar.setElevation(0);
        actionBar.hide();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void addListenerOnButton() {

        btn_battery_create = (Button) findViewById(R.id.button11);
        btn_battery_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intentCreate = new Intent(context, CreateBattery.class);
                intentCreate.putExtra("create_battery", 1);  // sets flag for create battery
                intentCreate.putExtra("userEmail", userEmail);
                intentCreate.putExtra("userID", userID);
                startActivity(intentCreate);
            }
        });
    }

    public void addListenerOnButton2() {
        btn_battery_archive = (Button) findViewById(R.id.button12);
        btn_battery_archive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (hasNoBatteries() == false) {
                    Intent intent = new Intent(context, ArchiveBattery.class);
                    intent.putExtra("userEmail", userEmail);
                    intent.putExtra("userID", userID);
                    startActivity(intent);
                } else {
                    createToast(toastText, duration);
                }
            }
        });
    }

    public void addListenerOnButton3() {
        btn_entry_create = (Button) findViewById(R.id.button8);
        btn_entry_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (hasNoBatteries() == false) {
                    Intent intent = new Intent(context, CreateEntry.class);
                    intent.putExtra("edit", false);
                    intent.putExtra("userEmail", userEmail);
                    intent.putExtra("userID", userID);
                    startActivity(intent);
                } else {
                    createToast(toastText, duration);
                }
            }
        });
    }

    public void addListenerOnButton5() {
        btn_view_stats = (Button) findViewById(R.id.button10);
        btn_view_stats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (hasNoBatteries() == false) {
                    Intent intent = new Intent(context, ViewStatistics.class);
                    intent.putExtra("userEmail", userEmail);
                    intent.putExtra("userID", userID);
                    startActivity(intent);
                } else {
                    createToast(toastText, duration);
                }
            }
        });
    }

    public void addListenerOnButton6() {
        btn_battery_create = (Button) findViewById(R.id.button1);
        btn_battery_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (hasNoBatteries() == false) {
                    Intent intent = new Intent(context, EditBattery.class);
                    intent.putExtra("userEmail", userEmail);
                    intent.putExtra("userID", userID);
                    startActivity(intent);
                } else {
                    createToast(toastText, duration);
                }
            }
        });
    }

    public void addListenerOnButton7() {
        btn_settings = (Button) findViewById(R.id.btn_settings);
        btn_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(context, Settings.class);
                intent.putExtra("userID", userID);
                startActivity(intent);
            }
        });
    }

    //methods
    public void createToast(CharSequence text, Integer duration) {
        Toast toast = Toast.makeText(getApplicationContext(), text, duration);
        toast.show();
    }

    public Boolean hasNoBatteries() {
        List<Battery> batteries;
        //start population of drop down list
        save = new SaveData(getApplicationContext());
        //gets all batteries in a List<Battery>
        try {
            //User user = save.getUserByEmail(userEmail);
            batteries = save.getAllBatteriesUser(userID);
            if (batteries.size() == 0) {
                return true;
            } else {
                return false;  //batteries exist
            }
        } catch (
                Exception e
                ) {
            Log.e("Main Activity no_batter", e.toString());
        }
        return true;
    }
}
