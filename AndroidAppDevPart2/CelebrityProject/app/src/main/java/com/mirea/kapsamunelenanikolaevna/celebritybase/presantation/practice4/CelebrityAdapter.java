package com.mirea.kapsamunelenanikolaevna.celebritybase.presantation.practice4;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mirea.kapsamunelenanikolaevna.celebritybase.R;
import com.mirea.kapsamunelenanikolaevna.celebritybase.domain.models.Celebrity;

import java.util.ArrayList;
import java.util.List;

public class CelebrityAdapter extends RecyclerView.Adapter<CelebrityAdapter.CelebrityViewHolder> {

    private List<Celebrity> celebrities = new ArrayList<>();

    public void setCelebrities(List<Celebrity> list) {
        this.celebrities = list != null ? list : new ArrayList<>();
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
        Celebrity celebrity = celebrities.get(position);
        holder.name.setText(celebrity.getName());
        holder.bio.setText(celebrity.getBiography());
    }

    @Override
    public int getItemCount() {
        return celebrities.size();
    }

    static class CelebrityViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView bio;

        public CelebrityViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.textViewName);
            bio = itemView.findViewById(R.id.textViewBio);
        }
    }
}