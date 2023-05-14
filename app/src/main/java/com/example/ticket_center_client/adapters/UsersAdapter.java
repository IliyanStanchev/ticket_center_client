package com.example.ticket_center_client.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ticket_center_client.R;
import com.example.ticket_center_client.listeners.UserClickListener;

import java.util.Collections;
import java.util.List;

import tuvarna.ticket_center_common.models.UserModel;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.MyViewHolder> {

    private List<UserModel> users = Collections.emptyList();
    private UserClickListener userClickListener;

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_user, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        ImageView avatar = holder.itemView.findViewById(R.id.ivIcon);
        avatar.setImageResource(R.mipmap.ic_user);

        TextView tvName = holder.itemView.findViewById(R.id.tvUserName);
        tvName.setText(users.get(position).getName());
        tvName.setTextSize(20);

        TextView tvMail = holder.itemView.findViewById(R.id.tvMail);
        tvMail.setText(users.get(position).getEmail());
        tvMail.setTextSize(20);

        TextView tvRole = holder.itemView.findViewById(R.id.tvRole);
        tvRole.setText(users.get(position).getRole().toString());
        tvRole.setTextSize(20);

        TextView tvStatus = holder.itemView.findViewById(R.id.tvStatus);
        tvStatus.setText(users.get(position).getStatus().toString());
        tvStatus.setTextSize(20);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public void setOnNewClickListener(UserClickListener userClickListener) {
        this.userClickListener = userClickListener;
    }

    public void setUsers(List<UserModel> users) {
        this.users = users;
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
            UserModel user = users.get(position);
            userClickListener.onClick(user);
        }
    }
}