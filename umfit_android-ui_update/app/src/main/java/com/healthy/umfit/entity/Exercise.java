package com.healthy.umfit.entity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Exercise implements Serializable {
    private String id;
    private String startTime;
    private String endTime;
    private String sportTime;
    private String distance;
    private String calories;
    private String location;
    private String averagePace;
    private String averageStepFrequency;
    private String averageHeartRate;
    private String device;
    private String type;
    private String uploadedDateTime;

    public Exercise(JSONObject joSport) {
        try {
            id = joSport.getString("_id");
            startTime = joSport.getString("startTime");
            endTime = joSport.getString("endTime");
            sportTime = joSport.getString("sportTime");
            distance = joSport.getString("distance");
            calories = joSport.getString("calories");
            location = joSport.getString("location");
            averagePace = joSport.getString("averagePace");
            averageStepFrequency = joSport.getString("averageStepFrequency");
            averageHeartRate = joSport.getString("averageHeartRate");
            device = joSport.getString("device");
            type = joSport.getString("type");
            uploadedDateTime = joSport.getString("uploadedDateTime");

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

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getSportTime() {
        return sportTime;
    }

    public void setSportTime(String sportTime) {
        this.sportTime = sportTime;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getCalories() {
        return calories;
    }

    public void setCalories(String calories) {
        this.calories = calories;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAveragePace() {
        return averagePace;
    }

    public void setAveragePace(String averagePace) {
        this.averagePace = averagePace;
    }

    public String getAverageStepFrequency() {
        return averageStepFrequency;
    }

    public void setAverageStepFrequency(String averageStepFrequency) {
        this.averageStepFrequency = averageStepFrequency;
    }

    public String getAverageHeartRate() {
        return averageHeartRate;
    }

    public void setAverageHeartRate(String averageHeartRate) {
        this.averageHeartRate = averageHeartRate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getUploadedDateTime() {
        return uploadedDateTime;
    }

    public void setUploadedDateTime(String uploadedDateTime) {
        this.uploadedDateTime = uploadedDateTime;
    }
}
