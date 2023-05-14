package com.example.ticket_center_client.requests;

import com.google.gson.Gson;

public class ResponseDeserializer {

    public static <T> T deserialize(Object response, Class<T> type) {
        Gson gson = new Gson();
        return gson.fromJson(gson.toJson(response), type);
    }
}
