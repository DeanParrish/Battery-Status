package com.example.parrish.test;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

import Classes.Battery;
import Classes.SaveData;


public class EditBattery extends Activity {

    SaveData save;
    String batteryName;
    Battery pBattery = new Battery();

    CharSequence toastText = "Select a battery";
    int duration = Toast.LENGTH_SHORT;
    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_battery);

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

        Arrays.sort(batteryNames);
        ListView batteryList = (ListView) findViewById(R.id.listView);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(EditBattery.this, android.R.layout.simple_list_item_single_choice, batteryNames);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_battery, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_edit) {
            if (batteryName != null) {
                Intent intent = new Intent(context, CreateBattery.class);
                intent.putExtra("create_battery", 0);  // sets flag for create battery
                intent.putExtra("battery",batteryName);
                startActivity(intent);
            } else {
                createToast(toastText, duration);
            }
        }
        if (id == android.R.id.home) {
            Intent intent = new Intent(context, MainActivity.class);
            startActivity(intent);
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