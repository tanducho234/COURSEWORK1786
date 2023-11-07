package com.example.coursework_1786.models;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "observations",
        foreignKeys = {
                @ForeignKey(entity = Hike.class,
                        parentColumns = "id",
                        childColumns = "hike_id",
                        onDelete = ForeignKey.CASCADE)
        })
public class Observation {
    @PrimaryKey(autoGenerate = true)
    public long observation_id;
    public String name;
    public String daytime;
    public String comment;
    public long hike_id; // Foreign key referencing the Hike entity

}
