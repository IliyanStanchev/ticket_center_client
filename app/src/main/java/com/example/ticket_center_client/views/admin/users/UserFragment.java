package com.example.ticket_center_client.views.admin.users;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.ticket_center_client.R;
import com.example.ticket_center_client.requests.APICallListener;

import tuvarna.ticket_center_common.models.UserModel;
import tuvarna.ticket_center_common.requests.RequestCodes;
import tuvarna.ticket_center_common.requests.RequestStatuses;
import tuvarna.ticket_center_common.responses.CommonResponse;

public class UserFragment extends Fragment implements APICallListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);

        UserModel selectedUser = (UserModel) getActivity().getIntent().getSerializableExtra("selectedUser");
        if( selectedUser == null ) {
            return view;
        }

        UserModel currentUser = (UserModel) getActivity().getIntent().getSerializableExtra("user");
        if( currentUser == null ) {
            return view;
        }

        TextView tvUserName = view.findViewById(R.id.tvUserName);
        TextView tvEmail = view.findViewById(R.id.tvUserName);
        ImageView avatar = view.findViewById(R.id.ivIcon);
        avatar.setImageResource(R.mipmap.ic_user);

        tvUserName.setText(selectedUser.getName());
        tvEmail.setText(selectedUser.getEmail());

        tvUserName.setTextSize(30);
        tvEmail.setTextSize(20);

        return view;
    }

    @Override
    public void onAPICallSuccess(CommonResponse response) {

        if( response.getRequestCode() == RequestCodes.USERS_EDIT ) {

            if( response.getRequestStatus() == RequestStatuses.SUCCESS ) {

            } else {
                String message = response.getResponse().toString();
                Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onAPICallFailure() {

        Toast.makeText(getContext(), getResources().getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
    }
}
