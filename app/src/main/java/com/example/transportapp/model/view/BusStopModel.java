package com.example.transportapp.model.view;

import com.example.transportapp.model.kmb.RouteStopResponse;
import com.example.transportapp.model.kmb.StopETAResponse;
import com.example.transportapp.model.kmb.StopResponse;

import java.util.List;

public class BusStopModel {
    public RouteStopResponse.RouteStopData routeStopData;
    public StopResponse.StopData stopData;
    public List<StopETAResponse.StopETAData> etaData;

    public BusStopModel(RouteStopResponse.RouteStopData routeStopData, StopResponse.StopData stopData, List<StopETAResponse.StopETAData> etaData) {
        this.routeStopData = routeStopData;
        this.stopData = stopData;
        this.etaData = etaData;
    }
}
