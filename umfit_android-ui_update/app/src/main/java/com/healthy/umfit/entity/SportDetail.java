package com.healthy.umfit.entity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class SportDetail implements Serializable {
    private String trackId;
    private String startTime;
    private String samplingTime;
    private String latitudeLongitude;
    private String altitude;
    private String accuracy;
    private String distance;
    private String pace;
    private String gait;
    private String speed;
    private String pause;
    private String heartRate;
    private String kilometerPace;
    private String milePace;
    private String lapPace;

    public SportDetail(String sportDetail) {
        try{
            JSONObject joSportDetail = new JSONObject(sportDetail);

            trackId = joSportDetail.getString("trackId");
            startTime = joSportDetail.getString("startTime");
            samplingTime = joSportDetail.getString("samplingTime");
            latitudeLongitude = joSportDetail.getString("latitudeLongitude");
            altitude = joSportDetail.getString("altitude");
            accuracy = joSportDetail.getString("accuracy");
            distance = joSportDetail.getString("distance");
            pace = joSportDetail.getString("pace");
            gait = joSportDetail.getString("gait");
            speed = joSportDetail.getString("speed");
            pause = joSportDetail.getString("pause");
            heartRate = joSportDetail.getString("heartRate");
            kilometerPace = joSportDetail.getString("kilometerPace");
            milePace = joSportDetail.getString("milePace");
            lapPace = joSportDetail.getString("lapPace");

        }catch (JSONException e){
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

    public String getSamplingTime() {
        return samplingTime;
    }

    public void setSamplingTime(String samplingTime) {
        this.samplingTime = samplingTime;
    }

    public String getLatitudeLongitude() {
        return latitudeLongitude;
    }

    public void setLatitudeLongitude(String latitudeLongitude) {
        this.latitudeLongitude = latitudeLongitude;
    }

    public String getAltitude() {
        return altitude;
    }

    public void setAltitude(String altitude) {
        this.altitude = altitude;
    }

    public String getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(String accuracy) {
        this.accuracy = accuracy;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getPace() {
        return pace;
    }

    public void setPace(String pace) {
        this.pace = pace;
    }

    public String getGait() {
        return gait;
    }

    public void setGait(String gait) {
        this.gait = gait;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getPause() {
        return pause;
    }

    public void setPause(String pause) {
        this.pause = pause;
    }

    public String getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(String heartRate) {
        this.heartRate = heartRate;
    }

    public String getKilometerPace() {
        return kilometerPace;
    }

    public void setKilometerPace(String kilometerPace) {
        this.kilometerPace = kilometerPace;
    }

    public String getMilePace() {
        return milePace;
    }

    public void setMilePace(String milePace) {
        this.milePace = milePace;
    }

    public String getLapPace() {
        return lapPace;
    }

    public void setLapPace(String lapPace) {
        this.lapPace = lapPace;
    }
}
