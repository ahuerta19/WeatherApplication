package com.huertaalexis.visualcrossingweatherapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherViewHolder>{

    private final List<Weather> wList;
    private final MainActivity mainAct;

    WeatherAdapter(List<Weather> empList, MainActivity ma) {
        this.wList = empList;
        mainAct = ma;
    }


    @NonNull
    @Override
    public WeatherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.weather_entry, parent, false);

        return new WeatherViewHolder(itemView);
    }

    private String formatHour(long epoch){
        Date dateTime = new Date(epoch * 1000); // Java time values need milliseconds
        SimpleDateFormat timeOnly = new SimpleDateFormat("h:mm a", Locale.getDefault());
        String fullDateStr = timeOnly.format(dateTime); // Thu Sep 29 12:00 AM, 2022
        return fullDateStr;
    }
    private String formatDay(long epoch){
        Date dateTime = new Date(epoch * 1000); // Java time values need milliseconds
        SimpleDateFormat dayDate = new SimpleDateFormat("EEEE", Locale.getDefault());
        String fullDateStr = dayDate.format(dateTime); // Thu Sep 29 12:00 AM, 2022
        return fullDateStr;
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherViewHolder holder, int position) {
        Weather weather = wList.get(position);

        String setTime = weather.getCurTime();
        long convertTime = Long.parseLong(setTime);

        String setDay = weather.getCurDay();

        if (setDay.equals("Today")) {
            holder.day.setText("Today");
        }else{
            long convertDay = Long.parseLong(setDay);
            holder.day.setText(formatDay(convertDay));
        }

        holder.time.setText(formatHour(convertTime));
        Picasso.get().load(weather.getCurCondImg()).into(holder.condImg);
        holder.temp.setText((String.format("%.0f° " + (MainActivity.getTemp() ? "F" : "C"),Double.parseDouble(weather.getCurTemp()))));
        holder.condDes.setText(weather.getCurCondDes());
    }

    @Override
    public int getItemCount() {
        return wList.size();
    }
}
