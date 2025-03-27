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

    public void setItems(List<BusStopModel> items) {
        this.items = items;
        notifyItemChanged(0, items.size());
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
        holder.tvStopName.setText(items.get(position).stopData.name_en);
        ArrayList<String> etaText = new ArrayList<>();
        for (StopETAResponse.StopETAData etaData : items.get(position).etaData) {
            long time = Time.getMinutesDifference(etaData.eta);
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
        holder.tvEtaTime.setText(String.join("\n", etaText));
    }

    @Override
    public int getItemCount() {
        return items.size();
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
