package com.bridgelabz.notemicroservice.exceptions;

public class NoteCreationException extends Exception{
	
	private static final long serialVersionUID = 1L;

	public NoteCreationException(String message) {
		super(message);
	}
}
