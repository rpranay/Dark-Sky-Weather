package com.dev.pranay.darkskyweather.Model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "forecast")
class TemperatureData (
        @PrimaryKey
        var time: Long,
        var icon: String,
        @ColumnInfo(name = "min_temeperature")
        @SerializedName("temperatureLow")
        var minTemperature: Float,
        @ColumnInfo(name = "max_temeperature")
        @SerializedName("temperatureHigh")
        var maxTemperature: Float
){}