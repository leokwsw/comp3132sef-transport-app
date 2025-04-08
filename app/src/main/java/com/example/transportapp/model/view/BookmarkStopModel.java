package com.example.transportapp.model.view;

public class BookmarkStopModel {
    private String stopId;
    private String stopName;

    public BookmarkStopModel(String stopId, String stopName) {
        this.stopId = stopId;
        this.stopName = stopName;
    }

    public String getStopId() {
        return stopId;
    }

    public void setStopId(String stopId) {
        this.stopId = stopId;
    }

    public String getStopName() {
        return stopName;
    }

    public void setStopName(String stopName) {
        this.stopName = stopName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookmarkStopModel that = (BookmarkStopModel) o;
        return stopId.equals(that.stopId);
    }

    @Override
    public int hashCode() {
        return stopId.hashCode();
    }
}