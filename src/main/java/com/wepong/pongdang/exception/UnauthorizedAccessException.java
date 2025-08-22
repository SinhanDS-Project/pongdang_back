package com.wepong.pongdang.exception;

public class UnauthorizedAccessException extends AuthException {
	public UnauthorizedAccessException() {
		super(ExceptionMessage.UNAUTHORIZED_ACCESS);
	}
}
