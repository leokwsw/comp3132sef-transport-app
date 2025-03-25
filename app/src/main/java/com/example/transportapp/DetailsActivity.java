package com.example.transportapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.app.ActivityCompat;

import com.example.transportapp.model.kmb.RouteResponse;
import com.example.transportapp.model.kmb.RouteStopResponse;
import com.example.transportapp.model.kmb.StopETAResponse;
import com.example.transportapp.model.kmb.StopResponse;
import com.example.transportapp.network.KmbApiService;
import com.example.transportapp.network.RetrofitClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private final String TAG = "Amomo";
    public static String ROUTE_KEY = "route";
    public static String DIRECTION_KEY = "direction";
    public static String DIRECTION_OUTBOUND = "outbound";
    public static String DIRECTION_INBOUND = "inbound";
    public static String SERVICE_TYPE_KEY = "service_type";

//    private String route = "";
//    private String direction = "";
//    private String serviceType = "";

    private String route = "74X";
    private String direction = "outbound";
    private String serviceType = "1";

    private GoogleMap mMap;
    private ImageButton btnToggleLocation;
    private FusedLocationProviderClient fusedLocationClient;
    private boolean isLocationEnabled = false;
    private static final int LOCATION_REQUEST_CODE = 100;
    private Marker userMarker;

    private AppCompatTextView title;

    private KmbApiService apiService;
    private List<StopResponse.StopData> stopDataList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_bus_detail);
        // init network service
        apiService = RetrofitClient.getService();
        // init ui components
        title = findViewById(R.id.route_name);

