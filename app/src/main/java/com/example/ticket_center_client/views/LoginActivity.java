package com.example.ticket_center_client.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ticket_center_client.R;
import com.example.ticket_center_client.databinding.ActivityLoginBinding;
import com.example.ticket_center_client.requests.APICallListener;
import com.example.ticket_center_client.requests.ResponseDeserializer;
import com.example.ticket_center_client.services.UserService;
import com.example.ticket_center_client.views.admin.AdminActivity;
import com.example.ticket_center_client.views.distributor.DistributorActivity;
import com.example.ticket_center_client.views.organizer.OrganizerActivity;

import tuvarna.ticket_center_common.models.LoginModel;
import tuvarna.ticket_center_common.models.UserModel;
import tuvarna.ticket_center_common.requests.RequestCodes;
import tuvarna.ticket_center_common.requests.RequestStatuses;
import tuvarna.ticket_center_common.responses.CommonResponse;

public class LoginActivity extends AppCompatActivity implements APICallListener {

    TextView errorMessage;
    ProgressBar loading;
    
    private ActivityLoginBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        final EditText mail = binding.etEmail;
        final EditText password = binding.etPassword;
        final Button login = binding.btnLogin;
        loading = binding.pbLoading;
        errorMessage = binding.tvErrorMessage;

        final Button verifyButton = binding.btnVerify;

        verifyButton.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), com.example.ticket_center_client.views.VerificationActivity.class);
            startActivity(intent);
        });

        login.setOnClickListener(v -> {
            loading.setVisibility(View.VISIBLE);

            UserService userService = new UserService(this);
            //userService.login( new LoginModel(mail.getText().toString(), password.getText().toString()));

            //Organizer
            userService.login( new LoginModel("ench3r@gmail.com", "sach"));

            //Admin
           //userService.login( new LoginModel("admin", "admin"));

            //Distributor
            //userService.login( new LoginModel("iliyan.stanchev@gmail.com", "sach"));
        });
    }

    @Override
    public void onAPICallSuccess(CommonResponse response) {

        loading.setVisibility(View.GONE);

        if( response.getRequestCode() == RequestCodes.USERS_LOGIN ) {

            if( response.getRequestStatus() == RequestStatuses.SUCCESS ) {
                UserModel userModel = ResponseDeserializer.deserialize(response.getResponse(), UserModel.class);
                handleLogin(userModel);
            } else {
                String message = response.getResponse().toString();
                errorMessage.setText(message);
            }
        }
    }

    private void handleLogin(UserModel userModel) {

        Intent intent = null;
        switch (userModel.getRole()) {
            case ADMIN:
                intent = new Intent(getApplicationContext(), AdminActivity.class);
                break;

            case DISTRIBUTOR:
                intent = new Intent(getApplicationContext(), DistributorActivity.class);
                break;

            case ORGANIZER:
                intent = new Intent(getApplicationContext(), OrganizerActivity.class);
                break;

            default:
                errorMessage.setText(getResources().getString(R.string.something_went_wrong));
        }

        intent.putExtra("userModel", userModel);
        startActivity(intent);
    }

    @Override
    public void onAPICallFailure() {

        loading.setVisibility(View.GONE);
        errorMessage.setText(getResources().getString(R.string.something_went_wrong));
    }
}