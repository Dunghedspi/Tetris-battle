package com.dung.network;

import java.io.Serializable;

public class Response implements Serializable {
    private StatusResponse status;
    private String data;

    public Response(StatusResponse status, String data) {
        this.status = status;
        this.data = data;
    }
    public StatusResponse getStatus() {
        return status;
    }
    public String getData() {
        return data;
    }
}
