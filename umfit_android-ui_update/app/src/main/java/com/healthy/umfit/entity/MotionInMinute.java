package com.healthy.umfit.entity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class MotionInMinute implements Serializable {
    private String date;
    private String minute;
    private String lastSyncTime;
    private String activeness;
    private String mode;
    private String steps;

    public MotionInMinute(String motionInMinute) {
        try{
            JSONObject joMotionInMinute = new JSONObject(motionInMinute);

            date = joMotionInMinute.getString("date");
            minute = joMotionInMinute.getString("minute");
            lastSyncTime = joMotionInMinute.getString("lastSyncTime");
            activeness = joMotionInMinute.getString("activeness");
            mode = joMotionInMinute.getString("mode");
            steps = joMotionInMinute.getString("steps");

        }catch (JSONException e){

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

    public String getActiveness() {
        return activeness;
    }

    public void setActiveness(String activeness) {
        this.activeness = activeness;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getSteps() {
        return steps;
    }

    public void setSteps(String steps) {
        this.steps = steps;
    }
}
