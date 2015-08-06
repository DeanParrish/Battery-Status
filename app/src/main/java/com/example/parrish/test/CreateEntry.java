package com.example.parrish.test;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Chronometer;
//import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
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
import Classes.User;

import com.appyvet.rangebar.RangeBar;
import com.rey.material.widget.EditText;


public class CreateEntry extends Activity {

    TextView textView_start;
    TextView textView_end;
    SaveData save;
    long timeSave = 0L;

    int orientation;
    Boolean startTimer = false;
    Boolean stopTimer = true;
    Boolean pauseTimer = false;
    Boolean submitTimer = false;
    String stringTime = "00:00";
    Integer totalSeconds;
    Boolean pauseToggle = false;
    long persistentTime = 0L;
    Boolean inEditMode = false;
    Integer inEditId;
    String inEditBatteryName;
    Entry inEditEntry;
    String userEmail;
    Integer userID;

    private RangeBar rangebar;

    public final static boolean isValidStartCharge(Integer start, Integer end) {
        if (start < 1 || start <= end) {
            return false;
        } else {
            return true;
        }
    }

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

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        // hides shadow from action bar
        ActionBar actionBar = getActionBar();
        actionBar.setElevation(0);
        // Enabling Up / Back navigation
        actionBar.setDisplayHomeAsUpEnabled(true);
        //hide label in action bar
//        actionBar.setDisplayShowTitleEnabled(false);
        setTitle("Menu");

        inEditMode = getIntent().getExtras().getBoolean("edit");
        userEmail = getIntent().getExtras().getString("userEmail");
        userID = getIntent().getExtras().getInt("userID");
        if (userID == 0){
            if (getIntent().getExtras().getString("userID") == null){
                userID = null;
            }
        }

        if (inEditMode == true) {
            inEditId = getIntent().getExtras().getInt("id");
            inEditBatteryName = getIntent().getExtras().getString("batteryName");
        }

        if (inEditMode == true) {
            save = new SaveData(getApplicationContext());
            inEditEntry = save.getEntry(userID, inEditId);

            TextView textViewToChange = (TextView) findViewById(R.id.title);
            textViewToChange.setText("EDIT ENTRY");
            textViewToChange.setBackgroundColor(Color.parseColor("#1976D2"));

            LinearLayout createChrono = (LinearLayout) findViewById(R.id.createChrono);
            createChrono.setVisibility(View.GONE);
            LinearLayout createButton = (LinearLayout) findViewById(R.id.createButton);
            createButton.setVisibility(View.GONE);

            Integer runtimeValue = Integer.parseInt(inEditEntry.getRunTime());
//            Integer runtimeValue = 3725;// 1 hour 15 min
            Integer npValueHours = 0;
            Integer npValueMinutes = 0;
            Integer npValueSeconds = 0;
            if (runtimeValue <= 7200) {
                npValueHours = runtimeValue / 3600;
                npValueMinutes = Math.abs(runtimeValue % 3600 / 60);
                npValueSeconds = runtimeValue % 60;
            }
            NumberPicker npHours = (NumberPicker) findViewById(R.id.np_hh);
            npHours.setMaxValue(2);
            npHours.setMinValue(0);
            npHours.setValue(npValueHours);
            npHours.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

            NumberPicker npMinutes = (NumberPicker) findViewById(R.id.np_mm);
            npMinutes.setMaxValue(59);
            npMinutes.setMinValue(0);
            npMinutes.setValue(npValueMinutes);
            npMinutes.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

            NumberPicker npSeconds = (NumberPicker) findViewById(R.id.np_ss);
            npSeconds.setMaxValue(59);
            npSeconds.setMinValue(0);
            npSeconds.setValue(npValueSeconds);
            npSeconds.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

            rangebar = (RangeBar) findViewById(R.id.rangebar);
            rangebar.setRangePinsByIndices(Integer.parseInt(inEditEntry.getEndCharge()), Integer.parseInt(inEditEntry.getStartCharge()));

            EditText textNotes = (EditText) findViewById(R.id.txtNotes);
            textNotes.setText(inEditEntry.getNotes());

            textView_end.setText("" + inEditEntry.getEndCharge());
            textView_start.setText("" + inEditEntry.getStartCharge());

        } else {
            LinearLayout editText = (LinearLayout) findViewById(R.id.editText);
            editText.setVisibility(View.GONE);
            LinearLayout editTime = (LinearLayout) findViewById(R.id.editTime);
            editTime.setVisibility(View.GONE);

            LinearLayout createChrono = (LinearLayout) findViewById(R.id.createChrono);
            createChrono.setVisibility(View.VISIBLE);

            LinearLayout createButton = (LinearLayout) findViewById(R.id.createButton);
            createButton.setVisibility(View.VISIBLE);

            final ImageButton buttonStopPressed = (ImageButton) findViewById(R.id.btnStop);

            if (savedInstanceState != null) {
                persistentTime = savedInstanceState.getLong("timerTime");
                submitTimer = savedInstanceState.getBoolean("submitTimer");
                startTimer = savedInstanceState.getBoolean("startTimer");
                stringTime = savedInstanceState.getString("stringTime");
                if (persistentTime != 0L) {
                    Chronometer chronoTime = (Chronometer) findViewById(R.id.chronoTime);
                    timeSave = persistentTime;
                    chronoTime.setBase(SystemClock.elapsedRealtime() - timeSave);
                    buttonStopPressed.setImageResource(R.mipmap.ic_stop_pressed);
                } else {
                    buttonStopPressed.setImageResource(R.mipmap.ic_stop);
                }
            }
        }

