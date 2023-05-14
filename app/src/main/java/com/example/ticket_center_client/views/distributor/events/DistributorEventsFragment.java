package com.example.ticket_center_client.views.distributor.events;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ticket_center_client.R;
import com.example.ticket_center_client.adapters.EventAdapter;
import com.example.ticket_center_client.databinding.FragmentGalleryBinding;
import com.example.ticket_center_client.listeners.EventClickListener;
import com.example.ticket_center_client.requests.APICallListener;
import com.example.ticket_center_client.requests.ResponseDeserializer;
import com.example.ticket_center_client.services.EventService;

import tuvarna.ticket_center_common.models.EventModel;
import tuvarna.ticket_center_common.models.EventsWrapper;
import tuvarna.ticket_center_common.models.UserModel;
import tuvarna.ticket_center_common.requests.RequestCodes;
import tuvarna.ticket_center_common.requests.RequestStatuses;
import tuvarna.ticket_center_common.responses.CommonResponse;

public class DistributorEventsFragment extends Fragment implements APICallListener, EventClickListener {

    EventAdapter eventAdapter = new EventAdapter();

    UserModel user;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        if (container != null) {
            container.removeAllViews();
        }

        View root = inflater.inflate(R.layout.fragment_distributor_events, container, false);

        user = (UserModel) getActivity().getIntent().getSerializableExtra("userModel");
        if (user == null) {
            return null;
        }

        RecyclerView recyclerView = root.findViewById(R.id.rvEvents);

        recyclerView.setAdapter(eventAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        EventService eventService = new EventService(this);
        eventService.getEvents(user.getEmail());

        return root;
    }

    @Override
    public void onAPICallSuccess(CommonResponse response) {
        if (response.getRequestCode() == RequestCodes.EVENTS_GET_ASSIGNED) {
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

        DistributorEventFragment fragment = new DistributorEventFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("eventModel", event);
        fragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_distributor_events_id, fragment).commit();
    }
}