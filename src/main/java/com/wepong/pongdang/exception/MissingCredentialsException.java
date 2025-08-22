package com.wepong.pongdang.exception;

public class MissingCredentialsException extends AuthException {
	public MissingCredentialsException() {
		super(ExceptionMessage.MISSING_CREDENTIALS);
	}
}
