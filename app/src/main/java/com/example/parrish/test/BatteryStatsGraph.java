package com.example.parrish.test;

import android.app.Activity;
import android.app.Dialog;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.SecondScale;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.PointsGraphSeries;
import com.jjoe64.graphview.series.Series;

import java.math.RoundingMode;
import java.text.NumberFormat;
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
        double highestPoint = 0;
        double minutes;
        int chargeUsed;
        save = new SaveData(view.getContext());
        graph = (GraphView) view.findViewById(R.id.graph);
        SecondScale secondScale;
        NumberFormat numberFormat;
        final View view2 = view;

        entryList = save.getAllEntriesForBattery(batteryName);
        DataPoint[] pointsRunTime = new DataPoint[entryList.size()];
        DataPoint[] pointsCharge = new DataPoint[entryList.size()];

        iterator = entryList.listIterator();
        while (iterator.hasNext()){
            entry = iterator.next();
            minutes = Double.parseDouble(entry.getRunTime()) / 60;
            point = new DataPoint(Double.parseDouble(Integer.toString(iteratorCount)), minutes);
            pointsRunTime[iteratorCount] = point;
            if (minutes > highestPoint){
                highestPoint = minutes;  //set respectful view for y-axis
            }
            chargeUsed = Integer.parseInt(entry.getStartCharge()) - Integer.parseInt(entry.getEndCharge());
            point = new DataPoint(Double.parseDouble(Integer.toString(iteratorCount)), Double.parseDouble(Integer.toString(chargeUsed)));
            pointsCharge[iteratorCount] = point;
            iteratorCount++;
        }
        LineGraphSeries<DataPoint> seriesRunTime = new LineGraphSeries<>(pointsRunTime);
        PointsGraphSeries<DataPoint> seriesCharge = new PointsGraphSeries<>(pointsCharge);
        seriesRunTime.setDrawDataPoints(true);
        seriesCharge.setColor(Color.parseColor("#FFB2B2"));     //light red for bar chart
        seriesRunTime.setTitle("Run Time");
        seriesCharge.setTitle("Charge Used");

        Viewport viewPort = graph.getViewport();
        viewPort.setMinY(0);
        viewPort.setYAxisBoundsManual(true);
        viewPort.setScrollable(true);
        highestPoint = 5*(Math.ceil(Math.abs(highestPoint / 5))); //rounds to nearest increment of 5 for runtime
        viewPort.setMaxY(highestPoint);
        viewPort.setMaxX(Double.parseDouble(Integer.toString(iteratorCount)));
        viewPort.setScalable(true);
        //viewPort.setMaxY(200);

        secondScale = graph.getSecondScale();
        secondScale.addSeries(seriesCharge);
        secondScale.setMinY(0);
        secondScale.setMaxY(100);
/*        graph.getSecondScale().addSeries(seriesCharge);
        graph.getSecondScale().setMinY(0);
        graph.getSecondScale().setMaxY(100);
        graph.getGridLabelRenderer().setVerticalLabelsSecondScaleColor(Color.RED);*/
        graph.getLegendRenderer().setVisible(true);
        graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
        graph.onDataChanged(false, true);

        numberFormat = NumberFormat.getNumberInstance();
        numberFormat.setRoundingMode(RoundingMode.DOWN);
        graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter(numberFormat, numberFormat));


        seriesRunTime.setOnDataPointTapListener(new OnDataPointTapListener() {
            @Override
            public void onTap(Series series, DataPointInterface dataPoint) {
                Dialog dialog = new Dialog(view2.getContext());
                Entry popup_entry;
                Long timeSeconds;

                dialog.setContentView(R.layout.popup_view_statistics);

                TextView txtBatteryName = (TextView) dialog.findViewById(R.id.txtBatteryName);
                TextView txtBatteryRunTime = (TextView) dialog.findViewById(R.id.txtRunTime);
                TextView txtBatteryStart = (TextView) dialog.findViewById(R.id.txtBatteryStart);
                TextView txtBatteryEnd = (TextView) dialog.findViewById(R.id.txtBatteryEnd);
                TextView txtEntryNumber = (TextView) dialog.findViewById(R.id.txtEntryNumber);

                popup_entry = entryList.get((int) dataPoint.getX());

                int entry = (int) dataPoint.getX();
                timeSeconds = Long.parseLong(popup_entry.getRunTime());

                if ((timeSeconds / 60) >= 60) {          //more than one hour
                    int hours;
                    String strHours;
                    int minutes;
                    String strMinutes;
                    int seconds;
                    String strSeconds;
                    hours = Integer.parseInt(Long.toString(timeSeconds / 3600));
                    if (hours < 10) {
                        strHours = "0" + Integer.toString(hours);
                    } else {
                        strHours = Integer.toString(hours);
                    }
                    minutes = (Integer.parseInt(Long.toString(timeSeconds)) / 60) - (hours * 60);
                    if (minutes < 10) {
                        strMinutes = "0" + Integer.toString(minutes);
                    } else {
                        strMinutes = Integer.toString(minutes);
                    }
                    seconds = Integer.parseInt(Long.toString(timeSeconds)) - ((hours * 3600) + (minutes * 60));
                    if (seconds < 10) {
                        strSeconds = "0" + Integer.toString(seconds);
                    } else {
                        strSeconds = Integer.toString(seconds);
                    }
                    txtBatteryRunTime.setText(strHours + ":" + strMinutes + ":" + strSeconds);
                } else if ((timeSeconds / 60) >= 1) {
                    int minutes = (Integer.parseInt(Long.toString(timeSeconds)) / 60);
                    int seconds = Integer.parseInt(Long.toString(timeSeconds)) - (minutes * 60);
                    String strMinutes;
                    String strSeconds;

                    if (minutes < 10) {
                        strMinutes = "0" + Integer.toString(minutes);
                    } else {
                        strMinutes = Integer.toString(minutes);
                    }
                    if (seconds < 10) {
                        strSeconds = "0" + Integer.toString(seconds);
                    } else {
                        strSeconds = Integer.toString(seconds);
                    }
                    txtBatteryRunTime.setText(strMinutes + ":" + strSeconds);
                } else {
                    int seconds = Integer.parseInt(Long.toString(timeSeconds));
                    if (seconds < 10) {
                        txtBatteryRunTime.setText("00:0" + seconds);
                    } else {
                        txtBatteryRunTime.setText("00:" + seconds);
                    }

                }

                txtEntryNumber.setText(Integer.toString(entry));
                txtBatteryName.setText(popup_entry.getBatteryName());
                // txtBatteryRunTime.setText(popup_entry.getRunTime());
                txtBatteryStart.setText(popup_entry.getStartCharge() + "%");
                txtBatteryEnd.setText(popup_entry.getEndCharge() + "%");

                dialog.show();
            }
        }) ;

        //graph.addSeries(seriesCharge);
        graph.addSeries(seriesRunTime);


    }
}
