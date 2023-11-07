package com.example.coursework_1786.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.Relation;

import java.util.List;

@Entity(tableName = "hikes")
public class Hike {
    @PrimaryKey(autoGenerate = true)
    public long id;
    public String name;
    public String location;
    public String date;
    public Boolean isParkingAvailable;
    public int length;
    public String level;
    public String desc;
}