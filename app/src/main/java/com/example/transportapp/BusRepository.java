package com.example.transportapp;

import java.util.ArrayList;
import java.util.List;

public class BusRepository {
    private static List<BusRoute> busRoutes = new ArrayList<>();

    static {
        // test
        busRoutes.add(new BusRoute("A30", "Airport Express", "A-B-C"));
    }

    public static void setRoutes(List<BusRoute> routes) {
        busRoutes = new ArrayList<>(routes);
    }

    public static List<BusRoute> searchRoutes(String query) {
        List<BusRoute> results = new ArrayList<>();
        if (query.isEmpty()) {
            results.addAll(busRoutes);
            return results;
        }

        String lowercaseQuery = query.toLowerCase();
        for (BusRoute route : busRoutes) {
            if (route.getNumber().toLowerCase().contains(lowercaseQuery) ||
                    route.getRouteName().toLowerCase().contains(lowercaseQuery)) {
                results.add(route);
            }
        }
        return results;
    }

    public static List<BusRoute> filterRoutes(String query, boolean nightBusOnly, boolean expressBusOnly, boolean airportBusOnly) {
        List<BusRoute> results = new ArrayList<>();
        String lowercaseQuery = query.toLowerCase();

        for (BusRoute route : busRoutes) {
            // Apply text search filter
            boolean matchesSearch = query.isEmpty() ||
                    route.getNumber().toLowerCase().contains(lowercaseQuery) ||
                    route.getRouteName().toLowerCase().contains(lowercaseQuery);

            // Apply type filters
            boolean matchesType = true;
            if (nightBusOnly && !route.getNumber().startsWith("N")) {
                matchesType = false;
            }
            if (expressBusOnly && !route.getNumber().startsWith("E")) {
                matchesType = false;
            }
            if (airportBusOnly && !route.getNumber().startsWith("A")) {
                matchesType = false;
            }

            if (matchesSearch && matchesType) {
                results.add(route);
            }
        }
        return results;
    }
}
