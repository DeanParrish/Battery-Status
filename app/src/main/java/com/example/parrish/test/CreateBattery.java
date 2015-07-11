package com.example.parrish.test;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
//import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Classes.Battery;
import Classes.SaveData;

import static java.lang.Integer.*;

import com.rey.material.widget.EditText;


public class CreateBattery extends Activity {

    Integer editFlag;
    SaveData save;

    public static boolean isValidBattery(CharSequence target) {
        Boolean stringMatch;
        if (TextUtils.isEmpty(target) == true) {
            return false;
        } else {
            String namePattern = ".{1,16}";
            Pattern pattern = Pattern.compile(namePattern);
            Matcher matcher = pattern.matcher(target);
            stringMatch = matcher.matches();
            return stringMatch;
        }
    }

    public static boolean isValidMah(CharSequence target) {
        Boolean stringMatch;
        Integer intTarget;
        String stringTarget;
        if (TextUtils.isEmpty(target) == true) {
            return false;
        } else {
            intTarget = parseInt(target.toString());
            stringTarget = intTarget.toString();
            String mahPattern = "[0-9]{3,5}";
            Pattern pattern = Pattern.compile(mahPattern);
            Matcher matcher = pattern.matcher(stringTarget);
            stringMatch = matcher.matches();
            return stringMatch;
        }
    }

    public static boolean isValidCycles(CharSequence target) {
        Boolean stringMatch;
        Integer intTarget;
        String stringTarget;
        if (TextUtils.isEmpty(target) == true) {
            return false;
        } else {
            intTarget = parseInt(target.toString());
            stringTarget = intTarget.toString();
            String cyclesPattern = "[0-9]{2,4}";
            Pattern pattern = Pattern.compile(cyclesPattern);
            Matcher matcher = pattern.matcher(stringTarget);
            stringMatch = matcher.matches();
            return stringMatch;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_battery);

        try {
            //       hides shadow from action bar
            ActionBar actionBar = getActionBar();
            actionBar.setElevation(0);
            // Enabling Up / Back navigation
            actionBar.setDisplayHomeAsUpEnabled(true);
            //hide label in action bar
//            actionBar.setDisplayShowTitleEnabled(false);
            setTitle("Menu");
        } catch (NullPointerException e) {
            Log.e("actionbar", e.toString());
        }


        Integer variable = getIntent().getExtras().getInt("create_battery");
        editFlag = variable;

        Spinner ddlBatteryType = (Spinner) findViewById(R.id.ddlType);
        Spinner ddlBatteryCells = (Spinner) findViewById(R.id.ddlCells);
        Integer[] cells = {1, 2, 3, 4, 5, 6, 7, 8};

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.battery_type, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ddlBatteryType.setAdapter(adapter);

        ArrayAdapter<Integer> adapterCells = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, cells);
        adapterCells.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ddlBatteryCells.setAdapter(adapterCells);

