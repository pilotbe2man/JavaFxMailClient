package com.jinyuan.controller;

import com.jinyuan.view.ViewFactory;
import com.pixelduke.control.Ribbon;
import com.pixelduke.control.ribbon.QuickAccessBar;
import com.pixelduke.control.ribbon.RibbonTab;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class DraftMailController extends AbstractController implements Initializable {

    @FXML
    Label errorLabel;

    @FXML
    Label attachNamesLabel;

    @FXML
    TextField toTextField;

    @FXML
    TextField ccTextField;

    @FXML
    TextField bccTextField;

    public DraftMailController(ModelAccess modelAccess) {
        super(modelAccess);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        createRibbon();
    }

    public void createRibbon() {
    }

    public void fetchRecipient(Scene scene) {
        FXMLLoader loader = (FXMLLoader) scene.getUserData();
        ChooseUserController chooseUserController = loader.getController();
        setEmailToField(toTextField, chooseUserController.getToValues());
        setEmailToField(ccTextField, chooseUserController.getCcValues());
        setEmailToField(bccTextField, chooseUserController.getBccValues());
    }

    public void setRecipient(Scene scene) {
        FXMLLoader loader = (FXMLLoader) scene.getUserData();
        ChooseUserController chooseUserController = loader.getController();
        chooseUserController.setToValues(toTextField.getText());
        chooseUserController.setCcValues(ccTextField.getText());
        chooseUserController.setBccValues(bccTextField.getText());
    }

    @FXML
    public void actionOnToButton() {
        Scene scene = ViewFactory.defaultFactory.getChooseUserScene();
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        setRecipient(scene);
        stage.showAndWait();
        fetchRecipient(scene);
    }

    @FXML
    public void actionOnCcButton() {
        Scene scene = ViewFactory.defaultFactory.getChooseUserScene();
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        setRecipient(scene);
        stage.showAndWait();
        fetchRecipient(scene);
    }

    @FXML
    public void actionOnBccButton() {
        Scene scene = ViewFactory.defaultFactory.getChooseUserScene();
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        setRecipient(scene);
        stage.showAndWait();
        fetchRecipient(scene);
    }

    @FXML
    public void actionOnAttachButton() {

    }

    @FXML
    public void actionOnSendButton() {

    }
}
