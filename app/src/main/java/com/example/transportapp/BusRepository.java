package com.example.transportapp;

import com.example.transportapp.model.kmb.RouteListResponse;

import java.util.ArrayList;
import java.util.List;

public class BusRepository {
    private static List<RouteListResponse.Route> busRoutes = new ArrayList<>();

    public static void setRoutes(List<RouteListResponse.Route> routes) {
        busRoutes = new ArrayList<>(routes);
    }

    public static List<RouteListResponse.Route> searchRoutes(String query) {
        List<RouteListResponse.Route> results = new ArrayList<>();
        if (query.isEmpty()) {
            results.addAll(busRoutes);
            return results;
        }

        String lowercaseQuery = query.toLowerCase();
        for (RouteListResponse.Route route : busRoutes) {
            if (route.route.toLowerCase().contains(lowercaseQuery) ||
                    String.format("%s", route.orig_en + " → " + route.dest_en).toLowerCase().contains(lowercaseQuery)) {
                results.add(route);
            }
        }
        return results;
    }

    public static List<RouteListResponse.Route> filterRoutes(String query, boolean nightBusOnly, boolean expressBusOnly, boolean airportBusOnly) {
        List<RouteListResponse.Route> results = new ArrayList<>();
        String lowercaseQuery = query.toLowerCase();

        for (RouteListResponse.Route route : busRoutes) {
            // Apply text search filter
            boolean matchesSearch = query.isEmpty() ||
                    route.route.toLowerCase().contains(lowercaseQuery) ||
                    String.format("%s", route.orig_en + " → " + route.dest_en).toLowerCase().contains(lowercaseQuery);

            // Apply type filters
            boolean matchesType = true;
            if (nightBusOnly && !route.route.startsWith("N")) {
                matchesType = false;
            }
            if (expressBusOnly && !route.route.startsWith("E")) {
                matchesType = false;
            }
            if (airportBusOnly && !route.route.startsWith("A")) {
                matchesType = false;
            }

            if (matchesSearch && matchesType) {
                results.add(route);
            }
        }
        return results;
    }
}
