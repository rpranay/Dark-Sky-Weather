package com.dev.pranay.darkskyweather.Model

import com.google.gson.annotations.SerializedName

class DailyForecast(
        @SerializedName("data")
        var temperatureDataList: List<TemperatureData>
){}