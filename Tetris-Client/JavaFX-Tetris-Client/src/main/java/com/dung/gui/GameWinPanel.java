package com.dung.gui;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

public class GameWinPanel extends BorderPane {
	public GameWinPanel() {
		final Label gameWinLabel = new Label(" GANE WIN  ");
		gameWinLabel.getStyleClass().add("gameOverStyle");
		setCenter(gameWinLabel);
	}
}
