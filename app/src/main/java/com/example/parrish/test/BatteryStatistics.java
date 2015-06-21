package com.example.parrish.test;

import android.app.ActionBar;
import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;
import android.widget.TextView;

import java.util.List;

import Classes.Battery;
import Classes.Entry;
import Classes.SaveData;
import Classes.Tab1Activity;
import Classes.Tab2Activity;


public class BatteryStatistics extends Activity {

    SaveData save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battery_statistics);

        //region Action Bar
        //       hides shadow from action bar
        ActionBar actionBar = getActionBar();
        actionBar.setElevation(0);
        // Enabling Up / Back navigation
        actionBar.setDisplayHomeAsUpEnabled(true);
        //hide label in action bar
        actionBar.setDisplayShowTitleEnabled(false);
        //endregion


//         1. get passed intent
        Intent intent = getIntent();

//         2. get person object from intent
        String stringBattery  = intent.getStringExtra("battery");
//        Battery classBattery = (Battery) getIntent().getSerializableExtra("classBattery");


//region Populate List from database
        List<Battery> batteries;
        Battery mbattery;
        save = new SaveData(getApplicationContext());
        //gets all batteries in a List<Battery>
        mbattery = save.getBattery(stringBattery);

        List<Entry> entries;
        Entry mEntry;
        entries = save.getAllEntriesForBattery(mbattery.getName());



        /**region Tab Host
        // create the TabHost that will contain the Tabs*/
        TabHost tabHost = (TabHost)findViewById(R.id.tabHost);
        tabHost.setup();

        TabHost.TabSpec tab1 = tabHost.newTabSpec("tab1");
        TabHost.TabSpec tab2 = tabHost.newTabSpec("tab2");

        // Set the Tab name and Activity that will be opened when particular Tab will be selected
        tab1.setContent(R.id.tab1);//new Intent(this, Tab1Activity.class));
        tab1.setIndicator("BASIC");
        tab2.setContent(R.id.tab2);//new Intent(this, Tab2Activity.class));
        tab2.setIndicator("GRAPHS");

        /** Add the tabs  to the TabHost to display. */
        tabHost.addTab(tab1);
        tabHost.addTab(tab2);

        TextView lblBatteryValue = (TextView) findViewById(R.id.lblBatteyValue);
        lblBatteryValue.setText(mbattery.getName());
        TextView lblCellValue = (TextView) findViewById(R.id.lblCellValue);
        lblCellValue.setText(mbattery.getCells());
        TextView lblMahValue = (TextView) findViewById(R.id.lblMahValue);
        lblMahValue.setText(mbattery.getMah());
        TextView lblTypeValue = (TextView) findViewById(R.id.lblTypeValue);
        lblTypeValue.setText(mbattery.getType());
        TextView lblCyclesValue = (TextView) findViewById(R.id.lblCyclesValue);
        lblCyclesValue.setText(mbattery.getCycles());

        //endregion
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_battery_statistics, menu);
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
}
