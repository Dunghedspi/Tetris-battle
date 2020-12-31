package com.dung.network;

import java.io.Serializable;

public class Request implements Serializable {
    private static final long serialVersionUID = 1L;
    private String type;
    private String data;

    public Request(String type, String data) {
        this.type = type;
        this.data = data;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
