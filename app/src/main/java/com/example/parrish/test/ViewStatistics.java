package com.example.parrish.test;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.os.Parcelable;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import Classes.Battery;
import Classes.SaveData;


public class ViewStatistics extends Activity {

    SaveData save;
    String batteryName;
    Battery pBattery = new Battery();
    ListView batteryList;
    List<Battery> allBatteries;
    int ddlTypePos;
    int ddlCellsPos;
    int ddlMahHighPos;
    int ddlMahLowPos;
    boolean boolChkCells;
    boolean boolChkType;
    boolean boolChkMah;

    CharSequence toastText = "Select a battery";
    int duration = Toast.LENGTH_SHORT;
    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_statistics);

        try {
            // hides shadow from action bar
            ActionBar actionBar = getActionBar();
            actionBar.setElevation(0);
            // Enabling Up / Back navigation
            actionBar.setDisplayHomeAsUpEnabled(true);
            //hide label in action bar
            actionBar.setDisplayShowTitleEnabled(false);
        } catch (NullPointerException e){
            Log.e("actionbar", e.toString());
        }

        //region Populate List from database

        Battery battery;
        String[] batteryNames;
        batteryList = (ListView) findViewById(R.id.listView);
        //start population of drop down list
        save = new SaveData(getApplicationContext());
        //gets all batteries in a List<Battery>
        allBatteries = save.getAllBatteries();

        if (allBatteries.size() == 0) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("no_batteries", true);
            startActivity(intent);
        }
        displayList(batteryList, allBatteries);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_statistics, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