        if (variable == 0) {
            int spinnerPositionCells;
            int spinnerPositionType;
            Intent intent = getIntent();
            String stringBattery = intent.getStringExtra("battery");

            //region Populate List from database
            List<Battery> batteries;
            Battery mbattery;
            save = new SaveData(getApplicationContext());
            //gets all batteries in a List<Battery>
            mbattery = save.getBattery(stringBattery);

            TextView textViewToChange = (TextView) findViewById(R.id.title);
            textViewToChange.setText("EDIT BATTERY");
            textViewToChange.setBackgroundColor(Color.parseColor("#f39c12"));

            EditText textInputToChange = (EditText) findViewById(R.id.txtBatteryName);
            textInputToChange.setText(mbattery.getName());
            textInputToChange.setEnabled(false);

            spinnerPositionCells = adapterCells.getPosition(Integer.parseInt(mbattery.getCells()));
            Spinner lblCellValue = (Spinner) findViewById(R.id.ddlCells);
            lblCellValue.setSelection(spinnerPositionCells);

            EditText lblMahValue = (EditText) findViewById(R.id.txtMah);
            lblMahValue.setText(mbattery.getMah());

            spinnerPositionType = adapter.getPosition(mbattery.getType());
            Spinner lblTypeValue = (Spinner) findViewById(R.id.ddlType);
            lblTypeValue.setSelection(spinnerPositionType);

            EditText lblCyclesValue = (EditText) findViewById(R.id.txtCycles);
            lblCyclesValue.setText(mbattery.getCycles());


        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_create_battery, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final Context context = this;
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        // set icon
        alertDialogBuilder.setIcon(R.mipmap.ic_alert);
        // set title
        alertDialogBuilder.setTitle("Battery not saved!");

        // set dialog message
        alertDialogBuilder
                .setMessage("You will lose any unsaved data.")
                .setCancelable(false)
                .setPositiveButton(R.string.alert_confirm, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (editFlag == 0) {
                            finish();
                        } else {
                            Intent intent = new Intent(context, MainActivity.class);
                            startActivity(intent);
                        }
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
        if (id == android.R.id.home) {
            alertDialog.show();
        }
        if (id == R.id.action_save) {
            onSubmitClick(item);
        }
        return true;//super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            final Context context = this;
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

            // set icon
            alertDialogBuilder.setIcon(R.mipmap.ic_alert);
            // set title
            alertDialogBuilder.setTitle("Battery not saved!");

            // set dialog message
            alertDialogBuilder
                    .setMessage("You will loose any unsaved data.")
                    .setCancelable(false)
                    .setPositiveButton(R.string.alert_confirm, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            if (editFlag == 0) {
                                finish();
                            } else {
                                Intent intent = new Intent(context, MainActivity.class);
                                startActivity(intent);
                            }
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

    public void onSubmitClick(MenuItem item) {
        SaveData save = new SaveData(getApplicationContext());
        Boolean isValidText = true;
        Boolean isValidMah = true;
        Boolean isValidCycles = true;
        String type;
        String batteryName;
        String capBatteryName;
        String uncapBatteryName;
        int batteryCells;
        int batteryMah;
        int batteryCycle;
        CharSequence toastTextCreate = "Battery created!";
        CharSequence toastTextEdit = "Battery updated!";
        int duration = Toast.LENGTH_SHORT;
        Toast toastCreate = Toast.makeText(getApplicationContext(), toastTextCreate, duration);
        Toast toastUpdate = Toast.makeText(getApplicationContext(), toastTextEdit, duration);
        //Screen fields and conversions
        Spinner spinnerType = (Spinner) findViewById(R.id.ddlType);
        Spinner ddlCells = (Spinner) findViewById(R.id.ddlCells);
        EditText txtBattName = (EditText) findViewById(R.id.txtBatteryName);
        EditText txtMah = (EditText) findViewById(R.id.txtMah);
        EditText txtCycle = (EditText) findViewById(R.id.txtCycles);

        String toastInvalid = "Fill in all fields and correct any errors";
        String toastBattery = "Enter a valid name";
        String toastMah = "Enter a valid number between 100 and 99999";
        String toastCycles = "Enter a valid number between 10 and 9999";

        if (isValidBattery(txtBattName.getText().toString()) != true) {
            txtBattName.setError(toastBattery);
            isValidText = false;
        }
        if (isValidMah(txtMah.getText().toString()) != true) {
            txtMah.setError(toastMah);
            isValidMah = false;
        }
        if (isValidCycles(txtCycle.getText().toString()) != true) {
            txtCycle.setError(toastCycles);
            isValidCycles = false;
        }
        if (isValidText == true && isValidCycles == true && isValidMah == true) {
            if (editFlag == 1) {
                try {
                    //Capitalize first letter of battery name
                    uncapBatteryName = txtBattName.getText().toString();
                    capBatteryName = uncapBatteryName.substring(0, 1).toUpperCase() + uncapBatteryName.substring(1);

                    type = spinnerType.getSelectedItem().toString();
                    batteryName = capBatteryName;//txtBattName.getText().toString();
                    batteryCells = parseInt(ddlCells.getSelectedItem().toString());
                    batteryMah = parseInt((txtMah.getText().toString()));
                    batteryCycle = parseInt(txtCycle.getText().toString());
                    //add the battery to the database
                    try {
                        //save.upgrade(2, 3);
                        save.addBattery(batteryName, batteryCells, batteryMah, batteryCycle, type);
                        toastCreate.show();
                        finish();
                    } catch (SQLiteException e) {
                        Log.e("Add Battery", e.toString());
                    }
                } catch (IllegalStateException e) {
                    Log.e("Add Battery", e.toString());
                }
            } else {
                type = spinnerType.getSelectedItem().toString();
                batteryName = txtBattName.getText().toString();
                batteryCells = parseInt(ddlCells.getSelectedItem().toString());
                batteryMah = parseInt((txtMah.getText().toString()));
                batteryCycle = parseInt(txtCycle.getText().toString());

                //update battery to the database
                save.updateBattery(batteryName, batteryCells, batteryMah, batteryCycle, type);
                toastUpdate.show();
                finish();
            }
        } else {
            createToast(toastInvalid, duration);
        }
    }

    //methods
    public void createToast(CharSequence text, Integer duration) {
        Toast toast = Toast.makeText(getApplicationContext(), text, duration);
        toast.show();
    }

}
