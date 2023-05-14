package com.example.ticket_center_client.views.organizer.events;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ticket_center_client.R;
import com.example.ticket_center_client.adapters.EventAdapter;
import com.example.ticket_center_client.listeners.EventClickListener;
import com.example.ticket_center_client.requests.APICallListener;
import com.example.ticket_center_client.requests.ResponseDeserializer;
import com.example.ticket_center_client.services.EventService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import tuvarna.ticket_center_common.models.EventModel;
import tuvarna.ticket_center_common.models.EventsWrapper;
import tuvarna.ticket_center_common.models.UserModel;
import tuvarna.ticket_center_common.requests.RequestCodes;
import tuvarna.ticket_center_common.requests.RequestStatuses;
import tuvarna.ticket_center_common.responses.CommonResponse;

public class EventsFragment extends Fragment implements EventClickListener, APICallListener {

    EventAdapter eventAdapter = new EventAdapter();

    UserModel user;

    public EventsFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (container != null) {
            container.removeAllViews();
        }

        user = (UserModel) getActivity().getIntent().getSerializableExtra("userModel");
        if (user == null) {
            return null;
        }

        View view = inflater.inflate(R.layout.fragment_events, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.rvEvents);

        recyclerView.setAdapter(eventAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        EventService eventService = new EventService(this);
        eventService.getOrganizerEvents(user.getEmail());

        FloatingActionButton fabAdd = view.findViewById(R.id.fabAdd);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventFragment eventFragment = new EventFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_events_id, eventFragment).commit();
            }
        });

        return view;
    }

    @Override
    public void onAPICallSuccess(CommonResponse response) {

        if (response.getRequestCode() == RequestCodes.EVENTS_GET) {
            if (response.getRequestStatus() == RequestStatuses.SUCCESS) {
                EventsWrapper wrapper = ResponseDeserializer.deserialize(response.getResponse(), EventsWrapper.class);
                eventAdapter.setEvents(wrapper.getEvents());
                eventAdapter.setOnViewClickListener(this);
            }
        }
    }

    @Override
    public void onAPICallFailure() {

    }

    @Override
    public void onClick(EventModel event) {

        AssignDistributorFragment assignDistributorFragment = new AssignDistributorFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("eventModel", event);
        assignDistributorFragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_events_id, assignDistributorFragment).commit();
    }
}
