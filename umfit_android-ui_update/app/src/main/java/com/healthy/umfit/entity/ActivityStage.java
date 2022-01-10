package com.healthy.umfit.entity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class ActivityStage implements Serializable {
    private String start;
    private String stop;
    private String mode;
    private String step;
    private String dis;
    private String cal;

    public ActivityStage(JSONObject joStage) {
        try {
            if (joStage.has("start")) {
                start = joStage.getString("start");
            }

            if (joStage.has("stop")) {
                stop = joStage.getString("stop");
            }

            if (joStage.has("mode")) {
                mode = joStage.getString("mode");
            }

            if (joStage.has("step")) {
                step = joStage.getString("step");
            }

            if (joStage.has("dis")) {
                dis = joStage.getString("dis");
            }

            if (joStage.has("cal")) {
                cal = joStage.getString("cal");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
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

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    public String getDis() {
        return dis;
    }

    public void setDis(String dis) {
        this.dis = dis;
    }

    public String getCal() {
        return cal;
    }

    public void setCal(String cal) {
        this.cal = cal;
    }

    public String getTimeDifference(){
        int difference = (Integer.parseInt(stop) - Integer.parseInt(start));
        return String.valueOf(difference);
    }
}
