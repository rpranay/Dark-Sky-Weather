package com.dev.pranay.darkskyweather.Model;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;

import com.dev.pranay.darkskyweather.Model.Local.TemperatureDataDao;
import com.dev.pranay.darkskyweather.Model.Local.WeatherDatabase;
import com.dev.pranay.darkskyweather.Model.Remote.DarkSkyAPI;
import com.dev.pranay.darkskyweather.Model.Remote.DarkSkyResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Repository {

    private static final String TAG = "Repository";

    private static final String BASE_URL = "https://api.darksky.net/";
    private static final String EXCLUDE_PARAMETERS = "minutely,hourly,alerts,flags";
    private static final String DB_NAME = "FORECAST_DB";

    private TemperatureDataDao mTemperatureDataDao;
    private WeatherDatabase mWeatherDatabase;
    private DarkSkyAPI mDarkSkyAPI;
    private static Repository instance;
    private MutableLiveData<CurrentWeather> mCurrentWeather;

    private Repository(Context context){
        mDarkSkyAPI = buildRetrofit();
        mWeatherDatabase = buildDatabase(context);
        mTemperatureDataDao = mWeatherDatabase.temperatureDataDao();
        mCurrentWeather = new MutableLiveData<>();
        mCurrentWeather.setValue(new CurrentWeather("", "", 0,0));
    }

    public static Repository getInstance(Context c){
        if(instance == null){
            instance = new Repository(c);
        }
        return instance;
    }

    public void fetchRequest(float lat, float lng){
        Call<DarkSkyResponse> responseCall = mDarkSkyAPI.getWeatherData(lat, lng, EXCLUDE_PARAMETERS);
        responseCall.enqueue(new Callback<DarkSkyResponse>() {
            @Override
            public void onResponse(Call<DarkSkyResponse> call, Response<DarkSkyResponse> response) {
                if(!response.isSuccessful()){
                    Log.d(TAG, call.request().url().toString());
                    Log.d(TAG, "onResponse: " + response.raw().message());
                    return;}
                Log.d(TAG, "onResponse: successful");
                DarkSkyResponse darkSkyResponse = response.body();
                Log.d(TAG, "onResponse: " + darkSkyResponse.getCurrentWeather().getSummary());
                mCurrentWeather.setValue(darkSkyResponse.getCurrentWeather());
                List<TemperatureData> mList = darkSkyResponse.getDailyForecast().getTemperatureDataList();
                updateLocalDB(mList);
            }

            @Override
            public void onFailure(Call<DarkSkyResponse> call, Throwable t) {

            }
        });
    }

    private void updateLocalDB(final List<TemperatureData> mList) {
        new AsyncTask<List<TemperatureData>, Void, Void>(){
            @Override
            protected Void doInBackground(List<TemperatureData>... lists) {
                mTemperatureDataDao.updateDB(mList);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Log.d(TAG, "onPostExecute: updated db");
            }
        }.execute(mList);
    }


    public LiveData<CurrentWeather> getCurrentWeather() {
        return mCurrentWeather;
    }

    public LiveData<List<TemperatureData>> getDailyForecastList() { return mTemperatureDataDao.getWeeklyForecast(); }

    private WeatherDatabase buildDatabase(Context context) {
        return Room.databaseBuilder(context,WeatherDatabase.class, DB_NAME)
                .build();
    }

    private DarkSkyAPI buildRetrofit(){
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(DarkSkyAPI.class);
    }
}
