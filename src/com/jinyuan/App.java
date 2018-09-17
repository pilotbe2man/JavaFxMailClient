package com.jinyuan;

import com.jinyuan.view.ViewFactory;

import javafx.application.Application;
import javafx.scene.Scene;
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
		primaryStage.setScene(scene);
		primaryStage.show();				
		
	}
	
	@Override
	public void stop(){
		ViewFactory.defaultFactory.getPersistenceAcess().SavePersistence();
	}

}