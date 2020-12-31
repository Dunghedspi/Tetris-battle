package com.dung.gui;

import com.dung.socket.ClientTcp;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Main extends Application {
	static ClientTcp clientSocket;

	public static void main(String[] args) {
		launch(args);
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				clientSocket.interrupt();
			}
		});
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		//start socket
		clientSocket = new ClientTcp();
		clientSocket.start();
		
		URL location = getClass().getClassLoader().getResource("startLayout.fxml");
		ResourceBundle resources = null;
		FXMLLoader fxmlLoader = new FXMLLoader(location, resources);
		Parent root = fxmlLoader.load();
		primaryStage.setTitle("TetrisJFX");
		Scene scene = new Scene(root, 410, 510);
		primaryStage.setScene(scene);
		primaryStage.setOnCloseRequest(e -> {
			try {
				clientSocket.close();
			} catch (IOException ioException) {
				ioException.printStackTrace();
			}
			clientSocket.interrupt();
			Platform.exit();
		});
		primaryStage.show();
	}
}