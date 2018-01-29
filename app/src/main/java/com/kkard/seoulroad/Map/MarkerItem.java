package com.kkard.seoulroad.Map;

/**
 * Created by SuGeun on 2017-10-31.
 */

public class MarkerItem {
    double lat;
    double lon;
    String title;
    public MarkerItem(double lat, double lon, String title) {
        this.lat = lat; this.lon = lon; this.title = title;
    }
    public double getLat() {return lat; }
    public void setLat(double lat) { this.lat = lat; }
    public double getLon() { return lon; }
    public void setLon(double lon) { this.lon = lon; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
}


