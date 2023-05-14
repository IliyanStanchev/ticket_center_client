package com.example.ticket_center_client.requests;

import tuvarna.ticket_center_common.responses.CommonResponse;

public interface APICallListener {

    void onAPICallSuccess(CommonResponse response);

    void onAPICallFailure();
}
