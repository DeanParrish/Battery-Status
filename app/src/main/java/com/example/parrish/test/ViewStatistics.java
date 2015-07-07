package com.example.parrish.test;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
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
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import java.util.Arrays;
import java.util.List;

import Classes.Battery;
import Classes.SaveData;

public class ViewStatistics extends Activity {

    final Context context = this;
    SaveData save;
    String batteryName;
    Battery pBattery = new Battery();
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

        //start population of drop down list
        save = new SaveData(getApplicationContext());
        //gets all batteries in a List<Battery>
        batteries = save.getAllBatteries();

        if (batteries.size() == 0) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("no_batteries", true);
            startActivity(intent);
        }
        //new string for battery names
        batteryNames = new String[batteries.size()];

        //loops through the List<Battery>
        for (int i = 0; i < batteries.size(); i++) {
            //gets the battery into the object battery
            battery = batteries.get(i);
            //appends the battery name to the batteryName array
            batteryNames[i] = battery.getName();
        }

        Arrays.sort(batteryNames);
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
//        if (id == R.id.action_details) {
//            if (batteryName != null) {
//                Intent intent = new Intent(context, BatteryStatistics.class);
//                intent.putExtra("battery",batteryName);
//                startActivity(intent);
//            } else {
//                createToast(toastText, duration);
//            }
//        }
        return super.onOptionsItemSelected(item);
    }

    //methods
    public void createToast(CharSequence text, Integer duration) {
        Toast toast = Toast.makeText(getApplicationContext(), text, duration);
        toast.show();
    }
}
