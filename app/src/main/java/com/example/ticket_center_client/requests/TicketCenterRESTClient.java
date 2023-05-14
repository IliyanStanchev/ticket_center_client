package com.example.ticket_center_client.requests;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TicketCenterRESTClient {

    private static TicketCenterRESTClient instance;

    private TicketCenterAPI ticketCenterAPI;

    private TicketCenterRESTClient() {

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(TicketCenterAPI.API_URL)
                .build();

        this.ticketCenterAPI = retrofit.create(TicketCenterAPI.class);
    }

    public static TicketCenterRESTClient getInstance() {
        if (instance == null) {
            instance = new TicketCenterRESTClient();
        }
        return instance;
    }

    public TicketCenterAPI getTicketCenterAPI() {
        return ticketCenterAPI;
    }
}
