package com.example.transportapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.transportapp.R;
import com.example.transportapp.model.kmb.StopETAResponse;
import com.example.transportapp.model.view.BusStopModel;
import com.example.transportapp.model.view.DetailsItemModel;
import com.example.transportapp.utils.Time;

import java.util.ArrayList;
import java.util.List;

public class DetailsAdapter extends RecyclerView.Adapter<DetailsAdapter.DetailsItemHolder> {

    private List<BusStopModel> items = new ArrayList<>();

    private List<BusStopModel> filteredItems = new ArrayList<>();

    public void setItems(List<BusStopModel> items) {
        this.items = items;
        this.filteredItems = new ArrayList<>(items);
        notifyItemChanged(0, items.size());
    }

    public void searchStops(String query) {
        filteredItems.clear();
        if (query.isEmpty()) {
            filteredItems.addAll(items);
        } else {
            String lowercaseQuery = query.toLowerCase();
            for (BusStopModel stop : items) {
                if (stop.stopData.name_en.toLowerCase().contains(lowercaseQuery)) {
                    filteredItems.add(stop);
                }
            }
        }
        notifyDataSetChanged();
    }

    public void filterByETARange(int minMinutes, int maxMinutes) {
        filteredItems.clear();
        for (BusStopModel stop : items) {
            boolean hasValidETA = false;
            for (StopETAResponse.StopETAData etaData : stop.etaData) {
                long time = Time.getMinutesDifference(etaData.eta);
                if (time >= minMinutes && time <= maxMinutes) {
                    hasValidETA = true;
                    break;
                }
            }
            if (hasValidETA) {
                filteredItems.add(stop);
            }
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DetailsItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DetailsItemHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.stop_item, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull DetailsItemHolder holder, int position) {
        holder.tvStopName.setText(filteredItems.get(position).stopData.name_en);
        ArrayList<String> etaText = new ArrayList<>();
        for (StopETAResponse.StopETAData etaData : filteredItems.get(position).etaData) {
            long time = Time.getMinutesDifference(etaData.eta);
            if (time == -1) {
                etaText.add("No Scheduled Bus");
            } else {

                String format = "%s ";

                if (time > 1) {
                    format += "Minutes";
                } else {
                    format += "Minute";
                }
                if (!etaData.rmk_en.isEmpty()) {
                    format += " -- %s";
                }
                etaText.add(String.format(format, time, etaData.rmk_en));
            }
        }
        holder.tvEtaTime.setText(String.join("\n", etaText));
    }

    @Override
    public int getItemCount() {
        return filteredItems.size();
    }

    public static class DetailsItemHolder extends RecyclerView.ViewHolder {
        public AppCompatTextView tvStopName;
        public AppCompatTextView tvEtaTime;
        public AppCompatImageButton ivBtnBookmark;


        public DetailsItemHolder(@NonNull View itemView) {
            super(itemView);
            tvStopName = itemView.findViewById(R.id.stop_name);
            tvEtaTime = itemView.findViewById(R.id.eta_time);
            ivBtnBookmark = itemView.findViewById(R.id.stop_bookmark_button);
        }
    }
}
