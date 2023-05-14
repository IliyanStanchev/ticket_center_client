package com.example.ticket_center_client.views.organizer.distributors;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ticket_center_client.R;
import com.example.ticket_center_client.adapters.DistributorAdapter;
import com.example.ticket_center_client.listeners.DistributorClickListener;
import com.example.ticket_center_client.requests.APICallListener;
import com.example.ticket_center_client.requests.ResponseDeserializer;
import com.example.ticket_center_client.services.DistributorService;

import tuvarna.ticket_center_common.models.DistributorModel;
import tuvarna.ticket_center_common.models.DistributorsWrapper;
import tuvarna.ticket_center_common.requests.RequestCodes;
import tuvarna.ticket_center_common.requests.RequestStatuses;
import tuvarna.ticket_center_common.responses.CommonResponse;

public class DistributorsFragment extends Fragment implements DistributorClickListener, APICallListener {

    DistributorAdapter distributorsAdapter = new DistributorAdapter();

    public DistributorsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (container != null) {
            container.removeAllViews();
        }

        View view = inflater.inflate(R.layout.fragment_distributors, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.rvUsers);

        recyclerView.setAdapter(distributorsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        DistributorService distributorService = new DistributorService(this);
        distributorService.getDistributors();

        return view;
    }

    @Override
    public void onClick(DistributorModel distributor) {

        Bundle bundle = new Bundle();
        bundle.putSerializable("selectedDistributor", distributor);

        DistributorFragment distributorFragment = new DistributorFragment();
        distributorFragment.setArguments(bundle);

        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_distributors_id, distributorFragment).addToBackStack(null).commit();
    }

    @Override
    public void onAPICallSuccess(CommonResponse response) {

        if (response.getRequestCode() == RequestCodes.DISTRIBUTORS_GET) {
            if (response.getRequestStatus() == RequestStatuses.SUCCESS) {
                DistributorsWrapper wrapper = ResponseDeserializer.deserialize(response.getResponse(), DistributorsWrapper.class);
                distributorsAdapter.setUsers(wrapper.getDistributors());
                distributorsAdapter.setOnViewClickListener(this);
            }
        }
    }

    @Override
    public void onAPICallFailure() {

    }
}