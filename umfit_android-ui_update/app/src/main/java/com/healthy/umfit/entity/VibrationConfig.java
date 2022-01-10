package com.healthy.umfit.entity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class VibrationConfig implements Serializable {
    private String mode;

    public VibrationConfig(String config) {
        try{
            JSONObject joConfig = new JSONObject(config);

            mode = joConfig.getString("mode");

        }catch (JSONException e){

        }
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }
}
