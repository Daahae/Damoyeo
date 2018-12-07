package com.daahae.damoyeo.exception;

public class ExceptionHandle extends Exception {

    public ExceptionHandle(String msg){
        super(msg);
    }

    public ExceptionHandle(Exception ex){
        super(ex);
    }
}
