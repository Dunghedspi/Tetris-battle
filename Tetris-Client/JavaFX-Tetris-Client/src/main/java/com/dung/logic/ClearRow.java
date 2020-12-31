package com.dung.logic;

public final class ClearRow {

    private final int linesRemoved;
    private final int[][] newMatrix;
    private final int scoreBonus;
    private final int row8;

    public ClearRow(int linesRemoved, int[][] newMatrix, int scoreBonus, int row8) {
        this.linesRemoved = linesRemoved;
        this.newMatrix = newMatrix;
        this.scoreBonus = scoreBonus;
        this.row8 = row8;
    }

    public int getLinesRemoved() {
        return linesRemoved;
    }

    public int[][] getNewMatrix() {
        return MatrixOperations.copy(newMatrix);
    }

    public int getScoreBonus() {
        return scoreBonus;
    }

    public int getRow8() {
        return this.row8;
    }
}
