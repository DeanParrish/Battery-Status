package com.example.parrish.test;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import Classes.Battery;
import Classes.Entry;
import Classes.SaveData;

/**
 * Created by Parrish on 6/22/2015.
 */
public class BatteryStatsBasic extends Fragment {
    // Store instance variables
    SaveData save;
    String batteryName;
    TextView txtviewBatteryName;
    TextView txtviewBatteryCells;
    TextView txtviewBatteryMAH;
    TextView txtviewBatteryCycles;
    TextView txtviewBatteryType;
    TextView txtviewCyclesComplete;

    public static BatteryStatsBasic newInstance(String batteryName) {
        BatteryStatsBasic batteryStatsBasic = new BatteryStatsBasic();
        Bundle args = new Bundle();
        args.putString("batteryName", batteryName);
        batteryStatsBasic.setArguments(args);
        return batteryStatsBasic;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.batteryName = this.getArguments().getString("batteryName");
        save = new SaveData(container.getContext());

        View view = inflater.inflate(R.layout.fragment_battery_stats_basic, container, false);
        txtviewBatteryName = (TextView) view.findViewById(R.id.txtBatteryName);
        txtviewBatteryCells = (TextView) view.findViewById(R.id.txtBatteryCells);
        txtviewBatteryMAH = (TextView) view.findViewById(R.id.txtBatteryMAH);
        txtviewBatteryCycles = (TextView) view.findViewById(R.id.txtCycles);
        txtviewBatteryType = (TextView) view.findViewById(R.id.txtBatteryType);
        txtviewCyclesComplete = (TextView) view.findViewById(R.id.txtCyclesCompleted);
        displayBatteryInfo();
        return view;
    }

    private void displayBatteryInfo(){
        Battery battery;
        List<Entry> entryList;
        String cycles;

        battery = save.getBattery(batteryName);

        txtviewBatteryName.setText(battery.getName());
        txtviewBatteryCells.setText(battery.getCells());
        txtviewBatteryType.setText(battery.getType());
        txtviewBatteryCycles.setText(battery.getCycles());
        txtviewBatteryMAH.setText(battery.getMah());

        entryList = save.getAllEntriesForBattery(batteryName);

        txtviewCyclesComplete.setText(Integer.toString(entryList.size()));
}
}