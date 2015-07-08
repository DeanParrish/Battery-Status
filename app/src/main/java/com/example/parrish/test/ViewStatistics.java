package com.example.parrish.test;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.util.Log;
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

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import Classes.Battery;
import Classes.SaveData;

public class ViewStatistics extends Activity {

    final Context context = this;
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
    List<Battery> batteries;
    Battery battery;
    String[] batteryNames;
    CharSequence toastText = "Select a battery";
    int duration = Toast.LENGTH_SHORT;
    // Listview Adapter
    ArrayAdapter<String> adapterSearch;

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
        } catch (NullPointerException e) {
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
/*        //new string for battery names
        batteryNames = new String[batteries.size()];

        //loops through the List<Battery>
        for (int i = 0; i < batteries.size(); i++) {
            //gets the battery into the object battery
            battery = batteries.get(i);
            //appends the battery name to the batteryName array
            batteryNames[i] = battery.getName();
        }

        Arrays.sort(batteryNames);*/
//        ListView batteryList = (ListView) findViewById(R.id.listView);
        final SwipeMenuListView batteryListSwipe = (SwipeMenuListView) findViewById(R.id.listView);

        // listening to single list item on click
//        batteryListSwipe.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            public void onItemClick(AdapterView<?> parent, View view,
//                                    int position, long id) {
//
//                // populating class with selected item
//                String name = ((TextView) view).getText().toString();
//                batteryName = name;
//                pBattery.setName(name);
//            }
//        });

        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                // create item details
                SwipeMenuItem viewDetails = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                viewDetails.setBackground(new ColorDrawable(Color.parseColor("#ff9b59b6")));
                // set item width
                viewDetails.setWidth(150);
                // set a icon
                viewDetails.setIcon(R.mipmap.ic_details_white);
                // add to menu
                menu.addMenuItem(viewDetails);


                // create "open" item
                SwipeMenuItem editItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                editItem.setBackground(new ColorDrawable(Color.parseColor("#f39c12")));
                // set item width
                editItem.setWidth(150);
                // set a icon
                editItem.setIcon(R.mipmap.ic_edit);

                // add to menu
                menu.addMenuItem(editItem);

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.parseColor("#ff7f8c8d")));
                // set item width
                deleteItem.setWidth(150);
                // set a icon
                deleteItem.setIcon(R.mipmap.ic_delete_white);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };

        // set creator
        batteryListSwipe.setMenuCreator(creator);

// step 2. listener item click event
        batteryListSwipe.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        //view details
//                        if (id == R.id.action_details) {
                        batteryName = batteryListSwipe.getItemAtPosition(position).toString();
                        if (batteryName != null) {
                            Intent intent = new Intent(context, BatteryStatistics.class);
                            intent.putExtra("battery", batteryName);
                            startActivity(intent);
                        } else {
                            createToast(toastText, duration);
                        }
//                        }
                        break;
                    case 1:
                        //edit battery
                        batteryName = batteryListSwipe.getItemAtPosition(position).toString();
                        if (batteryName != null) {
                            Intent intent = new Intent(context, CreateBattery.class);
                            intent.putExtra("create_battery", 0);  // sets flag for create battery
                            intent.putExtra("battery", batteryName);
                            startActivity(intent);
                        } else {
                            createToast(toastText, duration);
                        }

                        break;
                    case 2://delete
                        batteryName = batteryListSwipe.getItemAtPosition(position).toString();
                        final SaveData delete = new SaveData(getApplicationContext());
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

                        // set icon
                        alertDialogBuilder.setIcon(R.mipmap.ic_alert);
                        // set title
                        alertDialogBuilder.setTitle("Delete battery?");

                        // set dialog message
                        alertDialogBuilder
                                .setMessage("This action can't be undone and will delete all associated entries.")
                                .setCancelable(false)
                                .setPositiveButton(R.string.alert_confirm, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        try {
                                            delete.deleteBattery(batteryName);
                                            createToast(toastText, duration);
                                            finish();
                                            startActivity(getIntent());
                                        } catch (SQLiteException e) {
                                            Log.e("Add Battery", e.toString());
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
                        break;
                    default: {

                    }
                }
                return false;
            }
        });


        // set SwipeListener
        batteryListSwipe.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {

            @Override
            public void onSwipeStart(int position) {
                // swipe start
            }

            @Override
            public void onSwipeEnd(int position) {
                // swipe end
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_view_statistics, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

//        ListView batteryList = (ListView) findViewById(R.id.listView);
        SwipeMenuListView batteryListSwipe = (SwipeMenuListView) findViewById(R.id.listView);
        // Adding items to listview
        adapterSearch = new ArrayAdapter<>(ViewStatistics.this, android.R.layout.simple_list_item_1, batteryNames);
        batteryListSwipe.setAdapter(adapterSearch);
//        batteryListSwipe.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String arg0) {

                return true;
            }

            @Override
            public boolean onQueryTextChange(String arg0) {
                ViewStatistics.this.adapterSearch.getFilter().filter(arg0);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
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
    public void createToast(CharSequence text, Integer duration) {
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
