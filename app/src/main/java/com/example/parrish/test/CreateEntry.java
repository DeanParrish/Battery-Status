package com.example.parrish.test;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
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

import java.util.Arrays;
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
    Boolean startTimer = false;
    Boolean stopTimer = true;
    Boolean pauseTimer = false;
    Boolean resetTimer = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        List<Battery> batteries;
        Battery battery;
        String[] batteryNames;

        super.onCreate(savedInstanceState);
        if (getResources().getConfiguration().orientation ==
                Configuration.ORIENTATION_PORTRAIT) {
            setContentView(R.layout.activity_create_entry);
        } else {
            setContentView(R.layout.activity_create_entry_l);
        }
        initializeVariables();
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
                textView_start.setText(seekBar.getProgress() + " ");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                textView_start.setText(seekBar.getProgress() + " ");
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                textView_start.setText(seekBar.getProgress() + " ");
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
                textView_end.setText(seekBar.getProgress() + " ");
            }
        });

        //start population of drop down list
        save = new SaveData(getApplicationContext());
        //gets all batteries in a List<Battery>
        batteries = save.getAllBatteries();

        if (batteries.size() == 0) {
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
        Arrays.sort(batteryNames);

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
        final Context context = this;
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        // set icon
        alertDialogBuilder.setIcon(R.mipmap.ic_alert);
        // set title
        alertDialogBuilder.setTitle("Entry not saved!");

        // set dialog message
        alertDialogBuilder
                .setMessage("You will loose any unsaved data.")
                .setCancelable(false)
                .setPositiveButton(R.string.alert_confirm, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(context, MainActivity.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton(R.string.alert_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel();
                    }
                });
        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_save:
                // save action action
                onSubmitClick(item);
                return true;
            case android.R.id.home:
                alertDialog.show();
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

    public void onClickChronometer(View view) {
        Chronometer chronoTime = (Chronometer) findViewById(R.id.chronoTime);
        switch (view.getId()) {
            case R.id.btnStart:
                chronoTime.setBase(SystemClock.elapsedRealtime());
                chronoTime.start();
                startTimer = true;
                stopTimer = false;
                break;
            case R.id.btnStop:
                chronoTime.stop();
                stopTimer = true;
                startTimer = false;
                break;
            case R.id.btnPause:
                timeSave = chronoPause(chronoTime, timeSave);
                pauseTimer = true;
                break;
            case R.id.btnReset:
                chronoTime.stop();
                chronoTime.start();
                stopTimer = true;
                startTimer = false;
                break;
            default:
        }
    }

    public long chronoPause(Chronometer chrono, long timeSavePause) {
        if (timeSavePause == 0) {
            timeSave = chrono.getBase() - SystemClock.elapsedRealtime();
            chrono.stop();
        } else {
            chrono.setBase(SystemClock.elapsedRealtime() + timeSavePause);
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
        TextView lblStartPercent = (TextView) findViewById(R.id.lblPercentStart);
        Boolean isValidText = true;
        Boolean isValidTextPercent = true;
        String name;
        Integer seekStart;
        Integer seekEnd;
        int start;
        int end;
        long time;
        Context context = getApplicationContext();
        SaveData save = new SaveData(context);
        CharSequence toastText = "Entry created!";
        int duration = Toast.LENGTH_SHORT;
        int duration_l = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(context, toastText, duration);

        String toastTime = "Timer was not started";
        String toastStart = "Starting charge can not be 0 or less than end charge";

        time = (chronoTime.getBase() - SystemClock.elapsedRealtime()) * -1;
        seekStart = seekBar_start.getProgress();
        seekEnd = seekBar_end.getProgress();

        if (isValidTime(time) == false) {
            createToast(toastTime, duration);
            isValidText = false;
        } else if (isValidStartCharge(seekStart, seekEnd) == false) {
            createToast(toastStart, duration_l);
            isValidTextPercent = false;
        }

        if (isValidText == true && isValidTextPercent == true && startTimer == false && stopTimer == true) {
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
                } catch (SQLiteException e) {
                    Log.e("Add Entry", e.toString());
                }
            } catch (IllegalStateException e) {
                Log.e("Add Entry", e.toString());
            }
        }
    }

    public final static boolean isValidTime(Long time) {
        Integer seconds = time.intValue() / 1000;
        if (seconds > 0) {
            return true;
        } else {
            return false;
        }
    }

    public final static boolean isValidStartCharge(Integer start, Integer end) {
        if (start < 1 || start <= end) {
            return false;
        } else {
            return true;
        }
    }

    public void createToast(CharSequence text, Integer duration) {
        Toast toast = Toast.makeText(getApplicationContext(), text, duration);
        toast.show();
    }
}
