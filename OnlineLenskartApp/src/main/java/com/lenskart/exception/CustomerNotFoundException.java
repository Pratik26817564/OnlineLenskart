package com.lenskart.exception;

public class CustomerNotFoundException extends Exception {
	private static final long serialVersionUID = 1L;

	public CustomerNotFoundException()
	{
		super();
		
	}

	public CustomerNotFoundException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}
}
