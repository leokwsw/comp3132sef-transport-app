package com.example.transportapp.network;

import com.example.transportapp.model.KmbRouteResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface KmbApiService {
    @GET("route/")
    Call<KmbRouteResponse> getRoutes();
}
