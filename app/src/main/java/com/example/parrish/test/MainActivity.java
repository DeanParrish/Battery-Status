package com.example.parrish.test;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;


public class MainActivity extends Activity {

    Button btn_battery_create;
    Button btn_battery_archive;
    Button btn_entry_create;
    Button btn_entry_remove;
    Button btn_view_stats;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addListenerOnButton();
        addListenerOnButton2();
        addListenerOnButton3();
        addListenerOnButton4();
        addListenerOnButton5();

/*        Spinner ddlBatteryType = (Spinner) findViewById(R.id.ddlType);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.battery_type, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ddlBatteryType.setAdapter(adapter);*/
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

    public void addListenerOnButton() {
        final Context context = this;
        btn_battery_create = (Button) findViewById(R.id.button11);
        btn_battery_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(context, CreateBattery.class);
                startActivity(intent);
            }
        });
    }

    public void addListenerOnButton2() {
        final Context context = this;
        btn_battery_archive  = (Button) findViewById(R.id.button12);
        btn_battery_archive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(context, ArchiveBattery.class);
                startActivity(intent);
            }
        });
    }

    public void addListenerOnButton3() {
        final Context context = this;
        btn_entry_create  = (Button) findViewById(R.id.button8);
        btn_entry_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(context, CreateEntry.class);
                startActivity(intent);
            }
        });
    }
    public void addListenerOnButton4() {
        final Context context = this;
        btn_entry_remove  = (Button) findViewById(R.id.button9);
        btn_entry_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(context, RemoveEntry.class);
                startActivity(intent);
            }
        });
    }

    public void addListenerOnButton5() {
        final Context context = this;
        btn_view_stats = (Button) findViewById(R.id.button10);
        btn_view_stats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(context, ViewStatistics.class);
                startActivity(intent);
            }
        });
    }
}
