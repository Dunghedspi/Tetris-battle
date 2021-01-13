package com.dung.gui;

import com.dung.eventRequest.ExitGameEvent;
import com.dung.socket.ClientTcp;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class Main extends Application {
	private ClientTcp clientSocket;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		//start socket
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				try {
					clientSocket = new ClientTcp();
				} catch (Exception e) {
					Platform.runLater(() -> connectError());
				}
			}
		};
		Thread newThread = new Thread(runnable);
		newThread.setDaemon(true);
		newThread.start();

		URL location = getClass().getClassLoader().getResource("startLayout.fxml");
		ResourceBundle resources = null;
		FXMLLoader fxmlLoader = new FXMLLoader(location, resources);
		Parent root = fxmlLoader.load();
		primaryStage.setTitle("Tetris Battle");
		Scene scene = new Scene(root, 410, 510);
		primaryStage.setScene(scene);
		primaryStage.setOnCloseRequest(e -> {
			clientSocket.getSocketListener().postEvent(new ExitGameEvent());
			Platform.exit();
		});
		primaryStage.show();
	}

	public void connectError() {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setTitle("Error");
		alert.setContentText("Sorry!Could not connect to the server!");
		ButtonType buttonType = new ButtonType("Yes", ButtonBar.ButtonData.YES);
		alert.getButtonTypes().setAll(buttonType);
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == buttonType) {
			Platform.exit();
		}
		alert.show();
	}
}