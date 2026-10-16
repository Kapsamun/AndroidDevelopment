package com.mirea.kapsamunelenanikolaevna.celebritybase.presantation.practice4;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mirea.kapsamunelenanikolaevna.celebritybase.R;
import com.mirea.kapsamunelenanikolaevna.celebritybase.domain.models.Celebrity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CelebrityAdapter extends RecyclerView.Adapter<CelebrityAdapter.CelebrityViewHolder> {

    private List<Celebrity> items = new ArrayList<>();
    private Set<Integer> favoriteIds = new HashSet<>();
    private OnItemClickListener listener;
    private OnFavoriteClickListener favoriteListener;

    public interface OnItemClickListener {
        void onItemClick(Celebrity celebrity);
    }

    public interface OnFavoriteClickListener {
        void onFavoriteClick(Celebrity celebrity, boolean isFavorite);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setOnFavoriteClickListener(OnFavoriteClickListener listener) {
        this.favoriteListener = listener;
    }

    public void setItems(List<Celebrity> newItems) {
        this.items = newItems != null ? newItems : new ArrayList<>();
        notifyDataSetChanged();
    }

    public void setFavoriteIds(Set<Integer> ids) {
        this.favoriteIds = ids != null ? ids : new HashSet<>();
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
        private final ImageButton buttonFavorite;

        public CelebrityViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewPhoto = itemView.findViewById(R.id.imageViewPhoto);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewBio = itemView.findViewById(R.id.textViewBio);
            buttonFavorite = itemView.findViewById(R.id.buttonFavorite);
        }

        public void bind(Celebrity celebrity) {
            textViewName.setText(celebrity.getName());
            textViewBio.setText(celebrity.getBiography() != null ? celebrity.getBiography() : "");

            String url = celebrity.getPhotoUrl();
            if (url == null || url.isEmpty()) {
                imageViewPhoto.setImageResource(R.drawable.ic_launcher_foreground);
            } else {
                Picasso.get()
                        .load(url)
                        .placeholder(R.drawable.ic_launcher_foreground)
                        .error(R.drawable.ic_launcher_foreground)
                        .resize(200, 200)
                        .centerCrop()
                        .into(imageViewPhoto);
            }

            boolean isFav = favoriteIds.contains(celebrity.getId());
            buttonFavorite.setImageResource(isFav
                    ? android.R.drawable.btn_star_big_on
                    : android.R.drawable.btn_star_big_off);

            itemView.setOnClickListener(v -> {
                if (listener != null) listener.onItemClick(celebrity);
            });

            buttonFavorite.setOnClickListener(v -> {
                if (favoriteListener != null) {
                    favoriteListener.onFavoriteClick(celebrity, isFav);
                }
            });
        }
    }
}