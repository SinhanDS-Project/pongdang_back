package com.wepong.pongdang.exception;

public class SessionExpiredException extends AuthException {
	public SessionExpiredException() {
		super(ExceptionMessage.SESSION_EXPIRED);
	}
}