//        String lv_batteryName = batteryName;
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_details) {
            if (batteryName != null) {
                Intent intent = new Intent(context, BatteryStatistics.class);
                intent.putExtra("battery",batteryName);
                startActivity(intent);
            } else {
                createToast(toastText, duration);
            }
        } else if (id == R.id.action_filter){
            handleFilter();
        }


        return super.onOptionsItemSelected(item);
    }

    //methods
    public void createToast(CharSequence text, Integer duration){
        Toast toast = Toast.makeText(getApplicationContext(), text, duration);
        toast.show();
    }

    //handles battery list display
    public void displayList(ListView list, List<Battery> batteries ){
        Battery battery;
        String[] batteryNames = new String[batteries.size()];
        //loops through the List<Battery>
        for (int i = 0; i < batteries.size(); i++) {
            //gets the battery into the object battery
            battery = batteries.get(i);
            //appends the battery name to the batteryName array
            batteryNames[i] = battery.getName();
        }

        Arrays.sort(batteryNames);
        batteryList = (ListView) findViewById(R.id.listView);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(ViewStatistics.this, android.R.layout.simple_list_item_single_choice, batteryNames);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        batteryList.setAdapter(adapter);
        batteryList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        // listening to single list item on click
        batteryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // populating class with selected item
                String name = ((TextView) view).getText().toString();
                batteryName = name;
                pBattery.setName(name);
            }
        });
    }

    public void handleFilter(){
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.popup_filter_battery);
        dialog.setTitle("Filter Batteries");

        final Spinner ddlType = (Spinner) dialog.findViewById(R.id.ddlBatteryType);
        final Spinner ddlCells = (Spinner) dialog.findViewById(R.id.ddlBatteryCells);
        final Spinner ddlMahHigh = (Spinner) dialog.findViewById(R.id.ddlMAHHigh);
        final Spinner ddlMahLow = (Spinner) dialog.findViewById(R.id.ddlMAHLow);
        final Button btnFilter = (Button) dialog.findViewById(R.id.btnFilter);
        final  Button btnClearFilter = (Button) dialog.findViewById(R.id.btnClearFilter);
        final CheckBox chkCells = (CheckBox) dialog.findViewById(R.id.chkCells);
        final CheckBox chkMAH = (CheckBox) dialog.findViewById(R.id.chkMAH);
        final CheckBox chkType = (CheckBox) dialog.findViewById(R.id.chkBatteryType);


        ArrayAdapter<CharSequence> adapterType = ArrayAdapter.createFromResource(context, R.array.battery_type, android.R.layout.simple_spinner_dropdown_item);
        adapterType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ddlType.setAdapter(adapterType);

        ArrayAdapter<CharSequence> adapterCells = ArrayAdapter.createFromResource(context, R.array.cell_number, android.R.layout.simple_spinner_dropdown_item);
        adapterCells.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ddlCells.setAdapter(adapterCells);

        ArrayAdapter<CharSequence> adapterMAHHigh = ArrayAdapter.createFromResource(context, R.array.MAH, android.R.layout.simple_spinner_dropdown_item);
        adapterMAHHigh.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ddlMahHigh.setAdapter(adapterMAHHigh);

        ArrayAdapter<CharSequence> adapterMAHLow = ArrayAdapter.createFromResource(context, R.array.MAH, android.R.layout.simple_spinner_dropdown_item);
        adapterMAHLow.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ddlMahLow.setAdapter(adapterMAHLow);

        //set filter screen from previous filters
        if (ddlTypePos != 0){
            ddlType.setSelection(ddlTypePos);
        }
        if (ddlCellsPos != 0) {
            ddlCells.setSelection(ddlCellsPos);
        }
        if (ddlMahLowPos != 0){
            ddlMahLow.setSelection(ddlMahLowPos);
        }
        if (ddlMahHighPos != 0){
            ddlMahHigh.setSelection(ddlMahHighPos);
        }
        if (boolChkType == true){
            chkType.setChecked(true);
        }
        if (boolChkCells == true) {
            chkCells.setChecked(true);
        }
        if (boolChkMah == true) {
            chkMAH.setChecked(true);
        }

        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                List<Battery> filteredBatteries = new LinkedList<>();
                int filterMode = 0;
                Iterator<Battery> iterator;
                Battery battery;

                String batteryType = ddlType.getSelectedItem().toString();
                String batteryCells = ddlCells.getSelectedItem().toString();
                String batteryMahHigh = ddlMahHigh.getSelectedItem().toString();
                String batteryMahLow = ddlMahLow.getSelectedItem().toString();

                //set positions to keep state for future filters
                ddlTypePos = ddlType.getSelectedItemPosition();
                ddlCellsPos = ddlCells.getSelectedItemPosition();
                ddlMahHighPos = ddlMahHigh.getSelectedItemPosition();
                ddlMahLowPos = ddlMahLow.getSelectedItemPosition();
                boolChkType = chkType.isChecked();
                boolChkCells = chkCells.isChecked();
                boolChkMah = chkMAH.isChecked();

                //get filtration mode
                if (chkType.isChecked()) {
                    filterMode = filterMode + 1;
                }
                if (chkCells.isChecked()) {
                    filterMode = filterMode + 2;
                }
                if (chkMAH.isChecked()) {
                    filterMode = filterMode + 5;
                }

                iterator = allBatteries.iterator();

                while (iterator.hasNext()) {
                    battery = iterator.next();

                    //filter mode guide:
                    // 1 = filtering by Battery Type
                    // 2 = filtering by Cells
                    // 5 = filtering by MAH
                    // all of these can be combined for more filteration (IE: 7 = filtering by Cells AND MAH)
                    switch (filterMode) {
                        case 1:
                            if (battery.getType().equals(batteryType)) {
                                filteredBatteries.add(battery);
                            }
                            break;
                        case 2:
                            if (battery.getCells().equals(batteryCells)) {
                                filteredBatteries.add(battery);
                            }
                            break;
                        case 3:
                            if (battery.getType().equals(batteryType) && battery.getCells().equals(batteryCells)) {
                                filteredBatteries.add(battery);
                            }
                            break;
                        case 5:
                            if (Integer.parseInt(battery.getMah()) >= Integer.parseInt(batteryMahLow) &&
                                    Integer.parseInt(battery.getMah()) <= Integer.parseInt(batteryMahHigh)) {
                                filteredBatteries.add(battery);
                            }
                            break;
                        case 6:
                            if (battery.getType().equals(batteryType) && Integer.parseInt(battery.getMah()) >= Integer.parseInt(batteryMahLow) &&
                                    Integer.parseInt(battery.getMah()) <= Integer.parseInt(batteryMahHigh)) {
                                filteredBatteries.add(battery);
                            }
                            break;
                        case 7:
                            if (battery.getCells().equals(batteryCells) && Integer.parseInt(battery.getMah()) >= Integer.parseInt(batteryMahLow) &&
                                    Integer.parseInt(battery.getMah()) <= Integer.parseInt(batteryMahHigh)) {
                                filteredBatteries.add(battery);
                            }
                            break;
                        case 8:
                            if (battery.getCells().equals(batteryCells) && battery.getType().equals(batteryType) && Integer.parseInt(battery.getMah()) >= Integer.parseInt(batteryMahLow) &&
                                    Integer.parseInt(battery.getMah()) <= Integer.parseInt(batteryMahHigh)) {
                                filteredBatteries.add(battery);
                            }
                            break;
                        default:
                            filteredBatteries.add(battery);
                    }
                }
                displayList(batteryList, filteredBatteries);
                dialog.dismiss();
            }
        });

        btnClearFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //set to whole list from SaveData.GetAllEntries()
                displayList(batteryList, allBatteries);
                dialog.dismiss();
                //reset all positions of filter screen
                ddlTypePos = 0;
                ddlCellsPos = 0;
                ddlMahLowPos = 0;
                ddlMahHighPos = 0;
                boolChkType = false;
                boolChkCells = false;
                boolChkMah = false;
            }
        });

        dialog.show();

    }
}
