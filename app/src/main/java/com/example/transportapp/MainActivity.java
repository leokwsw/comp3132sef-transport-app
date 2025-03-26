package com.example.transportapp;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

//        KmbApiService apiService = RetrofitClient.getService();
//        apiService.getRoutes().enqueue(new Callback<RouteListResponse>() {
//            @Override
//            public void onResponse(Call<RouteListResponse> call, Response<RouteListResponse> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    for (RouteListResponse.Route route : response.body().data) {
//                        Log.d("API", "Route: " + route.route + ", From: " + route.orig_en + " To: " + route.dest_en);
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<RouteListResponse> call, Throwable t) {
//                Log.e("API_ERROR", "Failed to load data", t);
//            }
//        });

        RecyclerView recyclerView = findViewById(R.id.route_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BusAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        searchInput = findViewById(R.id.search_bar);
        setupSearch();
    }

    private void setupSearch() {
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                performSearch(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void performSearch(String query) {
        List<BusRoute> results;
        if (query.isEmpty()) {
            results = new ArrayList<>();
        } else {
            results = BusRepository.searchRoutes(query);
        }
        adapter.updateData(results);
    }
}