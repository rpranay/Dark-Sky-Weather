package com.dev.pranay.darkskyweather.Model.Remote

import com.dev.pranay.darkskyweather.Model.CurrentWeather
import com.dev.pranay.darkskyweather.Model.DailyForecast
import com.google.gson.annotations.SerializedName

class DarkSkyResponse (
        @SerializedName("currently")
        var currentWeather: CurrentWeather,
        @SerializedName("daily")
        var dailyForecast: DailyForecast
){}