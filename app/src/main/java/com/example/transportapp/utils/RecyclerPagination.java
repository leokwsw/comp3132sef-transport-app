package com.example.transportapp.utils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public abstract class RecyclerPagination extends RecyclerView.OnScrollListener {

    private final LinearLayoutManager layoutManager;

    public boolean isLastPage = false; // Set is last page
    public boolean isLoading = false;  // Set is loading

    public RecyclerPagination(LinearLayoutManager layoutManager) {
        this.layoutManager = layoutManager;
    }

    protected abstract void loadMoreItems();

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

        if (!isLoading && !isLastPage) {
            if (layoutManager.getChildCount() + firstVisibleItemPosition >= layoutManager.getItemCount()
                    && firstVisibleItemPosition >= 0) {
                isLastPage = false;
                isLoading = true;
                loadMoreItems();
            }
        }
    }
}

