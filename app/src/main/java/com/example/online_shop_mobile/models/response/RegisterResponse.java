package com.example.online_shop_mobile.models.response;

import com.example.online_shop_mobile.models.User;

public class RegisterResponse {
    private boolean error;
    private String message;

    public RegisterResponse(boolean error, String message) {
        this.error = error;
        this.message = message;
    }

    public boolean isError() {
        return error;
    }

    public String getMessage() {
        return message;
    }
}
