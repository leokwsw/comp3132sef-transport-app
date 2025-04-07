package com.example.transportapp.model.view;

public class BookmarkModel {
    private String routeId;
    private String routeName;
    private String direction;
    private String serviceType;
    private String stops;

    public BookmarkModel(String routeId, String routeName, String direction, String serviceType, String stops) {
        this.routeId = routeId;
        this.routeName = routeName;
        this.direction = direction;
        this.serviceType = serviceType;
        this.stops = stops;
    }

    // Getters and Setters
    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getStops() {
        return stops;
    }

    public void setStops(String stops) {
        this.stops = stops;
    }
}