package com.example.transportapp.network;

import com.example.transportapp.model.kmb.RouteListResponse;
import com.example.transportapp.model.kmb.RouteResponse;
import com.example.transportapp.model.kmb.RouteStopResponse;
import com.example.transportapp.model.kmb.StopETAResponse;
import com.example.transportapp.model.kmb.StopListResponse;
import com.example.transportapp.model.kmb.StopResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface KmbApiService {
    @GET("route/")
    Call<RouteListResponse> getRoutes();

    @GET("route/{route}/{direction}/{serviceType}")
    Call<RouteResponse> getRoute(
            @Path("route") String route,
            @Path("direction") String direction,
            @Path("serviceType") String serviceType
    );

    @GET("route-stop/{route}/{direction}/{serviceType}")
    Call<RouteStopResponse> getRouteStop(
            @Path("route") String route,
            @Path("direction") String direction,
            @Path("serviceType") String serviceType
    );

    @GET("stop/{stopId}")
    Call<StopResponse> getStopData(
            @Path("stopId") String stopId
    );

    @GET("eta/{stopId}/{route}/{serviceType}")
    Call<StopETAResponse> getStopETAData(
            @Path("stopId") String stopId,
            @Path("route") String route,
            @Path("serviceType") String serviceType
    );

    @GET("stop")
    Call<StopListResponse> getStopListData();

    @GET("stop-eta/{stop_id}")
    Call<StopETAResponse> getStopETAFromStopId(
            @Path("stop_id") String stopId
    );
}
