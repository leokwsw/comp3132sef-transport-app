package com.example.transportapp.model.kmb;

public class RouteResponse {
    public RouteData data;

    public static class RouteData {
        public String route;
        public String bound;
        public String service_type;
        public String orig_en;
        public String dest_en;
    }
}
