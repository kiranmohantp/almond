package com.conifer.pipelinejobs.bots.telegram.exceptions;

public class UnauthorizedAccessException extends Exception{
    public UnauthorizedAccessException(String message) {
        super(message);
    }
}
