package com.model;

import android.os.Parcel;
import android.os.Parcelable;



public class BPMeasurementModelList implements Parcelable {

    private String measurementTime;
    private float sysPressure;
    private float diabolicPressure;
    private float pulsePerMin;
    private String comments;
    private String typeBP;


    public BPMeasurementModelList(String measurementTime, float sysPressure, float diabolicPressure, float pulsePerMin, String comments, String typeBP) {
        this.measurementTime = measurementTime;
        this.sysPressure = sysPressure;
        this.diabolicPressure = diabolicPressure;
        this.pulsePerMin = pulsePerMin;
        this.comments = comments;
        this.typeBP = typeBP;
    }

    public String getTypeBP() {
        return typeBP;
    }

    public void setTypeBP(String typeBP) {
        this.typeBP = typeBP;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getMeasurementTime() {
        return measurementTime;
    }

    public void setMeasurementTime(String measurementTime) {
        this.measurementTime = measurementTime;
    }

    public float getSysPressure() {
        return sysPressure;
    }

    public void setSysPressure(float sysPressure) {
        this.sysPressure = sysPressure;
    }

    public float getDiabolicPressure() {
        return diabolicPressure;
    }

    public void setDiabolicPressure(float diabolicPressure) {
        this.diabolicPressure = diabolicPressure;
    }

    public float getPulsePerMin() {
        return pulsePerMin;
    }

    public void setPulsePerMin(float pulsePerMin) {
        this.pulsePerMin = pulsePerMin;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(measurementTime);
        dest.writeFloat(sysPressure);
        dest.writeFloat(diabolicPressure);
        dest.writeFloat(pulsePerMin);
        dest.writeString(comments);
        dest.writeString(typeBP);
    }



    public static  final Creator CREATOR=new Creator()
    {

        @Override
        public BPMeasurementModelList createFromParcel(Parcel source) {
            return new BPMeasurementModelList(source);
        }

        @Override
        public BPMeasurementModelList[] newArray(int size) {
            return new BPMeasurementModelList[size];
        }
    };



    protected BPMeasurementModelList(Parcel parcel){

        measurementTime = parcel.readString();
        sysPressure = parcel.readFloat();
        diabolicPressure = parcel.readFloat();
        pulsePerMin = parcel.readFloat();
        comments = parcel.readString();
        typeBP = parcel.readString();


    }


}
