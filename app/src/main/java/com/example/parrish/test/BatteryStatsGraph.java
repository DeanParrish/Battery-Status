package com.example.parrish.test;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import Classes.Entry;
import Classes.SaveData;
import lecho.lib.hellocharts.listener.ComboLineColumnChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.ComboLineColumnChartData;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.view.ComboLineColumnChartView;

public class BatteryStatsGraph extends Fragment {

    private Integer userID;
    private String batteryName;
    SaveData save;
    FloatingActionMenu floatingActionMenu;
    FloatingActionButton fabEdit;
    private FloatingActionButton fabDelete;
    private boolean boolFabShown = false;
    Entry selectedEntry;
    List<Entry> entriesForBattery;
    private int selectedPos;
    private ComboLineColumnChartView chart;
    private ListView entryListView;
    private View lastSelectedEntryView;
    private View lastView;
    private int listSelected;

    public static BatteryStatsGraph newInstance(Integer userID, String batteryName) {
        BatteryStatsGraph fragment = new BatteryStatsGraph();
        Bundle args = new Bundle();
        if (userID != null) {
            args.putInt("userID", userID);
        }else {
            args.putString("userID", "");
        }
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
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            view = inflater.inflate(R.layout.fragment_battery_stats_graph, container, false);
        } else {
            view = inflater.inflate(R.layout.fragment_battery_stats_graph_l, container, false);
        }

        this.userID = this.getArguments().getInt("userID");
        if (this.userID == 0){
            String tempString = this.getArguments().getString("userID");
            if (tempString.equals("")){
                this.userID = null;
            }
        }
        this.batteryName = this.getArguments().getString("batteryName");
        this.floatingActionMenu = (FloatingActionMenu) view.findViewById(R.id.actionMenu);
        this.fabEdit = (FloatingActionButton) view.findViewById(R.id.fabEdit);
        this.fabDelete = (FloatingActionButton) view.findViewById(R.id.fabDelete);
        this.chart = (ComboLineColumnChartView) view.findViewById(R.id.chart);
        floatingActionMenu.setVisibility(View.INVISIBLE);
        SaveData save = new SaveData(view.getContext());
        entriesForBattery = save.getAllEntriesForBattery(this.userID, this.batteryName);
        displayList(view, entriesForBattery);
        plotChart(view, -1);

        handleActionMenu(view);

