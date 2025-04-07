package com.example.transportapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageButton;

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

public class SearchActivity extends AppCompatActivity implements FilterDialog.FilterListener {

    private BusAdapter adapter;
    private EditText searchInput;
    private KmbApiService apiService;
    private boolean nightBusOnly = false;
    private boolean expressBusOnly = false;
    private boolean airportBusOnly = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EdgeToEdge.enable(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        apiService = RetrofitClient.getService();
        searchInput = findViewById(R.id.search_input);
        ImageButton filterButton = findViewById(R.id.filter_button);

        // Setup RecyclerView
        RecyclerView recyclerView = findViewById(R.id.search_results);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BusAdapter(new ArrayList<>(), (String route, String serviceType, String bound) -> {
            Intent intent = new Intent(SearchActivity.this, DetailsActivity.class);
            intent.putExtra(DetailsActivity.ROUTE_KEY, route);
            intent.putExtra(DetailsActivity.SERVICE_TYPE_KEY, serviceType);
            intent.putExtra(DetailsActivity.DIRECTION_KEY, bound.equals("O") ? DetailsActivity.DIRECTION_OUTBOUND : DetailsActivity.DIRECTION_INBOUND);
            startActivity(intent);
        }, this); // Pass context explicitly
        recyclerView.setAdapter(adapter);

        // Setup back button
        findViewById(R.id.back_button).setOnClickListener(v -> onBackPressed());

        // Setup filter button
        filterButton.setOnClickListener(v -> showFilterDialog());
        setupSearch();
        loadRoutes();
    }

    private void showFilterDialog() {
        new FilterDialog(this, nightBusOnly, expressBusOnly, airportBusOnly)
                .show(getSupportFragmentManager(), "filter_dialog");
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
        List<RouteListResponse.Route> filteredRoutes = BusRepository.filterRoutes(
                query,
                nightBusOnly,
                expressBusOnly,
                airportBusOnly
        );
        adapter.updateData(filteredRoutes);
    }

    private void loadRoutes() {
        apiService.getRoutes().enqueue(new Callback<RouteListResponse>() {
            @Override
            public void onResponse(Call<RouteListResponse> call, Response<RouteListResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    BusRepository.setRoutes(response.body().data);
                    performSearch(searchInput.getText().toString());
                }
            }

            @Override
            public void onFailure(Call<RouteListResponse> call, Throwable t) {
                // Handle error
            }
        });
    }

    @Override
    public void onFilterApplied(boolean nightBusOnly, boolean expressBusOnly, boolean airportBusOnly) {
        this.nightBusOnly = nightBusOnly;
        this.expressBusOnly = expressBusOnly;
        this.airportBusOnly = airportBusOnly;
        performSearch(searchInput.getText().toString());
    }
}