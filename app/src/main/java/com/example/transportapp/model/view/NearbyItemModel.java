package com.example.transportapp.model.view;

import com.example.transportapp.model.kmb.StopDataWithDistance;
import com.example.transportapp.model.kmb.StopETAData;

public class NearbyItemModel {
    public StopDataWithDistance stop;
    public StopETAData stopETAData;

    public NearbyItemModel(StopDataWithDistance stop, StopETAData stopETAData) {
        this.stop = stop;
        this.stopETAData = stopETAData;
    }
}
