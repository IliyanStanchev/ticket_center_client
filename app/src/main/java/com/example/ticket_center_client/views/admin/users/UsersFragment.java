package com.example.ticket_center_client.views.admin.users;

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
import com.example.ticket_center_client.adapters.UsersAdapter;
import com.example.ticket_center_client.listeners.UserClickListener;
import com.example.ticket_center_client.requests.APICallListener;
import com.example.ticket_center_client.requests.ResponseDeserializer;
import com.example.ticket_center_client.services.UserService;

import tuvarna.ticket_center_common.enumerables.UserStatuses;
import tuvarna.ticket_center_common.models.UserModel;
import tuvarna.ticket_center_common.models.UsersWrapper;
import tuvarna.ticket_center_common.requests.RequestCodes;
import tuvarna.ticket_center_common.requests.RequestStatuses;
import tuvarna.ticket_center_common.responses.CommonResponse;

public class UsersFragment extends Fragment implements UserClickListener, APICallListener {
    private UsersAdapter usersAdapter = new UsersAdapter();

    private AlertDialog.Builder builder;

    private AlertDialog dialog;


    public UsersFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_users, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.rvUsers);

        initRecyclerView(recyclerView);

        UserService userService = new UserService(this);
        userService.getUsers();

        return view;
    }

    private void initRecyclerView(RecyclerView recyclerView) {
        recyclerView.setAdapter(usersAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onClick(UserModel userModel) {

        builder = new AlertDialog.Builder(getContext());
        View view = getLayoutInflater().inflate(R.layout.dialog_user, null);

        EditText etMail = view.findViewById(R.id.etMail);
        EditText etName = view.findViewById(R.id.etEmail);
        EditText etPhone = view.findViewById(R.id.etPhone);
        EditText etHonorarium = view.findViewById(R.id.etPrice);
        EditText etRole = view.findViewById(R.id.etRole);
        EditText etStatus = view.findViewById(R.id.etStatus);

        Button btnAction = view.findViewById(R.id.btnAction);
        Button btnGoBack = view.findViewById(R.id.btnGoBack);

        UserStatuses newStatus = userModel.getStatus();

        if( userModel.getStatus() == UserStatuses.BLOCKED ){
            newStatus = UserStatuses.CONFIRMED;
            btnAction.setText("Unblock");
        } else if( userModel.getStatus() == UserStatuses.CONFIRMED ){
            newStatus = UserStatuses.BLOCKED;
            btnAction.setText("Block");
        }
        else {
            btnAction.setVisibility(View.GONE);
        }

        etMail.setText(userModel.getName());
        etName.setText(userModel.getEmail());
        etPhone.setText(userModel.getPhoneNumber());
        etStatus.setText(userModel.getStatus().toString());
        etHonorarium.setText( String.valueOf( userModel.getHonorarium() ));
        etRole.setText(userModel.getRole().toString());

        UserStatuses finalNewStatus = newStatus;
        btnAction.setOnClickListener(v -> {
            UserService userService = new UserService(UsersFragment.this);
            userModel.setStatus(finalNewStatus);
            userService.edit(userModel);
        });

        btnGoBack.setOnClickListener(v -> dialog.dismiss());

        builder.setView(view);
        dialog = builder.create();

        dialog.show();
    }

    @Override
    public void onAPICallSuccess(CommonResponse response) {

        if (response.getRequestCode() == RequestCodes.USERS_GET) {
            if (response.getRequestStatus() == RequestStatuses.SUCCESS) {
                UsersWrapper usersWrapper = ResponseDeserializer.deserialize(response.getResponse(), UsersWrapper.class);
                usersAdapter.setUsers(usersWrapper.getUsers());
                usersAdapter.setOnNewClickListener(this);
            }
        }

        if (response.getRequestCode() == RequestCodes.USERS_EDIT) {
            if (response.getRequestStatus() == RequestStatuses.SUCCESS) {
                UserService userService = new UserService(this);
                userService.getUsers();
                Toast.makeText(getContext(), "User status changed", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        }
    }

    @Override
    public void onAPICallFailure() {

        Toast.makeText(getContext(), getResources().getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
    }
}