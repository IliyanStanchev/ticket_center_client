package com.example.ticket_center_client.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ticket_center_client.R;

import java.util.Collections;
import java.util.List;

import tuvarna.ticket_center_common.models.RatingModel;

public class RatingAdapter extends RecyclerView.Adapter<RatingAdapter.MyViewHolder> {

    private List<RatingModel> ratings = Collections.emptyList();

    @NonNull
    @Override
    public RatingAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RatingAdapter.MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.rating_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RatingAdapter.MyViewHolder holder, int position) {

        TextView tvName = holder.itemView.findViewById(R.id.tvName);
        tvName.setText(ratings.get(position).getRatingUserName());
        tvName.setTextSize(22);
        tvName.setFocusable(false);

        RatingBar ratingBar = holder.itemView.findViewById(R.id.ratingBar);
        ratingBar.setRating(ratings.get(position).getRating());
        ratingBar.setIsIndicator(false);

        TextView tvComment = holder.itemView.findViewById(R.id.etComment);
        tvComment.setText(ratings.get(position).getComment());
        tvComment.setFocusable(false);
    }

    @Override
    public int getItemCount() {
        return ratings.size();
    }

    public void setRatings(List<RatingModel> ratings) {
        this.ratings = ratings;
        notifyDataSetChanged();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        public MyViewHolder(View itemView) {
            super(itemView);
        }
    }
}
