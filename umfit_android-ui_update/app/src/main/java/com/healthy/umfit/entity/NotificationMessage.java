package com.healthy.umfit.entity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class NotificationMessage implements Serializable {
    private String title;
    private String body;

    public NotificationMessage(String message) {
        try{
            JSONObject joMessage = new JSONObject(message);

            title = joMessage.getString("title");
            body = joMessage.getString("body");

        }catch (JSONException e){

        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
