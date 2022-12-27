package com.huertaalexis.visualcrossingweatherapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Future;

public class FutureAdapter extends RecyclerView.Adapter<FutureViewHolder>{
    private final List<Forecast> fList;
    private final FutureForecast mainAct;

    FutureAdapter(List<Forecast> empList, FutureForecast ma) {
        this.fList = empList;
        mainAct = ma;
    }

    @NonNull
    @Override
    public FutureViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.forecast_entry, parent, false);

        return new FutureViewHolder(itemView);
    }

    private String formatHour(long epoch){
        Date dateTime = new Date(epoch * 1000); // Java time values need milliseconds
        SimpleDateFormat dayDate = new SimpleDateFormat("EEEE MM/dd", Locale.getDefault());
        String fullDateStr = dayDate.format(dateTime); // Thu Sep 29 12:00 AM, 2022
        return fullDateStr;
    }

    @Override
    public void onBindViewHolder(@NonNull FutureViewHolder holder, int position) {
        Forecast forecast = fList.get(position);

        String setD = forecast.getForDate();
        long convertTime = Long.parseLong(setD);

        holder.date.setText(formatHour(convertTime));
        holder.temps.setText(forecast.getForTemps());
        holder.desc.setText(forecast.getForDesc());
        holder.cond.setText("(" + (String.format("%.0f",Double.parseDouble(forecast.getForCond()))) + "% precip.)");
        holder.uv.setText("UV Index: " + (String.format("%.0f ",Double.parseDouble(forecast.getForUV()))));
        holder.morning.setText((String.format("%.0f° " + (MainActivity.getTemp() ? "F" : "C"),Double.parseDouble(forecast.getForMor()))));
        holder.afternoon.setText((String.format("%.0f° " + (MainActivity.getTemp() ? "F" : "C"),Double.parseDouble(forecast.getForAft()))));
        holder.evening.setText((String.format("%.0f° " + (MainActivity.getTemp() ? "F" : "C"),Double.parseDouble(forecast.getForEve()))));
        holder.night.setText((String.format("%.0f° " + (MainActivity.getTemp() ? "F" : "C"),Double.parseDouble(forecast.getForCondNight()))));
        Picasso.get().load(forecast.getForCondImg()).into(holder.image);
        //ImageView image;

    }

    @Override
    public int getItemCount() {
        return fList.size();
    }
}
