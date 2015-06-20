package com.example.parrish.test;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import Classes.Battery;
import Classes.Entry;
import Classes.SaveData;


public class ViewStatistics extends Activity {

    SaveData save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_statistics);

        // hides shadow from action bar
        ActionBar actionBar = getActionBar();
        actionBar.setElevation(0);
        // Enabling Up / Back navigation
        actionBar.setDisplayHomeAsUpEnabled(true);
        //hide label in action bar
        actionBar.setDisplayShowTitleEnabled(false);


        //region Populate List from database
        List<Battery> batteries;
        Battery battery;
        String[] batteryNames;
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
            batteryNames[i] = battery.getName().toString();
        }

        ListView batteryList = (ListView) findViewById(R.id.listView);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(ViewStatistics.this, android.R.layout.simple_list_item_single_choice, batteryNames);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        batteryList.setAdapter(adapter);
        batteryList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

//        // listening to single list item on click
//        batteryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            final Context context = this;
//
//            public void onItemClick(AdapterView<?> parent, View view,
//                                    int position, long id) {
//
//                // selected item
//                String product = ((TextView) view).getText().toString();
//
//                // Launching new Activity on selecting single List Item
//                Intent intent = new Intent(context, CreateBattery.class);
//                intent.putExtra("Battery Name", "Test2");  // sets flag for create battery
//                startActivity(intent);
//            }
//            //endregion
//        });
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
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_details) {
            final Context context = this;
            Intent intent = new Intent(context, BatteryStatistics.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
