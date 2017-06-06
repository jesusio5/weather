package com.example.challenge.weather.raw.structures;

public class DailyWeather {
    String mTimeStamp;
    String mSummary;
    String mPrecipProb;
    String mTempMin;
    String mTempMax;
    String mTimeStampString;
    String mWindSpeed;
    String mSeaLevel;
    String mClouds;

    public DailyWeather(String mTimeStamp, String mSummary,
                        String mPrecipProb, String mTempMin,
                        String mTempMax, String mTimeStampString,
                        String mWindSpeed, String mSeaLevel, String mClouds) {
        this.mTimeStamp = mTimeStamp;
        this.mSummary = mSummary;
        this.mPrecipProb = mPrecipProb;
        this.mTempMin = mTempMin;
        this.mTempMax = mTempMax;
        this.mTimeStampString = mTimeStampString;
        this.mWindSpeed = mWindSpeed;
        this.mSeaLevel = mSeaLevel;
        this.mClouds = mClouds;
    }

    public DailyWeather() {
        this.mTimeStamp = "";
        this.mSummary = "";
        this.mPrecipProb = "";
        this.mTempMin = "";
        this.mTempMax = "";
        this.mTimeStampString = "";
        this.mWindSpeed = "";
        this.mSeaLevel = "";
    }

    public String getmTimeStamp() {
        return mTimeStamp;
    }

    public void setmTimeStamp(String mTimeStamp) {
        this.mTimeStamp = mTimeStamp;
    }

    public String getmSummary() {
        return mSummary;
    }

    public void setmSummary(String mSummary) {
        this.mSummary = mSummary;
    }

    public String getmPrecipProb() {
        return mPrecipProb;
    }

    public void setmPrecipProb(String mPrecipProb) {
        this.mPrecipProb = mPrecipProb;
    }

    public String getmTempMin() {
        return mTempMin;
    }

    public void setmTempMin(String mTempMin) {
        this.mTempMin = mTempMin;
    }

    public String getmTempMax() {
        return mTempMax;
    }

    public void setmTempMax(String mTempMax) {
        this.mTempMax = mTempMax;
    }

    public String getmTimeStampString() {
        return mTimeStampString;
    }

    public void setmTimeStampString(String mTimeStampString) {
        this.mTimeStampString = mTimeStampString;
    }

    public String getmWindSpeed() {
        return mWindSpeed;
    }

    public void setmWindSpeed(String mWindSpeed) {
        this.mWindSpeed = mWindSpeed;
    }

    public String getmSeaLevel() {
        return mSeaLevel;
    }

    public void setmSeaLevel(String mSeaLevel) {
        this.mSeaLevel = mSeaLevel;
    }

    public String getmClouds() {
        return mClouds;
    }

    public void setmClouds(String mClouds) {
        this.mClouds = mClouds;
    }

    @Override
    public String toString() {
        return "DailyWeather{" +
                "mTimeStamp='" + mTimeStamp + '\'' +
                ", mSummary='" + mSummary + '\'' +
                ", mPrecipProb='" + mPrecipProb + '\'' +
                ", mTempMin='" + mTempMin + '\'' +
                ", mTempMax='" + mTempMax + '\'' +
                ", mTimeStampString='" + mTimeStampString + '\'' +
                ", mWindSpeed='" + mWindSpeed + '\'' +
                ", mSeaLevel='" + mSeaLevel + '\'' +
                '}';
    }
}
