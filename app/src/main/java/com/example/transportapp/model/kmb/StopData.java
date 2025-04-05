package com.example.transportapp.model.kmb;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.SerializedName;

public class StopData {
    public String stop;
    public String name_tc;
    public String name_en;
    public String lat;
    @SerializedName("long")
    public String lon;

    public LatLng getLatLng() {
        return new LatLng(Double.parseDouble(lat), Double.parseDouble(lon));
    }
}