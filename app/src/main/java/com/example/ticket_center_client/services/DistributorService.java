package com.example.ticket_center_client.services;

import com.example.ticket_center_client.requests.APICallListener;
import com.example.ticket_center_client.requests.TicketCenterRESTClient;

import tuvarna.ticket_center_common.models.AssignDistributorModel;

public class DistributorService extends BaseService {

    public DistributorService(APICallListener apiCallListener) {
        super(apiCallListener);
    }

    public void getDistributors() {
        HandleCall(TicketCenterRESTClient.getInstance().getTicketCenterAPI().getDistributors());
    }

    public void getAssignedDistributors(Long id) {
        HandleCall(TicketCenterRESTClient.getInstance().getTicketCenterAPI().getAssignedDistributors(id));
    }

    public void getFreeDistributors(Long id) {
        HandleCall(TicketCenterRESTClient.getInstance().getTicketCenterAPI().getFreeDistributors(id));
    }

    public void assignDistributor(AssignDistributorModel assignDistributorModel) {
        HandleCall(TicketCenterRESTClient.getInstance().getTicketCenterAPI().assignDistributor(assignDistributorModel));
    }

    public void getDistributor(String email) {
        HandleCall(TicketCenterRESTClient.getInstance().getTicketCenterAPI().getDistributor(email));
    }
}
