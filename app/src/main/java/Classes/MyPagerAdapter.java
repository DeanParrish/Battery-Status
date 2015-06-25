package Classes;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.parrish.test.BatteryStatsBasic;
import com.example.parrish.test.BatteryStatsGraph;

/**
 * Created by Parrish on 6/23/2015.
 */

public class MyPagerAdapter extends FragmentPagerAdapter {
    String batteryName;
    public MyPagerAdapter(FragmentManager fragmentManager, String batteryName) {
        super(fragmentManager);
        this.batteryName = batteryName;
    }

    // Returns total number of pages
    @Override
    public int getCount() {
        return 2;
    }

    // Returns the fragment to display for that page
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                // Top Rated fragment activity
                return BatteryStatsBasic.newInstance(batteryName);
            case 1:
                // Games fragment activity
                return BatteryStatsGraph.newInstance(batteryName);
            case 2:
                // Movies fragment activity

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
