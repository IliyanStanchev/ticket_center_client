package com.example.ticket_center_client.services;

import com.example.ticket_center_client.requests.APICallListener;
import com.example.ticket_center_client.requests.TicketCenterRESTClient;

import tuvarna.ticket_center_common.models.ChangePasswordModel;
import tuvarna.ticket_center_common.models.LoginModel;
import tuvarna.ticket_center_common.models.RegistrationModel;
import tuvarna.ticket_center_common.models.UserModel;
import tuvarna.ticket_center_common.models.VerificationModel;


public class UserService extends BaseService {

    public UserService(APICallListener apiCallListener) {
        super(apiCallListener);
    }

    public void login(LoginModel loginModel) {

        HandleCall(TicketCenterRESTClient.getInstance().getTicketCenterAPI().login(loginModel));
    }

    public void register(RegistrationModel registrationModel) {

        HandleCall(TicketCenterRESTClient.getInstance().getTicketCenterAPI().register(registrationModel));
    }

    public void verify(VerificationModel verificationModel) {

        HandleCall(TicketCenterRESTClient.getInstance().getTicketCenterAPI().verify(verificationModel));
    }

    public void changePassword(ChangePasswordModel changePasswordModel) {

        HandleCall(TicketCenterRESTClient.getInstance().getTicketCenterAPI().changePassword(changePasswordModel));
    }

    public void edit(UserModel userModel) {

        HandleCall(TicketCenterRESTClient.getInstance().getTicketCenterAPI().editUser(userModel));
    }

    public void getUsers() {

        HandleCall(TicketCenterRESTClient.getInstance().getTicketCenterAPI().getUsers());
    }
}
