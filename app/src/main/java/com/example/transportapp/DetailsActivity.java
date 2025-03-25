package com.example.transportapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.Arrays;
import java.util.List;

public class DetailsActivity extends AppCompatActivity implements OnMapReadyCallback {

    public static String ROUTE_KEY = "route";
    public static String DIRECTION_KEY = "direction";
    public static String DIRECTION_OUTBOUND = "outbound";
    public static String DIRECTION_INBOUND = "inbound";
    public static String SERVICE_TYPE_KEY = "service_type";

//    private String route = "";
//    private String direction = "";
//    private String serviceType = "";

    private String route = "74B";
    private String direction = "outbound";
    private String serviceType = "1";

    private GoogleMap mMap;
    private ImageButton btnToggleLocation;
    private FusedLocationProviderClient fusedLocationClient;
    private boolean isLocationEnabled = false;
    private static final int LOCATION_REQUEST_CODE = 100;
    private Marker userMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_bus_detail);

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

        findViewById(R.id.back_button).setOnClickListener(v -> onBackPressed());

        btnToggleLocation = findViewById(R.id.btnToggleLocation);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        btnToggleLocation.setOnClickListener(v -> toggleUserLocation());


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

        List<LatLng> busRoute = Arrays.asList(
                new LatLng(22.3193, 114.1694),
                new LatLng(22.3283, 114.1925)
        );

        mMap.addPolyline(new PolylineOptions().addAll(busRoute).width(12f).color(Color.RED));
        for (LatLng stop : busRoute) {
            mMap.addMarker(new MarkerOptions().position(stop).title("巴士站"));
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
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
            if (location != null) {
                LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                if (userMarker != null) userMarker.remove();
                userMarker = mMap.addMarker(new MarkerOptions().position(userLocation).title("你的位置"));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15));
            }
        });
    }
}
