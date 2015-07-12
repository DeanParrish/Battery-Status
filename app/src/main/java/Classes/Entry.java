package Classes;

import java.io.Serializable;

/**
 * Created by Dean Parrish on 6/18/2015.
 */
public class Entry implements Serializable {
    private String batteryName;
    private int runTime;
    private int startCharge;
    private int endCharge;
    private String entryDate;
    private String entryTime;
    private String notes;
    private int id;

    public Entry() {
    }

    public Entry(String name, int time, int start, int end) {
        super();
        this.batteryName = name;
        this.runTime = time;
        this.startCharge = start;
        this.endCharge = end;
    }

    //setters
    public void setBatteryName(String name) {
        this.batteryName = name;
    }

    public void setRunTime(int time) {
        this.runTime = time;
    }

    public void setStartCharge(int charge) {
        this.startCharge = charge;
    }

    public void setEndCharge(int charge) {
        this.endCharge = charge;
    }

    public void setEntryDate(String date){this.entryDate = date; }

    public void setEntryTime(String time){this.entryTime = time; }

    public void setNotes(String notes){ this.notes = notes; }
    public void setId (int id){
        this.id = id;
    }

    //getters
    public String getBatteryName(){
        return this.batteryName;
    }

    public String getRunTime(){
        return Long.toString(this.runTime);
    }
    public String getStartCharge(){
        return  Integer.toString(this.startCharge);
    }

    public String getEndCharge(){
        return  Integer.toString(this.endCharge);
    }
    public String getEntryDate() {return this.entryDate; }
    public String getEntryTime() {return  this.entryTime; }
    public String getNotes() {return this.notes; }
    public String getId(){return Integer.toString(this.id); }
}
