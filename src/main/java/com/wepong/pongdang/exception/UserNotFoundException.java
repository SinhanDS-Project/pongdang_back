package com.wepong.pongdang.exception;

public class UserNotFoundException extends AuthException {
	public UserNotFoundException() {
		super(ExceptionMessage.USER_NOT_FOUND);
	}
}
