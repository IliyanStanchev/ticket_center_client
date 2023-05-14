package com.example.ticket_center_client.services;

import com.example.ticket_center_client.requests.APICallListener;
import com.example.ticket_center_client.requests.TicketCenterRESTClient;

import tuvarna.ticket_center_common.models.RatingModel;

public class RatingService extends BaseService {

    public RatingService(APICallListener apiCallListener) {
        super(apiCallListener);
    }

    public void addRating(RatingModel ratingModel) {
        HandleCall(TicketCenterRESTClient.getInstance().getTicketCenterAPI().addRating(ratingModel));
    }

    public void getRatings(String email) {
        HandleCall(TicketCenterRESTClient.getInstance().getTicketCenterAPI().getRatings(email));
    }
}
