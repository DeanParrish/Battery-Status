package Classes;

import java.io.Serializable;

/**
 * Created by Parrish on 6/17/2015.
 */
public class Battery implements Serializable {
    private String name;
    private int cells;
    private int mah;
    private int cycles;
    private String type;
    private Integer userID;
    private String synced;

    public Battery() {}

    public Battery(String name, int cells, int mah, int cycles, String type){
        super();
        this.name = name;
        this.cells = cells;
        this.mah = mah;
        this.cycles = cycles;
        this.type = type;
    }

    //returns generic message
    public String toString(){
        return "Battery Name: " + name + ", Cells: " + cells + ", MAH: " + mah +
                ", Cycles: " + cycles + ", Type: " + type;
    }

    //setters
    public void setUserID(Integer id){this.userID = id;}

    public void setName(String name) {
        this.name = name;
    }

    public void setCells(int cells){
        this.cells = cells;
    }

    public void setMah(int mah){
        this.mah = mah;
    }

    public void setCycles(int cycles){
        this.cycles = cycles;
    }

    public void setType(String type){
        this.type = type;
    }

    public void setRunCyclesFinished(int cycles){
        this.cycles = cycles;
    }
    public void setSynced(String synced){
        this.synced = synced;
    }

    //getters
    public Integer getUserID() {return this.userID;}
    public String getName(){
        return this.name.toString();
    }
    public String getCells(){
        return Integer.toString(this.cells);
    }
    public String getMah(){
        return Integer.toString(this.mah);
    }
    public String getCycles(){
        return Integer.toString(this.cycles);
    }
    public String getType(){
        return this.type.toString();
    }
    public String getSynced() { return this.synced; }


}
