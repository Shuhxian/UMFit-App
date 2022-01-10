package com.healthy.umfit.entity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class NotificationTimer implements Serializable {
    private String schedule;
    private String disabled;

    public NotificationTimer(String timer) {
        try{
            JSONObject joTimer = new JSONObject(timer);

            schedule = joTimer.getString("schedule");
            disabled = joTimer.getString("disabled");

        }catch (JSONException e){

        }
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public String getDisabled() {
        return disabled;
    }

    public void setDisabled(String disabled) {
        this.disabled = disabled;
    }
}
