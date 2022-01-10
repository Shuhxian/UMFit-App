package com.healthy.umfit.entity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class NotificationIcon implements Serializable {
    private String uri;

    public NotificationIcon(String icon) {
        try{
            JSONObject joIcon = new JSONObject(icon);

            uri = joIcon.getString("uri");

        }catch (JSONException e){

        }
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
