package com.example.ticket_center_client.views.organizer.events;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.ticket_center_client.R;
import com.example.ticket_center_client.databinding.FragmentEventBinding;
import com.example.ticket_center_client.requests.APICallListener;
import com.example.ticket_center_client.requests.ResponseDeserializer;
import com.example.ticket_center_client.services.EventService;

import java.time.LocalDate;
import java.time.LocalTime;

import tuvarna.ticket_center_common.enumerables.EventTypes;
import tuvarna.ticket_center_common.models.EventModel;
import tuvarna.ticket_center_common.models.TicketKindModel;
import tuvarna.ticket_center_common.models.UserModel;
import tuvarna.ticket_center_common.requests.RequestCodes;
import tuvarna.ticket_center_common.requests.RequestStatuses;
import tuvarna.ticket_center_common.responses.CommonResponse;

public class EventFragment extends Fragment implements APICallListener {

    private UserModel userModel;

    private EventModel eventModel;

    private FragmentEventBinding binding;

    Spinner spEventType;

    EditText etEventName;

    EditText etDescription;

    EditText etStartDate;

    EditText etTime;

    EditText etEndDate;

    EditText etLocation;

    TextView tvErrorMessage;

    Button btnConfirm;

    Button btnAddTickets;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        if (container != null) {
            container.removeAllViews();
        }
        binding = FragmentEventBinding.inflate(inflater, container, false);

        Bundle bundle = getArguments();
        if( bundle != null && bundle.getSerializable("eventModel") != null ){
            eventModel = (EventModel) bundle.getSerializable("eventModel");
        }
        else{
            eventModel = new EventModel();
        }

        userModel = (UserModel) getActivity().getIntent().getSerializableExtra("userModel");
        if( userModel == null ) {
            return null;
        }

        View root = binding.getRoot();

        spEventType = binding.spEventType;
        spEventType.setAdapter(new ArrayAdapter<EventTypes>(getContext(), android.R.layout.simple_spinner_item, EventTypes.values()));

        etEventName = binding.etEventName;
        etDescription = binding.etComment;
        etStartDate = binding.etStartDate;
        etTime = binding.etTime;
        etEndDate = binding.etEndDate;
        etLocation = binding.etLocation;
        tvErrorMessage = binding.tvErrorMessage;
        btnConfirm = binding.btnConfirm;
        btnAddTickets = binding.btnAddTickets;

        btnAddTickets.setOnClickListener(view -> {

            fillBuffer();

            Bundle bundle1 = new Bundle();
            bundle1.putSerializable("eventModel", eventModel);
            bundle1.putSerializable("ticketKindModel", new TicketKindModel());

            TicketKindsFragment ticketKindsFragment= new TicketKindsFragment();
            ticketKindsFragment.setArguments(bundle1);

            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_event_id, ticketKindsFragment, "eventFragment")
                    .addToBackStack(null)
                    .commit();
        });

        btnConfirm.setOnClickListener(view -> {

            fillBuffer();

            if( !validate() ){
               return;
            }

            eventModel.setOrganizerEmail(userModel.getEmail());

            EventService eventService = new EventService(EventFragment.this);
            eventService.createEvent(eventModel);
        });

        fillControls();

        return root;
    }

    private void fillControls() {

        spEventType.setSelection(eventModel.getEventType().ordinal());
        etEventName.setText(eventModel.getName());
        etDescription.setText(eventModel.getDescription());
        etStartDate.setText(eventModel.getStartDate().toString());
        etTime.setText(eventModel.getStartTime().toString());
        etEndDate.setText(eventModel.getEndDate().toString());
        etLocation.setText(eventModel.getLocation());
    }

    private void fillBuffer() {

        eventModel.setName(etEventName.getText().toString());
        eventModel.setDescription(etDescription.getText().toString());
        eventModel.setStartDate(etStartDate.getText().toString());
        eventModel.setStartTime(etTime.getText().toString());
        eventModel.setEndDate(etEndDate.getText().toString());
        eventModel.setLocation(etLocation.getText().toString());
        eventModel.setEventType(EventTypes.fromString(spEventType.getSelectedItem().toString()));
    }

    private boolean validate() {

        if( etEventName.getText().toString().isEmpty() ){
            tvErrorMessage.setText("Event name is required!");
            return false;
        }

        if( etDescription.getText().toString().isEmpty() ){
            tvErrorMessage.setText("Description is required!");
            return false;
        }

        if( etStartDate.getText().toString().isEmpty() ){
            tvErrorMessage.setText("Start date is required!");
            return false;
        }

        if( etTime.getText().toString().isEmpty() ){
            tvErrorMessage.setText("Time is required!");
            return false;
        }

        if( etEndDate.getText().toString().isEmpty() ){
            tvErrorMessage.setText("End date is required!");
            return false;
        }

        if( etLocation.getText().toString().isEmpty() ){
            tvErrorMessage.setText("Location is required!");
            return false;
        }

        if( eventModel.getTicketKinds().isEmpty() ){
            tvErrorMessage.setText("At least one ticket kind is required!");
            return false;
        }

        if( eventModel.getTicketKinds().stream().anyMatch(x -> x.getTickets().size() < 0) ){
            tvErrorMessage.setText("All ticket kinds must have quantity greater than 0!");
            return false;
        }

        return true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onAPICallSuccess(CommonResponse response) {

        if( response.getRequestCode() == RequestCodes.EVENTS_POST ) {

            if( response.getRequestStatus() == RequestStatuses.SUCCESS ) {
                handleSuccess();
            } else {
                String message = response.getResponse().toString();
                tvErrorMessage.setText(message);
            }
        }
    }

    private void handleSuccess() {

        etDescription.setText("");
        etEventName.setText("");
        etStartDate.setText(LocalDate.now().toString());
        etTime.setText(LocalTime.now().toString());
        etEndDate.setText(LocalDate.now().toString());
        etLocation.setText("");
        tvErrorMessage.setText("");

        eventModel = new EventModel();
        Toast.makeText(getContext(), "Event created successfully!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAPICallFailure() {

        tvErrorMessage.setText(getResources().getString(R.string.something_went_wrong));
    }
}