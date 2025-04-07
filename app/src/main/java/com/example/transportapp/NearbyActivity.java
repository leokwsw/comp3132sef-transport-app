package com.example.transportapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.FloatRange;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.transportapp.adapter.NearbyAdapter;
import com.example.transportapp.model.kmb.StopData;
import com.example.transportapp.model.kmb.StopDataWithDistance;
import com.example.transportapp.model.kmb.StopETAData;
import com.example.transportapp.model.kmb.StopETAResponse;
import com.example.transportapp.model.kmb.StopListResponse;
import com.example.transportapp.model.view.NearbyItemModel;
import com.example.transportapp.network.KmbApiService;
import com.example.transportapp.network.RetrofitClient;
import com.example.transportapp.utils.SystemUtils;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NearbyActivity extends AppCompatActivity {

    private static final int LOCATION_REQUEST_CODE = 100;

    // default is HKMU JCC, get from Google Map
    private @FloatRange(from = -90.0, to = 90.0) double mLatitudeDegrees = 22.3159553;
    private @FloatRange(from = -180.0, to = 180.0) double mLongitudeDegrees = 114.1785895;

    private KmbApiService apiService;

    private static final int NEARBY_RANGE_100 = 110;
    private static final int NEARBY_RANGE_200 = 210;
    private static final int NEARBY_RANGE_400 = 410;
    private int nearbyRange = NEARBY_RANGE_100;

    private List<StopData> busStops = new ArrayList<>();
    private List<StopDataWithDistance> nearbyBusStops = new ArrayList<>();
    private List<NearbyItemModel> nearbyItemModels = new ArrayList<>();

    private NearbyAdapter nearbyAdapter = new NearbyAdapter();

    private ProgressBar pd;

    private RadioButton rbRange100, rbRange200, rbRange400;

    private int loadedItemSize = 0;
    private int loadSize = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EdgeToEdge.enable(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby);

        apiService = RetrofitClient.getService();

        RecyclerView recyclerView = findViewById(R.id.nearby_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(nearbyAdapter);

        pd = findViewById(R.id.progressBar);

        requestLocationPermission();
        findViewById(R.id.back_button).setOnClickListener(v -> onBackPressed());

        if (SystemUtils.isEmulator()) {
            findViewById(R.id.rgRange).setVisibility(View.GONE);
            getStopList();
        } else {
            setupRadioButtons();
        }
    }

    private void requestLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            enableUserLocation();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            }, LOCATION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                enableUserLocation();
            } else {
                Toast.makeText(this, "Please Allow the Location Permission", Toast.LENGTH_LONG).show();
                requestLocationPermission();
            }
        }
    }

    private void enableUserLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices
                .getFusedLocationProviderClient(this)
                .getLastLocation()
                .addOnSuccessListener(this, location -> {
                    if (location != null) {
                        Log.d("NearbyActivity", String.format("%s : %s", location.getLatitude(), location.getLongitude()));
                        mLatitudeDegrees = location.getLatitude();
                        mLongitudeDegrees = location.getLongitude();

                        getStopList();
                    }
                });
    }

    private void getStopList() {
        apiService.getStopListData().enqueue(new Callback<StopListResponse>() {
            @Override
            public void onResponse(@NonNull Call<StopListResponse> call, @NonNull Response<StopListResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    busStops = response.body().data;

                    findMatchNearbyBusStop(false);
                }
            }

            @Override
            public void onFailure(@NonNull Call<StopListResponse> call, @NonNull Throwable t) {
                // TODO : Error Screen
            }
        });
    }

    private static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        float[] result = new float[1];
        Location.distanceBetween(lat1, lon1, lat2, lon2, result);
        return result[0];
    }

    private void findMatchNearbyBusStop(boolean skipFilter) {
        nearbyBusStops = new ArrayList<>();

        for (StopData busStop : busStops) {
            if (busStop.lat != null && busStop.lon != null) {
                try {
                    double distanceFromMe = calculateDistance(mLatitudeDegrees, mLongitudeDegrees, Double.parseDouble(busStop.lat), Double.parseDouble(busStop.lon));

                    if (skipFilter || distanceFromMe <= nearbyRange) {
                        StopDataWithDistance stopDataWithDistance = new StopDataWithDistance(busStop, distanceFromMe);
                        stopDataWithDistance.distance = distanceFromMe;
                        nearbyBusStops.add(stopDataWithDistance);
                    }
                } catch (NumberFormatException numberFormatException) {
                    // ignore
                }
            }
        }

        while (nearbyBusStops.isEmpty()) {
            findMatchNearbyBusStop(true);
        }

        nearbyBusStops.sort(Comparator.comparingDouble(o -> o.distance));
        getNearbyBusStopRouteWithETA();
    }

    private void getNearbyBusStopRouteWithETA() {
        for (StopDataWithDistance nearbyBusStop : nearbyBusStops) {
            apiService.getStopETAFromStopId(nearbyBusStop.stop).enqueue(new Callback<StopETAResponse>() {
                @Override
                public void onResponse(@NonNull Call<StopETAResponse> call, @NonNull Response<StopETAResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        for (StopETAData etaData : response.body().data) {
                            NearbyItemModel nearbyItemModel = new NearbyItemModel(nearbyBusStop, etaData);
                            nearbyItemModels.add(nearbyItemModel);

                            setupAdapter();
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<StopETAResponse> call, @NonNull Throwable t) {

                }
            });
        }
    }

    private void setupAdapter() {
        nearbyAdapter.setModels(nearbyItemModels);
        pd.setVisibility(View.GONE);
    }

    private void setupRadioButtons() {
        rbRange100 = findViewById(R.id.rbRange100);
        rbRange200 = findViewById(R.id.rbRange200);
        rbRange400 = findViewById(R.id.rbRange400);


        rbRange100.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                nearbyRange = NEARBY_RANGE_100;
                findMatchNearbyBusStop(false);
            }
        });
        rbRange200.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                nearbyRange = NEARBY_RANGE_200;
                findMatchNearbyBusStop(false);
            }
        });
        rbRange400.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                nearbyRange = NEARBY_RANGE_400;
                findMatchNearbyBusStop(false);
            }
        });
    }
}
