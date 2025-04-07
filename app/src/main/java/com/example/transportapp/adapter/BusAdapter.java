package com.example.transportapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.transportapp.R;
import com.example.transportapp.model.view.BookmarkModel;
import com.example.transportapp.model.kmb.RouteListResponse;
import com.example.transportapp.utils.BookmarkManager;

import java.util.List;

public class BusAdapter extends RecyclerView.Adapter<BusAdapter.ViewHolder> {

    private List<RouteListResponse.Route> routes;
    private BusAdapterCallback callback;
    private BookmarkManager bookmarkManager;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvNumber, tvRouteName, tvStops;
        public ImageButton btnBookmark;

        public ViewHolder(View itemView) {
            super(itemView);
            tvNumber = itemView.findViewById(R.id.tvNumber);
            tvRouteName = itemView.findViewById(R.id.tvRouteName);
            tvStops = itemView.findViewById(R.id.tvStops);
            btnBookmark = itemView.findViewById(R.id.btnBookmark);
        }
    }

    public BusAdapter(List<RouteListResponse.Route> routes, BusAdapterCallback callback, Context context) {
        this.routes = routes;
        this.callback = callback;
        this.bookmarkManager = new BookmarkManager(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.bus_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RouteListResponse.Route route = routes.get(position);
        holder.tvNumber.setText(route.route);
        holder.tvRouteName.setText(route.orig_en + " → " + route.dest_en);
        holder.tvStops.setText("");

        // Convert Route to BookmarkModel
        BookmarkModel bookmark = new BookmarkModel(
                route.route,
                route.orig_en + " → " + route.dest_en,
                route.bound,
                route.service_type,
                "" // Assuming stops information is not available in Route, you can adjust this
        );

        // Set bookmark button click listener
        holder.btnBookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bookmarkManager.isBookmarked(route.route)) {
                    bookmarkManager.removeBookmark(route.route);
                    holder.btnBookmark.setImageResource(R.drawable.ic_bookmark_outline);
                } else {
                    bookmarkManager.addBookmark(bookmark);
                    holder.btnBookmark.setImageResource(R.drawable.ic_bookmark_filled);
                }
            }
        });

        // Check if the route is already bookmarked
        if (bookmarkManager.isBookmarked(route.route)) {
            holder.btnBookmark.setImageResource(R.drawable.ic_bookmark_filled);
        } else {
            holder.btnBookmark.setImageResource(R.drawable.ic_bookmark_outline);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callback != null) {
                    callback.onClick(route.route, route.service_type, route.bound);
                }
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