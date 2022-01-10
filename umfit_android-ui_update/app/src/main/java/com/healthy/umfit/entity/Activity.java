package com.healthy.umfit.entity;

import android.text.format.DateFormat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class Activity implements Serializable {
    private String date;
    private String lastSyncTime;
    private String steps;
    private String distance;
    private String walkTime;
    private String runDistance;
    private String runTime;
    private String calories;
    private String runCalories;
    private String hour;

    private ArrayList<ActivityStage> activityStageList = new ArrayList<>();

    public Activity(JSONObject joItems) {
        try {
            if (joItems.has("date")) {
                date = joItems.getString("date");
            }

            if (joItems.has("lastSyncTime")) {
                lastSyncTime = joItems.getString("lastSyncTime");
            }

            if (joItems.has("steps")) {
                steps = joItems.getString("steps");
            }

            if (joItems.has("distance")) {
                distance = joItems.getString("distance");
            }

            if (joItems.has("walkTime")) {
                walkTime = joItems.getString("walkTime");
            }

            if (joItems.has("runDistance")) {
                runDistance = joItems.getString("runDistance");
            }

            if (joItems.has("runTime")) {
                runTime = joItems.getString("runTime");
            }

            if (joItems.has("calories")) {
                calories = joItems.getString("calories");
            }

            if (joItems.has("runCalories")) {
                runCalories = joItems.getString("runCalories");
            }

            if (joItems.has("stage")) {
                String stage = joItems.getString("stage");
                JSONArray jaStage = new JSONArray(stage);
                for (int j = 0; j < jaStage.length(); j++) {
                    JSONObject joStage = jaStage.getJSONObject(j);
                    ActivityStage activityStageObj = new ActivityStage(joStage);
                    activityStageList.add(activityStageObj);
                }
            }

            if (joItems.has("hour")) {
                hour = joItems.getString("hour");
            }

        } catch (JSONException e) {

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

    public String getSteps() {
        return steps;
    }

    public void setSteps(String steps) {
        this.steps = steps;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getWalkTime() {
        return walkTime;
    }

    public void setWalkTime(String walkTime) {
        this.walkTime = walkTime;
    }

    public String getRunDistance() {
        return runDistance;
    }

    public void setRunDistance(String runDistance) {
        this.runDistance = runDistance;
    }

    public String getRunTime() {
        return runTime;
    }

    public void setRunTime(String runTime) {
        this.runTime = runTime;
    }

    public String getCalories() {
        return calories;
    }

    public void setCalories(String calories) {
        this.calories = calories;
    }

    public String getRunCalories() {
        return runCalories;
    }

    public void setRunCalories(String runCalories) {
        this.runCalories = runCalories;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public ArrayList<ActivityStage> getActivityStageList() {
        return activityStageList;
    }

    public void setActivityStageList(ArrayList<ActivityStage> activityStageList) {
        this.activityStageList = activityStageList;
    }

    public String getFormattedDateFromTimestamp(){
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(Long.parseLong(lastSyncTime) * 1000);
        String date = DateFormat.format("dd-MM-yyyy hh:mm a", cal).toString();
        return date;
    }
}
