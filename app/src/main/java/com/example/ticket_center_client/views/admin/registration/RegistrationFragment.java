package com.example.ticket_center_client.views.admin.registration;

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
import androidx.fragment.app.Fragment;

import com.example.ticket_center_client.R;
import com.example.ticket_center_client.databinding.FragmentRegistrationBinding;
import com.example.ticket_center_client.requests.APICallListener;
import com.example.ticket_center_client.services.UserService;

import tuvarna.ticket_center_common.enumerables.RoleTypes;
import tuvarna.ticket_center_common.models.RegistrationModel;
import tuvarna.ticket_center_common.requests.RequestCodes;
import tuvarna.ticket_center_common.requests.RequestStatuses;
import tuvarna.ticket_center_common.responses.CommonResponse;


public class RegistrationFragment extends Fragment implements APICallListener {

    Spinner spRoleTypes;

    EditText mail;

    EditText firstName;

    EditText lastName;

    EditText phoneNumber;

    EditText honorarium;

    TextView errorMessage;

    private FragmentRegistrationBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentRegistrationBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        spRoleTypes = binding.spRoleTypes;

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.role_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spRoleTypes.setAdapter(adapter);

        mail = binding.etEmail;
        firstName = binding.etFirstName;
        lastName = binding.etLastName;
        phoneNumber = binding.etPhoneNumber;
        honorarium = binding.etPrice;
        errorMessage = binding.tvErrorMessage;

        Button register = binding.btnRegister;
        register.setOnClickListener(v -> {

            errorMessage.setText("");

            String roleType = spRoleTypes.getSelectedItem().toString();
            RoleTypes roleTypeValue = RoleTypes.fromString(roleType);

            String email = mail.getText().toString();
            String firstName = this.firstName.getText().toString();
            String lastName = this.lastName.getText().toString();
            String phoneNumber = this.phoneNumber.getText().toString();
            float honorarium = Float.parseFloat(this.honorarium.getText().toString());

            UserService userService = new UserService(this);
            userService.register( new RegistrationModel(roleTypeValue, email, firstName, lastName, phoneNumber, honorarium));
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onAPICallSuccess(CommonResponse response) {

        if( response.getRequestCode() == RequestCodes.USERS_REGISTER ) {

            if( response.getRequestStatus() == RequestStatuses.SUCCESS ) {
                handleRegistrationSuccess(response);

            } else {
                String message = response.getResponse().toString();
                errorMessage.setText(message);
            }
        }
    }

    private void handleRegistrationSuccess(CommonResponse response) {

        mail.setText("");
        firstName.setText("");
        lastName.setText("");
        phoneNumber.setText("");
        honorarium.setText("");

        Toast.makeText(getContext(), "Registration successful", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAPICallFailure() {

        errorMessage.setText(getResources().getString(R.string.something_went_wrong));
    }
}