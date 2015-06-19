package Classes;

/**
 * Created by Dean Parrish on 6/18/2015.
 */
public class Entry {
    private String batteryName;
    private int runTime;
    private int startCharge;
    private int endCharge;

    public Entry(){}

    public Entry(String name, int time, int start, int end){
        super();
        this.batteryName = name;
        this.runTime = time;
        this.startCharge = start;
        this.endCharge = end;
    }

    //setters
    public void setBatteryName(String name){
        this.batteryName = name;
    }

    public void setRunTime(int time){
        this.runTime = time;
    }

    public void setStartCharge(int charge){
        this.startCharge = charge;
    }

    public void setEndCharge(int charge){
        this.endCharge = charge;
    }
}