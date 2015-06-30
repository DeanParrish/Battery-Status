package com.example.parrish.test;

import android.app.Activity;
import android.app.Dialog;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;

import java.util.Iterator;
import java.util.List;

import Classes.Entry;
import Classes.SaveData;

public class BatteryStatsGraph extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    // TODO: Rename and change types of parameters
    private String batteryName;
    private GraphView graph;
    SaveData save;

    // TODO: Rename and change types and number of parameters
    public static BatteryStatsGraph newInstance(String batteryName) {
        BatteryStatsGraph fragment = new BatteryStatsGraph();
        Bundle args = new Bundle();
        args.putString("batteryName", batteryName);
        //args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public BatteryStatsGraph() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //get passed arguments here
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_battery_stats_graph, container, false);
        this.batteryName = this.getArguments().getString("batteryName");
        plotChart(view);

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        //generated
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        //generated
    }

    @Override
    public void onDetach() {        super.onDetach();
    }

    public void plotChart(View view){
        final List<Entry> entryList;
        Iterator<Entry> iterator;
        Entry entry;
        DataPoint point;
        int iteratorCount = 0;
        save = new SaveData(view.getContext());
        graph = (GraphView) view.findViewById(R.id.graph);
        final View view2 = view;

        entryList = save.getAllEntriesForBattery(batteryName);
        DataPoint[] points = new DataPoint[entryList.size()];

        iterator = entryList.listIterator();
        while (iterator.hasNext()){
            entry = iterator.next();
            point = new DataPoint(Double.parseDouble(Integer.toString(iteratorCount)), Double.parseDouble(entry.getRunTime()));
            points[iteratorCount] = point;
            iteratorCount++;
        }
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(points);
        series.setDrawDataPoints(true);
        Viewport viewPort = graph.getViewport();

        viewPort.setMinY(0);
        viewPort.setYAxisBoundsManual(true);
        viewPort.setScrollable(true);
        viewPort.setMaxY(100);
        graph.onDataChanged(false, true);


        series.setOnDataPointTapListener(new OnDataPointTapListener() {
            @Override
            public void onTap(Series series, DataPointInterface dataPoint) {
                Dialog dialog = new Dialog(view2.getContext());
                Entry popup_entry;

                dialog.setContentView(R.layout.popup_view_statistics);

                TextView txtBatteryName = (TextView) dialog.findViewById(R.id.txtBatteryName);
                TextView txtBatteryRunTime = (TextView) dialog.findViewById(R.id.txtRunTime);
                TextView txtBatteryStart = (TextView) dialog.findViewById(R.id.txtBatteryStart);
                TextView txtBatteryEnd = (TextView) dialog.findViewById(R.id.txtBatteryEnd);
                TextView txtEntryNumber = (TextView) dialog.findViewById(R.id.txtEntryNumber);

                popup_entry = entryList.get((int) dataPoint.getX());

                int entry = (int) dataPoint.getX();

                txtEntryNumber.setText(Integer.toString(entry));
                txtBatteryName.setText(popup_entry.getBatteryName());
                txtBatteryRunTime.setText(popup_entry.getRunTime());
                txtBatteryStart.setText(popup_entry.getStartCharge() + "%");
                txtBatteryEnd.setText(popup_entry.getEndCharge() + "%");

                dialog.show();
            }
        }) ;

        graph.addSeries(series);

    }
}
