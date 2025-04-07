package com.example.transportapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.transportapp.R;
import com.example.transportapp.model.view.NearbyItemModel;
import com.example.transportapp.utils.Time;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class NearbyAdapter extends RecyclerView.Adapter<NearbyAdapter.ItemViewHolder> {

    private List<NearbyItemModel> nearbyItemModels = new ArrayList<>();

    public void setModels(List<NearbyItemModel> nearbyItemModels) {
        this.nearbyItemModels = nearbyItemModels;
        notifyDataSetChanged();
    }

    public void addModels(List<NearbyItemModel> nearbyItemModels){
        this.nearbyItemModels.addAll(nearbyItemModels);
        notifyItemInserted(nearbyItemModels.size());
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
        holder.tvDest.setText(nearbyItemModel.stopETAData.dest_en);

        long time = Time.getMinutesDifference(nearbyItemModel.stopETAData.eta);
        ArrayList<String> etaText = new ArrayList<>();
        if (time == -1) {
            etaText.add("");
        } else {
            etaText.add(String.format("%s Min", time));
        }
        holder.tvEtaTime.setText(String.join("\n", etaText));

        holder.tvStop.setText(nearbyItemModel.stop.name_tc);

//        DecimalFormat dec = new DecimalFormat("#0.00");
//        holder.tvDistance.setText(String.format("%s M", dec.format(nearbyItemModel.stop.distance)));
    }

    @Override
    public int getItemCount() {
        return nearbyItemModels.size();
    }


    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        private AppCompatTextView tvRouteNum, tvDest, tvStop, tvEtaTime;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRouteNum = itemView.findViewById(R.id.route_name);
            tvDest = itemView.findViewById(R.id.destination);
            tvStop = itemView.findViewById(R.id.current_stop);
            tvEtaTime = itemView.findViewById(R.id.eta_time);
        }
    }
}
