package com.healthy.umfit.entity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Sleep implements Serializable {
    private String date;
    private String lastSyncTime;
    private String deepSleepTime;
    private String shallowSleepTime;
    private String wakeTime;
    private String start;
    private String stop;
    private String sleepScore;
    private String mode;

    public Sleep(String sleep) {
        try{
            JSONObject joSleep = new JSONObject(sleep);

            date = joSleep.getString("date");
            lastSyncTime = joSleep.getString("lastSyncTime");
            deepSleepTime = joSleep.getString("deepSleepTime");
            shallowSleepTime = joSleep.getString("shallowSleepTime");
            wakeTime = joSleep.getString("wakeTime");
            start = joSleep.getString("start");
            stop = joSleep.getString("stop");
            sleepScore = joSleep.getString("sleepScore");
            mode = joSleep.getString("mode");

        }catch (JSONException e){

        }

    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLastSyncTime() {
        return lastSyncTime;
    }

    public void setLastSyncTime(String lastSyncTime) {
        this.lastSyncTime = lastSyncTime;
    }

    public String getDeepSleepTime() {
        return deepSleepTime;
    }

    public void setDeepSleepTime(String deepSleepTime) {
        this.deepSleepTime = deepSleepTime;
    }

    public String getShallowSleepTime() {
        return shallowSleepTime;
    }

    public void setShallowSleepTime(String shallowSleepTime) {
        this.shallowSleepTime = shallowSleepTime;
    }

    public String getWakeTime() {
        return wakeTime;
    }

    public void setWakeTime(String wakeTime) {
        this.wakeTime = wakeTime;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getStop() {
        return stop;
    }

    public void setStop(String stop) {
        this.stop = stop;
    }

    public String getSleepScore() {
        return sleepScore;
    }

    public void setSleepScore(String sleepScore) {
        this.sleepScore = sleepScore;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }
}
