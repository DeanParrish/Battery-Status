package com.example.parrish.test;

import android.content.res.Configuration;
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
    Integer userID;
    String batteryName;
    TextView txtviewBatteryName;
    TextView txtviewBatteryCells;
    TextView txtviewBatteryMAH;
    TextView txtviewBatteryCycles;
    TextView txtviewBatteryType;
    TextView txtviewCyclesComplete;
    TextView txtviewCyclesLeft;

    public static BatteryStatsBasic newInstance(Integer user, String batteryName) {
        BatteryStatsBasic batteryStatsBasic = new BatteryStatsBasic();
        Bundle args = new Bundle();
        args.putInt("userID", user);
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
        this.userID = this.getArguments().getInt("userID");
        this.batteryName = this.getArguments().getString("batteryName");
        save = new SaveData(container.getContext());
        View view;

        view = inflater.inflate(R.layout.fragment_battery_stats_basic, container, false);
        txtviewBatteryName = (TextView) view.findViewById(R.id.txtBatteryName);
        txtviewBatteryCells = (TextView) view.findViewById(R.id.txtBatteryCells);
        txtviewBatteryMAH = (TextView) view.findViewById(R.id.txtBatteryMAH);
        txtviewBatteryCycles = (TextView) view.findViewById(R.id.txtCycles);
        txtviewBatteryType = (TextView) view.findViewById(R.id.txtBatteryType);
        txtviewCyclesComplete = (TextView) view.findViewById(R.id.txtCyclesCompleted);
        txtviewCyclesLeft = (TextView) view.findViewById(R.id.txtCyclesLeft);

        displayBatteryInfo();
        return view;
    }

    private void displayBatteryInfo(){
        Battery battery;
        List<Entry> entryList;
        Integer cyclesLeft;

        battery = save.getBattery(userID, batteryName);

        txtviewBatteryName.setText(battery.getName());
        txtviewBatteryCells.setText(battery.getCells());
        txtviewBatteryType.setText(battery.getType());
        txtviewBatteryCycles.setText(battery.getCycles());
        txtviewBatteryMAH.setText(battery.getMah());

        entryList = save.getAllEntriesForBattery(userID, batteryName);

        txtviewCyclesComplete.setText(Integer.toString(entryList.size()));
        cyclesLeft = Integer.parseInt(battery.getCycles()) - entryList.size();
        if (cyclesLeft < 0){
            txtviewCyclesLeft.setText("Completed cycles exceed rated cycles!");
            txtviewCyclesLeft.setTextColor(-65536); //Red color
        } else {
            txtviewCyclesLeft.setText(cyclesLeft.toString());
        }
}
}