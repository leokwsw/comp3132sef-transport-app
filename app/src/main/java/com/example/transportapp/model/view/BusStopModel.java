package com.example.transportapp.model.view;

import com.example.transportapp.model.kmb.RouteStopResponse;
import com.example.transportapp.model.kmb.StopData;
import com.example.transportapp.model.kmb.StopETAData;
import com.example.transportapp.model.kmb.StopETAResponse;
import com.example.transportapp.model.kmb.StopResponse;

import java.util.List;

public class BusStopModel {
    public RouteStopResponse.RouteStopData routeStopData;
    public StopData stopData;
    public List<StopETAData> etaData;

    public BusStopModel(RouteStopResponse.RouteStopData routeStopData, StopData stopData, List<StopETAData> etaData) {
        this.routeStopData = routeStopData;
        this.stopData = stopData;
        this.etaData = etaData;
    }
}
