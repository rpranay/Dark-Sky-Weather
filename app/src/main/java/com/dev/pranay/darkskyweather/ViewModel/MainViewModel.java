package com.dev.pranay.darkskyweather.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.dev.pranay.darkskyweather.Model.CurrentWeather;
import com.dev.pranay.darkskyweather.Model.Repository;
import com.dev.pranay.darkskyweather.Model.TemperatureData;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private Repository mRepo;

    public MainViewModel(@NonNull Application application) {
        super(application);
        mRepo = Repository.getInstance(application);

    }

    public void fetchRequest(float lat, float lng){
        mRepo.fetchRequest(lat, lng);
    }

    public LiveData<CurrentWeather> getCurrentWeather() {
        return mRepo.getCurrentWeather();
    }

    public LiveData<List<TemperatureData>> getTemperatureDataList() {
        return mRepo.getDailyForecastList();
    }
}
