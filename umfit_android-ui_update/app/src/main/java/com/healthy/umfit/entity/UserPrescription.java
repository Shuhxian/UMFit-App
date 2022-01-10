package com.healthy.umfit.entity;

public class UserPrescription {
    private String targetWarmUpDuration;
    private String targetFirstMainExerciseDuration;
    private String targetFirstRestDuration;
    private String targetSecondMainExerciseDuration;
    private String targetSecondRestDuration;
    private String targetCoolDownDuration;
    private String exercise_frequency;
    private String maxHeartRate;
    private String startDate;
    private String endDate;
    private String targetTotalExerciseDuration;

    public String getTargetWarmUpDuration() {
        return targetWarmUpDuration;
    }

    public void setTargetWarmUpDuration(String targetWarmUpDuration) {
        this.targetWarmUpDuration = targetWarmUpDuration;
    }

    public String getTargetFirstMainExerciseDuration() {
        return targetFirstMainExerciseDuration;
    }

    public void setTargetFirstMainExerciseDuration(String targetFirstMainExerciseDuration) {
        this.targetFirstMainExerciseDuration = targetFirstMainExerciseDuration;
    }

    public String getExercise_frequency() {
        return exercise_frequency;
    }

    public void setExercise_frequency(String exercise_frequency) {
        this.exercise_frequency = exercise_frequency;
    }

    public String getTargetFirstRestDuration() {
        return targetFirstRestDuration;
    }

    public void setTargetFirstRestDuration(String targetFirstRestDuration) {
        this.targetFirstRestDuration = targetFirstRestDuration;
    }

    public String getTargetSecondMainExerciseDuration() {
        return targetSecondMainExerciseDuration;
    }

    public void setTargetSecondMainExerciseDuration(String targetSecondMainExerciseDuration) {
        this.targetSecondMainExerciseDuration = targetSecondMainExerciseDuration;
    }

    public String getTargetSecondRestDuration() {
        return targetSecondRestDuration;
    }

    public void setTargetSecondRestDuration(String targetSecondRestDuration) {
        this.targetSecondRestDuration = targetSecondRestDuration;
    }

    public String getTargetCoolDownDuration() {
        return targetCoolDownDuration;
    }

    public void setTargetCoolDownDuration(String targetCoolDownDuration) {
        this.targetCoolDownDuration = targetCoolDownDuration;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getTargetTotalExerciseDuration() {
        double iTotal;
        try {
            iTotal = Double.parseDouble(targetWarmUpDuration) + Double.parseDouble(targetFirstMainExerciseDuration) +
                    Double.parseDouble(targetCoolDownDuration);
        } catch (Exception e) {
            e.printStackTrace();
            iTotal = 0;
        }

        targetTotalExerciseDuration = String.valueOf((int) iTotal);
        return targetTotalExerciseDuration;
    }

    public void setTargetTotalExerciseDuration(String targetTotalExerciseDuration) {
        this.targetTotalExerciseDuration = targetTotalExerciseDuration;
    }

}
