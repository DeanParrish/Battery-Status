package com.example.parrish.test;

import android.app.Activity;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import Classes.Battery;
import Classes.SaveData;


public class CreateBattery extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_battery);

        Spinner ddlBatteryType = (Spinner) findViewById(R.id.ddlType);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.battery_type, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ddlBatteryType.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_create_battery, menu);

        return super.onCreateOptionsMenu(menu);

//        getMenuInflater().inflate(R.menu.menu_create_battery, menu);
//        return true;
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

    public void onSubmitClick(View view) {
        SaveData save = new SaveData(view.getContext());
        //Screen fields and conversions
        Spinner spinnerType = (Spinner) findViewById(R.id.ddlType);
        EditText txtBattName = (EditText) findViewById(R.id.txtBatteryName);
        EditText txtCells = (EditText) findViewById(R.id.txtBatteryCells);
        EditText txtMah = (EditText) findViewById(R.id.txtMah);
        EditText txtCycle = (EditText) findViewById(R.id.txtCycles);

        try {
            String type = spinnerType.getSelectedItem().toString();
            String batteryName = txtBattName.getText().toString();
            int batteryCells = Integer.parseInt(txtCells.getText().toString());
            int batteryMah = Integer.parseInt((txtMah.getText().toString()));
            int batteryCycle = Integer.parseInt(txtCycle.getText().toString());
        } catch (IllegalStateException e){
            Log.e("Add Battery", e.toString());
        }


/*        //add the battery to the database
        try {
            save.addBattery(batteryName,batteryCells, batteryMah, batteryCycle,  type);
        } catch (SQLiteException e){
            Log.e("Add Battery", e.toString());
        }*/

    }
}
