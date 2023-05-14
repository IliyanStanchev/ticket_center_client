package com.example.ticket_center_client.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ticket_center_client.R;
import com.example.ticket_center_client.listeners.DistributorClickListener;

import java.util.Collections;
import java.util.List;

import tuvarna.ticket_center_common.models.DistributorModel;

public class DistributorAdapter extends RecyclerView.Adapter<DistributorAdapter.MyViewHolder> {

    private List<DistributorModel> distributors = Collections.emptyList();

    private DistributorClickListener distributorClickListener;

    @NonNull
    @Override
    public DistributorAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DistributorAdapter.MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.distributor_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DistributorAdapter.MyViewHolder holder, int position) {

        ImageView avatar = holder.itemView.findViewById(R.id.ivIcon);
        avatar.setImageResource(R.mipmap.ic_user);

        TextView tvName = holder.itemView.findViewById(R.id.tvName);
        tvName.setText(distributors.get(position).getFirstName() + " " + distributors.get(position).getLastName());
        tvName.setTextSize(22);

        TextView tvPhoneNumber = holder.itemView.findViewById(R.id.tvPhoneNumber);
        tvPhoneNumber.setText(distributors.get(position).getPhoneNumber());
        tvPhoneNumber.setTextSize(22);

        TextView tvMail = holder.itemView.findViewById(R.id.tvHonorarium);
        tvMail.setText(String.valueOf(distributors.get(position).getEmail()));
        tvMail.setTextSize(22);

        RatingBar ratingBar = holder.itemView.findViewById(R.id.ratingBar);
        ratingBar.setRating(distributors.get(position).getRating());
        ratingBar.setIsIndicator(false);
    }

    @Override
    public int getItemCount() {
        return distributors.size();
    }

    public void setOnViewClickListener(DistributorClickListener listener) {
        this.distributorClickListener = listener;
    }

    public void setUsers(List<DistributorModel> distributors) {
        this.distributors = distributors;
        notifyDataSetChanged();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public MyViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            final int position = getAdapterPosition();
            DistributorModel distributor = distributors.get(position);
            if(distributorClickListener != null){
                distributorClickListener.onClick(distributor);
            }
        }
    }
}
