package com.example.parrish.test;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.SecondScale;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.PointsGraphSeries;
import com.jjoe64.graphview.series.Series;

import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import Classes.Battery;
import Classes.Entry;
import Classes.SaveData;

public class BatteryStatsGraph extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private String batteryName;
    private GraphView graph;
    SaveData save;

    public static BatteryStatsGraph newInstance(String batteryName) {
        BatteryStatsGraph fragment = new BatteryStatsGraph();
        Bundle args = new Bundle();
        args.putString("batteryName", batteryName);
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
        SaveData save = new SaveData(view.getContext());
        List<Entry> entriesForBattery = save.getAllEntriesForBattery(this.batteryName);
        plotChart(view);
        displayList(view, entriesForBattery);

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
        final View view2 = view;

        entryList = save.getAllEntriesForBattery(batteryName);
        DataPoint[] pointsRunTime = new DataPoint[entryList.size()];
        DataPoint[] pointsCharge = new DataPoint[entryList.size()];

        iterator = entryList.listIterator();
        //sets datapoints for the graph; includes BOTH series
        while (iterator.hasNext()){
            entry = iterator.next();
            minutes = Double.parseDouble(entry.getRunTime()) / 60;
            point = new DataPoint(Double.parseDouble(Integer.toString(iteratorCount)), minutes);
            pointsRunTime[iteratorCount] = point;
            if (minutes > highestPoint){
                highestPoint = minutes;  //for respectful view for y-axis
            }
            chargeUsed = Integer.parseInt(entry.getStartCharge()) - Integer.parseInt(entry.getEndCharge());
            point = new DataPoint(Double.parseDouble(Integer.toString(iteratorCount)), Double.parseDouble(Integer.toString(chargeUsed)));
            pointsCharge[iteratorCount] = point;
            iteratorCount++;
        }
        //setting attributes of series
        LineGraphSeries<DataPoint> seriesRunTime = new LineGraphSeries<>(pointsRunTime);
        PointsGraphSeries<DataPoint> seriesCharge = new PointsGraphSeries<>(pointsCharge);
        seriesRunTime.setDrawDataPoints(true);
        seriesCharge.setColor(Color.parseColor("#FFB2B2"));     //light red for bar chart
        seriesRunTime.setTitle("Run Time");
        seriesCharge.setTitle("Charge Used");

        //changing viewport for appropriate view
        Viewport viewPort = graph.getViewport();
        viewPort.setMinY(0);
        viewPort.setYAxisBoundsManual(true);
        viewPort.setScrollable(true);
        highestPoint = 5*(Math.ceil(Math.abs(highestPoint / 5))); //rounds to nearest increment of 5 for runtime
        viewPort.setMaxY(highestPoint);
        viewPort.setMaxX(Double.parseDouble(Integer.toString(iteratorCount)));
        viewPort.setScalable(true);

        //setting second scale for charge used
        secondScale = graph.getSecondScale();
        secondScale.addSeries(seriesCharge);
        secondScale.setMinY(0);
        secondScale.setMaxY(100);

        //legend
        graph.getLegendRenderer().setVisible(true);
        graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
        graph.onDataChanged(false, true);

        //makes label number whole values
        graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter(){
            @Override
            public String formatLabel(double value, boolean isValueX){
                if ((value % 1) != 0){
                    return super.formatLabel(Math.ceil(value), isValueX);
                } else {
                    return super.formatLabel(value, isValueX);
                }
            }
        });

        //tap handler for runTime series
        seriesRunTime.setOnDataPointTapListener(new OnDataPointTapListener() {
            @Override
            public void onTap(Series series, DataPointInterface dataPoint) {
                Dialog dialog = new Dialog(view2.getContext());
                Entry popup_entry;
                Long timeSeconds;
                //set dialog instance view to layout
                dialog.setContentView(R.layout.popup_view_statistics);

                TextView txtBatteryName = (TextView) dialog.findViewById(R.id.txtBatteryName);
                TextView txtBatteryRunTime = (TextView) dialog.findViewById(R.id.txtRunTime);
                TextView txtBatteryStart = (TextView) dialog.findViewById(R.id.txtBatteryStart);
                TextView txtBatteryEnd = (TextView) dialog.findViewById(R.id.txtBatteryEnd);
                TextView txtEntryNumber = (TextView) dialog.findViewById(R.id.txtEntryNumber);
                //TextView txtNotes = (TextView) dialog.findViewById(R.id.txtNotes);
                EditText txtNotes = (EditText) dialog.findViewById(R.id.txtNotes);
                TextView txtDate = (TextView) dialog.findViewById(R.id.txtDate);
                TextView txtTime = (TextView) dialog.findViewById(R.id.txtTime);

                popup_entry = entryList.get((int) dataPoint.getX());
                String date = popup_entry.getEntryDate();

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
                } else if ((timeSeconds / 60) >= 1) {  //more than one minute
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
                txtBatteryStart.setText(popup_entry.getStartCharge() + "%");
                txtBatteryEnd.setText(popup_entry.getEndCharge() + "%");
                txtNotes.setText(popup_entry.getNotes());
                txtDate.setText(popup_entry.getEntryDate());
                txtTime.setText(popup_entry.getEntryTime());

                dialog.show();
            }
        }) ;

        graph.addSeries(seriesRunTime);
    }

    //handles battery list display
    public void displayList(View view, List<Entry> entries){
        Entry entry;
        String[] entryDates = new String[entries.size()];
        //loops through the List<Battery>
        for (int i = 0; i < entries.size(); i++) {
            //gets the battery into the object battery
            entry = entries.get(i);
            //appends the battery name to the batteryName array
            entryDates[i] = entry.getEntryDate() + " " + entry.getEntryTime();
        }

        Arrays.sort(entryDates);
        ListView entryList = (ListView) view.findViewById(R.id.listEntries);

        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(BatteryStatsGraph.this, android.R.layout.simple_list_item_single_choice, entryDates);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_list_item_single_choice, entryDates);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        entryList.setAdapter(adapter);
        entryList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        // listening to single list item on click
        entryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // populating class with selected item
                String name = ((TextView) view).getText().toString();
                batteryName = name;
            }
        });
    }
}
