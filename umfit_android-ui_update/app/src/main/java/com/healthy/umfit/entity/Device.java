package com.healthy.umfit.entity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Device implements Serializable {
    private String deviceType;
    private String deviceName;
    private String deviceId;
    private String macAddress;
    private String lastDataSyncTime;

    public Device(String device) {
        try{
            JSONObject joDevice = new JSONObject(device);

            deviceType = joDevice.getString("deviceType");
            deviceName = joDevice.getString("deviceName");
            deviceId = joDevice.getString("deviceId");
            macAddress = joDevice.getString("macAddress");
            lastDataSyncTime = joDevice.getString("lastDataSyncTime");

        }catch (JSONException e){

        }
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getLastDataSyncTime() {
        return lastDataSyncTime;
    }

    public void setLastDataSyncTime(String lastDataSyncTime) {
        this.lastDataSyncTime = lastDataSyncTime;
    }
}
