package com.example.ticket_center_client.views.organizer.events;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ticket_center_client.R;
import com.example.ticket_center_client.adapters.DistributorAdapter;
import com.example.ticket_center_client.requests.APICallListener;
import com.example.ticket_center_client.requests.ResponseDeserializer;
import com.example.ticket_center_client.services.DistributorService;

import java.util.ArrayList;
import java.util.List;

import tuvarna.ticket_center_common.models.AssignDistributorModel;
import tuvarna.ticket_center_common.models.DistributorModel;
import tuvarna.ticket_center_common.models.DistributorsWrapper;
import tuvarna.ticket_center_common.models.EventModel;
import tuvarna.ticket_center_common.requests.RequestCodes;
import tuvarna.ticket_center_common.requests.RequestStatuses;
import tuvarna.ticket_center_common.responses.CommonResponse;

public class AssignDistributorFragment extends Fragment implements APICallListener {

    TextView tvName;

    TextView tvLocation;

    TextView tvStartDate;

    TextView tvRemainingTickets;

    DistributorAdapter distributorAdapter = new DistributorAdapter();

    DistributorService distributorService;

    List<DistributorModel> freeDistributors = new ArrayList<>();
    EditText etMail;
    EditText etPhone;
    EditText etHonorarium;
    EventModel eventModel;

    RatingBar ratingBar;
    private AlertDialog.Builder builder;
    private AlertDialog dialog;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        if (container != null) {
            container.removeAllViews();
        }

        eventModel = (EventModel) getArguments().getSerializable("eventModel");
        if (eventModel == null)
            return null;

        View root = inflater.inflate(R.layout.fragment_assign_distributor, container, false);

        RecyclerView rvDistributors = root.findViewById(R.id.rvDistributors);
        rvDistributors.setAdapter(distributorAdapter);
        rvDistributors.setLayoutManager(new LinearLayoutManager(getContext()));

        tvName = root.findViewById(R.id.tvName);
        tvName.setTextSize(20);

        tvLocation = root.findViewById(R.id.tvLocation);
        tvLocation.setTextSize(20);

        tvStartDate = root.findViewById(R.id.tvStartDate);
        tvStartDate.setTextSize(20);

        tvRemainingTickets = root.findViewById(R.id.tvRemainingTickets);
        tvRemainingTickets.setTextSize(20);

        tvName.setText("Event: " + eventModel.getName());
        tvLocation.setText("Location: " + eventModel.getLocation());
        tvStartDate.setText("Start date: " + eventModel.getStartDate().toString() + " - " + eventModel.getStartTime().toString());
        tvRemainingTickets.setText("Tickets: " + eventModel.getRemainingTickets() + " / " + eventModel.getTotalTickets());

        distributorService = new DistributorService(this);
        distributorService.getAssignedDistributors(eventModel.getId());
        distributorService.getFreeDistributors(eventModel.getId());

        Button btnAssign = root.findViewById(R.id.btnAdd);
        btnAssign.setOnClickListener(v -> assign());

        Button btnGoBack = root.findViewById(R.id.btnBack);
        btnGoBack.setOnClickListener(v -> getActivity().onBackPressed());

        return root;
    }

    private void assign() {

        if( freeDistributors.size() == 0 ){
            Toast.makeText(getContext(), "No free distributors available", Toast.LENGTH_SHORT).show();
            return;
        }

        builder = new AlertDialog.Builder(getContext());
        View view = getLayoutInflater().inflate(R.layout.dialog_distributors, null);

        Spinner spinner = view.findViewById(R.id.spDistributors);
        ArrayAdapter<DistributorModel> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, freeDistributors);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        etMail = view.findViewById(R.id.etMail);
        etPhone = view.findViewById(R.id.etPhoneNumber);
        etHonorarium = view.findViewById(R.id.etHonorarium);
        ratingBar = view.findViewById(R.id.rbRating);

        etMail.setFocusable(false);
        etPhone.setFocusable(false);
        etHonorarium.setFocusable(false);
        ratingBar.setIsIndicator(true);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected item
                DistributorModel selectedItem = (DistributorModel) parent.getItemAtPosition(position);
                etMail.setText(selectedItem.getEmail());
                etPhone.setText(selectedItem.getPhoneNumber());
                etHonorarium.setText(String.valueOf(selectedItem.getHonorarium()));
                ratingBar.setRating(selectedItem.getRating());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do something when nothing is selected
                etMail.setText("");
                etPhone.setText("");
                etHonorarium.setText("");
                ratingBar.setRating(0);
            }
        });

        Button btnAction = view.findViewById(R.id.btnAction);
        Button btnGoBack = view.findViewById(R.id.btnGoBack);

        btnAction.setOnClickListener(v -> {
            DistributorModel distributorModel = (DistributorModel) spinner.getSelectedItem();
            distributorService.assignDistributor(new AssignDistributorModel(distributorModel.getEmail(), eventModel.getId()));
        });

        btnGoBack.setOnClickListener(v -> dialog.dismiss());

        builder.setView(view);
        dialog = builder.create();

        dialog.show();
    }

    @Override
    public void onAPICallSuccess(CommonResponse response) {

        if (response.getRequestCode() == RequestCodes.DISTRIBUTORS_GET_ASSIGNED) {
            if (response.getRequestStatus() == RequestStatuses.SUCCESS) {
                DistributorsWrapper wrapper = ResponseDeserializer.deserialize(response.getResponse(), DistributorsWrapper.class);
                distributorAdapter.setUsers(wrapper.getDistributors());
            }
        }

        if (response.getRequestCode() == RequestCodes.DISTRIBUTORS_GET_FREE) {
            if (response.getRequestStatus() == RequestStatuses.SUCCESS) {
                DistributorsWrapper wrapper = ResponseDeserializer.deserialize(response.getResponse(), DistributorsWrapper.class);
                freeDistributors = wrapper.getDistributors();
            }
        }

        if (response.getRequestCode() == RequestCodes.DISTRIBUTORS_ASSIGN) {
            if (response.getRequestStatus() == RequestStatuses.SUCCESS) {
                distributorService.getAssignedDistributors(eventModel.getId());
                distributorService.getFreeDistributors(eventModel.getId());
                dialog.dismiss();
            }
        }
    }

    @Override
    public void onAPICallFailure() {

    }
}
