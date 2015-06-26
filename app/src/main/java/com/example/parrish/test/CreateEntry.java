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
import android.os.Handler;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageButton;
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
    Boolean submitTimer = false;
    String stringTime = "00:00";
    Integer totalSeconds;
    Boolean pauseToggle = false;

//    Chronometer chronoTime = (Chronometer) findViewById(R.id.chronoTime);

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
                textView_start.setText(seekBar.getProgress() + "");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                textView_start.setText(seekBar.getProgress() + "");
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                textView_start.setText(seekBar.getProgress() + "");
            }
        });

        //seekbar end
        seekBar_end.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
                progress = progresValue;
                textView_end.setText(seekBar.getProgress() + "");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                textView_end.setText(seekBar.getProgress() + "");
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                textView_end.setText(seekBar.getProgress() + "");
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
                        finish();
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

        Chronometer chronoTime = (Chronometer) findViewById(R.id.chronoTime);
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_save:
                // save action action
                stringTime = chronoTime.getText().toString();
                onSubmitClick(item);
                return true;
            case android.R.id.home:
                alertDialog.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
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
                            finish();
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
            alertDialog.show();
        }
        return super.onKeyDown(keyCode, event);
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

        Long stopTimeSave;
        final Handler handler = new Handler();

        final ImageButton buttonStartPressed = (ImageButton) findViewById(R.id.btnStart);
//        buttonStartPressed.setImageResource(R.mipmap.ic_start_pressed);
//        buttonStartPressed.setImageResource(R.mipmap.ic_start);


        final ImageButton buttonStopPressed = (ImageButton) findViewById(R.id.btnStop);
//        buttonStopPressed.setImageResource(R.mipmap.ic_stop_pressed);
//        buttonStopPressed.setImageResource(R.mipmap.ic_stop);

        ImageButton buttonPausePressed = (ImageButton) findViewById(R.id.btnPause);
//        buttonPausePressed.setImageResource(R.mipmap.ic_pause_pressed);
//        buttonPausePressed.setImageResource(R.mipmap.ic_pause);

        final ImageButton buttonResetPressed = (ImageButton) findViewById(R.id.btnReset);
