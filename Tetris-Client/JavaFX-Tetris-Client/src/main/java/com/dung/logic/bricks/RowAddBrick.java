package com.dung.logic.bricks;

import com.dung.logic.MatrixOperations;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RowAddBrick implements Brick{
	private final List<int[][]> brickMatrix = new ArrayList<>();
	public RowAddBrick(int numberRows){
		Random rd = new Random();
		int index = rd.nextInt(10);
		int [][] matrix = new int [numberRows][10];
		for(int i = 0; i<numberRows; i++){
			for(int j = 0; j<10; j++){
				if(j == index){
					matrix[i][j] = 0;
				}else{
					matrix[i][j] = 8;
				}
			}
		}
		brickMatrix.add(matrix);
	}
	@Override
	public List<int[][]> getShapeMatrix() {
		return MatrixOperations.deepCopyList(brickMatrix);
	}
}
