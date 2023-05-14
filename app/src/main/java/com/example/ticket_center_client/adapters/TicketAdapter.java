package com.example.ticket_center_client.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ticket_center_client.R;
import com.example.ticket_center_client.listeners.TicketClickListener;
import com.example.ticket_center_client.views.distributor.events.DistributorTicketKindFragment;

import tuvarna.ticket_center_common.models.TicketKindModel;
import tuvarna.ticket_center_common.models.TicketModel;

public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.MyViewHolder> {

    private TicketKindModel ticketKindModel;

    private TicketClickListener listener;

    private boolean supportModify = true;

    @NonNull
    @Override
    public TicketAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TicketAdapter.MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_ticket, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TicketAdapter.MyViewHolder holder, int position) {

        ImageView avatar = holder.itemView.findViewById(R.id.ivIcon);
        avatar.setImageResource(R.drawable.ticket_24);

        TextView tvName = holder.itemView.findViewById(R.id.tvNote);
        tvName.setText(ticketKindModel.getTickets().get(position).getNote());

        ImageButton btnDelete = holder.itemView.findViewById(R.id.ivDelete);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ticketKindModel.getTickets().remove(position);
                notifyDataSetChanged();
            }
        });

        if(!supportModify){
            btnDelete.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return ticketKindModel.getTickets().size();
    }

    public void setTicketKindModel(TicketKindModel ticketKindModel) {
        this.ticketKindModel = ticketKindModel;
        notifyDataSetChanged();
    }

    public void setListener(TicketClickListener listener) {
        this.listener = listener;
    }

    public boolean isSupportModify() {
        return supportModify;
    }

    public void setSupportModify(boolean supportModify) {
        this.supportModify = supportModify;
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public MyViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            final int position = getAdapterPosition();
            TicketModel model = ticketKindModel.getTickets().get(position);
            if(listener != null)
                listener.onClick(model);
        }
    }
}