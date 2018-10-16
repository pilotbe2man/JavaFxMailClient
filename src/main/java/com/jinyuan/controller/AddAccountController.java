package com.jinyuan.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javax.naming.OperationNotSupportedException;

import com.jinyuan.controller.services.CreateAndRegisterEmailAccountService;
import com.jinyuan.model.EmailConstants;
import com.jinyuan.view.ViewFactory;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddAccountController extends AbstractController implements Initializable{
	

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField addressField;
    
    @FXML
    private Label statusLabel;
    
    void clearStatusLabel() {
    	new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					Thread.sleep(2000);
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							statusLabel.setText("");
						}
					});
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
    }
    
    @FXML
    void cancelBtnAction() {
    	Stage stage = (Stage)addressField.getScene().getWindow();
    	stage.setScene(ViewFactory.defaultFactory.getMailTypeSelectionScene());
    }
    
    @FXML
    void loginBtnAction() {

    	//TODO add validation
    	if (addressField.getText().isEmpty()) {
    		statusLabel.setText("Please input the email address...");
    		clearStatusLabel();
    		return;
    	}
    	
    	if (!isValidateEmail(addressField.getText())) {
    		statusLabel.setText("Invalidate email addres...");
    		clearStatusLabel();
    		return;
    	}
    	
    	if (passwordField.getText().isEmpty()) {
    		statusLabel.setText("Please input the password...");
    		clearStatusLabel();
    		return;
    	}
    	
    	//TODO add props handling
    	statusLabel.setText("");
    	ViewFactory.isLogout = false;
    	ViewFactory.mainViewInitialized = false;
    	
    	CreateAndRegisterEmailAccountService createAndRegisterEmailAccountService = 
    			new CreateAndRegisterEmailAccountService(
    					addressField.getText(),
    					passwordField.getText(), 
    					getModelAccess());
    	createAndRegisterEmailAccountService.start();
    	statusLabel.setText("logging in....");
    	createAndRegisterEmailAccountService.setOnSucceeded(e->{
    		if(createAndRegisterEmailAccountService.getValue() != EmailConstants.LOGIN_STATE_SUCCEDED){
    			statusLabel.setText("an error occured...");
    		}else{
    			Stage stage = (Stage)addressField.getScene().getWindow();//just getting a reference to the stage

    			if(ViewFactory.mainViewInitialized){
        			//close the window
        			stage.close();
    			} else{
    				try {
						stage.setScene(ViewFactory.defaultFactory.getMainScene());
					} catch (OperationNotSupportedException e1) {
						e1.printStackTrace();
					}
    			}
    		}
    	});
    }

	public AddAccountController(ModelAccess modelAccess) {
		super(modelAccess);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
	}

}
