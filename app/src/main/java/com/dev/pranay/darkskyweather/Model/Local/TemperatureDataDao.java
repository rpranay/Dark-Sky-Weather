package com.dev.pranay.darkskyweather.Model.Local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import com.dev.pranay.darkskyweather.Model.TemperatureData;

import java.util.List;

@Dao
public abstract class TemperatureDataDao {

    @Insert
    public abstract void insertAll(List<TemperatureData> list);

    @Query("DELETE FROM forecast")
    public abstract void deleteAll();

    @Transaction
    public void updateDB(List<TemperatureData> newList){
        deleteAll();
        insertAll(newList);
    }

    @Query("SELECT * FROM forecast ORDER BY time ASC")
    public abstract LiveData<List<TemperatureData>> getWeeklyForecast();


}
