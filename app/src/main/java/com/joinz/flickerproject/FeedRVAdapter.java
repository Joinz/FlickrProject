package com.joinz.flickerproject;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class FeedRVAdapter extends RecyclerView.Adapter<FeedRVAdapter.ViewHolder> {
    private List<String> titles;

    public FeedRVAdapter(List<String> titles) {
        this.titles = titles;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_view, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        if (position != RecyclerView.NO_POSITION) {
            viewHolder.tv.setText(titles.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return titles.size();
    }

    public void setTitles(List<String> titles) {
        this.titles = titles;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv;

        public ViewHolder(View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.tvItem);
        }


    }
}
