package com.dung.logic;

import com.dung.eventResponse.RowsResponseEvent;
import com.dung.lib.EventBusCustom;
import com.google.common.eventbus.Subscribe;
import com.dung.eventRequest.RowsEvent;
import com.dung.logic.bricks.Brick;
import com.dung.logic.bricks.BrickGenerator;
import com.dung.logic.bricks.RandomBrickGenerator;
import com.dung.logic.bricks.RowAddBrick;
import com.dung.logic.rotator.BrickRotator;
import com.dung.logic.rotator.NextShapeInfo;

import java.awt.*;

public class SimpleBoard extends EventBusCustom implements Board {

	private final int width;
	private final int height;
	private final BrickGenerator brickGenerator;
	private final BrickRotator brickRotator;
	private int[][] currentGameMatrix;
	private Point currentOffset;
	private final Score score;
	private int numberRows;

	public SimpleBoard(int width, int height) {
		this.width = width;
		this.height = height;
		currentGameMatrix = new int[width][height];
		brickGenerator = new RandomBrickGenerator();
		brickRotator = new BrickRotator();
		score = new Score();
		attach(this);
	}

	@Override
	public boolean moveBrickDown() {
		int[][] currentMatrix = MatrixOperations.copy(currentGameMatrix);
		Point p = new Point(currentOffset);
		p.translate(0, 1);
		boolean conflict = MatrixOperations.intersect(currentMatrix, brickRotator.getCurrentShape(), (int) p.getX(), (int) p.getY());
		if (conflict) {
			return false;
		} else {
			currentOffset = p;
			this.numberRows = 0;
			return true;
		}
	}


	@Override
	public boolean moveBrickLeft() {
		int[][] currentMatrix = MatrixOperations.copy(currentGameMatrix);
		Point p = new Point(currentOffset);
		p.translate(-1, 0);
		boolean conflict = MatrixOperations.intersect(currentMatrix, brickRotator.getCurrentShape(), (int) p.getX(), (int) p.getY());
		if (conflict) {
			return false;
		} else {
			currentOffset = p;
			return true;
		}
	}

	@Override
	public boolean moveBrickRight() {
		int[][] currentMatrix = MatrixOperations.copy(currentGameMatrix);
		Point p = new Point(currentOffset);
		p.translate(1, 0);
		boolean conflict = MatrixOperations.intersect(currentMatrix, brickRotator.getCurrentShape(), (int) p.getX(), (int) p.getY());
		if (conflict) {
			return false;
		} else {
			currentOffset = p;
			return true;
		}
	}

	@Override
	public boolean rotateLeftBrick() {
		int[][] currentMatrix = MatrixOperations.copy(currentGameMatrix);
		NextShapeInfo nextShape = brickRotator.getNextShape();
		boolean conflict = MatrixOperations.intersect(currentMatrix, nextShape.getShape(), (int) currentOffset.getX(), (int) currentOffset.getY());
		if (conflict) {
			return false;
		} else {
			brickRotator.setCurrentShape(nextShape.getPosition());
			return true;
		}
	}

	@Override
	public boolean createNewBrick() {
		Brick currentBrick = brickGenerator.getBrick();
		brickRotator.setBrick(currentBrick);
		currentOffset = new Point(4, 0);
		return MatrixOperations.intersect(currentGameMatrix, brickRotator.getCurrentShape(), (int) currentOffset.getX(), (int) currentOffset.getY());
	}

	@Override
	public int[][] getBoardMatrix() {
		return currentGameMatrix;
	}

	@Override
	public ViewData getViewData() {
		return new ViewData(brickRotator.getCurrentShape(), (int) currentOffset.getX(), (int) currentOffset.getY(), brickGenerator.getNextBrick().getShapeMatrix().get(0));
	}

	@Override
	public void mergeBrickToBackground() {
		currentGameMatrix = MatrixOperations.merge(currentGameMatrix, brickRotator.getCurrentShape(), (int) currentOffset.getX(), (int) currentOffset.getY() - this.numberRows);
	}

	@Override
	public ClearRow clearRows() {
		ClearRow clearRow = MatrixOperations.checkRemoving(currentGameMatrix);
		currentGameMatrix = clearRow.getNewMatrix();
		return clearRow;
	}

	@Override
	public Score getScore() {
		return score;
	}


	@Override
	public void newGame() {
		currentGameMatrix = new int[width][height];
		score.reset();
		createNewBrick();
	}

	@Override
	public void addToTheBottomOfTheBoard(int numberRows) {
		Brick newBrick = new RowAddBrick(numberRows);
		currentGameMatrix = MatrixOperations.addRows(currentGameMatrix, newBrick.getShapeMatrix().get(0));
	}

	@Override
	public void addRowsToClient(RowsResponseEvent rowsEvent) {
		int numberRows = Integer.parseInt(rowsEvent.getNumberRows());
		addToTheBottomOfTheBoard(numberRows);
		if (MatrixOperations.intersect(currentGameMatrix, brickRotator.getCurrentShape(), (int) currentOffset.getX(), (int) currentOffset.getY())) {
			currentOffset.setLocation(currentOffset.getX(), currentOffset.getY() - numberRows);
			while (moveBrickDown()) ;
			mergeBrickToBackground();
		}
	}
}