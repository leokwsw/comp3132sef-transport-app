package com.example.transportapp;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.transportapp.model.KmbRouteResponse;
import com.example.transportapp.network.KmbApiService;
import com.example.transportapp.network.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        KmbApiService apiService = RetrofitClient.getService();
        apiService.getRoutes().enqueue(new Callback<KmbRouteResponse>() {
            @Override
            public void onResponse(Call<KmbRouteResponse> call, Response<KmbRouteResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    for (KmbRouteResponse.Route route : response.body().data) {
                        Log.d("API", "Route: " + route.route + ", From: " + route.orig_en + " To: " + route.dest_en);
                    }
                }
            }

            @Override
            public void onFailure(Call<KmbRouteResponse> call, Throwable t) {
                Log.e("API_ERROR", "Failed to load data", t);
            }
        });

    }
}