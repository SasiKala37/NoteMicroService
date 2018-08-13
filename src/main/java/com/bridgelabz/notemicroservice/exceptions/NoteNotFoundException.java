package com.bridgelabz.notemicroservice.exceptions;

public class NoteNotFoundException extends Exception {

	private static final long serialVersionUID = -2212157402347668282L;

	public NoteNotFoundException(String message) {
		super(message);
	}
}
