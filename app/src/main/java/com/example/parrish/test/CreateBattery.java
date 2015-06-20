package com.example.parrish.test;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import Classes.Battery;
import Classes.SaveData;


public class CreateBattery extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_battery);

//       hides shadow from action bar
        ActionBar actionBar = getActionBar();
        actionBar.setElevation(0);
        // Enabling Up / Back navigation
        actionBar.setDisplayHomeAsUpEnabled(true);
        //hide label in action bar
        actionBar.setDisplayShowTitleEnabled(false);

        Integer variable = getIntent().getExtras().getInt("create_battery");

        if (variable == 0) {
            TextView textViewToChange = (TextView) findViewById(R.id.title);
            textViewToChange.setText("EDIT BATTERY");
            textViewToChange.setBackgroundColor(Color.parseColor("#f39c12"));

            EditText textInputToChange = (EditText) findViewById(R.id.txtBatteryName);
            textInputToChange.setEnabled(false);
        }

        Spinner ddlBatteryType = (Spinner) findViewById(R.id.ddlType);
        Spinner ddlBatteryCells = (Spinner) findViewById(R.id.ddlCells);
        Integer[] cells = {1, 2, 3, 4, 5, 6, 7, 8};

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.battery_type, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ddlBatteryType.setAdapter(adapter);

        ArrayAdapter<Integer> adapterCells = new ArrayAdapter<Integer>( this, android.R.layout.simple_spinner_dropdown_item, cells);
        adapterCells.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ddlBatteryCells.setAdapter(adapterCells);
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        switch (id) {
            case R.id.action_save:
                onSubmitClick(item);
        }

        return super.onOptionsItemSelected(item);
    }

    public void onSubmitClick(MenuItem item) {
        SaveData save = new SaveData(getApplicationContext());
        String type;
        String batteryName;
        int batteryCells;
        int batteryMah;
        int batteryCycle;
        CharSequence toastText = "Battery created!";
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(getApplicationContext(), toastText, duration);
        //Screen fields and conversions
        Spinner spinnerType = (Spinner) findViewById(R.id.ddlType);
        Spinner ddlCells = (Spinner) findViewById(R.id.ddlCells);
        EditText txtBattName = (EditText) findViewById(R.id.txtBatteryName);
        EditText txtMah = (EditText) findViewById(R.id.txtMah);
        EditText txtCycle = (EditText) findViewById(R.id.txtCycles);

        try {
            type = spinnerType.getSelectedItem().toString();
            batteryName = txtBattName.getText().toString();
            batteryCells = Integer.parseInt(ddlCells.getSelectedItem().toString());
            batteryMah = Integer.parseInt((txtMah.getText().toString()));
            batteryCycle = Integer.parseInt(txtCycle.getText().toString());
            //add the battery to the database
            try {
                save.addBattery(batteryName, batteryCells, batteryMah, batteryCycle, type);
                toast.show();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            } catch (SQLiteException e) {
                Log.e("Add Battery", e.toString());
            }
        } catch (IllegalStateException e) {
            Log.e("Add Battery", e.toString());
        }
    }
}