        return view;
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
    public void onResume() {
        super.onResume();
        if (lastView != null && entriesForBattery.size() >= 1){
            displayList(lastView, save.getAllEntriesForBattery(userID, batteryName));
            plotChart(lastView, selectedPos);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (this.isVisible()) {
            boolFabShown = false;
            floatingActionMenu.setVisibility(View.INVISIBLE);
        }
    }

    public void plotChart(final View view, int selectedPos) {
        final List<Entry> entryList;
        Iterator<Entry> iterator;
        Entry entry;
        PointValue point;
        int iteratorCount = 0;
        double minutes;
        int chargeUsed;
        save = new SaveData(view.getContext());

        entryList = save.getAllEntriesForBattery(userID, batteryName);
        List<PointValue> pointsRunTime = new ArrayList<>();
        List<SubcolumnValue> pointsCharge;
        List<Column> columns = new ArrayList<>();
        List<AxisValue> axisValuesEntry = new ArrayList<>();
        List<AxisValue> axisValuesRunTime = new ArrayList<>();
        List<AxisValue> axisValuesChargeUsed = new ArrayList<>();

        iterator = entryList.listIterator();
        //sets data points for the graph; includes BOTH series
        while (iterator.hasNext()) {
            pointsCharge = new ArrayList<>();
            //get entry
            entry = iterator.next();
            //get minutes
            minutes = Double.parseDouble(entry.getRunTime()) / 60;
            point = new PointValue(Float.parseFloat(Integer.toString(iteratorCount)), Float.parseFloat(Double.toString(minutes)));
            axisValuesRunTime.add(new AxisValue(Float.parseFloat(Double.toString(minutes))));
            pointsRunTime.add(point);
            //get charge used
            chargeUsed = Integer.parseInt(entry.getStartCharge()) - Integer.parseInt(entry.getEndCharge());
            //set bar graph color
            if (selectedPos == -1 || iteratorCount != selectedPos) {
                if (chargeUsed > 80 && chargeUsed <= 100) {
                    pointsCharge.add(new SubcolumnValue(Float.parseFloat(Integer.toString(chargeUsed)), Color.parseColor("#4CAF50")));
                } else if (chargeUsed <= 80 && chargeUsed > 60) {
                    pointsCharge.add(new SubcolumnValue(Float.parseFloat(Integer.toString(chargeUsed)), Color.parseColor("#8BC34A")));

                } else if (chargeUsed <= 60 && chargeUsed > 40) {
                    pointsCharge.add(new SubcolumnValue(Float.parseFloat(Integer.toString(chargeUsed)), Color.parseColor("#FFEB3B")));

                } else if (chargeUsed <= 40 && chargeUsed > 20) {
                    pointsCharge.add(new SubcolumnValue(Float.parseFloat(Integer.toString(chargeUsed)), Color.parseColor("#FF9800")));
                } else {
                    pointsCharge.add(new SubcolumnValue(Float.parseFloat(Integer.toString(chargeUsed)), Color.parseColor("#F44336")));
                }
            } else {
                pointsCharge.add(new SubcolumnValue(Float.parseFloat(Integer.toString(chargeUsed)), Color.GRAY));
            }
            Column column = new Column(pointsCharge);
            column.setHasLabels(true);
            columns.add(column);
            axisValuesEntry.add(new AxisValue(Float.parseFloat(Integer.toString(iteratorCount))));
            iteratorCount++;
        }

        for (int i = 0; i <= 100; i+=25){
            axisValuesChargeUsed.add(new AxisValue(Float.parseFloat(Integer.toString(i))));
        }
        //line information/styling
        List<Line> lines = new ArrayList<>();

        Line line = new Line(pointsRunTime);
        line.setColor(Color.parseColor("#9b59b6"));
        line.setShape(ValueShape.CIRCLE);

        lines.add(line);
        LineChartData lineChartData = new LineChartData(lines);

        chart.setOnValueTouchListener(new ComboLineColumnChartOnValueSelectListener() {
            @Override
            public void onColumnValueSelected(int i, int i1, SubcolumnValue subcolumnValue) {

            }

            @Override
            public void onPointValueSelected(int i, int i1, PointValue pointValue) {

                final Dialog dialog = new Dialog(view.getContext());
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

                listSelected = entryListView.getCount() - i1 - 1;
                entryListView.setSelection(listSelected);
                Handler handler;
                handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        entryListView.performItemClick(view, listSelected, R.id.listEntries);
                    }
                }, 50);

                handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dialog.show();
                    }
                }, 100);

            }

            @Override
            public void onValueDeselected() {

            }
        });

        //axis values
        Axis axisX = new Axis();
        Axis axisY = new Axis();
        Axis axisYRight = new Axis();

        axisX.setName("Entry");
        axisX.setValues(axisValuesEntry);

        axisY.setName("Run Time");
        axisY.setValues(axisValuesRunTime);

        axisYRight.setName("Charge Used");
        axisYRight.setValues(axisValuesChargeUsed);

        //column chart data
        ColumnChartData columnChartData = new ColumnChartData(columns);

        //output chart data
        ComboLineColumnChartData chartData = new ComboLineColumnChartData(columnChartData, lineChartData);

        chartData.setAxisXBottom(axisX);
        chartData.setAxisYLeft(axisY);
        chartData.setAxisYRight(axisYRight);

        chart.setComboLineColumnChartData(chartData);
    }

    //handles battery list display
    public void displayList(View view, List<Entry> entries) {
        Entry entry;
        String[] entryDates = new String[entries.size()];
        List<Map<String, String>> adapter = new ArrayList<>();
        Map<String, String> listValues;
        Collections.reverse(entries);
        //loops through the List<Battery>
        for (int i = 0; i < entries.size(); i++) {
            //gets the battery into the object battery
            entry = entries.get(i);
            listValues = new HashMap<>(2);
            listValues.put("entryDate", entry.getEntryDate() + " " + entry.getEntryTime());
            if (!entry.getEditDate().equals("") &&
                !entry.getEditTime().equals("") && !entry.getEditTime().equals(entry.getEntryTime())){
                listValues.put("editDate", "Last Edited: " + entry.getEditDate() + " " + entry.getEditTime());
            }
            adapter.add(listValues);

            //appends the battery name to the batteryName array
            entryDates[i] = entry.getEntryDate() + " " + entry.getEntryTime();

        }

        Arrays.sort(entryDates, Collections.reverseOrder());
        entryListView = (ListView) view.findViewById(R.id.listEntries);
        SimpleAdapter simpleAdapter = new SimpleAdapter(view.getContext(), adapter, android.R.layout.simple_list_item_2,
                                                        new String[] {"entryDate", "editDate"},
                                                        new int[] {android.R.id.text1, android.R.id.text2});
        entryListView.setAdapter(simpleAdapter);
        entryListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        // listening to single list item on click
        entryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (lastSelectedEntryView != null){
                    lastSelectedEntryView.setBackgroundColor(Color.WHITE);
                    lastSelectedEntryView.findViewById(android.R.id.text1).setBackgroundColor(Color.WHITE);
                    lastSelectedEntryView.findViewById(android.R.id.text2).setBackgroundColor(Color.WHITE);
                }

                // populating class with selected item
                selectedPos = Math.abs((position - entriesForBattery.size()) + 1);
                //View listView = entryListView.getChildAt(position - entryListView.getFirstVisiblePosition());
                for (int i = 0; i < entryListView.getCount(); i++){
                    View listView = getChildView(i, entryListView);
                    if (i != position) {
                        listView.findViewById(android.R.id.text1).setBackgroundColor(Color.WHITE);
                        listView.findViewById(android.R.id.text2).setBackgroundColor(Color.WHITE);
                        listView.setBackgroundColor(Color.WHITE);
                    } else {
                        listView.findViewById(android.R.id.text1).setBackgroundColor(Color.parseColor("#E6E6E6"));
                        listView.findViewById(android.R.id.text2).setBackgroundColor(Color.parseColor("#E6E6E6"));
                        listView.setBackgroundColor(Color.parseColor("#E6E6E6"));
                        lastSelectedEntryView = listView;
                    }
                }

                selectedEntry = entriesForBattery.get(position);
                plotChart(view, selectedPos);
                listSelected = position;


                if (!boolFabShown) {
                    floatingActionMenu.setVisibility(View.VISIBLE);
                    floatingActionMenu.setBackgroundColor(Color.parseColor("#01FFFFFF"));
                    floatingActionMenu.setAnimation(AnimationUtils.loadAnimation(view.getContext(), R.anim.fab_slide_in_from_right));
                    boolFabShown = true;
                }
            }
        });
    }

    public void handleActionMenu(final View view) {
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
                                    save.deleteEntry(userID, Integer.parseInt(selectedEntry.getId()));
                                    entriesForBattery.remove(selectedPos);
                                    floatingActionMenu.close(true);
                                    floatingActionMenu.setAnimation(AnimationUtils.loadAnimation(view.getContext(), R.anim.fab_slide_out_to_right));
                                    floatingActionMenu.setVisibility(View.INVISIBLE);
                                    boolFabShown = false;
                                    Toast toast = Toast.makeText(view.getContext(), "Entry Deleted!", Toast.LENGTH_SHORT);
                                    TextView textView = (TextView) toast.getView().findViewById(android.R.id.message);
                                    View toastView = toast.getView();
                                    int color = toastView.getSolidColor();
                                    textView.setBackgroundColor(color);
                                    toast.show();
                                    displayList(view, save.getAllEntriesForBattery(userID, batteryName));
                                    plotChart(view, -1);
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
                    Intent intent = new Intent(view.getContext(), CreateEntry.class);
                    intent.putExtra("edit", true);
                    intent.putExtra("id", Integer.parseInt(selectedEntry.getId()));
                    intent.putExtra("batteryName",batteryName);
                    lastView = view;
                    startActivity(intent);
                    displayList(view, save.getAllEntriesForBattery(userID, batteryName));
                    plotChart(view, -1);
                } catch (SQLiteException e) {
                    Log.e("Edit entry", e.toString());
                }

            }
        });

    }

    public View getChildView(int pos, ListView listView){
        final int firstListViewItem = listView.getFirstVisiblePosition();
        final int lastListViewItem = listView.getLastVisiblePosition();

        if (pos < firstListViewItem || pos > lastListViewItem){
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListViewItem;
            return listView.getChildAt(childIndex);
        }
    }
}
