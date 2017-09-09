package com.app.web;

/**
 * Created by Sergey on 08.09.2017.
 */
public class AccountAdapter {

    private int statusCode;
    private String message;

    public void success(){
        statusCode = 200;
    }

    public void clientError(){
        statusCode = 400;
    }

    public void serverError(){
        statusCode = 500;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
