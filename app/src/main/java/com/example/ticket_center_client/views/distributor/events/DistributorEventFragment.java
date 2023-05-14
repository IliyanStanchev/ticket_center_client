package com.example.ticket_center_client.views.distributor.events;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ticket_center_client.R;
import com.example.ticket_center_client.adapters.TicketKindAdapter;
import com.example.ticket_center_client.listeners.TicketKindClickListener;
import com.example.ticket_center_client.requests.APICallListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import tuvarna.ticket_center_common.models.EventModel;
import tuvarna.ticket_center_common.models.TicketKindModel;
import tuvarna.ticket_center_common.responses.CommonResponse;

public class DistributorEventFragment extends Fragment implements APICallListener, TicketKindClickListener {

    private EventModel eventModel;

    private TicketKindAdapter ticketKindsAdapter = new TicketKindAdapter();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (container != null) {
            container.removeAllViews();
        }

        View view = inflater.inflate(R.layout.fragment_distributor_event, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.rvTicketKinds);

        eventModel = (EventModel) getArguments().getSerializable("eventModel");
        ticketKindsAdapter.setSupportModify(false);
        ticketKindsAdapter.setEvent(eventModel);
        ticketKindsAdapter.setClickListener(this);

        EditText etEventName = view.findViewById(R.id.etEmail);
        EditText etLocation = view.findViewById(R.id.etLocation);
        EditText etStartDate = view.findViewById(R.id.etStartDate);
        EditText etDescription = view.findViewById(R.id.etDescription);

        etEventName.setText(eventModel.getName());
        etLocation.setText(eventModel.getLocation());
        etStartDate.setText(eventModel.getStartDate());
        etDescription.setText(eventModel.getDescription());

        FloatingActionButton fabBack = view.findViewById(R.id.fabBack);
        fabBack.setOnClickListener(view1 -> {
            DistributorEventsFragment eventFragment = new DistributorEventsFragment();
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_distributor_event_id, eventFragment, "eventFragment")
                    .addToBackStack(null)
                    .commit();
        });

        recyclerView.setAdapter(ticketKindsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return view;
    }

    @Override
    public void onAPICallSuccess(CommonResponse response) {

    }

    @Override
    public void onAPICallFailure() {

    }

    @Override
    public void onClick(TicketKindModel ticketKind) {

        DistributorTicketKindFragment ticketKindFragment = new DistributorTicketKindFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("ticketKindModel", ticketKind);
        bundle.putSerializable("eventModel", eventModel);
        ticketKindFragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_distributor_event_id, ticketKindFragment).commit();

    }

    @Override
    public void onEdit(TicketKindModel ticketKindModel) {

    }
}
