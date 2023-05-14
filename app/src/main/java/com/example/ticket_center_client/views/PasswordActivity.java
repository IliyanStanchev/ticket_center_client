package com.example.ticket_center_client.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ticket_center_client.R;
import com.example.ticket_center_client.databinding.ActivityPasswordBinding;
import com.example.ticket_center_client.requests.APICallListener;
import com.example.ticket_center_client.services.UserService;

import tuvarna.ticket_center_common.models.ChangePasswordModel;
import tuvarna.ticket_center_common.requests.RequestCodes;
import tuvarna.ticket_center_common.requests.RequestStatuses;
import tuvarna.ticket_center_common.responses.CommonResponse;

public class PasswordActivity extends AppCompatActivity implements APICallListener {

    TextView errorText;
    ProgressBar loadingProgressBar;

    private ActivityPasswordBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        final String mail = getIntent().getStringExtra("mail");

        final EditText password = binding.etPassword;
        final Button confirm = binding.btnConfirm;

        loadingProgressBar = binding.pbLoading;
        errorText = binding.tvErrorMessage;

        confirm.setOnClickListener(v -> {

            if( !validate() ){
                return;
            }
            
            loadingProgressBar.setVisibility(View.VISIBLE);
            UserService userService = new UserService(this);
            userService.changePassword(new ChangePasswordModel(mail, "", password.getText().toString()));
        });
    }

    private boolean validate() {

        boolean valid = true;

        String password = binding.etPassword.getText().toString();
        String confirmPassword = binding.etConfirmPassword.getText().toString();

        if( password.isEmpty() || password.length() < 4 || password.length() > 10 ) {
            binding.etPassword.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            binding.etPassword.setError(null);
        }

        if( confirmPassword.isEmpty() || confirmPassword.length() < 4 || confirmPassword.length() > 10 || !(confirmPassword.equals(password)) ) {
            binding.etConfirmPassword.setError("Password Do not match");
            valid = false;
        } else {
            binding.etConfirmPassword.setError(null);
        }

        return valid;
    }

    @Override
    public void onAPICallSuccess(CommonResponse response) {

        loadingProgressBar.setVisibility(View.GONE);

        if( response.getRequestCode() == RequestCodes.USERS_CHANGE_PASSWORD ) {

            if( response.getRequestStatus() == RequestStatuses.SUCCESS ) {
                handleSuccess();
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

    private void handleSuccess() {

        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        Toast.makeText(getApplicationContext(), "Password changed successfully", Toast.LENGTH_SHORT).show();
    }
}