package com.example.transportapp.model.view;

import com.example.transportapp.model.kmb.StopETAData;
import com.example.transportapp.model.kmb.StopETAResponse;

import java.util.List;

public class DetailsItemModel {
    public String stopName;
    public List<StopETAData> data;

    public DetailsItemModel(String stopName, List<StopETAData> data) {
        this.stopName = stopName;
        this.data = data;
    }
}
