package com.dung.gui;

import com.google.common.eventbus.Subscribe;
import com.dung.eventRequest.LoginEvent;
import com.dung.eventResponse.LoginStatusResponseEvent;
import com.dung.lib.EventBusCustom;
import com.dung.logic.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class GuiControllerStart extends EventBusCustom {
	public GuiControllerStart() {
		attach(this);
	}

	@FXML
	private TextField username;
	@FXML
	private Button startGameButton;
	@FXML
	private Label title;

	public void handeClickButton() {
		if (username != null && username.getText().trim().length() > 0) {
			sendUserName();
		}
	}

	public void sendUserName() {
		 postEvent(new LoginEvent(username.getText()));
	}
	
	@Subscribe
	public void recvStatusLogin(LoginStatusResponseEvent loginStatus) {
		try {
			Stage primaryStage = (Stage) username.getScene().getWindow();
			URL location = getClass().getClassLoader().getResource("homeLayout.fxml");
			ResourceBundle resources = null;
			FXMLLoader fxmlLoader = new FXMLLoader(location, resources);
			Parent root = fxmlLoader.load();
			HomeGuiController c = fxmlLoader.getController();
			c.setUser(new User(username.getText()));
			Scene scene = new Scene(root, 410, 510);
			detach(this);
			primaryStage.setScene(scene);
		} catch (IOException e){
			e.getMessage();
		}
	}
}