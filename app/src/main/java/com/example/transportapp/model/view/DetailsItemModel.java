package com.example.transportapp.model.view;

import com.example.transportapp.model.kmb.StopETAResponse;

import java.util.List;

public class DetailsItemModel {
    public String stopName;
    public List<StopETAResponse.StopETAData> data;

    public DetailsItemModel(String stopName, List<StopETAResponse.StopETAData> data) {
        this.stopName = stopName;
        this.data = data;
    }
}
