package com.mirea.kapsamunelenanikolaevna.recyclerviewapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    private final List<HistoricalEvent> events;

    public EventAdapter(List<HistoricalEvent> events) {
        this.events = events;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_item, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        HistoricalEvent event = events.get(position);

        holder.title.setText(event.getTitle());
        holder.description.setText(event.getDescription());

        // Пункт 1.3 — загрузка картинки по имени drawable
        String pkgName = holder.itemView.getContext().getPackageName();
        int resId = holder.itemView.getContext()
                .getResources()
                .getIdentifier(event.getImageName(), "drawable", pkgName);

        if (resId != 0) {
            holder.image.setImageResource(resId);
        } else {
            holder.image.setImageResource(R.drawable.ic_launcher_foreground);
        }
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    static class EventViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title;
        TextView description;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.imageView);
            title = itemView.findViewById(R.id.textViewTitle);
            description = itemView.findViewById(R.id.textViewDescription);
        }
    }
}