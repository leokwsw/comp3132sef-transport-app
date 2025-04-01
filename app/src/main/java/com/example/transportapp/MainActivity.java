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

import com.example.transportapp.adapter.BusAdapter;
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
        adapter = new BusAdapter(new ArrayList<>(), busRoute -> {
            Intent intent = new Intent(this, DetailsActivity.class);
            intent.putExtra(DetailsActivity.ROUTE_KEY, busRoute.route);
            intent.putExtra(DetailsActivity.SERVICE_TYPE_KEY, busRoute.service_type);
            intent.putExtra(DetailsActivity.DIRECTION_KEY, busRoute.bound == "O" ? DetailsActivity.DIRECTION_OUTBOUND : DetailsActivity.DIRECTION_INBOUND);
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);

        searchInput = findViewById(R.id.search_bar);
        setupSearch();
    }

    private void setupSearch() {
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                performSearch(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // Setup search bar
        searchInput = findViewById(R.id.search_bar);
        searchInput.setOnClickListener(v -> {
            Intent intent = new Intent(this, SearchActivity.class);
            startActivity(intent);
        });

        // Load initial data
        loadRoutes();
    }

    private void performSearch(String query) {
        List<RouteListResponse.Route> results;
        if (query.isEmpty()) {
            results = new ArrayList<>();
        } else {
            results = BusRepository.searchRoutes(query);
        }
        adapter.updateData(results);
    }

    private void loadRoutes() {
        apiService.getRoutes().enqueue(new Callback<RouteListResponse>() {
            @Override
            public void onResponse(Call<RouteListResponse> call, Response<RouteListResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    BusRepository.setRoutes(response.body().data);
                    adapter.updateData(response.body().data);
                }
            }

            @Override
            public void onFailure(Call<RouteListResponse> call, Throwable t) {
                Log.e("API_ERROR", "Failed to load routes", t);
            }
        });
    }
}