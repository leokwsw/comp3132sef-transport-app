package com.example.transportapp;

public class BusRoute {
    private String number;
    private String routeName;
    private String stops;

    public BusRoute(String number, String routeName, String stops) {
        this.number = number;
        this.routeName = routeName;
        this.stops = stops;
    }

    // Getters
    public String getNumber() { return number; }
    public String getRouteName() { return routeName; }
    public String getStops() { return stops; }
}
