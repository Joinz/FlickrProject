package com.joinz.flickerproject.feed;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.joinz.flickerproject.R;
import com.joinz.flickerproject.model.PhotoItem;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.annotations.NonNull;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.ViewHolder> {
    private List<PhotoItem> photos;
    private OnPhotoClickListener listener;

    public FeedAdapter(List<PhotoItem> photos, OnPhotoClickListener listener) {
        this.photos = photos;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_view, viewGroup, false);

        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.itemView.setOnClickListener(v -> {
            int position = viewHolder.getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(photos.get(position));
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        if (position != RecyclerView.NO_POSITION) {
            viewHolder.tv.setText(photos.get(position).getTitle());
            viewHolder.setImage(getUrl(photos.get(position)));
        }
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }

    public void setData(List<PhotoItem> photos, OnPhotoClickListener listener) {
        this.photos = photos;
        this.listener = listener;
        notifyDataSetChanged();
    }

    private String getUrl(PhotoItem photoItem) {
        return String.format(
                "https://farm%s.staticflickr.com/%s/%s_%s_%s.jpg",
                photoItem.getFarm(),
                photoItem.getServer(),
                photoItem.getId(),
                photoItem.getSecret(),
                "n"
        );
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView iv;
        private TextView tv;

        public ViewHolder(View itemView) {
            super(itemView);
            iv = itemView.findViewById(R.id.ivItem);
            tv = itemView.findViewById(R.id.tvItem);
        }

        public void setImage(String uri) {
            Glide.with(itemView).load(uri).into(iv);
        }
    }
}
