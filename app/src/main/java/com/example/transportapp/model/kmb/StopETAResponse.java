package com.example.transportapp.model.kmb;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StopETAResponse {
    public List<StopETAData> data;

    public static class StopETAData {
        public String co;
        public String route;
        public String dir;
        public String service_type;
        public String seq;
        public String dest_en;
        public String eta_seq;
        public String eta;
        public String rmk_en;
        public String data_timestamp;
    }
}