//        if (getIntent().getExtras() != null) {
//            route = getIntent().getExtras().getString(ROUTE_KEY);
//            direction = getIntent().getExtras().getString(DIRECTION_KEY);
//            serviceType = getIntent().getExtras().getString(SERVICE_TYPE_KEY);
//
//            if (
//                    route == null || route.isEmpty() ||
//                            direction == null || direction.isEmpty() ||
//                            serviceType == null || serviceType.isEmpty()
//            ) {
//                onBackPressed();
//            }
//        } else {
//            onBackPressed();
//        }

        setTitle();


        findViewById(R.id.back_button).setOnClickListener(v -> onBackPressed());

        btnToggleLocation = findViewById(R.id.btnToggleLocation);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        btnToggleLocation.setOnClickListener(v -> toggleUserLocation());
    }

    private void setTitle() {
        apiService.getRoute(route, direction, serviceType).enqueue(new Callback<RouteResponse>() {
            @Override
            public void onResponse(@NonNull Call<RouteResponse> call, @NonNull Response<RouteResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("API", "Route : From : " + response.body().data.orig_en);
                    Log.d("API", "Route : To : " + response.body().data.dest_en);
                    title.setText(String.format("%s -> %s", response.body().data.orig_en, response.body().data.dest_en));
                }
            }

            @Override
            public void onFailure(@NonNull Call<RouteResponse> call, @NonNull Throwable t) {
                Log.e("API_ERROR", "Route : Failed to load data", t);
            }
        });
    }

    private void getRouteData() {
        apiService.getRouteStop(route, direction, serviceType).enqueue(new Callback<RouteStopResponse>() {
            @Override
            public void onResponse(@NonNull Call<RouteStopResponse> call, @NonNull Response<RouteStopResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<String> stopIds = response.body().data.stream().map(d -> d.stop).collect(Collectors.toList());
                    for (String stopId : stopIds) {
                        apiService.getStopData(stopId).enqueue(new Callback<StopResponse>() {
                            @Override
                            public void onResponse(@NonNull Call<StopResponse> call, @NonNull Response<StopResponse> response) {
                                if (response.isSuccessful() && response.body() != null) {
                                    Log.d(TAG, "Stop : " + response.body().data.name_en);
                                    Log.d(TAG, "Stop Lat : " + response.body().data.lat);
                                    Log.d(TAG, "Stop Long : " + response.body().data.lon);
                                    Log.d(TAG, "Stop Lat,Long : " + response.body().data.lat + "," + response.body().data.lon);
                                    stopDataList.add(response.body().data);
                                    if (stopDataList.size() == stopIds.size())
                                        drawRouteOnMap(stopDataList);
                                }
                            }

                            @Override
                            public void onFailure(@NonNull Call<StopResponse> call, @NonNull Throwable t) {
                                Log.e(TAG, "Stop : Failed to load data", t);
                            }
                        });

                        apiService.getStopETAData(stopId, route, serviceType).enqueue(new Callback<StopETAResponse>() {
                            @Override
                            public void onResponse(@NonNull Call<StopETAResponse> call, @NonNull Response<StopETAResponse> response) {
                                if (response.isSuccessful() && response.body() != null) {
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
                                    sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
                                    List<StopETAResponse.StopETAData> data = response.body().data;

                                    for (StopETAResponse.StopETAData eta : data) {
                                        if (eta.eta != null && !eta.eta.isEmpty()) {
                                            try {
                                                Log.d(TAG, "Stop ETA : " + eta.eta_seq + ":::" + sdf.parse(eta.eta));
                                            } catch (ParseException e) {
                                                throw new RuntimeException(e);
                                            }
                                        }


                                    }

                                }
                            }

                            @Override
                            public void onFailure(@NonNull Call<StopETAResponse> call, @NonNull Throwable t) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onFailure(Call<RouteStopResponse> call, Throwable t) {
                Log.e(TAG, "Route Stop : Failed to load data", t);
            }
        });
    }

    private void drawRouteOnMap(List<StopResponse.StopData> stopDataList) {

        // move camera to first bus stop
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(stopDataList.get(0).getLatLng(), 13));

        // add marker
        for (StopResponse.StopData stop : stopDataList) {
            mMap.addMarker(new MarkerOptions().position(stop.getLatLng()).title(stop.name_en));
        }

        // fetch Directions
        new FetchRouteTask().execute(getDirectionsUrl(stopDataList.stream().map(StopResponse.StopData::getLatLng).collect(Collectors.toList())));

//        List<LatLng> routePoints = stopDataList.stream().map(StopResponse.StopData::getLatLng).collect(Collectors.toList());
//        for (int i = 0; i < routePoints.size() - 1; i++) {
//            LatLng start = routePoints.get(i);
//            LatLng end = routePoints.get(i + 1);
//
//            new FetchRouteTask().execute(getDirectionsUrlForTwoPoint(start, end));
//        }

    }

    private String getDirectionsUrlForTwoPoint(LatLng origin, LatLng destination) {
        return "https://maps.googleapis.com/maps/api/directions/json?"
                + "origin=" + origin.latitude + "," + origin.longitude
                + "&destination=" + destination.latitude + "," + destination.longitude
                + "&mode=driving"
                + "&key=AIzaSyBP10JHjUiM6UuBzG7K1-IRCz5npFcCMOk";
    }

    private String getDirectionsUrl(List<LatLng> routePoints) {

        Log.d(TAG, "size : " + routePoints.size());

        if (routePoints.size() < 2) return null;

        LatLng origin = routePoints.get(0);
        LatLng destination = routePoints.get(routePoints.size() - 1);

        StringBuilder waypoints = new StringBuilder();
        if (routePoints.size() > 2) {
            waypoints.append("&waypoints=");
            for (int i = 1; i < routePoints.size() - 1; i++) {
                LatLng point = routePoints.get(i);
                waypoints.append(point.latitude).append(",").append(point.longitude);
                if (i < routePoints.size() - 2) {
                    waypoints.append("|");
                }
            }
        }

        String url = "https://maps.googleapis.com/maps/api/directions/json?"
                + "origin=" + origin.latitude + "," + origin.longitude
                + "&destination=" + destination.latitude + "," + destination.longitude
                + waypoints.toString()
                + "&mode=driving"
//                + "&mode=transit"
//                + "&transit_mode=train|tram|subway"
                + "&key=AIzaSyBP10JHjUiM6UuBzG7K1-IRCz5npFcCMOk";

        Log.d(TAG, url);

        return url;
    }

    private class FetchRouteTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                return response.toString();
            } catch (Exception e) {
                Log.e(TAG, "Error fetching route", e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                Log.d(TAG, "onPostExecute");
                Log.d(TAG, result);
                drawRoute(result);
            }
        }
    }

    private void drawRoute(String jsonData) {
        try {
            JSONArray routes = new JSONObject(jsonData).getJSONArray("routes");
            if (routes.length() > 0) {
                JSONArray legs = routes.getJSONObject(0).getJSONArray("legs");

                for (int i = 0; i < legs.length(); i++) {
                    JSONArray steps = legs.getJSONObject(i).getJSONArray("steps");
                    List<LatLng> path = new ArrayList<>();

                    for (int j = 0; j < steps.length(); j++) {
                        JSONObject step = steps.getJSONObject(j);

                        JSONObject startLocation = step.getJSONObject("start_location");
                        LatLng startLatLng = new LatLng(startLocation.getDouble("lat"), startLocation.getDouble("lng"));

                        JSONObject endLocation = step.getJSONObject("end_location");
                        LatLng endLatLng = new LatLng(endLocation.getDouble("lat"), endLocation.getDouble("lng"));

                        path.add(startLatLng);
                        path.add(endLatLng);
                    }

                    mMap.addPolyline(new PolylineOptions()
                            .addAll(path)
                            .width(12f)
                            .color(Color.RED));
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Error parsing route", e);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.getUiSettings().setAllGesturesEnabled(true);
        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(getLayoutInflater()));

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            enableUserLocation();
        }

        getRouteData();

        mMap.addPolyline(new PolylineOptions().addAll(stopDataList.stream().map(StopResponse.StopData::getLatLng).collect(Collectors.toList())).width(12f).color(Color.RED));
        for (StopResponse.StopData stop : stopDataList) {
            mMap.addMarker(new MarkerOptions().position(stop.getLatLng()).title(stop.name_en));
        }

        mMap.setOnInfoWindowClickListener(marker -> new BusStopDialogFragment(marker.getTitle()).show(getSupportFragmentManager(), "busStopDialog"));
    }

    private void toggleUserLocation() {
        if (isLocationEnabled) {
            isLocationEnabled = false;
            btnToggleLocation.setImageResource(R.drawable.ic_location_off);
            if (userMarker != null) userMarker.remove();
        } else {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                enableUserLocation();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
            }
        }
    }

    private void enableUserLocation() {
        isLocationEnabled = true;
        btnToggleLocation.setImageResource(R.drawable.ic_location_on);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
            if (location != null) {
                LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                Log.d("Location", String.format("My Location : %s ;;; %s", location.getLatitude(), location.getLongitude()));
                if (userMarker != null) userMarker.remove();
                userMarker = mMap.addMarker(new MarkerOptions().position(userLocation).title("你的位置"));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15));
            }
        });
    }
}
