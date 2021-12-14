package com.example.memo.domain;

import java.io.Serializable;

public class City implements Serializable {
    private String cityName; //城市名
    private String currTemp; //温度
    private String tempRange;   //温度范围
    private String weather; //天气
    private String wind; //风级
    private String windSpeed; //风级
    private String pmLevel; //pm等级
    private String date; //日期
    private String week;   //星期
    private String url;

    public City() {
    }

    public City(String cityName, String currTemp, String tempRange, String weather, String wind, String windSpeed, String pmLevel, String date, String week, String url) {
        this.cityName = cityName;
        this.currTemp = currTemp;
        this.tempRange = tempRange;
        this.weather = weather;
        this.wind = wind;
        this.windSpeed = windSpeed;
        this.pmLevel = pmLevel;
        this.date = date;
        this.week = week;
        this.url = url;
    }

    public City(String cityName) {
        this.cityName = cityName;
    }

    public City(String cityName, String url) {
        this.cityName = cityName;
        this.url = url;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCurrTemp() {
        return currTemp;
    }

    public void setCurrTemp(String currTemp) {
        this.currTemp = currTemp;
    }

    public String getTempRange() {
        return tempRange;
    }

    public void setTempRange(String tempRange) {
        this.tempRange = tempRange;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getWind() {
        return wind;
    }

    public void setWind(String wind) {
        this.wind = wind;
    }

    public String getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(String windSpeed) {
        this.windSpeed = windSpeed;
    }

    public String getPmLevel() {
        return pmLevel;
    }

    public void setPmLevel(String pmLevel) {
        this.pmLevel = pmLevel;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "City{" +
                "cityName='" + cityName + '\'' +
                ", currTemp=" + currTemp +
                ", weather='" + weather + '\'' +
                ", wind='" + wind + '\'' +
                ", windSpeed='" + windSpeed + '\'' +
                ", pmLevel='" + pmLevel + '\'' +
                ", date='" + date + '\'' +
                ", week='" + week + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
