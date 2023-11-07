package com.example.coursework_1786;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coursework_1786.models.Hike;

import java.util.List;

public class HikeAdapter extends RecyclerView.Adapter<HikeAdapter.ViewHolder> {

    private List<Hike> hikes;
    private OnItemClickListener listener;
    private boolean showButtons; // Flag to determine whether to show buttons or not

    public HikeAdapter(List<Hike> hikes, OnItemClickListener listener, boolean showButtons) {
        this.hikes = hikes;
        this.listener = listener;
        this.showButtons = showButtons;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (showButtons) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_hike, parent, false);
        }
        else{
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_search, parent, false);
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Hike hike = hikes.get(position);
        holder.hikeName.setText(hike.name);

        if (showButtons) {
            holder.moreButton.setOnClickListener(v -> listener.onMoreButtonClick(hike));
            holder.deleteButton.setOnClickListener(v -> listener.onDeleteButtonClick(hike));
        } else {
//            // Hide the buttons by setting their visibility to GONE
//            holder.moreButton.setVisibility(View.GONE);
//            holder.deleteButton.setVisibility(View.GONE);
        }

        holder.hikeName.setOnClickListener(v -> listener.onHikeNameClick(hike));
    }

    @Override
    public int getItemCount() {
        return hikes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView hikeName;
        Button moreButton;
        Button deleteButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            hikeName = itemView.findViewById(R.id.hikeName);
            if (showButtons) {
                moreButton = itemView.findViewById(R.id.moreButton);
                deleteButton = itemView.findViewById(R.id.deleteAllHikeButton);
            }
            else{

            }
        }
    }

    public interface OnItemClickListener {
        void onMoreButtonClick(Hike hike);
        void onDeleteButtonClick(Hike hike);
        void onHikeNameClick(Hike hike);
    }
}