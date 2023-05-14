package com.example.ticket_center_client.services;

import com.example.ticket_center_client.requests.APICallListener;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tuvarna.ticket_center_common.responses.CommonResponse;

public class BaseService {

    private final APICallListener apiCallListener;

    public BaseService(APICallListener apiCallListener) {
        this.apiCallListener = apiCallListener;
    }

    protected void HandleCall(Call<CommonResponse> call) {

        call.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                if (response.isSuccessful()) {
                    getApiCallListener().onAPICallSuccess(response.body());
                } else {
                    log(response.errorBody().toString());
                    getApiCallListener().onAPICallFailure();
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                log(new Exception(t).toString());
                getApiCallListener().onAPICallFailure();
            }
        });
    }

    public APICallListener getApiCallListener() {
        return apiCallListener;
    }

    private void log(String message) {
    }
}
