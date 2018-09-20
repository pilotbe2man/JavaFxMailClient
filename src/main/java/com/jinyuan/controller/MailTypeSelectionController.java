package com.jinyuan.controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.jinyuan.view.ViewFactory;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class MailTypeSelectionController extends AbstractController implements Initializable{
	

    @FXML
    private RadioButton mail_qq;
    
    @FXML
    private RadioButton mail_163;
    
    @FXML
    private RadioButton mail_126;
    
    @FXML
    private RadioButton mail_sina;
    
    @FXML
    private RadioButton mail_hotmail;
    
    @FXML
    private RadioButton mail_gmail;
    
    @FXML
    private RadioButton mail_foxmail;
    
    @FXML
    private RadioButton mail_139;
    
    @FXML
    private RadioButton mail_sohu;
    
    @FXML
    private RadioButton mail_outlook;
    
    @FXML
    private RadioButton mail_other;
    
    @FXML
    private ImageView icon;
    
    final ToggleGroup group = new ToggleGroup();
    
    @FXML
    void continueBtnAction() throws Exception {

    	//TODO add validation
    	Stage stage = (Stage)mail_qq.getScene().getWindow();
    	stage.setScene(ViewFactory.defaultFactory.getAddAccountScene(group.getSelectedToggle().getUserData().toString()));
    }
    
    @FXML
    void cancelBtnAction() {
    	
    	//TODO add validation
    	Stage stage = (Stage)mail_qq.getScene().getWindow();
    	stage.close();
    }

    
	public MailTypeSelectionController(ModelAccess modelAccess) {
		
		super(modelAccess);
		
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		mail_qq.setUserData("qq");
		mail_163.setUserData("163");
		mail_126.setUserData("126");
		mail_sina.setUserData("sina");
		mail_hotmail.setUserData("hotmail");
		mail_gmail.setUserData("gmail");
		mail_foxmail.setUserData("fox");
		mail_139.setUserData("139");
		mail_sohu.setUserData("sohu");
		mail_outlook.setUserData("outlook");
		mail_other.setUserData("other");
		
		mail_qq.setToggleGroup(group);
		mail_163.setToggleGroup(group);
		mail_126.setToggleGroup(group);
		mail_sina.setToggleGroup(group);
		mail_hotmail.setToggleGroup(group);
		mail_gmail.setToggleGroup(group);
		mail_foxmail.setToggleGroup(group);
		mail_139.setToggleGroup(group);
		mail_sohu.setToggleGroup(group);
		mail_outlook.setToggleGroup(group);
		mail_other.setToggleGroup(group);
		
		group.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
		    public void changed(ObservableValue<? extends Toggle> ov,
		        Toggle old_toggle, Toggle new_toggle) {
		            if (group.getSelectedToggle() != null) {
		                final Image image =  ViewFactory.defaultFactory.emailIcon(group.getSelectedToggle().getUserData().toString());
		                icon.setImage(image);
		                icon.setFitHeight(170);
		                icon.setFitWidth(170);
		            }                
		        }
		});
		
		mail_qq.setSelected(true);

	}

}
