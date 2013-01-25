package com.gengfo.exception;

public class MultiKeyFieldException extends Exception{
	
	public MultiKeyFieldException(){
		super("Has more than one key filed in table");
	}

}
