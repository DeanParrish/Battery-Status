package com.example.parrish.test;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.LinkedList;
import java.util.List;

import Classes.Battery;
import Classes.Entry;
import Classes.SaveData;


public class CreateEntry extends Activity {

    SeekBar seekBar_start;
    TextView textView_start;
    SeekBar seekBar_end;
    TextView textView_end;
    SaveData save;
    long timeSave;

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
//        textView_start.setText(seekBar_start.getProgress());
//        textView_end.setText(seekBar_start.getProgress());

        //Seekbar start
        seekBar_start.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
                progress = progresValue;
                textView_start.setText(seekBar.getProgress() +" ");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                textView_start.setText(seekBar.getProgress() +" ");
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                textView_start.setText(seekBar.getProgress() +" ");
            }
        });

        //seekbar end
        seekBar_end.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
                progress = progresValue;
                textView_end.setText(seekBar.getProgress() + " ");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                textView_end.setText(seekBar.getProgress() + " ");
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                textView_end.setText(seekBar.getProgress() +" ");
            }
        });

        //start population of drop down list
        save = new SaveData(getApplicationContext());
        //gets all batteries in a List<Battery>
        batteries = save.getAllBatteries();

        if (batteries.size() == 0){
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("no_batteries", true);
            startActivity(intent);
        }
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
                onSubmitClick(item);
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

    public void onClickChronometer(View view){
        Chronometer chronoTime = (Chronometer) findViewById(R.id.chronoTime);
        switch (view.getId()){
            case R.id.btnStart:
                chronoTime.setBase(SystemClock.elapsedRealtime());
                chronoTime.start();
                break;
            case R.id.btnStop:
                chronoTime.stop();
                break;
            case R.id.btnPause:
                timeSave = chronoPause(chronoTime, timeSave);
                break;
            case R.id.btnReset:
                chronoTime.stop();
                chronoTime.start();
                break;
            default:
        }
    }

    public long chronoPause(Chronometer chrono, long timeSave){
        if (timeSave == 0) {
            timeSave = chrono.getBase() - SystemClock.elapsedRealtime();
            chrono.stop();
        } else {
            chrono.setBase(SystemClock.elapsedRealtime() + timeSave);
            chrono.start();
            timeSave = 0;
        }
        return timeSave;
    }

    public void onSubmitClick(MenuItem item) {
        TextView txtStart = (TextView) findViewById(R.id.txtStart);
        TextView txtEnd = (TextView) findViewById(R.id.txtEnd);
        Spinner ddlName = (Spinner) findViewById(R.id.ddlName);
        Chronometer chronoTime = (Chronometer) findViewById(R.id.chronoTime);
        String name;
        int start;
        int end;
        long time;
        Context context = getApplicationContext();
        SaveData save = new SaveData(context);
        CharSequence toastText = "Entry created!";
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, toastText, duration);

        try {
            name = ddlName.getSelectedItem().toString();
            start = Integer.parseInt(txtStart.getText().toString().substring(0, txtStart.length() - 1));
            end = Integer.parseInt(txtEnd.getText().toString().substring(0, txtEnd.length() - 1));
            time = (chronoTime.getBase() - SystemClock.elapsedRealtime()) * -1;

            try {
                save.addEntry(name, time, start, end);
                toast.show();
                Intent intent = new Intent(context, MainActivity.class);
                startActivity(intent);
            } catch (SQLiteException e){
                Log.e("Add Entry", e.toString());
            }
        } catch (IllegalStateException e){
            Log.e("Add Entry", e.toString());
        }

        //testing entries
        List<Entry> entries = new LinkedList<Entry>();
        entries = save.getAllEntries();
        Log.d("entries", "1");
    }

}
