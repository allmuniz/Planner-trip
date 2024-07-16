package com.project.planner.exceptions;

public class IncorrectDateException extends RuntimeException{
    public IncorrectDateException(String message){
        super(message);
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
