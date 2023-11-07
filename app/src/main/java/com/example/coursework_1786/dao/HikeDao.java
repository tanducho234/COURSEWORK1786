package com.example.coursework_1786.dao;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.coursework_1786.models.Hike;

import java.util.List;

@Dao
public interface HikeDao {
    @Insert
    long insertHike(Hike hike);

    @Query("SELECT * FROM hikes ORDER BY name")
    List<Hike> getAllHikes();

    @Query("SELECT * FROM hikes WHERE id = :hikeId")
    Hike getHikeById(long hikeId);
    @Update
    void updateHike(Hike hike);
    @Delete
    void deleteHike(Hike hike);
    @Query("DELETE FROM hikes")
    void deleteAllHikes();
    @Query("SELECT * FROM hikes WHERE name LIKE '%' || :keyword || '%'")
    List<Hike> findHikesWithKeyword(String keyword);

}