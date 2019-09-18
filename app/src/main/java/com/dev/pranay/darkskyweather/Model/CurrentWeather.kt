package com.dev.pranay.darkskyweather.Model

import com.google.gson.annotations.SerializedName

class CurrentWeather (
        var summary: String,
        var icon: String,
        var temperature: Float,
        @SerializedName("apparentTemperature")
        var feelsLikeTemperature: Float
){}