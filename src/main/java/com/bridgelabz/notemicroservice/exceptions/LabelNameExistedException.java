package com.bridgelabz.notemicroservice.exceptions;

public class LabelNameExistedException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public LabelNameExistedException(String message) {
		  super(message);
	  }
}
