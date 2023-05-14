package com.example.ticket_center_client.views.distributor.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RatingBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.ticket_center_client.R;
import com.example.ticket_center_client.requests.APICallListener;
import com.example.ticket_center_client.requests.ResponseDeserializer;
import com.example.ticket_center_client.services.DistributorService;

import tuvarna.ticket_center_common.models.DistributorModel;
import tuvarna.ticket_center_common.models.UserModel;
import tuvarna.ticket_center_common.requests.RequestCodes;
import tuvarna.ticket_center_common.requests.RequestStatuses;
import tuvarna.ticket_center_common.responses.CommonResponse;

public class DistributorProfileFragment extends Fragment implements APICallListener {

    EditText email;
    EditText firstName;
    EditText phone;
    EditText honorarium;

    RatingBar ratingBar;

    private UserModel user;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_distributor_profile, container, false);

        user = (UserModel) getActivity().getIntent().getSerializableExtra("userModel");
        if (user == null) {
            return root;
        }

        email = root.findViewById(R.id.etEmail);
        firstName = root.findViewById(R.id.etFirstName);
        phone = root.findViewById(R.id.etPhoneNumber);
        honorarium = root.findViewById(R.id.etPrice);
        ratingBar = root.findViewById(R.id.ratingBar);

        email.setFocusable(false);
        firstName.setFocusable(false);
        phone.setFocusable(false);
        honorarium.setFocusable(false);

        ratingBar.setIsIndicator(false);

        DistributorService distributorService = new DistributorService(this);
        distributorService.getDistributor(user.getEmail());

        return root;
    }

    @Override
    public void onAPICallSuccess(CommonResponse response) {

        if (response.getRequestCode() == RequestCodes.DISTRIBUTORS_GET) {
            if (response.getRequestStatus() == RequestStatuses.SUCCESS) {

                DistributorModel distributor = ResponseDeserializer.deserialize(response.getResponse(), DistributorModel.class);
                email.setText(distributor.getEmail());
                firstName.setText(distributor.getFirstName() + " " + distributor.getLastName());
                phone.setText(distributor.getPhoneNumber());
                honorarium.setText(String.valueOf(distributor.getHonorarium()));
                ratingBar.setRating(distributor.getRating());
                ratingBar.setIsIndicator(false);
            }
        }
    }

    @Override
    public void onAPICallFailure() {

    }
}