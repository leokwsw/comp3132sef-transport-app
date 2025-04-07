package com.example.transportapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.transportapp.adapter.BookmarkAdapter;
import com.example.transportapp.model.view.BookmarkModel;
import com.example.transportapp.utils.BookmarkManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BookmarkActivity extends AppCompatActivity {

    private RecyclerView bookmarksRecyclerView;
    private BookmarkAdapter bookmarkAdapter;
    private BookmarkManager bookmarkManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EdgeToEdge.enable(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);

        bookmarkManager = new BookmarkManager(this);

        // Setup RecyclerView
        bookmarksRecyclerView = findViewById(R.id.bookmarks_list);
        bookmarksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        bookmarkAdapter = new BookmarkAdapter(new ArrayList<>(), this, (String route, String serviceType, String bound) -> {
            Intent intent = new Intent(this, DetailsActivity.class);
            intent.putExtra(DetailsActivity.ROUTE_KEY, route);
            intent.putExtra(DetailsActivity.SERVICE_TYPE_KEY, serviceType);
            intent.putExtra(DetailsActivity.DIRECTION_KEY, Objects.equals(bound, "O") ? DetailsActivity.DIRECTION_OUTBOUND : DetailsActivity.DIRECTION_INBOUND);
            startActivity(intent);
        });
        bookmarksRecyclerView.setAdapter(bookmarkAdapter);

        // Load bookmarks
        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> onBackPressed());

        loadBookmarks();
    }

    private void loadBookmarks() {
        List<BookmarkModel> bookmarks = bookmarkManager.getBookmarks();
        bookmarkAdapter = new BookmarkAdapter(bookmarks, this, (String route, String serviceType, String bound) -> {
            Intent intent = new Intent(this, DetailsActivity.class);
            intent.putExtra(DetailsActivity.ROUTE_KEY, route);
            intent.putExtra(DetailsActivity.SERVICE_TYPE_KEY, serviceType);
            intent.putExtra(DetailsActivity.DIRECTION_KEY, Objects.equals(bound, "O") ? DetailsActivity.DIRECTION_OUTBOUND : DetailsActivity.DIRECTION_INBOUND);
            startActivity(intent);
        });
        bookmarksRecyclerView.setAdapter(bookmarkAdapter);
    }
}