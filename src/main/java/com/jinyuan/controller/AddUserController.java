package com.jinyuan.controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import com.jinyuan.controller.persistence.ValidAddressBook;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddUserController extends AbstractController implements Initializable{
	

    @FXML
    private TextField addressField;
    
    @FXML
    private Label statusLabel;
    
    @FXML
    void addBtnAction() {

    	//TODO add validation
    	String userAddress = addressField.getText();
    	
    	if (userAddress.isEmpty()) {
    		statusLabel.setText("Plase input the email addres...");
    		clearStatusLabel();
    		return;
    	}
    	
    	if (!isValidateEmail(userAddress)) {
    		statusLabel.setText("Invalidate email addres...");
    		clearStatusLabel();
    		return;
    	}
    	
    	//duplication check
    	List<ValidAddressBook> adbList = getModelAccess().getValidAddressBookList();
    	for (ValidAddressBook validAddressBook : adbList) {
			if (validAddressBook.getAddress().equalsIgnoreCase(userAddress)) {
				statusLabel.setText("Duplicated email addres...");
				clearStatusLabel();
				return;
			}
		}
    	
    	ValidAddressBook adb = new ValidAddressBook(userAddress);
    	getModelAccess().addUser(adb);
    	
    	Stage stage = (Stage)addressField.getScene().getWindow();
    	stage.close();
    }

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
    
	public AddUserController(ModelAccess modelAccess) {
		super(modelAccess);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
	}

}
