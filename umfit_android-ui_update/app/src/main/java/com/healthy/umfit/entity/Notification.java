package com.healthy.umfit.entity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Notification implements Serializable {
    private String messageId;
    private String state;
    private DeviceNotificationEffect deviceNotificationEffectObj;

    public Notification(String notification) {
        try{
            JSONObject joNotification = new JSONObject(notification);

            messageId = joNotification.getString("messageId");
            state = joNotification.getString("state");
            deviceNotificationEffectObj = new DeviceNotificationEffect(joNotification.getString("deviceNotificationEffect"));
        }catch (JSONException e){

        }
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public DeviceNotificationEffect getDeviceNotificationEffectObj() {
        return deviceNotificationEffectObj;
    }

    public void setDeviceNotificationEffectObj(DeviceNotificationEffect deviceNotificationEffectObj) {
        this.deviceNotificationEffectObj = deviceNotificationEffectObj;
    }
}
