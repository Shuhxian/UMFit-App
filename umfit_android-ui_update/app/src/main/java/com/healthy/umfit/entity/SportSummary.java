package com.healthy.umfit.entity;


import java.io.Serializable;
import java.util.ArrayList;

public class SportSummary implements Serializable {
    private ArrayList<Sport> sportList = new ArrayList<>();
    private int exerciseSessions = 0;
    private int averageTargetHeartRate = 0;
    private int averageMaximumTargetHeartRate = 0;
    private int exerciseDuration = 0;
    private String lastUpdated = ""; //based on the latest sport summary

    private double averageExerciseDuration = 0.0;
    private double averageHeartRate = 0.0;

    public SportSummary(ArrayList<Sport> sportList) {
        try {
            this.sportList = sportList;

            exerciseSessions = sportList.size();

            for (int i = 0; i < sportList.size(); i++) {
                Sport sportObj = sportList.get(i);

//                if(i==0){
//                 lastUpdated = sportObj.getTimestamp();
//                }

                averageTargetHeartRate = (averageTargetHeartRate + Integer.parseInt(sportObj.getAverageHeartRate())) / 2;
                exerciseDuration = (exerciseDuration + Integer.parseInt(sportObj.getSportTime())) / 2;

                if (averageTargetHeartRate > averageMaximumTargetHeartRate) {
                    averageMaximumTargetHeartRate = averageTargetHeartRate;
                }

                int sportTime = Integer.parseInt(sportObj.getSportTime());
                averageExerciseDuration += sportTime;

                int heartRate = Integer.parseInt(sportObj.getAverageHeartRate());
                averageHeartRate += heartRate;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Sport> getSportList() {
        return sportList;
    }

    public void setSportList(ArrayList<Sport> sportList) {
        this.sportList = sportList;
    }

    public int getExerciseSessions() {
        return exerciseSessions;
    }

    public void setExerciseSessions(int exerciseSessions) {
        this.exerciseSessions = exerciseSessions;
    }

    public int getAverageTargetHeartRate() {
        return averageTargetHeartRate;
    }

    public int getAverageTargetHeartRate(int pos) {
        return Integer.parseInt(getSportList().get(pos).getAverageHeartRate());
    }

    public void setAverageTargetHeartRate(int averageTargetHeartRate) {
        this.averageTargetHeartRate = averageTargetHeartRate;
    }

    public int getAverageMaximumTargetHeartRate() {
        return averageMaximumTargetHeartRate;
    }

    public int getAverageMaximumTargetHeartRate(int pos) {
        //todo
        return Integer.parseInt(getSportList().get(pos).getAverageHeartRate());
    }

    public void setAverageMaximumTargetHeartRate(int averageMaximumTargetHeartRate) {
        this.averageMaximumTargetHeartRate = averageMaximumTargetHeartRate;
    }

    public int getExerciseDuration() {
        //in minute
        return exerciseDuration / 60;
    }

    public int getExerciseDuration(int pos) {
        //in minute
        double dSportTime = Double.parseDouble(getSportList().get(pos).getSportTime());
        int iSportTime = (int) Math.ceil(dSportTime / 60);
        return iSportTime;
    }

    public void setExerciseDuration(int exerciseDuration) {
        this.exerciseDuration = exerciseDuration;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public String getLastUpdated(int pos) {
        return getSportList().get(pos).getTimestamp();
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public int getAverageExerciseDuration() {
        double dAverageDuration = averageExerciseDuration / sportList.size();
        int iAvgSportTime = (int) Math.ceil(dAverageDuration);
        return iAvgSportTime;
    }

    public int getAverageHeartRate() {
        double dAverageHeartRate = averageHeartRate / sportList.size();
        int iAvgHeartRate = (int) Math.ceil(dAverageHeartRate);
        return iAvgHeartRate;
    }
}