//        buttonResetPressed.setImageResource(R.mipmap.ic_reset_pressed);
//        buttonResetPressed.setImageResource(R.mipmap.ic_reset);

        switch (view.getId()) {
            case R.id.btnStart:
                if (stopTimer == true && pauseTimer != true) {
                    buttonStartPressed.setImageResource(R.mipmap.ic_start_pressed);
                    buttonPausePressed.setImageResource(R.mipmap.ic_pause);
                    buttonResetPressed.setImageResource(R.mipmap.ic_reset);
                    buttonStopPressed.setImageResource(R.mipmap.ic_stop);
                    chronoTime.setBase(SystemClock.elapsedRealtime());
                    chronoTime.start();
                    stringTime = "00:01";
                    startTimer = true;
                    stopTimer = false;
                    submitTimer = false;
                    pauseTimer = false;
                    timeSave = 0;
                }
                break;
            case R.id.btnStop:
                if (startTimer == true && pauseTimer != true) {
                    buttonStopPressed.setImageResource(R.mipmap.ic_stop_pressed);
                    buttonPausePressed.setImageResource(R.mipmap.ic_pause);
                    buttonStartPressed.setImageResource(R.mipmap.ic_start);
                    buttonResetPressed.setImageResource(R.mipmap.ic_reset);
                    chronoTime.stop();
                    stringTime = chronoTime.getText().toString();
                    startTimer = false;
                    stopTimer = true;
                    submitTimer = true;
                    pauseTimer = false;
                }
                break;
            case R.id.btnPause:
                if (startTimer == true) {
                    buttonResetPressed.setImageResource(R.mipmap.ic_reset);
                    buttonStopPressed.setImageResource(R.mipmap.ic_stop);

                    if (pauseToggle == false){
                        buttonPausePressed.setImageResource(R.mipmap.ic_pause_pressed);
                        buttonStartPressed.setImageResource(R.mipmap.ic_start);
                        pauseTimer = true;
                        pauseToggle = true;
                    }else{
                        buttonPausePressed.setImageResource(R.mipmap.ic_pause);
                        buttonStartPressed.setImageResource(R.mipmap.ic_start_pressed);
                        pauseTimer = false;
                        pauseToggle = false;
                    }
                    timeSave = chronoPause(chronoTime, timeSave);
                    startTimer = true;
                    stopTimer = false;
                    submitTimer = false;
                }
                break;
            case R.id.btnReset:
                buttonResetPressed.setImageResource(R.mipmap.ic_reset_pressed);
                buttonStartPressed.setImageResource(R.mipmap.ic_start);
                buttonPausePressed.setImageResource(R.mipmap.ic_pause);
                buttonStopPressed.setImageResource(R.mipmap.ic_stop);
                chronoTime.setBase(SystemClock.elapsedRealtime());
                chronoTime.stop();
                chronoTime.start();
//                startTimer = true;
                stopTimer = false;
                submitTimer = false;
                pauseTimer = false;

                startTimer = false;
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        buttonResetPressed.setImageResource(R.mipmap.ic_reset);
                        buttonStartPressed.setImageResource(R.mipmap.ic_start_pressed);
                        startTimer = true;
                    }
                }, 300);
                buttonStopPressed.setEnabled(true);
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
        Boolean isValidTextPercent = true;
        Boolean isValidTimeText;
        String name;
        Integer seekStart;
        Integer seekEnd;
        Integer start;
        Integer end;
        Integer time;
        Context context = getApplicationContext();
        SaveData save = new SaveData(context);
        CharSequence toastText = "Entry created!";
        int duration = Toast.LENGTH_SHORT;
        int duration_l = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(context, toastText, duration);

        String toastTimeStart = "Timer was not started";
        String toastTimeStop = "Timer was not stopped";
        String toastStart = "Starting charge can not be 0 or less than end charge";

        seekStart = seekBar_start.getProgress();
        seekEnd = seekBar_end.getProgress();

        if (submitTimer == true) { //stopped
            if (isValidTime(stringTime) != 0) {
                isValidTimeText = true;
            } else {
                createToast(toastTimeStart, duration);
                isValidTimeText = false;
            }
        } else {                            //start
            if (isValidTime(stringTime) != 0) {
                createToast(toastTimeStop, duration);
                isValidTimeText = false;
            } else {
                createToast(toastTimeStart, duration);
                isValidTimeText = false;
            }
        }

        if (isValidStartCharge(seekStart, seekEnd) == false) {
            createToast(toastStart, duration_l);
            isValidTextPercent = false;
        }

        if (isValidTimeText == true && isValidTextPercent == true) {
            try {
                name = ddlName.getSelectedItem().toString();
                start = Integer.valueOf(txtStart.getText().toString());//txtStart.getText().toString().substring(0, txtStart.length() - 1));
                end = Integer.valueOf(txtEnd.getText().toString());//.substring(0, txtEnd.length() - 1));
                time = totalSeconds;//(chronoTime.getBase() - SystemClock.elapsedRealtime()) * -1;

                try {
                    save.addEntry(name, time, start, end);
                    toast.show();
                    finish();
                } catch (SQLiteException e) {
                    Log.e("Add Entry", e.toString());
                }
            } catch (IllegalStateException e) {
                Log.e("Add Entry", e.toString());
            }
        }
    }

    public final Integer isValidTime(String time) {

        Integer seconds;
        Integer minutes;
        Integer hours;
        Integer timeSize = time.length();

        if (time == "00:00") {
            return 0;
        } else {
            if (timeSize == 5) {
                seconds = Integer.parseInt(time.substring(3, 5));
                minutes = Integer.parseInt(time.substring(0, 2));
                totalSeconds = seconds + (minutes * 60);
                return totalSeconds;
            } else if (timeSize == 7) {
                seconds = Integer.parseInt(time.substring(5, 7));
                minutes = Integer.parseInt(time.substring(2, 4));
                hours = Integer.parseInt(time.substring(0,1));
                totalSeconds = seconds + (minutes * 60) + (hours * 3600);
                return totalSeconds;
            } else if (timeSize == 8) {
                seconds = Integer.parseInt(time.substring(7, 8));
                minutes = Integer.parseInt(time.substring(4, 5));
                hours = Integer.parseInt(time.substring(0,2));
                totalSeconds = seconds + (minutes * 60) + (hours * 3600);
                return totalSeconds;
            }
            else{
                return 1;
            }
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
