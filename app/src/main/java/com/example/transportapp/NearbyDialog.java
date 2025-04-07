package com.example.transportapp;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class NearbyDialog extends DialogFragment {
    private NearbyListener listener;

    private static final int NEARBY_RANGE_100 = 110;
    private static final int NEARBY_RANGE_200 = 210;
    private static final int NEARBY_RANGE_400 = 410;
    private int nearbyRange = NEARBY_RANGE_100;

    public interface NearbyListener {
        void onFilterApplied(int nearbyRange);
    }

    public NearbyDialog(NearbyListener listener, int nearbyRange) {
        this.listener = listener;
        this.nearbyRange = nearbyRange;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_nearby, null);

        RadioButton rb100M = view.findViewById(R.id.rb100m);
        RadioButton rb200M = view.findViewById(R.id.rb200m);
        RadioButton rb400M = view.findViewById(R.id.rb400m);
        Button cancelButton = view.findViewById(R.id.cancel_button);
        Button applyButton = view.findViewById(R.id.apply_button);

        // Set initial states
        rb100M.setChecked(nearbyRange == NEARBY_RANGE_100);
        rb100M.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) nearbyRange = NEARBY_RANGE_100;
        });
        rb200M.setChecked(nearbyRange == NEARBY_RANGE_200);
        rb200M.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) nearbyRange = NEARBY_RANGE_200;
        });
        rb400M.setChecked(nearbyRange == NEARBY_RANGE_400);
        rb400M.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) nearbyRange = NEARBY_RANGE_400;
        });

        cancelButton.setOnClickListener(v -> dismiss());
        applyButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onFilterApplied(
                        nearbyRange
                );
            }
            dismiss();
        });

        builder.setView(view);
        return builder.create();
    }
} 