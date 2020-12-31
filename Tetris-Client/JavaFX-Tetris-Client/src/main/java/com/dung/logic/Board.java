package com.dung.logic;

import com.dung.eventResponse.RowsResponseEvent;

public interface Board {

	boolean moveBrickDown();

	boolean moveBrickLeft();

	boolean moveBrickRight();

	boolean rotateLeftBrick();

    boolean createNewBrick();

    int[][] getBoardMatrix();

	ViewData getViewData();

	void mergeBrickToBackground();

	ClearRow clearRows();

	Score getScore();

	void newGame();

	void addToTheBottomOfTheBoard(int numberRows);

	public void addRowsToClient(RowsResponseEvent rowsEvent);

}
