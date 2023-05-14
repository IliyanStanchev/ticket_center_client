package com.example.ticket_center_client.services;

import com.example.ticket_center_client.requests.APICallListener;
import com.example.ticket_center_client.requests.TicketCenterRESTClient;

import tuvarna.ticket_center_common.models.EventModel;
import tuvarna.ticket_center_common.models.PurchaseTicketModel;

public class EventService extends BaseService {


    public EventService(APICallListener apiCallListener) {
        super(apiCallListener);
    }

    public void createEvent(EventModel eventModel) {
        HandleCall(TicketCenterRESTClient.getInstance().getTicketCenterAPI().createEvent(eventModel));
    }

    public void getEvents(String email) {
        HandleCall(TicketCenterRESTClient.getInstance().getTicketCenterAPI().getEvents(email));
    }

    public void getTickets(Long eventId, String ticketKindName) {
        HandleCall(TicketCenterRESTClient.getInstance().getTicketCenterAPI().getTickets(eventId, ticketKindName));
    }

    public void checkInTicket(Long id) {
        HandleCall(TicketCenterRESTClient.getInstance().getTicketCenterAPI().checkInTicket(id));
    }

    public void purchaseTicket(PurchaseTicketModel purchaseTicketModel) {
        HandleCall(TicketCenterRESTClient.getInstance().getTicketCenterAPI().purchaseTicket(purchaseTicketModel));
    }

    public void checkOutTicket(Long id) {
        HandleCall(TicketCenterRESTClient.getInstance().getTicketCenterAPI().checkOutTicket(id));
    }

    public void getOrganizerEvents(String email) {
        HandleCall(TicketCenterRESTClient.getInstance().getTicketCenterAPI().getEventsByOrganizer(email));
    }
}
