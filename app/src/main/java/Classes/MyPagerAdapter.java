package Classes;

import android.content.Context;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.parrish.test.BatteryStatsBasic;
import com.example.parrish.test.BatteryStatsGraph;
import com.jjoe64.graphview.series.PointsGraphSeries;

/**
 * Created by Parrish on 6/23/2015.
 */

public class MyPagerAdapter extends FragmentPagerAdapter {
    String batteryName;
    int entries;
    Context context;
    public MyPagerAdapter(FragmentManager fragmentManager, String batteryName, Context con) {
        super(fragmentManager);
        this.batteryName = batteryName;
        this.context = con;
        SaveData save = new SaveData(context);
        this.entries = save.getAllEntriesForBattery(batteryName).size();
    }

    // Returns total number of pages
    @Override
    public int getCount() {
        if (entries >= 1) {
            return 2;
        } else {
            return 1;
        }
    }

    // Returns the fragment to display for that page
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                // Battery fragment activity
                return BatteryStatsBasic.newInstance(batteryName);
            case 1:
                // Entries fragment activity
                return BatteryStatsGraph.newInstance(batteryName);


            default: return new Fragment();
        }
    }

    // Returns the page title for the top indicator
    @Override
    public CharSequence getPageTitle(int position) {
        String[] tabs = {"Basic", "Graph"};
        return tabs[position];
    }

}
