package com.example.transportapp.model.kmb;

import com.google.gson.annotations.SerializedName;

public class StopDataWithDistance extends StopData {
    public String stop;
    public String name_tc;
    public String name_en;
    public String lat;
    @SerializedName("long")
    public String lon;
    public double distance;

    public StopDataWithDistance(StopData busStop, double distance) {
        this.stop = busStop.stop;
        this.name_tc = busStop.name_tc;
        this.name_en = busStop.name_en;
        this.lat = busStop.lat;
        this.lon = busStop.lon;
        this.distance = distance;
    }
}