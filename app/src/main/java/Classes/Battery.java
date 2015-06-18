package Classes;

/**
 * Created by Parrish on 6/17/2015.
 */
public class Battery {
    private String name;
    private int cells;
    private int mah;
    private int cycles;
    private String type;

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

    //getters
    public String getName(){
        return this.name.toString();
    }
}
