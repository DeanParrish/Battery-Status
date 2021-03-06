package com.example.parrish.test;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
//import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Classes.Battery;
import Classes.SaveData;
import Classes.User;

import static java.lang.Integer.*;

import com.rey.material.widget.EditText;


public class CreateBattery extends Activity {

    Integer editFlag;
    String userEmail;
    Integer userID;
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
            actionBar.setDisplayShowTitleEnabled(false);
            setTitle("Menu");

        } catch (NullPointerException e) {
            Log.e("actionbar", e.toString());
        }

        View testView = findViewById(R.id.action_save );


        Integer variable = getIntent().getExtras().getInt("create_battery");
        editFlag = variable;
        userEmail = getIntent().getExtras().getString("userEmail");
        userID = getIntent().getExtras().getInt("userID");
        if (userID == 0){
            if (getIntent().getExtras().getString("userID") == null){
                userID = null;
            }
        }

        //handleLongClick(findViewById(R.id.action_save), true);

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
            mbattery = save.getBattery(userID, stringBattery);

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
        //super.onCreateOptionsMenu(menu);
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        //menu.
        inflater.inflate(R.menu.menu_create_battery, menu);

        for (int i = 0; i < menu.size(); i++){
            final MenuItem item = menu.getItem(i);
            if (item.getItemId() == R.id.action_save){
                View itemActionView = item.getActionView();
                if (itemActionView != null){
                    itemActionView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onSubmitClick(item);
                        }
                    });
                    itemActionView.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            Toast.makeText(getApplicationContext(), "Save", Toast.LENGTH_SHORT).show();
                            return true;
                        }
                    });
                }
            }
        }
        return true;
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
                            return;
                        } else {
                            finish();
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
                                return;
                            } else {
                                finish();
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
        final Context context = this;
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
        CharSequence toastTextDuplicateBattery = "Battery already exists!";
        int duration = Toast.LENGTH_SHORT;
        Toast toastCreate = Toast.makeText(getApplicationContext(), toastTextCreate, duration);
        Toast toastUpdate = Toast.makeText(getApplicationContext(), toastTextEdit, duration);
        Toast toastDuplicateBattery = Toast.makeText(getApplicationContext(),toastTextDuplicateBattery, duration);
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
        String toastDuplicateBattery2 = "Battery already exists!";

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
                    batteryName = capBatteryName;
                    batteryCells = parseInt(ddlCells.getSelectedItem().toString());
                    batteryMah = parseInt((txtMah.getText().toString()));
                    batteryCycle = parseInt(txtCycle.getText().toString());
                    //add the battery to the database
                    try {
                        List<Battery> listBatteries;
                        listBatteries = save.getAllBatteriesUser(userID);
                        Battery compareBattery;
                        boolean addBattery = true;

                        if (listBatteries.size() > 1){
                            addBattery = true;
                            Iterator<Battery> iterator = listBatteries.iterator();
                            //iterate and compare battery names; if exists, set flag to not add battery; kick back an error message
                            while (iterator.hasNext()){
                                compareBattery = iterator.next();
                                if (compareBattery.getName().equals(batteryName)){
                                    addBattery = false;
                                }
                            }
                        }

                        if (addBattery == true) {
                            save.addBattery(userID, batteryName, batteryCells, batteryMah, batteryCycle, type, "");
                            toastCreate.show();
                            finish();
                        } else {
                            txtBattName.setError(toastDuplicateBattery2);
                            txtBattName.requestFocus();
                            createToast(toastDuplicateBattery2, duration);
                        }
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
                save.updateBattery(userID, batteryName, batteryCells, batteryMah, batteryCycle, type, "");
                toastUpdate.show();
                finish();
            }
        } else {
            if (!txtMah.getText().toString().trim().equals("")){
                Integer intMah = parseInt(txtMah.getText().toString());
                EditText lblMahValue = (EditText) findViewById(R.id.txtMah);
                lblMahValue.setText(intMah.toString());
            }
            if (!txtCycle.getText().toString().trim().equals("")){
                Integer intCycles = parseInt(txtCycle.getText().toString());
                EditText lblCyclesValue = (EditText) findViewById(R.id.txtCycles);
                lblCyclesValue.setText(intCycles.toString());
            }
            createToast(toastInvalid, duration);
        }
    }

    //methods
    public void createToast(CharSequence text, Integer duration) {
        Toast toast = Toast.makeText(getApplicationContext(), text, duration);
        toast.show();
    }

    public void handleLongClick(final MenuItem menuItem, final boolean enable){
        final View actionView = menuItem.getActionView();
        //final View actionView = MenuItemCompat.getActionView(menuItem);
        //final View actionView = findViewById(R.id.m)
        //menuItem.view
        if (actionView == null){
            return;
        }
        //final CharSequence title = menuItem.getTitle();
        if(!enable){
            actionView.setOnLongClickListener(null);
        } else {
            actionView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    final int[] screenPos = new int[2];
                    final Rect displayFrame = new Rect();
                    actionView.getLocationOnScreen(screenPos);
                    actionView.getWindowVisibleDisplayFrame(displayFrame);

                    final Context context = actionView.getContext();
                    final int width = actionView.getWidth();
                    final int height = actionView.getHeight();
                    final int midy = screenPos[1] + height / 2;
                    final int screenWidth = context.getResources().getDisplayMetrics().widthPixels;

                    final Toast toolTip = Toast.makeText(context, "TOAST", Toast.LENGTH_SHORT);
                    TextView textView = (TextView) toolTip.getView().findViewById(android.R.id.message);
                    View toastView = toolTip.getView();
                    int color = toastView.getSolidColor();
                    textView.setBackgroundColor(color);
                    toolTip.show();
                    return true;
                }
            });
        }

    }
}
