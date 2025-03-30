package com.example.transportapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.transportapp.model.kmb.RouteListResponse;
import com.example.transportapp.network.KmbApiService;
import com.example.transportapp.network.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private BusAdapter adapter;
    private EditText searchInput;
    private KmbApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Initialize API service
        apiService = RetrofitClient.getService();

        // Setup RecyclerView
        RecyclerView recyclerView = findViewById(R.id.route_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BusAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        // Setup search bar
        searchInput = findViewById(R.id.search_bar);
        searchInput.setOnClickListener(v -> {
            Intent intent = new Intent(this, SearchActivity.class);
            startActivity(intent);
        });

        // Load initial data
        loadRoutes();
    }

    private void loadRoutes() {
        apiService.getRoutes().enqueue(new Callback<RouteListResponse>() {
            @Override
            public void onResponse(Call<RouteListResponse> call, Response<RouteListResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<BusRoute> routes = new ArrayList<>();
                    for (RouteListResponse.Route route : response.body().data) {
                        routes.add(new BusRoute(route.route, route.orig_en + " → " + route.dest_en, ""));
                    }
                    BusRepository.setRoutes(routes);
                    adapter.updateData(routes);
                }
            }

            @Override
            public void onFailure(Call<RouteListResponse> call, Throwable t) {
                Log.e("API_ERROR", "Failed to load routes", t);
            }
        });
    }
}