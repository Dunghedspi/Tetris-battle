package com.dung.app;

import com.dung.eventRequest.RowsEvent;
import com.dung.eventResponse.RowsResponseEvent;
import com.dung.gui.GuiController;
import com.dung.lib.EventBusCustom;
import com.dung.logic.*;
import com.dung.logic.events.EventSource;
import com.dung.logic.events.InputEventListener;
import com.dung.logic.events.MoveEvent;
import com.google.common.eventbus.Subscribe;

import java.util.Random;

public class GameController extends EventBusCustom implements InputEventListener {

	private final Board board = new SimpleBoard(25, 10);

	private final GuiController viewGuiController;

	public GameController(GuiController c) {
		attach(this);
		viewGuiController = c;
		board.createNewBrick();
		viewGuiController.setEventListener(this);
		viewGuiController.initGameView(board.getBoardMatrix(), board.getViewData());
		viewGuiController.bindScore(board.getScore().scoreProperty());
	}

	@Override
	public DownData onDownEvent(MoveEvent event) {
		boolean canMove = board.moveBrickDown();
		ClearRow clearRow = null;
		if (!canMove) {
			board.mergeBrickToBackground();
			clearRow = board.clearRows();
			if (clearRow.getLinesRemoved() > 0) {
				board.getScore().add(clearRow.getScoreBonus());
			}
			if (board.createNewBrick()) {
				viewGuiController.gameOver();
			}
			if (clearRow.getLinesRemoved() != 0) sendRows(clearRow.getLinesRemoved()-clearRow.getRow8());
			viewGuiController.refreshGameBackground(board.getBoardMatrix());
		} else {
			if (event.getEventSource() == EventSource.USER) {
				board.getScore().add(1);
			}
		}
		return new DownData(clearRow, board.getViewData());
	}

	public void sendRows(int numberRows) {
		postEvent(new RowsEvent(String.valueOf(numberRows)));
	}

	@Override
	public ViewData onLeftEvent(MoveEvent event) {
		board.moveBrickLeft();
		return board.getViewData();
	}

	@Override
	public ViewData onRightEvent(MoveEvent event) {
		board.moveBrickRight();
		return board.getViewData();
	}

	@Override
	public ViewData onRotateEvent(MoveEvent event) {
		board.rotateLeftBrick();
		return board.getViewData();
	}
	
	@Override
	public void createNewGame() {
		board.newGame();
		viewGuiController.refreshGameBackground(board.getBoardMatrix());
	}

	@Subscribe
	public void addRowsToClient(RowsResponseEvent rowsEvent) {
		board.addRowsToClient(rowsEvent);
		viewGuiController.refreshGameBackground(board.getBoardMatrix());
	}
}
