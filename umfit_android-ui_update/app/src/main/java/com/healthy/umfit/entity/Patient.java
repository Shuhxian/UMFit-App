package com.healthy.umfit.entity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Patient implements Serializable {
    private String id;
    private String lastUpdatedHeartRate;
    private String lastUpdatedHeartRateDateTime;
    private String todayStepsCount;
    private String lastUpdatedStepsDateTime;
    private String todayCaloriesBurned;
    private String lastUpdatedCaloriesDateTime;
    private String todayDistance;
    private String lastUpdatedDistanceDateTime;
    private String uploadedDateTime;

    public Patient(JSONObject patientObj) {
        try {
            id = patientObj.getString("_id");
            lastUpdatedHeartRate = patientObj.getString("lastUpdatedHeartRate");
            lastUpdatedHeartRateDateTime = patientObj.getString("lastUpdatedHeartRateDateTime");
            todayStepsCount = patientObj.getString("todayStepsCount");
            lastUpdatedStepsDateTime = patientObj.getString("lastUpdatedStepsDateTime");
            todayCaloriesBurned = patientObj.getString("todayCaloriesBurned");
            lastUpdatedCaloriesDateTime = patientObj.getString("lastUpdatedCaloriesDateTime");
            todayDistance = patientObj.getString("todayDistance");
            lastUpdatedDistanceDateTime = patientObj.getString("lastUpdatedDistanceDateTime");
            uploadedDateTime = patientObj.getString("uploadedDateTime");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLastUpdatedHeartRate() {
        return lastUpdatedHeartRate;
    }

    public void setLastUpdatedHeartRate(String lastUpdatedHeartRate) {
        this.lastUpdatedHeartRate = lastUpdatedHeartRate;
    }

    public String getLastUpdatedHeartRateDateTime() {
        return lastUpdatedHeartRateDateTime;
    }

    public void setLastUpdatedHeartRateDateTime(String lastUpdatedHeartRateDateTime) {
        this.lastUpdatedHeartRateDateTime = lastUpdatedHeartRateDateTime;
    }

    public String getTodayStepsCount() {
        return todayStepsCount;
    }

    public void setTodayStepsCount(String todayStepsCount) {
        this.todayStepsCount = todayStepsCount;
    }

    public String getLastUpdatedStepsDateTime() {
        return lastUpdatedStepsDateTime;
    }

    public void setLastUpdatedStepsDateTime(String lastUpdatedStepsDateTime) {
        this.lastUpdatedStepsDateTime = lastUpdatedStepsDateTime;
    }

    public String getTodayCaloriesBurned() {
        return todayCaloriesBurned;
    }

    public void setTodayCaloriesBurned(String todayCaloriesBurned) {
        this.todayCaloriesBurned = todayCaloriesBurned;
    }

    public String getLastUpdatedCaloriesDateTime() {
        return lastUpdatedCaloriesDateTime;
    }

    public void setLastUpdatedCaloriesDateTime(String lastUpdatedCaloriesDateTime) {
        this.lastUpdatedCaloriesDateTime = lastUpdatedCaloriesDateTime;
    }

    public String getTodayDistance() {
        return todayDistance;
    }

    public void setTodayDistance(String todayDistance) {
        this.todayDistance = todayDistance;
    }

    public String getLastUpdatedDistanceDateTime() {
        return lastUpdatedDistanceDateTime;
    }

    public void setLastUpdatedDistanceDateTime(String lastUpdatedDistanceDateTime) {
        this.lastUpdatedDistanceDateTime = lastUpdatedDistanceDateTime;
    }

    public String getUploadedDateTime() {
        return uploadedDateTime;
    }

    public void setUploadedDateTime(String uploadedDateTime) {
        this.uploadedDateTime = uploadedDateTime;
    }
}
