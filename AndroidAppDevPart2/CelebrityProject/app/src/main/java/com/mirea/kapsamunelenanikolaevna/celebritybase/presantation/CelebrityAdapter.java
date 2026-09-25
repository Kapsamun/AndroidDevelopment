package com.mirea.kapsamunelenanikolaevna.celebritybase.presantation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mirea.kapsamunelenanikolaevna.celebritybase.R;
import com.mirea.kapsamunelenanikolaevna.celebritybase.domain.models.Celebrity;

import java.util.ArrayList;
import java.util.List;

public class CelebrityAdapter extends RecyclerView.Adapter<CelebrityAdapter.CelebrityViewHolder> {

    private List<Celebrity> items = new ArrayList<>();
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Celebrity celebrity);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setItems(List<Celebrity> newItems) {
        this.items = newItems != null ? newItems : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CelebrityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_celebrity, parent, false);
        return new CelebrityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CelebrityViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class CelebrityViewHolder extends RecyclerView.ViewHolder {

        private final ImageView imageViewPhoto;
        private final TextView textViewName;
        private final TextView textViewBio;

        public CelebrityViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewPhoto = itemView.findViewById(R.id.imageViewPhoto);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewBio = itemView.findViewById(R.id.textViewBio);
        }

        public void bind(Celebrity celebrity) {
            textViewName.setText(celebrity.getName());
            textViewBio.setText(celebrity.getBiography());

            // Загрузка фото через Glide по URL из Retrofit/заглушки
            Glide.with(itemView.getContext())
                    .load(celebrity.getPhotoUrl())
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .error(R.drawable.ic_launcher_foreground)
                    .centerCrop()
                    .into(imageViewPhoto);

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemClick(celebrity);
                }
            });
        }
    }
}