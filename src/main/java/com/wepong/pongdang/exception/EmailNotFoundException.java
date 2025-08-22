package com.wepong.pongdang.exception;

public class EmailNotFoundException extends AuthException {
	public EmailNotFoundException() {
		super(ExceptionMessage.EMAIL_NOT_FOUND);
	}
}
