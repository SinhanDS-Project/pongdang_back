package com.wepong.pongdang.exception;

public class InvalidTokenException extends AuthException {
	public InvalidTokenException() {
		super(ExceptionMessage.INVALID_TOKEN);
	}
}
