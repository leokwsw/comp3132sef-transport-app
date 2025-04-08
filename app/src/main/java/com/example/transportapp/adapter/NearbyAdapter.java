package com.example.transportapp.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.transportapp.R;
import com.example.transportapp.model.view.NearbyItemModel;
import com.example.transportapp.utils.BookmarkManager;
import com.example.transportapp.utils.Time;

import java.util.ArrayList;
import java.util.List;

public class NearbyAdapter extends RecyclerView.Adapter<NearbyAdapter.ItemViewHolder> {

    private List<NearbyItemModel> nearbyItemModels = new ArrayList<>();
    private BusAdapterCallback callback;


    public void setModels(@NonNull List<NearbyItemModel> nearbyItemModels) {
        this.nearbyItemModels.clear();
        this.nearbyItemModels.addAll(nearbyItemModels);
        notifyDataSetChanged();
    }

    public void setCallback(BusAdapterCallback callback) {
        this.callback = callback;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemViewHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.route_item, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        NearbyItemModel nearbyItemModel = nearbyItemModels.get(position);

        holder.tvRouteNum.setText(nearbyItemModel.stopETAData.route);
        holder.tvDest.setText(String.format("To : %s", nearbyItemModel.stopETAData.dest_en));

        long time = Time.getMinutesDifference(nearbyItemModel.stopETAData.eta);
        ArrayList<String> etaText = new ArrayList<>();
        if (time == -1) {
            etaText.add("");
        } else {
            etaText.add(String.format("%s Min", time));
        }
        holder.tvEtaTime.setText(String.join("\n", etaText));

        holder.tvStop.setText(String.format("Current : %s", nearbyItemModel.stop.name_en));

        holder.itemView.setOnClickListener(v -> {
            if (callback != null) {

                callback.onClick(nearbyItemModel.stopETAData.route, nearbyItemModel.stopETAData.service_type, nearbyItemModel.stopETAData.dir);
            }
        });

    }

    @Override
    public int getItemCount() {
        return nearbyItemModels.size();
    }


    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        public View itemView;
        private AppCompatTextView tvRouteNum, tvDest, tvStop, tvEtaTime;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            tvRouteNum = itemView.findViewById(R.id.route_name);
            tvDest = itemView.findViewById(R.id.destination);
            tvStop = itemView.findViewById(R.id.current_stop);
            tvEtaTime = itemView.findViewById(R.id.eta_time);
        }
    }
}
