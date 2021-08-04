package com.address.exception;

public class MongoDBException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public MongoDBException() {
		super();
	}

	public MongoDBException(String message, Throwable cause) {
		super(message, cause);
	}

	public MongoDBException(String message) {
		super(message);
	}

	public MongoDBException(Throwable cause) {
		super(cause);
	}

}
