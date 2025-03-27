package com.example.transportapp.model.kmb;

import java.util.List;

public class RouteStopResponse {
    public List<RouteStopData> data;

    public static class RouteStopData {
        public String route;
        public String bound;
        public String service_type;
        public String seq;
        public String stop;
    }
}
