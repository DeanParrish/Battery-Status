package com.example.parrish.test;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

    public void onSubmitClick(View view) {
        EditText txtBatteryName = (EditText) findViewById(R.id.txtBatteryName);
        EditText txtBatteryCells = (EditText) findViewById(R.id.txtBatteryCells);
        EditText txtBatteryMah = (EditText) findViewById(R.id.txtMah);

        String batteryName;
        String batteryCells;
        String batteryMah;
        //test git push
        batteryName = txtBatteryName.getText().toString();
        batteryCells = txtBatteryCells.getText().toString();
        batteryMah = txtBatteryMah.getText().toString();


    }
}
