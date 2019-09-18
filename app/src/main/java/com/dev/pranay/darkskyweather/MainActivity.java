package com.dev.pranay.darkskyweather;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dev.pranay.darkskyweather.Model.CurrentWeather;
import com.dev.pranay.darkskyweather.Model.TemperatureData;
import com.dev.pranay.darkskyweather.ViewModel.MainViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final int REQUEST_PERMISSION = 22;
    private static final String PERMISSION_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;

    private ProgressBar progressBar;
    private RecyclerView mRecyclerView;
    private TextView summary, temperature, label;
    private ImageView icon;
    private CardView cardView;
    private MainViewModel mainViewModel;
    private DailyForecastAdapter mDailyForecastAdapter;
    private float latitude, longitude;
    private LocationManager locationManager;
    private Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        initRecyclerView();
        handlePermissions();

        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        mainViewModel.getCurrentWeather().observe(this, new Observer<CurrentWeather>() {
            @Override
            public void onChanged(CurrentWeather currentWeather) {
                if (currentWeather.getSummary().isEmpty()) {
                    hideLoading();
                    return;
                }
                summary.setText("Currently it is " + currentWeather.getSummary());
                temperature.setText(currentWeather.getTemperature() + " ℉");
                String filename = parseIconName(currentWeather.getIcon());
                int id = getResources().getIdentifier(filename, "drawable", getApplicationContext().getPackageName());
                icon.setImageResource(id);
                hideLoading();

            }
        });
        mainViewModel.getTemperatureDataList().observe(this, new Observer<List<TemperatureData>>() {
            @Override
            public void onChanged(List<TemperatureData> temperatureData) {
                mDailyForecastAdapter.setTemperatureDataList(temperatureData);
                mDailyForecastAdapter.notifyDataSetChanged();
            }
        });

    }

    private void handlePermissions() {
        ActivityCompat.requestPermissions(this, new String[]{
                PERMISSION_LOCATION}, REQUEST_PERMISSION);
    }

    private void initRecyclerView() {
        mDailyForecastAdapter = new DailyForecastAdapter(getApplication(), new ArrayList<TemperatureData>());
        mRecyclerView.setAdapter(mDailyForecastAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initViews() {
        cardView = findViewById(R.id.cv);
        progressBar = findViewById(R.id.progressbar);
        mRecyclerView = findViewById(R.id.rvDailyForecast);
        summary = findViewById(R.id.tvSummary);
        temperature = findViewById(R.id.tvTemperature);
        icon = findViewById(R.id.ivIcon);
        label = findViewById(R.id.label);
        showLoading();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.refresh) {
            Log.d(TAG, "onOptionsItemSelected: refresh clicked");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if(isPermissionGranted()){
                    setLatitudeAndLongitudeAndRequestData();
                }
                else if (shouldShowRequestPermissionRationale(PERMISSION_LOCATION)) {
                    handlePermissions();
                } else {
                    Toast.makeText(this, "Permission denied, go to settings to grant permission", Toast.LENGTH_LONG).show();
                }
            }else{
                setLatitudeAndLongitudeAndRequestData();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean isPermissionGranted(){
        return getApplicationContext().checkCallingOrSelfPermission(PERMISSION_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void showLoading() {
        cardView.setVisibility(View.INVISIBLE);
        label.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
    }

    private void hideLoading() {
        cardView.setVisibility(View.VISIBLE);
        label.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
    }

    public static String parseIconName(String s) {
        return s.replace('-', '_');
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setLatitudeAndLongitudeAndRequestData();
            } else {
                Toast.makeText(this, "Location permission needed, click refresh to try again", Toast.LENGTH_LONG).show();
            }
        }
    }

    @SuppressLint("MissingPermission")
    private void setLatitudeAndLongitudeAndRequestData() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if(location != null){
            latitude = (float)location.getLatitude();
            longitude = (float)location.getLongitude();
            mainViewModel.fetchRequest(latitude, longitude);
        }else{
            Toast.makeText(this, "GPS maybe turned off, try again", Toast.LENGTH_LONG).show();
        }

    }
}
