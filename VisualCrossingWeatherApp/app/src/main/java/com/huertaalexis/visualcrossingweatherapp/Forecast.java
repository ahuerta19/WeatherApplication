package com.huertaalexis.visualcrossingweatherapp;

public class Forecast {
    private final String forDate;
    private final String forTemps;
    private final int forCondImg;
    private final String forDesc;
    private final String forCond;
    private final String forUV;
    private final String forMor;
    private final String forAft;
    private final String forEve;
    private final String forCondNight;

    public Forecast(String forDate, String forTemps, int forCondImg, String forDesc, String forCond, String forUV, String forMor, String forAft, String forEve, String forCondNight) {
        this.forDate = forDate;
        this.forTemps = forTemps;
        this.forCondImg = forCondImg;
        this.forDesc = forDesc;
        this.forCond = forCond;
        this.forUV = forUV;
        this.forMor = forMor;
        this.forAft = forAft;
        this.forEve = forEve;
        this.forCondNight = forCondNight;
    }
    String getForDate(){
        return forDate;
    }
    String getForTemps(){
        return forTemps;
    }
    String getForDesc(){
        return forDesc;
    }
    String getForCond(){
        return forCond;
    }
    String getForUV(){
        return forUV;
    }
    String getForMor(){
        return forMor;
    }
    String getForAft(){
        return forAft;
    }
    String getForEve(){
        return forEve;
    }
    String getForCondNight(){
        return forCondNight;
    }
    int getForCondImg(){
        return forCondImg;
    }
}
