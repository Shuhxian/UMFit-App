package com.healthy.umfit.entity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class User implements Serializable {
    private String userId;
    private String userToken;
    private String phoneNumber;
    private String fcmToken;
    private String userName;
    private String exerciseCount;
    private String targetHeartRate;
    private String targetMaxHeartRate;
    private String averageHeartRate;
    private String maxHeartRate;
    private String exerciseDuration;
    private String birthday;
    private String height;
    private String weight;
    private String gender;
    private String email;
    private String targetExerciseDuration;
    private String targetExerciseFrequency;
    private String activityGoal;
    private String stepCount;
    private String isUpdateTokenNeeded;
    private UserPrescription userPrescription;

    public String getTargetExerciseDuration() {
        return targetExerciseDuration;
    }

    public void setTargetExerciseDuration(String targetExerciseDuration) {
        this.targetExerciseDuration = targetExerciseDuration;
    }

    public User(String userStr) {
        try {
            JSONObject userObj = new JSONObject(userStr);

            updateData(userObj);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void updateData(JSONObject userObj){
        try {
            if (userObj.has("phone_number")) {
                phoneNumber = userObj.getString("phone_number");
            }

            if (userObj.has("email")) {
                email = userObj.getString("email");
            }

            if (userObj.has("token")) {
                userToken = userObj.getString("token");
            }

            if (userObj.has("fcm_token")) {
                fcmToken = userObj.getString("fcm_token");
            }

            if (userObj.has("name")) {
                userName = userObj.getString("name");
            }

            if (userObj.has("birthday")) {
                birthday = userObj.getString("birthday");
            }

            if (userObj.has("height")) {
                height = userObj.getString("height");
            }

            if (userObj.has("weight")) {
                weight = userObj.getString("weight");
            }

            if (userObj.has("gender")) {
                gender = userObj.getString("gender");
            }

            if (userObj.has("target_step_count")) {
                activityGoal = userObj.getString("target_step_count");
            }

            if (userObj.has("is_update_token_need")) {
                isUpdateTokenNeeded = userObj.getString("is_update_token_need");
            }

            if (userObj.has("prescription")){
                JSONObject jo = userObj.getJSONObject("prescription");

                userPrescription = new UserPrescription();
                userPrescription.setTargetWarmUpDuration(jo.getString("target_warmup_duration"));
                userPrescription.setTargetFirstMainExerciseDuration(jo.getString("target_exercise_duration"));
                userPrescription.setExercise_frequency(jo.getString("target_exercise_frequency"));

                targetExerciseFrequency = jo.getString("target_exercise_frequency");
                targetHeartRate = jo.getString("target_heart_rate");
                targetMaxHeartRate = jo.getString("target_max_heart_rate");
                targetExerciseDuration = jo.getString("target_total_exercise_duration");
                maxHeartRate = jo.getString("max_heart_rate");
                averageHeartRate = jo.getString("average_heart_rate");
                exerciseCount = jo.getString("exercise_frequency");
                exerciseDuration = jo.getString("exercise_duration");

                if (jo.has("step_count")) {
                    stepCount = jo.getString("step_count");
                }
                userPrescription.setTargetCoolDownDuration(jo.getString("target_cooldown_duration"));
                userPrescription.setStartDate(jo.getString("start_time"));
                userPrescription.setEndDate(jo.getString("end_time"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getAverageHeartRate() {
        return averageHeartRate;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAverageHeartRate(String averageHeartRate) {
        averageHeartRate = averageHeartRate;
    }

    public String getMaxHeartRate() {
        return maxHeartRate;
    }

    public void setMaxHeartRate(String maxHeartRate) {
        this.maxHeartRate = maxHeartRate;
    }

    public String getExerciseDuration() {
        return exerciseDuration;
    }

    public void setExerciseDuration(String exerciseDuration) {
        this.exerciseDuration = exerciseDuration;
    }

    public String getTargetMaxHeartRate() {
        return targetMaxHeartRate;
    }

    public void setTargetMaxHeartRate(String targetMaxHeartRate) {
        this.targetMaxHeartRate = targetMaxHeartRate;
    }

    public String getTargetExerciseFrequency() {
        return targetExerciseFrequency;
    }

    public void setTargetExerciseFrequency(String targetExerciseFrequency) {
        this.targetExerciseFrequency = targetExerciseFrequency;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getExerciseCount() {
        return exerciseCount;
    }

    public void setExerciseCount(String exerciseCount) {
        this.exerciseCount = exerciseCount;
    }

    public String getTargetHeartRate() {
        return targetHeartRate;
    }

    public void setTargetHeartRate(String targetHeartRate) {
        this.targetHeartRate = targetHeartRate;
    }

    public String getStepCount() {
        return stepCount;
    }

    public void setStepCount(String stepCount) {
        this.stepCount = stepCount;
    }

    public String getActivityGoal() {
        return activityGoal;
    }

    public void setActivityGoal(String activityGoal) {
        this.activityGoal = activityGoal;
    }

    public boolean IsUpdateTokenNeeded() {
        return (isUpdateTokenNeeded.equals("1"));
    }

    public void setIsUpdateTokenNeeded(String isUpdateTokenNeeded) {
        this.isUpdateTokenNeeded = isUpdateTokenNeeded;
    }

    public UserPrescription getUserPrescription() {
        return userPrescription;
    }

    public void setUserPrescription(UserPrescription userPrescription) {
        this.userPrescription = userPrescription;
    }

    public Profile getProfile(){
        return new Profile(userId,gender,birthday,height,weight,userName);
    }
}
