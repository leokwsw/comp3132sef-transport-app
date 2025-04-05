package com.example.transportapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.transportapp.adapter.BusAdapter;
import com.example.transportapp.model.kmb.RouteListResponse;
import com.example.transportapp.network.KmbApiService;
import com.example.transportapp.network.RetrofitClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private BusAdapter adapter;
    private AppCompatEditText searchInput;
    private KmbApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EdgeToEdge.enable(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        // Initialize API service
        apiService = RetrofitClient.getService();

        // Setup RecyclerView
        RecyclerView recyclerView = findViewById(R.id.route_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BusAdapter(new ArrayList<>(), busRoute -> {
            Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
            intent.putExtra(DetailsActivity.ROUTE_KEY, busRoute.route);
            intent.putExtra(DetailsActivity.SERVICE_TYPE_KEY, busRoute.service_type);
            intent.putExtra(DetailsActivity.DIRECTION_KEY, Objects.equals(busRoute.bound, "O") ? DetailsActivity.DIRECTION_OUTBOUND : DetailsActivity.DIRECTION_INBOUND);
            startActivity(intent);
        }, this);
        recyclerView.setAdapter(adapter);

        // Setup search input
        searchInput = findViewById(R.id.search_bar);
        Button bookmarkButton = findViewById(R.id.bookmark_button);

        searchInput.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SearchActivity.class);
            startActivity(intent);
        });

        bookmarkButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, BookmarkActivity.class);
            startActivity(intent);
        });

        setupSearch();
        loadRoutes();
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