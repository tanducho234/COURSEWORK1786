package com.example.coursework_1786.database;


import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.coursework_1786.dao.HikeDao;
import com.example.coursework_1786.dao.ObservationDao;
import com.example.coursework_1786.models.Hike;
import com.example.coursework_1786.models.Observation;

@Database(entities = {Hike.class, Observation.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract HikeDao hikeDao();
    public abstract ObservationDao observationDao();

}

