package com.example.transportapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.transportapp.model.view.BookmarkModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class BookmarkManager {
    private static final String PREFS_NAME = "BookmarksPrefs";
    private static final String BOOKMARKS_KEY = "bookmarks";
    private SharedPreferences sharedPreferences;
    private Gson gson;

    public BookmarkManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
    }

    public void addBookmark(BookmarkModel bookmark) {
        List<BookmarkModel> bookmarks = getBookmarks();
        bookmarks.add(bookmark);
        saveBookmarks(bookmarks);
    }

    public void removeBookmark(String routeId) {
        List<BookmarkModel> bookmarks = getBookmarks();
        bookmarks.removeIf(bookmark -> bookmark.getRouteId().equals(routeId));
        saveBookmarks(bookmarks);
    }

    public List<BookmarkModel> getBookmarks() {
        String bookmarksJson = sharedPreferences.getString(BOOKMARKS_KEY, null);
        if (bookmarksJson == null) {
            return new ArrayList<>();
        }
        Type type = new TypeToken<List<BookmarkModel>>() {
        }.getType();
        return gson.fromJson(bookmarksJson, type);
    }

    private void saveBookmarks(List<BookmarkModel> bookmarks) {
        String bookmarksJson = gson.toJson(bookmarks);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(BOOKMARKS_KEY, bookmarksJson);
        editor.apply();
    }

    public boolean isBookmarked(String routeId) {
        List<BookmarkModel> bookmarks = getBookmarks();
        for (BookmarkModel bookmark : bookmarks) {
            if (bookmark.getRouteId().equals(routeId)) {
                return true;
            }
        }
        return false;
    }
}