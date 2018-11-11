package com.daahae.damoyeo.exception;

public class PositionNumberException extends Exception {

    public PositionNumberException(String msg){
        super(msg);
    }

    public PositionNumberException(Exception ex){
        super(ex);
    }
}
