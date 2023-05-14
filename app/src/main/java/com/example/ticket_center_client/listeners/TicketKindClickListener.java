package com.example.ticket_center_client.listeners;

import tuvarna.ticket_center_common.models.TicketKindModel;

public interface TicketKindClickListener {

    void onClick(TicketKindModel ticketKind);

    void onEdit(TicketKindModel ticketKindModel);
}
