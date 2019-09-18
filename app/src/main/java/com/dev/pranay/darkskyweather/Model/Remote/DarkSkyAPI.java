package com.dev.pranay.darkskyweather.Model.Remote;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface DarkSkyAPI {
    @GET("forecast/a9a07502a2ad466e3592e1ad193922ad/{lat},{lng}")
    public Call<DarkSkyResponse> getWeatherData(
            @Path("lat") float lat,
            @Path("lng") float lng,
            @Query(value = "exclude", encoded = true) String exclude);
}
