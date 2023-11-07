package com.example.coursework_1786.dao;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.coursework_1786.models.Hike;
import com.example.coursework_1786.models.Observation;

import java.util.List;

@Dao
public interface ObservationDao {
    @Insert
    long insertObservation(Observation observation);

    @Query("SELECT * FROM observations ORDER BY name")
    List<Observation> getAllObservation();

    @Query("SELECT * FROM observations WHERE hike_id = :hikeId ORDER BY name")
    List<Observation> getAllObservationsForHikeId(long hikeId);
    @Query("SELECT * FROM observations WHERE observation_id = :observationId")
    Observation getObservationById(long observationId);
    @Update
    void updateObservation(Observation observation);
    @Delete
    void deleteObservation(Observation observation);
}