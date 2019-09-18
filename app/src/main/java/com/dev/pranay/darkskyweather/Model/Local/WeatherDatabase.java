package com.dev.pranay.darkskyweather.Model.Local;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.dev.pranay.darkskyweather.Model.TemperatureData;

@Database(entities = TemperatureData.class, version = 1)
public abstract class WeatherDatabase extends RoomDatabase {
    public abstract TemperatureDataDao temperatureDataDao();
}
