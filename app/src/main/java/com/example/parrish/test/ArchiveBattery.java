package com.example.parrish.test;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.service.notification.StatusBarNotification;
import android.support.v4.view.MenuItemCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

import Classes.Battery;
import Classes.SaveData;


public class ArchiveBattery extends Activity {

    SaveData save;
    String batteryName;

    int duration = Toast.LENGTH_SHORT;
    CharSequence toastText = "Battery deleted";
    CharSequence toastTextError = "Select a battery";

    List<Battery> batteries;
    Battery battery;
    String[] batteryNames;

    ArrayAdapter<String> adapterSearch;
    String userEmail;
    Integer userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archive_battery);

        // hides shadow from action bar
        ActionBar actionBar = getActionBar();
        actionBar.setElevation(0);
        // Enabling Up / Back navigation
        actionBar.setDisplayHomeAsUpEnabled(true);
        //hide label in action bar
        actionBar.setDisplayShowTitleEnabled(false);

        userEmail = getIntent().getExtras().getString("userEmail");
        userID = getIntent().getExtras().getInt("userID");

        //region Populate List from database
//        List<Battery> batteries;
//        Battery battery;
//        String[] batteryNames;
        //start population of drop down list
        save = new SaveData(getApplicationContext());
        //gets all batteries in a List<Battery>
        //batteries = save.getAllBatteries();
        batteries = save.getAllBatteriesUser(userID);

        if (batteries.size() == 0) {
            finish();
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
        ListView batteryList = (ListView) findViewById(R.id.listView);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(ArchiveBattery.this, android.R.layout.simple_list_item_single_choice, batteryNames);
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
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_archive_battery, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);

        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        ListView batteryList = (ListView) findViewById(R.id.listView);

        adapterSearch = new ArrayAdapter<>(ArchiveBattery.this, android.R.layout.simple_list_item_single_choice, batteryNames);
        adapterSearch.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        batteryList.setAdapter(adapterSearch);
        batteryList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String arg0) {

                return true;
            }

            @Override
            public boolean onQueryTextChange(String arg0) {
                ArchiveBattery.this.adapterSearch.getFilter().filter(arg0);
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
        final Context context = this;
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
                            delete.deleteBattery(userID, batteryName);
                            createToast(toastText, duration);
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_archive) {
            if (batteryName != null) {
                // show alert dialog
                alertDialog.show();
            } else {
                createToast(toastTextError, duration);
            }
        }

        if (id == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //methods
    public void createToast(CharSequence text, Integer duration) {
        Toast toast = Toast.makeText(getApplicationContext(), text, duration);
        toast.show();
    }

}
