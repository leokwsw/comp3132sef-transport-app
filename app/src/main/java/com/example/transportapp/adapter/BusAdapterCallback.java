package com.example.transportapp.adapter;

import com.example.transportapp.model.kmb.RouteListResponse;

public interface BusAdapterCallback {
    void onClick(String route, String serviceType, String bound);
}
