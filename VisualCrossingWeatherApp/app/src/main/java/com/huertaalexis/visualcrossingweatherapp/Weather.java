package com.huertaalexis.visualcrossingweatherapp;

import android.widget.ImageView;
import android.widget.TextView;

public class Weather {
    private final String curDay;
    private final String curTime;
    private final int curCondImg;
    private final String curTemp;
    private final String curCondDes;

    Weather(String cD, String cT, int cCI, String cTmp, String curCD){
        curDay = cD;
        curTime = cT;
        curCondImg = cCI;
        curTemp = cTmp;
        curCondDes = curCD;
    }



    String getCurDay(){ return curDay;}

    String getCurTime(){ return curTime;}

    int getCurCondImg(){ return curCondImg;}

    String getCurTemp(){ return curTemp;}

    String getCurCondDes(){ return curCondDes;}


}
