package com.example.transportapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.transportapp.R;

import com.example.transportapp.model.view.BookmarkModel;


import java.util.List;

public class BookmarkAdapter extends RecyclerView.Adapter<BookmarkAdapter.BookmarkViewHolder> {

    private List<BookmarkModel> bookmarks;
    private Context context;
    private BusAdapterCallback callback;

    public BookmarkAdapter(List<BookmarkModel> bookmarks, Context context, BusAdapterCallback callback) {
        this.bookmarks = bookmarks;
        this.context = context;
        this.callback = callback;
    }

    @NonNull
    @Override
    public BookmarkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bookmark_item, parent, false);
        return new BookmarkViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookmarkViewHolder holder, int position) {
        BookmarkModel bookmark = bookmarks.get(position);
        holder.tvNumber.setText(bookmark.getRouteId());
        holder.tvRouteName.setText(bookmark.getRouteName());
        holder.tvStops.setText(bookmark.getStops()); // This should now work

        // Set content descriptions for accessibility
        holder.tvNumber.setContentDescription("Bus Number: " + bookmark.getRouteId());
        holder.tvRouteName.setContentDescription("Route Name: " + bookmark.getRouteName());
        holder.tvStops.setContentDescription("Bus Stop: " + bookmark.getStops());
        holder.view.setOnClickListener(v -> {
            if(callback != null){
                callback.onClick(bookmark.getRouteId(), bookmark.getServiceType(), bookmark.getDirection());
            }
        });
    }

    @Override
    public int getItemCount() {
        return bookmarks.size();
    }

    public static class BookmarkViewHolder extends RecyclerView.ViewHolder {
        public View view;
        public TextView tvNumber, tvRouteName, tvStops;

        public BookmarkViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            tvNumber = itemView.findViewById(R.id.tvNumber);
            tvRouteName = itemView.findViewById(R.id.tvRouteName);
            tvStops = itemView.findViewById(R.id.tvStops);
        }
    }
}