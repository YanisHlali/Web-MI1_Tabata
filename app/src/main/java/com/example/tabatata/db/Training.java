package com.example.tabatata.db;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "training")
public class Training {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private int preparation;
    private int sequence;
    private int cycle;
    private int work;
    private int rest;
    private int longRest;



    // Getters and setters

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name;}

    public int getPreparation() { return preparation; }
    public void setPreparation(int preparation) { this.preparation = preparation; }

    public int getSequence() { return preparation; }
    public void setSequence(int sequence) { this.sequence = sequence; }

    public int getCycle() { return cycle; }
    public void setCycle(int cycle) { this.cycle = cycle; }

    public int getWork() { return work; }
    public void setWork(int work) { this.work = work; }

    public int getRest() { return rest; }
    public void setRest(int rest) {this.rest = rest; }

    public int getLongRest() { return longRest; }
    public void setLongRest(int longRest) { this.longRest = longRest; }


}