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
import com.example.ticket_center_client.databinding.ActivityVerificationBinding;
import com.example.ticket_center_client.requests.APICallListener;
import com.example.ticket_center_client.services.UserService;

import tuvarna.ticket_center_common.models.VerificationModel;
import tuvarna.ticket_center_common.requests.RequestCodes;
import tuvarna.ticket_center_common.requests.RequestStatuses;
import tuvarna.ticket_center_common.responses.CommonResponse;

public class VerificationActivity extends AppCompatActivity implements APICallListener {

    TextView errorText;
    ProgressBar loadingProgressBar;

    EditText mail;

    private ActivityVerificationBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVerificationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        final EditText verificationCode = binding.etVerificationCode;
        final Button loginButton = binding.btnLogin;

        mail = binding.etEmail;
        loadingProgressBar = binding.pbLoading;
        errorText = binding.tvErrorMessage;

        final Button verifyButton = binding.btnVerify;

        loginButton.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        });

        verifyButton.setOnClickListener(v -> {
            loadingProgressBar.setVisibility(View.VISIBLE);

            UserService userService = new UserService(this);
            userService.verify(new VerificationModel(mail.getText().toString(), verificationCode.getText().toString()));
        });
    }

    @Override
    public void onAPICallSuccess(CommonResponse response) {

        loadingProgressBar.setVisibility(View.GONE);

        if( response.getRequestCode() == RequestCodes.USERS_VERIFY ) {

            if( response.getRequestStatus() == RequestStatuses.SUCCESS ) {
                handleVerification();
            } else {
                String message = response.getResponse().toString();
                errorText.setText(message);
            }
        }
    }

    @Override
    public void onAPICallFailure() {
        loadingProgressBar.setVisibility(View.GONE);
        errorText.setText(getResources().getString(R.string.something_went_wrong));
    }

    private void handleVerification() {

        Intent intent = new Intent(getApplicationContext(), PasswordActivity.class);
        intent.putExtra("mail", mail.getText().toString());
        startActivity(intent);

    }
}