package com.healthy.umfit.entity;

import android.text.format.DateFormat;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Locale;

public class HeartRate implements Serializable {

    private String date;
    private String minute;
    private String lastSyncTime;
    private String heartRateData;
    private String timestamp;
    private String measureType;

    public HeartRate(JSONObject joHeartRate) {
        try{

            if(joHeartRate.has("date")) {
                date = joHeartRate.getString("date");
            }

            if(joHeartRate.has("minute")) {
                minute = joHeartRate.getString("minute");
            }

            if(joHeartRate.has("lastSyncTime")) {
                lastSyncTime = joHeartRate.getString("lastSyncTime");
            }

            if(joHeartRate.has("heartRateData")) {
                heartRateData = joHeartRate.getString("heartRateData");
            }

            if(joHeartRate.has("timestamp")) {
                timestamp = joHeartRate.getString("timestamp");
            }

            if(joHeartRate.has("measureType")) {
                measureType = joHeartRate.getString("measureType");
            }

        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMinute() {
        return minute;
    }

    public void setMinute(String minute) {
        this.minute = minute;
    }

    public String getLastSyncTime() {
        return lastSyncTime;
    }

    public void setLastSyncTime(String lastSyncTime) {
        this.lastSyncTime = lastSyncTime;
    }

    public String getHeartRateData() {
        return heartRateData;
    }

    public int getHeartRateDataInInteger(){
        return Integer.parseInt(heartRateData);
    }

    public void setHeartRateData(String heartRateData) {
        this.heartRateData = heartRateData;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getFormattedDateFromTimestamp(){
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(Long.parseLong(timestamp) * 1000);
        String date = DateFormat.format("MM/dd hh:mm", cal).toString();
        return date;
    }

    public String getMeasureType() {
        return measureType;
    }

    public void setMeasureType(String measureType) {
        this.measureType = measureType;
    }
}
