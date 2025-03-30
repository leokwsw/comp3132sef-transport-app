package com.example.transportapp;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class FilterDialog extends DialogFragment {
    private FilterListener listener;
    private boolean nightBusOnly;
    private boolean expressBusOnly;
    private boolean airportBusOnly;

    public interface FilterListener {
        void onFilterApplied(boolean nightBusOnly, boolean expressBusOnly, boolean airportBusOnly);
    }

    public FilterDialog(FilterListener listener, boolean nightBusOnly, boolean expressBusOnly, boolean airportBusOnly) {
        this.listener = listener;
        this.nightBusOnly = nightBusOnly;
        this.expressBusOnly = expressBusOnly;
        this.airportBusOnly = airportBusOnly;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_filter, null);

        CheckBox nightBusCheckbox = view.findViewById(R.id.night_bus_checkbox);
        CheckBox expressBusCheckbox = view.findViewById(R.id.express_bus_checkbox);
        CheckBox airportBusCheckbox = view.findViewById(R.id.airport_bus_checkbox);
        Button cancelButton = view.findViewById(R.id.cancel_button);
        Button applyButton = view.findViewById(R.id.apply_button);

        // Set initial states
        nightBusCheckbox.setChecked(nightBusOnly);
        expressBusCheckbox.setChecked(expressBusOnly);
        airportBusCheckbox.setChecked(airportBusOnly);

        cancelButton.setOnClickListener(v -> dismiss());
        applyButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onFilterApplied(
                    nightBusCheckbox.isChecked(),
                    expressBusCheckbox.isChecked(),
                    airportBusCheckbox.isChecked()
                );
            }
            dismiss();
        });

        builder.setView(view);
        return builder.create();
    }
} 