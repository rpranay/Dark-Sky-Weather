package com.dev.pranay.darkskyweather;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dev.pranay.darkskyweather.Model.TemperatureData;

import java.util.List;

class DailyForecastAdapter extends RecyclerView.Adapter<DailyForecastAdapter.MyViewHolder> {

    private Context c;
    private List<TemperatureData> temperatureDataList;

    public DailyForecastAdapter(Context c, List<TemperatureData> mList){
        this.c = c;
        temperatureDataList = mList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(c).inflate(R.layout.each_row, parent, false);
        return new MyViewHolder(v);
    }

    public void setTemperatureDataList(List<TemperatureData> temperatureDataList) {
        this.temperatureDataList = temperatureDataList;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        TemperatureData temperatureData = temperatureDataList.get(position);
        holder.minTemperature.setText(temperatureData.getMinTemperature()+" ℉");
        holder.maxTemperature.setText(temperatureData.getMaxTemperature()+" ℉");
        String filename = MainActivity.parseIconName(temperatureData.getIcon());
        int id = c.getResources().getIdentifier(filename, "drawable", c.getPackageName());
        holder.icon.setImageResource(id);
        holder.time.setText(microSecondsToDate(temperatureData.getTime()));

    }

    @Override
    public int getItemCount() {
        return temperatureDataList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView time, maxTemperature, minTemperature;
        ImageView icon;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.tvDateTime);
            maxTemperature = itemView.findViewById(R.id.tvMaxTemperature);
            minTemperature = itemView.findViewById(R.id.tvMinTemperature);
            icon = itemView.findViewById(R.id.ivListIcon);
        }
    }

    private String microSecondsToDate(long time){
        String s = "";
        Date date = new Date(time*1000);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        s = calendar.getTime().toString();
        s = s.substring(4,11);
        return s;
    }
}
