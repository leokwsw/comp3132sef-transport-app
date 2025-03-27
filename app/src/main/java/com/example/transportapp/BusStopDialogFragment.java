package com.example.transportapp;

import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class BusStopDialogFragment extends DialogFragment {

    private String busStopName;

    // Constructor to pass bus stop name
    public BusStopDialogFragment(String busStopName) {
        this.busStopName = busStopName;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(requireActivity());

        // Inflate the custom layout
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_bus_stop, null);
        builder.setView(view);

        // Get references to UI elements
        TextView title = view.findViewById(R.id.dialog_title);
        Button closeButton = view.findViewById(R.id.btn_close);

        // Set the bus stop name
        title.setText(busStopName);

        // Close button action
        closeButton.setOnClickListener(v -> dismiss());

        return builder.create();
    }
}