        rangebar = (RangeBar) findViewById(R.id.rangebar);
        rangebar.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex,
                                              int rightPinIndex,
                                              String leftPinValue, String rightPinValue) {
                textView_end.setText("" + leftPinIndex);
                textView_start.setText("" + rightPinIndex);
            }
        });

        //start population of drop down list
        save = new SaveData(getApplicationContext());
        //gets all batteries in a List<Battery>
        batteries = save.getAllBatteriesUser(userID);

//        if (batteries.size() == 0) {
//            Intent intent = new Intent(this, MainActivity.class);
//            intent.putExtra("no_batteries", true);
//            startActivity(intent);
//        }
        //new string for battery names
        batteryNames = new String[batteries.size()];

        //loops through the List<Battery>
        for (
                int i = 0;
                i < batteries.size(); i++) {
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

        if (inEditMode == true) {
            ddlBatteryName.setEnabled(false);
            ddlBatteryName.setClickable(false);
            String inEditName = inEditEntry.getBatteryName();
            Integer spinnerPositionName = adapter.getPosition(inEditName);
            ddlBatteryName.setSelection(spinnerPositionName);
        }
        //end population
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_entry, menu);
        return true;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
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

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Saving variables
        savedInstanceState.putLong("timerTime", persistentTime);
        savedInstanceState.putBoolean("submitTimer", submitTimer);
        savedInstanceState.putBoolean("startTimer", startTimer);
        savedInstanceState.putString("stringTime", stringTime);

        // Call at the end
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        // Call at the start
        super.onRestoreInstanceState(savedInstanceState);

        // Retrieve variables
        timeSave = savedInstanceState.getLong("timerTime");
        submitTimer = savedInstanceState.getBoolean("submitTimer");
        startTimer = savedInstanceState.getBoolean("startTimer");
        stringTime = savedInstanceState.getString("stringTime", stringTime);
    }

    // A private method to help us initialize our variables.
    private void initializeVariables() {
        textView_start = (TextView) findViewById(R.id.txtStart);
        textView_end = (TextView) findViewById(R.id.txtEnd);
    }

    public void onClickChronometer(View view) {
        Display display = ((WindowManager)
                getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int screenOrientation;
        Chronometer chronoTime = (Chronometer) findViewById(R.id.chronoTime);

        final Handler handler = new Handler();
        final ImageButton buttonStartPressed = (ImageButton) findViewById(R.id.btnStart);
        final ImageButton buttonStopPressed = (ImageButton) findViewById(R.id.btnStop);
        final ImageButton buttonPausePressed = (ImageButton) findViewById(R.id.btnPause);
        final ImageButton buttonResetPressed = (ImageButton) findViewById(R.id.btnReset);

        switch (view.getId()) {
            case R.id.btnStart:
                if (stopTimer == true && pauseTimer != true) {
                    screenOrientation = display.getRotation();
                    orientation = this.getResources().getConfiguration().orientation;
                    if (screenOrientation == 0 || screenOrientation == 2) {
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    } else if (screenOrientation == 1) {
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    } else if (screenOrientation == 3) {
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
                    }

                    buttonStartPressed.setImageResource(R.mipmap.ic_start_pressed);
                    buttonPausePressed.setImageResource(R.mipmap.ic_pause);
                    buttonResetPressed.setImageResource(R.mipmap.ic_reset);
                    buttonStopPressed.setImageResource(R.mipmap.ic_stop);

                    if (timeSave == 0L) {
                        chronoTime.setBase(SystemClock.elapsedRealtime()); //SystemClock.elapsedRealtime());
                    } else {
                        chronoTime.setBase(SystemClock.elapsedRealtime() - timeSave); //SystemClock.elapsedRealtime());
                    }

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
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
                    buttonStopPressed.setImageResource(R.mipmap.ic_stop_pressed);
                    buttonPausePressed.setImageResource(R.mipmap.ic_pause);
                    buttonStartPressed.setImageResource(R.mipmap.ic_start);
                    buttonResetPressed.setImageResource(R.mipmap.ic_reset);
                    chronoTime.stop();
                    persistentTime = SystemClock.elapsedRealtime() - chronoTime.getBase();
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

                    if (pauseToggle == false) {
                        buttonPausePressed.setImageResource(R.mipmap.ic_pause_pressed);
                        buttonStartPressed.setImageResource(R.mipmap.ic_start);
                        pauseTimer = true;
                        pauseToggle = true;
                    } else {
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
        EditText txtNotes = (EditText) findViewById(R.id.txtNotes);
        Boolean isValidTextPercent = true;
        Boolean isValidTimeText = true;
        Boolean isValidNotes;
        String name;
        Integer seekStart;
        Integer seekEnd;
        Integer start;
        Integer end;
        Integer time;
        String notes;
        Context context = getApplicationContext();
        SaveData save = new SaveData(context);
        CharSequence toastText = "Entry created!";
        CharSequence toastUpdate = "Entry updated!";
        int duration = Toast.LENGTH_SHORT;
        int duration_l = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(context, toastText, duration);

        String toastTimeStart = "Timer was not started";
        String toastTimeStop = "Timer was not stopped";
        String toastTimeLong = "Time can not exceed 2 hours";
        String toastTimeShort = "Time must be at least be 1 second";
        String toastStart = "Starting charge can not be 0 or less than end charge";

        seekStart = Integer.valueOf(txtStart.getText().toString());
        seekEnd = Integer.valueOf(txtEnd.getText().toString());

        if (inEditMode == false) {
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
        } else { // entry is in edit mode
            if (isValidTime() == 1) {
                isValidTimeText = true;
            }else if(isValidTime() == 2){
                isValidTimeText = false;
                createToast(toastTimeShort, duration);
            }
            else{ //validtime == 3
                isValidTimeText = false;
                createToast(toastTimeLong, duration);
            }
        }

        if (isValidStartCharge(seekStart, seekEnd) == false) {
            createToast(toastStart, duration_l);
            isValidTextPercent = false;
        }

        if (txtNotes.getText().length() > 150) {
            txtNotes.setError("Notes cannot be longer than 150 characters");
            isValidNotes = false;
        } else {
            isValidNotes = true;
        }

        if (inEditMode == false) {
            if (isValidTimeText == true && isValidTextPercent == true && isValidNotes == true) {
                try {
                    name = ddlName.getSelectedItem().toString();
                    start = Integer.valueOf(txtStart.getText().toString());//txtStart.getText().toString().substring(0, txtStart.length() - 1));
                    end = Integer.valueOf(txtEnd.getText().toString());//.substring(0, txtEnd.length() - 1));
                    time = totalSeconds;//(chronoTime.getBase() - SystemClock.elapsedRealtime()) * -1;
                    notes = txtNotes.getText().toString();

                    try {
                        int entryID = save.getLastEntryId(userID);
                        save.addEntry(userID, entryID, name, time, start, end, notes, "");
                        toast.show();
                        finish();
                    } catch (SQLiteException e) {
                        Log.e("Add Entry", e.toString());
                    }
                } catch (IllegalStateException e) {
                    Log.e("Add Entry", e.toString());
                }
            }
        } else {  //in edit mode
            if (isValidTimeText == true && isValidTextPercent == true && isValidNotes == true) {
                try {
                    name = ddlName.getSelectedItem().toString();
                    start = Integer.valueOf(txtStart.getText().toString());//txtStart.getText().toString().substring(0, txtStart.length() - 1));
                    end = Integer.valueOf(txtEnd.getText().toString());//.substring(0, txtEnd.length() - 1));
                    time = totalSeconds;//(chronoTime.getBase() - SystemClock.elapsedRealtime()) * -1;
                    notes = txtNotes.getText().toString();

                    try {
                        save.updateEntry(userID, inEditId, name, time, start, end, notes, "");
                        createToast(toastUpdate, duration);
                        finish();
                    } catch (SQLiteException e) {
                        Log.e("Add Entry", e.toString());
                    }
                } catch (IllegalStateException e) {
                    Log.e("Add Entry", e.toString());
                }
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
                hours = Integer.parseInt(time.substring(0, 1));
                totalSeconds = seconds + (minutes * 60) + (hours * 3600);
                return totalSeconds;
            } else if (timeSize == 8) {
                seconds = Integer.parseInt(time.substring(7, 8));
                minutes = Integer.parseInt(time.substring(4, 5));
                hours = Integer.parseInt(time.substring(0, 2));
                totalSeconds = seconds + (minutes * 60) + (hours * 3600);
                return totalSeconds;
            } else {
                return 1;
            }
        }
    }

    public void createToast(CharSequence text, Integer duration) {
        Toast toast = Toast.makeText(getApplicationContext(), text, duration);
        toast.show();
    }

    public final Integer isValidTime() {
        NumberPicker npHours = (NumberPicker) findViewById(R.id.np_hh);
        NumberPicker npMinutes = (NumberPicker) findViewById(R.id.np_mm);
        NumberPicker npSeconds = (NumberPicker) findViewById(R.id.np_ss);

        Integer hours = npHours.getValue();
        Integer minutes = npMinutes.getValue();
        Integer seconds = npSeconds.getValue();

        totalSeconds = hours * 3600 + minutes * 60 + seconds;
        if (totalSeconds > 7200) {  //checks to make sure no more than 2 hours is saved
            return 3;
        } else if (totalSeconds < 1) {
            return 2;
        } else{
            return 1;
        }
    }
}
