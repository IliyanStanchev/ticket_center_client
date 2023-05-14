package com.example.ticket_center_client.views.distributor.events;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ticket_center_client.R;
import com.example.ticket_center_client.adapters.TicketAdapter;
import com.example.ticket_center_client.listeners.TicketClickListener;
import com.example.ticket_center_client.requests.APICallListener;
import com.example.ticket_center_client.requests.ResponseDeserializer;
import com.example.ticket_center_client.services.EventService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import tuvarna.ticket_center_common.models.EventModel;
import tuvarna.ticket_center_common.models.TicketKindModel;
import tuvarna.ticket_center_common.models.TicketModel;
import tuvarna.ticket_center_common.models.TicketsWrapper;
import tuvarna.ticket_center_common.requests.RequestCodes;
import tuvarna.ticket_center_common.requests.RequestStatuses;
import tuvarna.ticket_center_common.responses.CommonResponse;

public class DistributorTicketKindFragment extends Fragment implements APICallListener, TicketClickListener {

    private TicketKindModel ticketKindModel;

    private EventModel eventModel;

    private TicketModel currentTicket;

    TicketAdapter ticketAdapter = new TicketAdapter();

    EventService eventService = new EventService(this);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (container != null) {
            container.removeAllViews();
        }

        View view = inflater.inflate(R.layout.fragment_distributor_ticket_kind, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.rvTickets);

        ticketKindModel = (TicketKindModel) getArguments().getSerializable("ticketKindModel");
        eventModel = (EventModel) getArguments().getSerializable("eventModel");

        FloatingActionButton fabBack = view.findViewById(R.id.fabBack);
        fabBack.setOnClickListener(view1 -> {
            DistributorEventFragment eventFragment = new DistributorEventFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("eventModel", eventModel);
            eventFragment.setArguments(bundle);
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_distributor_ticket_kind_id, eventFragment, "eventFragment")
                    .addToBackStack(null)
                    .commit();
        });

        ticketAdapter.setSupportModify(false);
        ticketAdapter.setTicketKindModel(ticketKindModel);
        recyclerView.setAdapter(ticketAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        eventService.getTickets(eventModel.getId(), ticketKindModel.getName());

        return view;
    }

    @Override
    public void onAPICallSuccess(CommonResponse response) {

        if (response.getRequestCode() == RequestCodes.EVENTS_TICKETS_GET) {
            if (response.getRequestStatus() == RequestStatuses.SUCCESS) {

                TicketsWrapper ticketsWrapper = ResponseDeserializer.deserialize(response.getResponse(), TicketsWrapper.class);
                ticketKindModel.setTickets((ArrayList<TicketModel>) ticketsWrapper.getTickets());
                ticketAdapter.setTicketKindModel(ticketKindModel);
                ticketAdapter.notifyDataSetChanged();
                ticketAdapter.setListener(this);
            }
        }

        if (response.getRequestCode() == RequestCodes.EVENTS_TICKETS_CHECK_IN) {
            if (response.getRequestStatus() == RequestStatuses.SUCCESS) {

                InvoiceFragment createInvoiceFragment = new InvoiceFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("ticketModel", currentTicket);
                bundle.putSerializable("eventModel", eventModel);
                bundle.putSerializable("ticketKindModel", ticketKindModel);
                createInvoiceFragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_distributor_ticket_kind_id, createInvoiceFragment, "createInvoiceFragment")
                        .addToBackStack(null)
                        .commit();
            }
            else {
                Toast.makeText(getContext(), "Ticket has been checked in by another distributor", Toast.LENGTH_SHORT).show();
                eventService.getTickets(eventModel.getId(), ticketKindModel.getName());
            }
        }
    }

    @Override
    public void onAPICallFailure() {

        Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(TicketModel ticketModel) {

        eventService.checkInTicket(ticketModel.getId());
        currentTicket = ticketModel;
    }
}
