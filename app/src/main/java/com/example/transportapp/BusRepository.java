package com.example.transportapp;

import java.util.ArrayList;
import java.util.List;

public class BusRepository {
    private static List<BusRoute> busRoutes = new ArrayList<>();

    static {
        // test
        busRoutes.add(new BusRoute("A30", "Airport Express", "A-B-C"));
    }

    public static List<BusRoute> searchRoutes(String query) {
        List<BusRoute> results = new ArrayList<>();
        String[] keywords = query.toLowerCase().split(" ");

        for (BusRoute route : busRoutes) {
            boolean matchAll = true;
            for (String keyword : keywords) {
                if (!(route.getNumber().toLowerCase().contains(keyword) ||
                        route.getRouteName().toLowerCase().contains(keyword) ||
                        route.getStops().toLowerCase().contains(keyword))) {
                    matchAll = false;
                    break;
                }
            }
            if (matchAll) results.add(route);
        }
        return results;
    }
}
