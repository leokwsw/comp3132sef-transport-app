package com.example.transportapp.model.kmb;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StopResponse {
    public StopData data;

    public static class StopData {
        public String stop;
        public String name_en;
        public String lat;
        @SerializedName("long")
        public String lon;
    }
}
