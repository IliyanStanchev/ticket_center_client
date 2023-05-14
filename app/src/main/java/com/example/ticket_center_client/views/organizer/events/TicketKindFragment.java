package com.example.ticket_center_client.views.organizer.events;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ticket_center_client.R;
import com.example.ticket_center_client.adapters.TicketAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import tuvarna.ticket_center_common.models.EventModel;
import tuvarna.ticket_center_common.models.TicketKindModel;
import tuvarna.ticket_center_common.models.TicketModel;

public class TicketKindFragment extends Fragment {

    private TicketKindModel ticketKindModel;

    private EventModel eventModel;

    private AlertDialog.Builder builder;

    private AlertDialog dialog;

    TicketAdapter ticketAdapter = new TicketAdapter();

    public TicketKindFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (container != null) {
            container.removeAllViews();
        }

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ticket_kind, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.rvTickets);

        ticketKindModel = (TicketKindModel) getArguments().getSerializable("ticketKindModel");
        eventModel = (EventModel) getArguments().getSerializable("eventModel");

        ticketAdapter.setTicketKindModel(ticketKindModel);

        FloatingActionButton fabAdd = view.findViewById(R.id.fabAdd);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder = new AlertDialog.Builder(getContext());
                view = getLayoutInflater().inflate(R.layout.dialog_ticket, null);

                EditText etNote = view.findViewById(R.id.etNote);

                Button btnAction = view.findViewById(R.id.btnAction);
                Button btnGoBack = view.findViewById(R.id.btnGoBack);

                btnAction.setText("Confirm");

                btnAction.setOnClickListener(v -> {

                    TicketModel ticketModel = new TicketModel();
                    ticketModel.setNote(etNote.getText().toString());

                    ticketKindModel.addTicket(ticketModel);
                    ticketAdapter.notifyDataSetChanged();
                    dialog.dismiss();
                });

                btnGoBack.setOnClickListener(v -> dialog.dismiss());

                builder.setView(view);
                dialog = builder.create();

                dialog.show();
            }
        });

        FloatingActionButton fabBack = view.findViewById(R.id.fabBack);
        fabBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                eventModel.addTicketKind(ticketKindModel);

                TicketKindsFragment ticketKindsFragment = new TicketKindsFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("eventModel", eventModel);
                ticketKindsFragment.setArguments(bundle);

                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.ticket_kind_id, ticketKindsFragment, "eventFragment")
                        .addToBackStack(null)
                        .commit();
            }
        });

        initRecyclerView(recyclerView);
        return view;
    }

    private void initRecyclerView(RecyclerView recyclerView) {
        recyclerView.setAdapter(ticketAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}