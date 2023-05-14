package com.example.ticket_center_client.views.distributor.events;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.ticket_center_client.R;
import com.example.ticket_center_client.requests.APICallListener;
import com.example.ticket_center_client.requests.ResponseDeserializer;
import com.example.ticket_center_client.services.EventService;

import tuvarna.ticket_center_common.models.EventModel;
import tuvarna.ticket_center_common.models.EventsWrapper;
import tuvarna.ticket_center_common.models.PurchaseTicketModel;
import tuvarna.ticket_center_common.models.TicketKindModel;
import tuvarna.ticket_center_common.models.TicketModel;
import tuvarna.ticket_center_common.models.UserModel;
import tuvarna.ticket_center_common.requests.RequestCodes;
import tuvarna.ticket_center_common.requests.RequestStatuses;
import tuvarna.ticket_center_common.responses.CommonResponse;

public class InvoiceFragment extends Fragment implements APICallListener {

    private EventModel eventModel;

    private TicketModel currentTicket;

    private TicketKindModel ticketKindModel;

    private UserModel userModel;

    TextView tvEventName;
    TextView tvLocation;
    TextView tvStartDate;
    TextView tvPrice;

    EditText etEmail;
    EditText etName;
    EditText etPhoneNumber;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (container != null) {
            container.removeAllViews();
        }

        View view = inflater.inflate(R.layout.fragment_invoice, container, false);

        userModel = (UserModel) getActivity().getIntent().getSerializableExtra("userModel");

        ticketKindModel = (TicketKindModel) getArguments().getSerializable("ticketKindModel");
        eventModel = (EventModel) getArguments().getSerializable("eventModel");
        currentTicket = (TicketModel) getArguments().getSerializable("ticketModel");

        tvEventName = view.findViewById(R.id.tvName);
        tvLocation = view.findViewById(R.id.tvLocation);
        tvStartDate = view.findViewById(R.id.tvStartDate);
        tvPrice = view.findViewById(R.id.tvPrice);

        etEmail = view.findViewById(R.id.etEmail);
        etName = view.findViewById(R.id.etName);
        etPhoneNumber = view.findViewById(R.id.etPhoneNumber);

        tvEventName.setText(eventModel.getName());
        tvLocation.setText(eventModel.getLocation());
        tvStartDate.setText(eventModel.getStartDate() + " " + eventModel.getStartTime());
        tvPrice.setText(String.valueOf(ticketKindModel.getPrice()));

        Button btnBack = view.findViewById(R.id.btnGoBack);
        btnBack.setOnClickListener(view1 -> {
            EventService eventService = new EventService(this);
            eventService.checkOutTicket(currentTicket.getId());});

        Button btnBuy = view.findViewById(R.id.btnBuy);
        btnBuy.setOnClickListener(v -> buyTicket());

        return view;
    }

    private void buyTicket() {

        if(!validateFields()) {
            return;
        }

        EventService eventService = new EventService(this);
        eventService.purchaseTicket(new PurchaseTicketModel(currentTicket.getId()
                                                        , userModel.getEmail()
                                                        , ticketKindModel.getPrice()
                                                        , etEmail.getText().toString()
                                                        , etName.getText().toString()
                                                        , etPhoneNumber.getText().toString()));

    }

    private boolean validateFields() {

        if(etEmail.getText().toString().isEmpty()) {
            etEmail.setError("Email is required");
            return false;
        }

        if(etName.getText().toString().isEmpty()) {
            etName.setError("Name is required");
            return false;
        }

        if(etPhoneNumber.getText().toString().isEmpty()) {
            etPhoneNumber.setError("Phone number is required");
            return false;
        }

        return true;
    }

    @Override
    public void onAPICallSuccess(CommonResponse response) {

        if (response.getRequestCode() == RequestCodes.EVENTS_TICKETS_CHECK_OUT) {
            if (response.getRequestStatus() == RequestStatuses.SUCCESS) {
                DistributorTicketKindFragment fragment = new DistributorTicketKindFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("eventModel", eventModel);
                bundle.putSerializable("ticketKindModel", ticketKindModel);
                fragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_invoice_id, fragment, "invoiceFragment")
                        .addToBackStack(null)
                        .commit();

            }
        }

        if (response.getRequestCode() == RequestCodes.EVENTS_TICKETS_PURCHASE) {
            if (response.getRequestStatus() == RequestStatuses.SUCCESS) {
                DistributorTicketKindFragment fragment = new DistributorTicketKindFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("eventModel", eventModel);
                bundle.putSerializable("ticketKindModel", ticketKindModel);
                fragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_invoice_id, fragment, "invoiceFragment")
                        .addToBackStack(null)
                        .commit();
                Toast.makeText(getContext(), "Ticket purchased successfully", Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    public void onAPICallFailure() {
        Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
    }
}
