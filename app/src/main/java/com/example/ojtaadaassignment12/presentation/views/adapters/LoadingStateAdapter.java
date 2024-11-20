package com.example.ojtaadaassignment12.presentation.views.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.paging.LoadState;
import androidx.paging.LoadStateAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ojtaadaassignment12.R;

public class LoadingStateAdapter extends LoadStateAdapter<LoadingStateAdapter.LoadingViewHolder> {

    @NonNull
    @Override
    public LoadingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, @NonNull LoadState loadState) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
        return new LoadingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LoadingViewHolder holder, @NonNull LoadState loadState) {
        // Show or hide progress bar based on the load state
        holder.progressBar.setVisibility(loadState instanceof LoadState.Loading ? View.VISIBLE : View.GONE);
    }


    public static class LoadingViewHolder extends RecyclerView.ViewHolder {
        ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.loading_more_progress_bar);
        }
    }
}
