package com.healthy.umfit.entity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Body implements Serializable {
    private String weight;
    private String height;
    private String bmi;
    private String fatRate;
    private String bodyWaterRate;
    private String boneMass;
    private String metabolism;
    private String muscleRate;
    private String visceralFat;
    private String impedance;
    private String weightType;
    private String timestamp;

    public Body(String body) {
        try{
            JSONObject joBody = new JSONObject(body);

            weight = joBody.getString("weight");
            height = joBody.getString("height");
            bmi = joBody.getString("bmi");
            fatRate = joBody.getString("fatRate");
            bodyWaterRate = joBody.getString("bodyWaterRate");
            boneMass = joBody.getString("boneMass");
            metabolism = joBody.getString("metabolism");
            muscleRate = joBody.getString("muscleRate");
            visceralFat = joBody.getString("visceralFat");
            impedance = joBody.getString("impedance");
            weightType = joBody.getString("weightType");
            timestamp = joBody.getString("timestamp");

        }catch (JSONException e){

        }
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getBmi() {
        return bmi;
    }

    public void setBmi(String bmi) {
        this.bmi = bmi;
    }

    public String getFatRate() {
        return fatRate;
    }

    public void setFatRate(String fatRate) {
        this.fatRate = fatRate;
    }

    public String getBodyWaterRate() {
        return bodyWaterRate;
    }

    public void setBodyWaterRate(String bodyWaterRate) {
        this.bodyWaterRate = bodyWaterRate;
    }

    public String getBoneMass() {
        return boneMass;
    }

    public void setBoneMass(String boneMass) {
        this.boneMass = boneMass;
    }

    public String getMetabolism() {
        return metabolism;
    }

    public void setMetabolism(String metabolism) {
        this.metabolism = metabolism;
    }

    public String getMuscleRate() {
        return muscleRate;
    }

    public void setMuscleRate(String muscleRate) {
        this.muscleRate = muscleRate;
    }

    public String getVisceralFat() {
        return visceralFat;
    }

    public void setVisceralFat(String visceralFat) {
        this.visceralFat = visceralFat;
    }

    public String getImpedance() {
        return impedance;
    }

    public void setImpedance(String impedance) {
        this.impedance = impedance;
    }

    public String getWeightType() {
        return weightType;
    }

    public void setWeightType(String weightType) {
        this.weightType = weightType;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
