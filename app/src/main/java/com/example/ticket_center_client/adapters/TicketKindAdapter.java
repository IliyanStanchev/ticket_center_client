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
import com.example.ticket_center_client.listeners.TicketKindClickListener;

import java.util.ArrayList;

import tuvarna.ticket_center_common.models.EventModel;
import tuvarna.ticket_center_common.models.TicketKindModel;

public class TicketKindAdapter extends RecyclerView.Adapter<TicketKindAdapter.MyViewHolder> {

    private EventModel event;
    private TicketKindClickListener ticketKindClickListener;

    private boolean supportModify = true;

    @NonNull
    @Override
    public TicketKindAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TicketKindAdapter.MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_ticket_kind, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TicketKindAdapter.MyViewHolder holder, int position) {

        ImageView avatar = holder.itemView.findViewById(R.id.ivIcon);
        avatar.setImageResource(R.drawable.ticket_24);

        ArrayList<TicketKindModel> ticketKinds = event.getTicketKinds();

        TextView tvName = holder.itemView.findViewById(R.id.tvTicketKind);
        tvName.setText(ticketKinds.get(position).getName());

        TextView tvPrice = holder.itemView.findViewById(R.id.tvPrice);
        tvPrice.setText(String.valueOf(ticketKinds.get(position).getPrice()));
        tvPrice.setTextSize(30);

        ImageButton btnDelete = holder.itemView.findViewById(R.id.ivDelete);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                event.getTicketKinds().remove(position);
                notifyDataSetChanged();
            }
        });

        ImageButton btnEdit = holder.itemView.findViewById(R.id.ivEdit);
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ticketKindClickListener.onEdit(ticketKinds.get(position));
                notifyDataSetChanged();
            }
        });

        if(!supportModify){
            btnDelete.setVisibility(View.GONE);
            btnEdit.setVisibility(View.GONE);
        }
    }

    public void setSupportModify(boolean supportModify) {
        this.supportModify = supportModify;
    }

    @Override
    public int getItemCount() {
        return event.getTicketKinds().size();
    }

    public void setClickListener(TicketKindClickListener clickListener) {
        this.ticketKindClickListener = clickListener;
    }

    public void setEvent(EventModel event) {
        this.event = event;
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
            TicketKindModel model = event.getTicketKinds().get(position);
            ticketKindClickListener.onClick(model);
        }
    }
}
