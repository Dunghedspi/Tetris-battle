package com.dung.eventResponse;

public class RowsResponseEvent {
	String numberRows;

	public RowsResponseEvent(String numberRows) {
		this.numberRows = numberRows;
	}

	public String getNumberRows() {
		return numberRows;
	}

	public void setNumberRows(String numberRows) {
		this.numberRows = numberRows;
	}
}
