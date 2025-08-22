package com.wepong.pongdang.exception;

public class InvalidUpdatePasswordException extends RuntimeException {
    public InvalidUpdatePasswordException() {
        super(ExceptionMessage.INVALID_UPDATE_PASSWORD);
    }
}