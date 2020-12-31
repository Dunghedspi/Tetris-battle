package com.dung.eventRequest;

public class RowsEvent {
	String numberRows;
	public RowsEvent(String numberRows){
		this.numberRows = numberRows;		
	}

	public String getNumberRows() {
		return numberRows;
	}
}