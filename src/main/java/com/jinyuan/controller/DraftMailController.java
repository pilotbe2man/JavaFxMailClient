package com.jinyuan.controller;

import com.jinyuan.view.ViewFactory;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class DraftMailController extends AbstractController implements Initializable {

    @FXML
    Label errorLabel;

    @FXML
    TextField toTextField;

    @FXML
    TextField ccTextField;

    @FXML
    TextField bccTextField;

    @FXML
    FlowPane attachFlowPane;

    @FXML
    RadioButton mailSecurityClassRadioButton1;

    @FXML
    RadioButton mailSecurityClassRadioButton2;

    @FXML
    RadioButton mailSecurityClassRadioButton3;

    @FXML
    RadioButton mailSecurityClassRadioButton4;

    @FXML
    RadioButton mailSecurityClassRadioButton5;

    @FXML
    RadioButton mailSecurityClassRadioButton6;


    String[] mAttachFileSecurityClass = new String[] {
            "Class 1",
            "Class 2",
            "Class 3",
            "Class 4",
            "Class 5",
            "Class 6"
    };

    final ToggleGroup group = new ToggleGroup();

    public DraftMailController(ModelAccess modelAccess) {
        super(modelAccess);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        mailSecurityClassRadioButton1.setToggleGroup(group);
        mailSecurityClassRadioButton2.setToggleGroup(group);
        mailSecurityClassRadioButton3.setToggleGroup(group);
        mailSecurityClassRadioButton4.setToggleGroup(group);
        mailSecurityClassRadioButton5.setToggleGroup(group);
        mailSecurityClassRadioButton6.setToggleGroup(group);
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
        System.out.println("Clicked on actionOnAttachButton");

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        File selected = fileChooser.showOpenDialog(mailSecurityClassRadioButton1.getScene().getWindow());
        System.out.println("selected file = " + selected.getAbsolutePath());

        attachFlowPane.setHgap(5);
        attachFlowPane.setPadding(new Insets(5, 0, 5,0));
        attachFlowPane.getChildren().add(getAttachFile(selected));
    }

    /**
     * @author pilot
     * remove the attach file
     * @param aFile
     */
    public void remoteAttachFile(File aFile) {
        int iSize = attachFlowPane.getChildren().size();
        for (int i = 0; i < iSize; i++) {
            Node eachNode = attachFlowPane.getChildren().get(i);
            if (((File)eachNode.getUserData()).getAbsolutePath().equalsIgnoreCase(aFile.getAbsolutePath())) {
                attachFlowPane.getChildren().remove(i);
                break;
            }
        }
    }

    public Node getAttachFile(File aFile) {
        SplitMenuButton attachButton1 = new SplitMenuButton();

        String fileName = aFile.getName();
        attachButton1.setGraphic(getFileIcon(aFile));

        attachButton1.getStyleClass().add("attach-menu-button");

        attachButton1.setText(fileName);

        MenuItem actionItemView = new MenuItem();
        MenuItem actionItemSaveAs = new MenuItem();
        MenuItem actionItemOpenFolder = new MenuItem();
        MenuItem actionItemRemove = new MenuItem();

        actionItemRemove.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                System.out.println("Delete action on the [" + aFile.getName() + "] file");

                remoteAttachFile(aFile);
            }
        });

        actionItemView.setText("Open");
        actionItemSaveAs.setText("Save as");
        actionItemOpenFolder.setText("Open Folder");
        actionItemRemove.setText("Remove");

        attachButton1.getItems().add(actionItemView);
        attachButton1.getItems().add(actionItemSaveAs);
        attachButton1.getItems().add(actionItemOpenFolder);
        attachButton1.getItems().add(actionItemRemove);

        HBox box= new HBox();
        box.setSpacing(5) ;
        box.setAlignment(Pos.CENTER_LEFT);

        ComboBox attachClassLabel = new ComboBox();
        attachClassLabel.getItems().addAll(mAttachFileSecurityClass);
        attachClassLabel.getSelectionModel().select(0);
        attachClassLabel.getStyleClass().add("mail_attach_security_class_1");
        attachClassLabel.getStyleClass().add("attach-menu-button");

        System.out.println("combox class = " + attachClassLabel.getStyleClass().toString());

        attachClassLabel.valueProperty().addListener((obs, oldItem, newItem) -> {
            attachClassLabel.getEditor().getStyleClass().add("mail_attach_security_class_" + attachClassLabel.getSelectionModel().getSelectedIndex());
        });

        box.setUserData(aFile);
        box.getChildren().addAll(attachButton1, attachClassLabel);

        return box;
    }

    @FXML
    public void actionOnSendButton() {

    }
}
