package com.example.parrish.test;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import Classes.Battery;
import Classes.SaveData;


public class CreateEntry extends Activity {

    SeekBar seekBar_start;
    TextView textView_start;
    SeekBar seekBar_end;
    TextView textView_end;
    SaveData save;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        List<Battery> batteries;
        Battery battery;
        String[] batteryNames;


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_entry);
        initializeVariables();

        // hides shadow from action bar
        ActionBar actionBar = getActionBar();
        actionBar.setElevation(0);
        // Enabling Up / Back navigation
        actionBar.setDisplayHomeAsUpEnabled(true);
        //hide label in action bar
        actionBar.setDisplayShowTitleEnabled(false);

        // Initialize the textview with '0'.
        textView_start.setText(seekBar_start.getProgress() + "%");
        textView_end.setText(seekBar_start.getProgress() + "%");

        //Seekbar start
        seekBar_start.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
                progress = progresValue;
                textView_start.setText(seekBar.getProgress() + "%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                textView_start.setText(seekBar.getProgress() + "%");
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                textView_start.setText(seekBar.getProgress() + "%");
            }
        });

        //seekbar end
        seekBar_end.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
                progress = progresValue;
                textView_end.setText(seekBar.getProgress() + "%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                textView_end.setText(seekBar.getProgress() + "%");
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                textView_end.setText(seekBar.getProgress() + "%");
            }
        });

        //start population of drop down list
        save = new SaveData(getApplicationContext());
        //gets all batteries in a List<Battery>
        batteries = save.getAllBatteries();
        //new string for battery names
        batteryNames = new String[batteries.size()];

        //loops through the List<Battery>
        for (int i = 0; i < batteries.size(); i++) {
            //gets the battery into the object battery
            battery = batteries.get(i);
            //appends the battery name to the batteryName array
            batteryNames[i] = battery.getName().toString();
        }

        Spinner ddlBatteryName = (Spinner) findViewById(R.id.ddlName);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(CreateEntry.this, android.R.layout.simple_spinner_dropdown_item, batteryNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ddlBatteryName.setAdapter(adapter);

        //end population
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_entry, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_save:
                // save action action
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    // A private method to help us initialize our variables.
    private void initializeVariables() {
        seekBar_start = (SeekBar) findViewById(R.id.seekBar);
        textView_start = (TextView) findViewById(R.id.txtStart);
        seekBar_end = (SeekBar) findViewById(R.id.seekBar2);
        textView_end = (TextView) findViewById(R.id.txtEnd);
    }

    private void onSubmitClick(View view) {
        SaveData save = new SaveData(view.getContext());


    }

}
