package com.example.transportapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BusAdapter extends RecyclerView.Adapter<BusAdapter.ViewHolder> {
    private List<BusRoute> routes;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvNumber, tvRouteName, tvStops;

        public ViewHolder(View itemView) {
            super(itemView);
            tvNumber = itemView.findViewById(R.id.tvNumber);
            tvRouteName = itemView.findViewById(R.id.tvRouteName);
            tvStops = itemView.findViewById(R.id.tvStops);
        }
    }

    public BusAdapter(List<BusRoute> routes) {
        this.routes = routes;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.bus_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        BusRoute route = routes.get(position);
        holder.tvNumber.setText(route.getNumber());
        holder.tvRouteName.setText(route.getRouteName());
        holder.tvStops.setText(route.getStops());
    }

    @Override
    public int getItemCount() {
        return routes.size();
    }

    public void updateData(List<BusRoute> newRoutes) {
        routes = newRoutes;
        notifyDataSetChanged();
    }
}
