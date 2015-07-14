package com.example.parrish.test;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import Classes.Battery;
import Classes.Entry;
import Classes.SaveData;
import Classes.SlidingTabLayout;
import lecho.lib.hellocharts.formatter.AxisValueFormatter;
import lecho.lib.hellocharts.formatter.SimpleAxisValueFormatter;
import lecho.lib.hellocharts.listener.LineChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.view.LineChartView;

public class BatteryStatsGraph extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private String batteryName;
    private GraphView graph;
    SaveData save;
    ArrayAdapter<String> adapterSearch;
    FloatingActionMenu floatingActionMenu;
    FloatingActionButton fabEdit;
    private FloatingActionButton fabDelete;
    private boolean boolFabShown = false;
    private ViewPager pager;
    Entry selectedEntry;
    List<Entry> entriesForBattery;
    boolean boolGraphDrawn = false;
    private int selectedPos;
    private LineChartView chart;
    private ListView entryListView;

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
        View view;
        // Inflate the layout for this fragment
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            view = inflater.inflate(R.layout.fragment_battery_stats_graph, container, false);
        } else {
            view = inflater.inflate(R.layout.fragment_battery_stats_graph_l, container, false);
        }

        pager = (ViewPager) view.findViewById(R.id.pager);
        this.batteryName = this.getArguments().getString("batteryName");
        this.floatingActionMenu = (FloatingActionMenu) view.findViewById(R.id.actionMenu);
        this.fabEdit = (FloatingActionButton) view.findViewById(R.id.fabEdit);
        this.fabDelete = (FloatingActionButton) view.findViewById(R.id.fabDelete);
        this.chart = (LineChartView) view.findViewById(R.id.chart);
        floatingActionMenu.setVisibility(View.INVISIBLE);
        SaveData save = new SaveData(view.getContext());
        entriesForBattery = save.getAllEntriesForBattery(this.batteryName);
        plotChart(view);
        displayList(view, entriesForBattery);
        handleActionMenu(view, entriesForBattery);

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
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (this.isVisible()) {
            boolFabShown = false;
            floatingActionMenu.setVisibility(View.INVISIBLE);
        }
    }

    public void plotChart(View view) {
        final List<Entry> entryList;
        Iterator<Entry> iterator;
        Entry entry;
        //DataPoint point;
        PointValue point;
        int iteratorCount = 0;
        double highestPoint = 0;
        double minutes;
        int chargeUsed;
        save = new SaveData(view.getContext());
        //graph = (GraphView) view.findViewById(R.id.graph);
        SecondScale secondScale;
        final View view2 = view;

        entryList = save.getAllEntriesForBattery(batteryName);
/*        DataPoint[] pointsRunTime = new DataPoint[entryList.size()];
        DataPoint[] pointsCharge = new DataPoint[entryList.size()];*/
        List<PointValue> pointsRunTime = new ArrayList<>();
        List<PointValue> pointsCharge = new ArrayList<>();
        List<AxisValue> axisValuesEntry = new ArrayList<>();
        List<AxisValue> axisValuesRunTime = new ArrayList<>();

        iterator = entryList.listIterator();
        //sets data points for the graph; includes BOTH series
        while (iterator.hasNext()) {
            entry = iterator.next();
            minutes = Double.parseDouble(entry.getRunTime()) / 60;
            point = new PointValue(Float.parseFloat(Integer.toString(iteratorCount)), Float.parseFloat(Double.toString(minutes)));
            axisValuesRunTime.add(new AxisValue(Float.parseFloat(Double.toString(minutes))));
            pointsRunTime.add(point);
            if (minutes > highestPoint) {
                highestPoint = minutes;  //for respectful view for y-axis
            }
            chargeUsed = Integer.parseInt(entry.getStartCharge()) - Integer.parseInt(entry.getEndCharge());
            point = new PointValue(Float.parseFloat(Integer.toString(iteratorCount)), Float.parseFloat(Integer.toString(chargeUsed)));
            pointsCharge.add(point);
            axisValuesEntry.add(new AxisValue(Float.parseFloat(Integer.toString(iteratorCount))));
            iteratorCount++;
        }

        List<Line> lines = new ArrayList<>();

        Line line = new Line(pointsRunTime);
        line.setColor(Color.RED);
        line.setShape(ValueShape.CIRCLE);

        lines.add(line);
        LineChartData chartData = new LineChartData(lines);

        Axis axisX = new Axis();
        Axis axisY = new Axis();

        axisX.setName("Entry");
        axisX.setValues(axisValuesEntry);

        axisY.setName("Run Time");
        axisY.setValues(axisValuesRunTime);


        chart.setOnValueTouchListener(new LineChartOnValueSelectListener() {
            @Override
            public void onValueSelected(int i, int i1, PointValue pointValue) {
                Dialog dialog = new Dialog(view2.getContext());
                Entry popup_entry;
                Long timeSeconds;

                //set dialog instance view to layout
                dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.popup_view_statistics);

                TextView txtBatteryName = (TextView) dialog.findViewById(R.id.txtBatteryName);
                TextView txtBatteryRunTime = (TextView) dialog.findViewById(R.id.txtRunTime);
                TextView txtBatteryStart = (TextView) dialog.findViewById(R.id.txtBatteryStart);
                TextView txtBatteryEnd = (TextView) dialog.findViewById(R.id.txtBatteryEnd);
                TextView txtEntryNumber = (TextView) dialog.findViewById(R.id.txtEntryNumber);
                EditText txtNotes = (EditText) dialog.findViewById(R.id.txtNotes);
                TextView txtDate = (TextView) dialog.findViewById(R.id.txtDate);
                TextView txtTime = (TextView) dialog.findViewById(R.id.txtTime);

                popup_entry = entryList.get(i1);
                String date = popup_entry.getEntryDate();

                //int entry = (int) dataPoint.getX();
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

                txtEntryNumber.setText(Integer.toString(i1));
                txtBatteryName.setText(popup_entry.getBatteryName());
                txtBatteryStart.setText(popup_entry.getStartCharge() + "%");
                txtBatteryEnd.setText(popup_entry.getEndCharge() + "%");
                txtNotes.setText(popup_entry.getNotes());
                txtDate.setText(popup_entry.getEntryDate());
                txtTime.setText(popup_entry.getEntryTime());

                dialog.show();

            }

            @Override
            public void onValueDeselected() {

            }
        });

        chartData.setAxisXBottom(axisX);
        chartData.setAxisYLeft(axisY);
        chartData.setBaseValue(Float.NEGATIVE_INFINITY);

        chart.setLineChartData(chartData);
    }

    //handles battery list display
    public void displayList(View view, List<Entry> entries) {
        final View view2 = view;
        Entry entry;
        String[] entryDates = new String[entries.size()];
        //loops through the List<Battery>
        for (int i = 0; i < entries.size(); i++) {
            //gets the battery into the object battery
            entry = entries.get(i);
            //appends the battery name to the batteryName array
            entryDates[i] = entry.getEntryDate() + " " + entry.getEntryTime();
        }

        Arrays.sort(entryDates, Collections.reverseOrder());
        //final SwipeMenuListView entryList = (SwipeMenuListView) view2.findViewById(R.id.listEntries);
        entryListView = (ListView) view.findViewById(R.id.listEntries);
        adapterSearch = new ArrayAdapter<String>(view2.getContext(), android.R.layout.simple_list_item_1, entryDates);
        entryListView.setAdapter(adapterSearch);

        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(BatteryStatsGraph.this, android.R.layout.simple_list_item_single_choice, entryDates);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_selectable_list_item, entryDates);
        adapter.setDropDownViewResource(android.R.layout.simple_selectable_list_item);
        entryListView.setAdapter(adapter);
        entryListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);


        // listening to single list item on click
        entryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // populating class with selected item
                selectedPos = Math.abs((position - entriesForBattery.size()) + 1);
                selectedEntry = entriesForBattery.get(selectedPos);
                //String str = (String) entriesForBattery.get(position));
                if (boolFabShown == false) {
                    floatingActionMenu.setVisibility(View.VISIBLE);
                    floatingActionMenu.setBackgroundColor(Color.parseColor("#01FFFFFF"));
                    floatingActionMenu.setAnimation(AnimationUtils.loadAnimation(view.getContext(), R.anim.fab_slide_in_from_right));
                    boolFabShown = true;
                }
            }
        });
    }

    public void handleActionMenu(final View view, final List<Entry> entryList) {
        fabDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (selectedEntry == null) {
                    Toast toast = Toast.makeText(view.getContext(), "Please select an entry", Toast.LENGTH_SHORT);
                    toast.show();
                }

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(view.getContext());

                // set icon
                alertDialogBuilder.setIcon(R.mipmap.ic_alert);
                // set title
                alertDialogBuilder.setTitle("Delete entry");

                // set dialog message
                alertDialogBuilder
                        .setMessage("This action can't be undone and will delete all associated entries.")
                        .setCancelable(false)
                        .setPositiveButton(R.string.alert_confirm, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                try {
                                    save.deleteEntry(Integer.parseInt(selectedEntry.getId()));
                                    entriesForBattery.remove(selectedPos);
                                    floatingActionMenu.close(true);
                                    floatingActionMenu.setAnimation(AnimationUtils.loadAnimation(view.getContext(), R.anim.fab_slide_out_to_right));
                                    floatingActionMenu.setVisibility(View.INVISIBLE);
                                    boolFabShown = false;
                                    Toast toast = Toast.makeText(view.getContext(), "Entry Deleted!", Toast.LENGTH_SHORT);
                                    TextView textView = (TextView) toast.getView().findViewById(android.R.id.message);
                                    View toastView = (View) toast.getView();
                                    int color = toastView.getSolidColor();
                                    textView.setBackgroundColor(color);
                                    toast.show();
                                    displayList(view, save.getAllEntriesForBattery(batteryName));
                                    plotChart(view);
                                } catch (SQLiteException e) {
                                    Log.e("Delete entry", e.toString());
                                }
                            }
                        })
                        .setNegativeButton(R.string.alert_cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // if this button is clicked, just close
                                // the dialog box and do nothing
                                dialog.cancel();
                            }
                        });
                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

            }
        });

        fabEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (selectedEntry == null) {
                    Toast toast = Toast.makeText(view.getContext(), "Please select an entry", Toast.LENGTH_SHORT);
                    toast.show();
                }

                try {
                    Intent intent = new Intent(view.getContext(), EditBattery.class);
                    intent.putExtra("edit", true);
                    intent.putExtra("id", selectedEntry.getId());
                    startActivity(intent);
                } catch (SQLiteException e) {
                    Log.e("Edit entry", e.toString());
                }

            }
        });

    }
}
