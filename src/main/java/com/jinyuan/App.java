package com.jinyuan;

import com.jinyuan.controller.PrototypeController;
import com.jinyuan.view.ViewFactory;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class App extends Application{
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		Scene scene;
//		if(ViewFactory.defaultFactory.getPersistenceAcess().validPersistencefound()){
//			scene = ViewFactory.defaultFactory.getMainScene();
//		} else {
//			scene = ViewFactory.defaultFactory.getMailTypeSelectionScene();
//		}

		scene = ViewFactory.defaultFactory.getPrototypeScene();
		KeyCombination keyCombinationShiftC = new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_ANY);
		try {
			scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
				@Override
				public void handle(KeyEvent event) {
					if (keyCombinationShiftC.match(event)) {
						FXMLLoader loader = (FXMLLoader) scene.getUserData();
						PrototypeController prototypeController = loader.getController();
						prototypeController.handeClickedOnNewMailMenuButton();
					}
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}

		primaryStage.setScene(scene);
		primaryStage.show();				
		
	}
	
	@Override
	public void stop(){
		ViewFactory.defaultFactory.getPersistenceAcess().SavePersistence();
	}

}