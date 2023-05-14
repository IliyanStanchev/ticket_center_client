package com.example.ticket_center_client.views.organizer.events;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

public class TicketKindsFragment extends Fragment implements TicketKindClickListener, APICallListener {

    private EventModel eventModel;

    private AlertDialog.Builder builder;

    private AlertDialog dialog;

    private TicketKindAdapter ticketKindsAdapter = new TicketKindAdapter();

    public TicketKindsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (container != null) {
            container.removeAllViews();
        }
        View view = inflater.inflate(R.layout.fragment_ticket_kinds, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.rvTicketKinds);

        eventModel = (EventModel) getArguments().getSerializable("eventModel");
        ticketKindsAdapter.setEvent(eventModel);
        ticketKindsAdapter.setClickListener(this);

        FloatingActionButton fab = view.findViewById(R.id.fabAdd);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TicketKindModel ticketKindModel = new TicketKindModel();
                modifyTicketKind(ticketKindModel);
            }
        });

        FloatingActionButton fabBack = view.findViewById(R.id.fabBack);
        fabBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventFragment eventFragment = new EventFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("eventModel", eventModel);
                eventFragment.setArguments(bundle);

                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.ticket_kinds_id, eventFragment, "eventFragment")
                        .addToBackStack(null)
                        .commit();
            }
        });

        initRecyclerView(recyclerView);
        return view;
    }

    private void initRecyclerView(RecyclerView recyclerView) {
        recyclerView.setAdapter(ticketKindsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onAPICallSuccess(CommonResponse response) {


    }

    @Override
    public void onAPICallFailure() {

        Toast.makeText(getContext(), getResources().getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onClick(TicketKindModel ticketKind) {

        Bundle bundle = new Bundle();
        bundle.putSerializable("ticketKindModel", ticketKind);
        bundle.putSerializable("eventModel", eventModel);

        TicketKindFragment ticketKindFragment= new TicketKindFragment();
        ticketKindFragment.setArguments(bundle);

        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.ticket_kinds_id, ticketKindFragment, "ticketKindsFragment")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onEdit(TicketKindModel ticketKindModel) {

        modifyTicketKind(ticketKindModel);
    }

    public void modifyTicketKind(TicketKindModel ticketKindModel) {

        builder = new AlertDialog.Builder(getContext());
        View view = getLayoutInflater().inflate(R.layout.dialog_ticket_kind, null);

        EditText etName = view.findViewById(R.id.etEmail);
        EditText etPrice = view.findViewById(R.id.etPrice);
        EditText etDescription = view.findViewById(R.id.etComment);
        EditText etLimitPerBuyer = view.findViewById(R.id.etLimitPerBuyer);

        etName.setText(ticketKindModel.getName());
        etPrice.setText(String.valueOf(ticketKindModel.getPrice()));
        etDescription.setText(ticketKindModel.getDescription());
        etLimitPerBuyer.setText(String.valueOf(ticketKindModel.getLimitPerBuyer()));

        Button btnAction = view.findViewById(R.id.btnAction);
        Button btnGoBack = view.findViewById(R.id.btnGoBack);

        btnAction.setText("Confirm");

        btnAction.setOnClickListener(v -> {

            TicketKindModel ticketKind = new TicketKindModel();
            ticketKind.setName(etName.getText().toString());
            ticketKind.setPrice((float) Double.parseDouble(etPrice.getText().toString()));
            ticketKind.setDescription(etDescription.getText().toString());
            ticketKind.setLimitPerBuyer(Integer.parseInt(etLimitPerBuyer.getText().toString()));

            eventModel.addTicketKind(ticketKind);
            ticketKindsAdapter.notifyDataSetChanged();
            dialog.dismiss();
        });

        btnGoBack.setOnClickListener(v -> dialog.dismiss());

        builder.setView(view);
        dialog = builder.create();

        dialog.show();
    }
}
