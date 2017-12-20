package com.model;

/**
 * Created by admin on 03-11-2017.
 */

public class BPModel {
    String date;
    String pulsePerMin;
    String systolicPressure;
    String diabolicPressure;


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPulsePerMin() {
        return pulsePerMin;
    }

    public void setPulsePerMin(String pulsePerMin) {
        this.pulsePerMin = pulsePerMin;
    }

    public String getSystolicPressure() {
        return systolicPressure;
    }

    public void setSystolicPressure(String systolicPressure) {
        this.systolicPressure = systolicPressure;
    }

    public String getDiabolicPressure() {
        return diabolicPressure;
    }

    public void setDiabolicPressure(String diabolicPressure) {
        this.diabolicPressure = diabolicPressure;
    }
}
