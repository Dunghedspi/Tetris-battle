package com.dung.network;

import java.io.Serializable;

public class Request implements Serializable {
    private static final long serialVersionUID = 1L;
    private CodeRequest type;
    private String data;

    public Request(CodeRequest type, String data) {
        this.type = type;
        this.data = data;
    }

    public CodeRequest getType() {
        return type;
    }

    public void setType(CodeRequest type) {
        this.type = type;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
