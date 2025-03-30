package com.example.transportapp;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private final View mWindow;

    public CustomInfoWindowAdapter(LayoutInflater inflater) {
        // Inflate the custom layout for the Info Window
        mWindow = inflater.inflate(R.layout.custom_info_window, null);
    }

    private void renderWindowText(Marker marker, View view) {
        TextView title = view.findViewById(R.id.info_title);
        ImageView icon = view.findViewById(R.id.info_icon);

        // Set title (bus stop name)
        title.setText(marker.getTitle());

        // Customize the icon (optional)
//        if (marker.getTitle().contains("巴士站")) {
//            icon.setImageResource(R.drawable.ic_bus_stop);
//        } else {
//            icon.setImageResource(R.drawable.ic_default_marker);
//        }
    }

    @Override
    public View getInfoWindow(Marker marker) {
        renderWindowText(marker, mWindow);
        return mWindow;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null; // Returning null will use getInfoWindow() instead
    }
}

