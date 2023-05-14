package com.example.ticket_center_client.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ticket_center_client.R;
import com.example.ticket_center_client.listeners.EventClickListener;

import java.util.Collections;
import java.util.List;

import tuvarna.ticket_center_common.models.EventModel;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.MyViewHolder> {

    private List<EventModel> events = Collections.emptyList();

    private EventClickListener eventClickListener;

    @NonNull
    @Override
    public EventAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new EventAdapter.MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.event_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull EventAdapter.MyViewHolder holder, int position) {

        EventModel event = events.get(position);

        TextView tvName = holder.itemView.findViewById(R.id.tvName);
        tvName.setText(event.getName());

        TextView tvLocation = holder.itemView.findViewById(R.id.tvLocation);
        tvLocation.setText(event.getLocation());

        TextView tvStartDate = holder.itemView.findViewById(R.id.tvStartDate);
        tvStartDate.setText(event.getStartDate() + " " + event.getStartTime());
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public void setOnViewClickListener(EventClickListener listener) {
        this.eventClickListener = listener;
    }

    public void setEvents(List<EventModel> events) {
        this.events = events;
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
            EventModel model = events.get(position);
            eventClickListener.onClick(model);
        }
    }
}
