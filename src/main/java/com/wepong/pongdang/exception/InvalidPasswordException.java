package com.wepong.pongdang.exception;

public class InvalidPasswordException extends AuthException {
	public InvalidPasswordException() {
		super(ExceptionMessage.INVALID_PASSWORD);
	}
}