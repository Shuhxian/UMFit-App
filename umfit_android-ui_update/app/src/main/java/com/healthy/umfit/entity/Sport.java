package com.healthy.umfit.entity;

import android.webkit.JsPromptResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Sport implements Serializable {
    private String trackId;
    private String startTime;
    private String endTime;
    private String sportTime;
    private String distance;
    private String calories;
    private String location;
    private String averagePace;
    private String averageStepFrequency;
    private String averageStrideLength;
    private String timestamp;
    private String averageHeartRate;
    private String altitudeAscend;
    private String altitudeDescend;
    private String secondHalfStartTime;
    private String strokeCount;
    private String foreHandCount;
    private String backHandCount;
    private String serveCount;
    private String device;
    private String deviceName;
    private String type;

    public Sport(JSONObject joSport) {
        try {
            trackId = joSport.getString("track_id");
            startTime = joSport.getString("start_time");
            endTime = joSport.getString("end_time");
            sportTime = joSport.getString("sport_time");
            distance = joSport.getString("distance");
            calories = joSport.getString("calories");
            location = joSport.getString("location");
            averagePace = joSport.getString("average_pace");
            averageStepFrequency = joSport.getString("average_step_frequency");
            averageStrideLength = joSport.getString("average_stride_length");
            timestamp = joSport.getString("timestamp");
            averageHeartRate = joSport.getString("average_heart_rate");

            if (joSport.has("secondHalfStartTime")) {
                secondHalfStartTime = joSport.getString("secondHalfStartTime");
            }


            if (joSport.has("device")) {
                device = joSport.getString("device");
            }

            if (joSport.has("deviceName")) {
                deviceName = joSport.getString("device_name");
            }

            if (joSport.has("type")) {
                type = joSport.getString("type");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getTrackId() {
        return trackId;
    }

    public void setTrackId(String trackId) {
        this.trackId = trackId;
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
        double dSportTime = Double.parseDouble(sportTime);
        int iSportTime = (int) Math.ceil(dSportTime / 60);
        String sSportTime = String.valueOf(iSportTime);
        return sSportTime;
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

    public String getAverageStrideLength() {
        return averageStrideLength;
    }

    public void setAverageStrideLength(String averageStrideLength) {
        this.averageStrideLength = averageStrideLength;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getAverageHeartRate() {
        return averageHeartRate;
    }

    public void setAverageHeartRate(String averageHeartRate) {
        this.averageHeartRate = averageHeartRate;
    }

    public String getAltitudeAscend() {
        return altitudeAscend;
    }

    public void setAltitudeAscend(String altitudeAscend) {
        this.altitudeAscend = altitudeAscend;
    }

    public String getAltitudeDescend() {
        return altitudeDescend;
    }

    public void setAltitudeDescend(String altitudeDescend) {
        this.altitudeDescend = altitudeDescend;
    }

    public String getSecondHalfStartTime() {
        return secondHalfStartTime;
    }

    public void setSecondHalfStartTime(String secondHalfStartTime) {
        this.secondHalfStartTime = secondHalfStartTime;
    }

    public String getStrokeCount() {
        return strokeCount;
    }

    public void setStrokeCount(String strokeCount) {
        this.strokeCount = strokeCount;
    }

    public String getForeHandCount() {
        return foreHandCount;
    }

    public void setForeHandCount(String foreHandCount) {
        this.foreHandCount = foreHandCount;
    }

    public String getBackHandCount() {
        return backHandCount;
    }

    public void setBackHandCount(String backHandCount) {
        this.backHandCount = backHandCount;
    }

    public String getServeCount() {
        return serveCount;
    }

    public void setServeCount(String serveCount) {
        this.serveCount = serveCount;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
