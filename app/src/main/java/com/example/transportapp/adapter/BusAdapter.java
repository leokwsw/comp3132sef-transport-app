package com.example.transportapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.transportapp.R;
import com.example.transportapp.model.kmb.RouteListResponse;

import java.util.List;

public class BusAdapter extends RecyclerView.Adapter<BusAdapter.ViewHolder> {
    private List<RouteListResponse.Route> routes;
    private BusAdapterCallback callback;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvNumber, tvRouteName, tvStops;
        public View itemView;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            tvNumber = itemView.findViewById(R.id.tvNumber);
            tvRouteName = itemView.findViewById(R.id.tvRouteName);
            tvStops = itemView.findViewById(R.id.tvStops);
        }
    }

    public BusAdapter(List<RouteListResponse.Route> routes, BusAdapterCallback callback) {
        this.routes = routes;
        this.callback = callback;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.bus_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        RouteListResponse.Route route = routes.get(position);
        holder.tvNumber.setText(route.route);
        holder.tvRouteName.setText(route.orig_en + " → " + route.dest_en);
        holder.tvStops.setText("");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onClick(route);
            }
        });
    }

    @Override
    public int getItemCount() {
        return routes.size();
    }

    public void updateData(List<RouteListResponse.Route> newRoutes) {
        routes = newRoutes;
        notifyItemRangeChanged(0, newRoutes.size());
    }
}
