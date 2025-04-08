package com.example.transportapp.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.transportapp.R;
import com.example.transportapp.model.kmb.StopETAData;
import com.example.transportapp.model.view.BookmarkStopModel;
import com.example.transportapp.model.view.BusStopModel;
import com.example.transportapp.utils.Time;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DetailsAdapter extends RecyclerView.Adapter<DetailsAdapter.DetailsItemHolder> {

    private List<BusStopModel> items = new ArrayList<>();
    private List<BusStopModel> filteredItems = new ArrayList<>();
    private Set<BookmarkStopModel> bookmarkedStops = new HashSet<>();
    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "BookmarksPrefs";
    private static final String BOOKMARKS_KEY = "bookmarked_stops";

    public DetailsAdapter(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        loadBookmarkedStops();
    }

    public void setItems(List<BusStopModel> items) {
        this.items = items;
        this.filteredItems = new ArrayList<>(items);
        notifyItemRangeChanged(0, items.size());
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
            for (StopETAData etaData : stop.etaData) {
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
        BusStopModel busStop = filteredItems.get(position);
        holder.tvStopName.setText(busStop.stopData.name_en);
        ArrayList<String> etaText = new ArrayList<>();
        for (StopETAData etaData : busStop.etaData) {
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

        // Create a new BookmarkStopModel instance for the current bus stop
        BookmarkStopModel bookmark = new BookmarkStopModel(busStop.stopData.stop, busStop.stopData.name_en);

        // Set bookmark button click listener
        holder.ivBtnBookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bookmarkedStops.contains(bookmark)) {
                    bookmarkedStops.remove(bookmark);
                    holder.ivBtnBookmark.setImageResource(R.drawable.ic_bookmark_outline);
                } else {
                    bookmarkedStops.add(bookmark);
                    holder.ivBtnBookmark.setImageResource(R.drawable.ic_bookmark_filled);
                }
                saveBookmarkedStops();
            }
        });

        // Check if the stop is already bookmarked
        if (bookmarkedStops.contains(bookmark)) {
            holder.ivBtnBookmark.setImageResource(R.drawable.ic_bookmark_filled);
        } else {
            holder.ivBtnBookmark.setImageResource(R.drawable.ic_bookmark_outline);
        }
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

    private void saveBookmarkedStops() {
        Gson gson = new Gson();
        String json = gson.toJson(bookmarkedStops);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(BOOKMARKS_KEY, json);
        editor.apply();
    }

    private void loadBookmarkedStops() {
        Gson gson = new Gson();
        String json = sharedPreferences.getString(BOOKMARKS_KEY, null);
        Type type = new TypeToken<Set<BookmarkStopModel>>() {}.getType();
        bookmarkedStops = gson.fromJson(json, type);
        if (bookmarkedStops == null) {
            bookmarkedStops = new HashSet<>();
        }
    }
}