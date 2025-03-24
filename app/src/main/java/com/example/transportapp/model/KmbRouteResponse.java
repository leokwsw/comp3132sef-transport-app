package com.example.transportapp.model;

import java.util.List;

public class KmbRouteResponse {
    public List<Route> data;

    public static class Route {
        public String route;
        public String bound;
        public String service_type;
        public String orig_en;
        public String dest_en;
    }
}
