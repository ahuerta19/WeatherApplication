package com.huertaalexis.visualcrossingweatherapp;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class WeatherViewHolder extends RecyclerView.ViewHolder {
    TextView day;
    TextView time;
    ImageView condImg;
    TextView temp;
    TextView condDes;

    WeatherViewHolder(View view) {
        super(view);
        day = view.findViewById(R.id.currentDay);
        time = view.findViewById(R.id.currentTime);
        condImg = view.findViewById(R.id.currentConditions);
        temp = view.findViewById(R.id.currentTemps);
        condDes = view.findViewById(R.id.currentCond);
    }

}
