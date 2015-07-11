package com.example.parrish.test;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import Classes.MyPagerAdapter;
import Classes.SaveData;
import Classes.SlidingTabLayout;

public class BatteryStatistics extends FragmentActivity {

    SaveData save;
    ViewPager viewPager;
    MyPagerAdapter mPagerAdapter;
    SlidingTabLayout mtabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battery_statistics);

//         1. get passed intent
        Intent intent = getIntent();

//         2. get person object from intent
        String stringBattery  = intent.getStringExtra("battery");

        viewPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new MyPagerAdapter(getSupportFragmentManager(), stringBattery);
        viewPager.setAdapter(mPagerAdapter);
        mtabs = (SlidingTabLayout) findViewById(R.id.tabs);
        mtabs.setDistributeEvenly(true);
        mtabs.setViewPager(viewPager);

        try{
            //region Action Bar
            //       hides shadow from action bar
            ActionBar actionBar = getActionBar();
            actionBar.setElevation(0);
            // Enabling Up / Back navigation
            actionBar.setDisplayHomeAsUpEnabled(true);
            //hide label in action bar
//            actionBar.setDisplayShowTitleEnabled(false);
            setTitle("Battery List");
            //endregion
        } catch (NullPointerException e){
            Log.e("actionbar", e.toString());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_battery_statistics, menu);
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        final Context context = this;

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
