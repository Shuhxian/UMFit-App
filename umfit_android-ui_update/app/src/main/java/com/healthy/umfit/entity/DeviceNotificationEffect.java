package com.healthy.umfit.entity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class DeviceNotificationEffect implements Serializable {
    private VibrationConfig vibrationConfigObj;
    private NotificationMessage notificationMessageObj;
    private NotificationIcon notificationIconObj;
    private NotificationTimer notificationTimerObj;

    public DeviceNotificationEffect(String deviceNotificationEffect) {
        try{
            JSONObject joDeviceNotificationEffect = new JSONObject(deviceNotificationEffect);

            vibrationConfigObj = new VibrationConfig(joDeviceNotificationEffect.getString("vibrationConfig"));

            notificationMessageObj = new NotificationMessage(joDeviceNotificationEffect.getString("message"));

            notificationIconObj = new NotificationIcon(joDeviceNotificationEffect.getString("icon"));

            notificationTimerObj = new NotificationTimer(joDeviceNotificationEffect.getString("timer"));

        }catch (JSONException e){

        }

    }

    public VibrationConfig getVibrationConfigObj() {
        return vibrationConfigObj;
    }

    public void setVibrationConfigObj(VibrationConfig vibrationConfigObj) {
        this.vibrationConfigObj = vibrationConfigObj;
    }

    public NotificationMessage getNotificationMessageObj() {
        return notificationMessageObj;
    }

    public void setNotificationMessageObj(NotificationMessage notificationMessageObj) {
        this.notificationMessageObj = notificationMessageObj;
    }

    public NotificationIcon getNotificationIconObj() {
        return notificationIconObj;
    }

    public void setNotificationIconObj(NotificationIcon notificationIconObj) {
        this.notificationIconObj = notificationIconObj;
    }

    public NotificationTimer getNotificationTimerObj() {
        return notificationTimerObj;
    }

    public void setNotificationTimerObj(NotificationTimer notificationTimerObj) {
        this.notificationTimerObj = notificationTimerObj;
    }
}
