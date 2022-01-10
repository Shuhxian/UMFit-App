package com.healthy.umfit.entity;

import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Profile implements Serializable {
    private String userId;
    private String gender;
    private String birthday;
    private String height;
    private String weight;
    private String nickName;

    public Profile(String userId, String gender, String birthday, String height, String weight, String nickName) {
        this.userId = userId;
        this.gender = gender;
        this.birthday = birthday;
        this.height = height;
        this.weight = weight;
        this.nickName = nickName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        try {
            if (birthday != null) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
                Date sourceDate = null;
                try {
                    sourceDate = dateFormat.parse(birthday);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                SimpleDateFormat targetFormat = new SimpleDateFormat("MMMM yyyy");
                return targetFormat.format(sourceDate);

            } else {
                return birthday;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getHeight() {
        return height;
    }

    public String getHeightInCm() {
        if (height != null) {
            return height.replace(".0", "") + " cm";
        } else {
            return height;
        }
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public String getWeightInKg() {
        return weight + "0kg";
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
