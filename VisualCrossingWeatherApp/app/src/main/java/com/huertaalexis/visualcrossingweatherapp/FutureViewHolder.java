package com.huertaalexis.visualcrossingweatherapp;

import android.media.Image;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class FutureViewHolder extends RecyclerView.ViewHolder{
    TextView date;
    TextView temps;
    TextView desc;
    TextView cond;
    TextView uv;
    TextView morning;
    TextView afternoon;
    TextView evening;
    TextView night;
    ImageView image;

    FutureViewHolder(View view){
        super(view);
        date = view.findViewById(R.id.forecastDate);
        temps = view.findViewById(R.id.forecastTemps);
        desc = view.findViewById(R.id.forecastDesc);
        cond = view.findViewById(R.id.forecastCond);
        uv = view.findViewById(R.id.forecastUV);
        morning = view.findViewById(R.id.forecastMorning);
        afternoon = view.findViewById(R.id.forecastAfternoon);
        evening = view.findViewById(R.id.forecastEvening);
        night = view.findViewById(R.id.forecastNight);
        image = view.findViewById(R.id.imageCond);
    }
}
