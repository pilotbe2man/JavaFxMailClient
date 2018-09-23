package com.jinyuan.controller;

import com.jinyuan.view.ViewFactory;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class EmailDetailController extends AbstractController implements Initializable {

    @FXML
    VBox rootVBox;

    @FXML
    Button replyInContentButton;

    @FXML
    Button replyAllInContentButton;

    @FXML
    Button forwardInContentButton;

    @FXML
    WebView mailContentWebView;

    @FXML
    Label fromLabel;

    @FXML
    Label subjectLabel;

    @FXML
    Label dateLabel;

    @FXML
    FlowPane toFollowPane;

    @FXML
    FlowPane ccFlowPane;

    @FXML
    Label ccLabel;

    @FXML
    Label mailSecurityClassLabel;

    @FXML
    FlowPane attachFlowPane;

    @FXML
    Label favLabel;

    boolean isFav;

    PrototypeController.MailItem mMailItem;

    public EmailDetailController(ModelAccess modelAccess, PrototypeController.MailItem aItem) {
        super(modelAccess);
        mMailItem = aItem;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        replyInContentButton.setGraphic(ViewFactory.defaultFactory.resolveIconWithName("/com/jinyuan/view/images/mail_reply.png"));
        replyAllInContentButton.setGraphic(ViewFactory.defaultFactory.resolveIconWithName("/com/jinyuan/view/images/reply_to_all.png"));
        forwardInContentButton.setGraphic(ViewFactory.defaultFactory.resolveIconWithName("/com/jinyuan/view/images/forward.png"));

        fromLabel.setText("<" + mMailItem.fromProperty().getValue() + ">");
        subjectLabel.setText(mMailItem.subjectProperty().getValue());
        dateLabel.setText(mMailItem.receivedDateProperty().getValue());

        //for test code
        for (int i = 0 ; i < 5; i++) {
            Label to1 = new Label(mMailItem.toProperty().getValue());
            if (i > 0)
                to1.setText(",  " + mMailItem.toProperty().getValue());
            toFollowPane.getChildren().add(to1);
        }


        //for test code
        if (!mMailItem.isCC()) {
            ccFlowPane.getChildren().remove(ccLabel);
        } else {
            for (int i = 0 ; i < 5; i++) {
                Label to1 = new Label(mMailItem.toProperty().getValue());
                if (i > 0)
                    to1.setText(",  " + mMailItem.toProperty().getValue());
                ccFlowPane.getChildren().add(to1);
            }
        }

        favLabel.setText("");
        isFav = mMailItem.followProperty().getValue();
        setFav();

        if (mMailItem.categoryProperty().getValue().isEmpty()) {
            mailSecurityClassLabel.setText("[Class 1]");
            mailSecurityClassLabel.getStyleClass().add("mail_security_class_1");
        } else {
            //classified mail 1 ~ 6
            mailSecurityClassLabel.setText("[Class 2]");
            mailSecurityClassLabel.getStyleClass().add("mail_security_class_2");
            mailSecurityClassLabel.setText("[Class 3]");
            mailSecurityClassLabel.getStyleClass().add("mail_security_class_3");
            mailSecurityClassLabel.setText("[Class 4]");
            mailSecurityClassLabel.getStyleClass().add("mail_security_class_4");
            mailSecurityClassLabel.setText("[Class 5]");
            mailSecurityClassLabel.getStyleClass().add("mail_security_class_5");
            mailSecurityClassLabel.setText("[Class 6]");
            mailSecurityClassLabel.getStyleClass().add("mail_security_class_6");
        }

        //attach controls
        if (mMailItem.attachProperty().getValue()) {
            //one attach file
            String[] extAry = new String[] {
                    ".png",
                    ".pdf",
                    ".rar",
                    ".exe",
                    ".mp4",
            };

            for (int i = 0 ; i < 5; i++) {
                SplitMenuButton attachButton1 = new SplitMenuButton();

//                attachButton1.setGraphic(ViewFactory.defaultFactory.resolveIconWithName("images/attach.png"));
                String fileName = "TestSampleFile" + extAry[i];
                ImageView imgV = getFileIcon(new File(fileName));
                attachButton1.setGraphic(imgV);

                attachButton1.getStyleClass().add("attach-menu-button");

                attachButton1.setText(fileName);

                MenuItem actionItemView = new MenuItem();
                MenuItem actionItemSaveAs = new MenuItem();
                MenuItem actionItemOpenFolder = new MenuItem();
                MenuItem actionItemRemove = new MenuItem();

                actionItemView.setText("Open");
                actionItemSaveAs.setText("Save as");
                actionItemOpenFolder.setText("Open Folder");
                actionItemRemove.setText("Remove");

                attachButton1.getItems().add(actionItemView);
                attachButton1.getItems().add(actionItemSaveAs);
                attachButton1.getItems().add(actionItemOpenFolder);
                attachButton1.getItems().add(actionItemRemove);

                actionItemRemove.setOnAction(new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent t) {
                        System.out.println("Delete action on the [" + fileName + "] file");

                        remoteAttachFile((File) imgV.getUserData());
                    }
                });

                HBox box= new HBox();
                box.setSpacing(5) ;
                box.setAlignment(Pos.CENTER_LEFT);
                Label attachClassLabel = new Label();
                attachClassLabel.setText("[Class " + (i + 1) + "]");
                attachClassLabel.getStyleClass().add("mail_attach_security_class_1");
                box.getChildren().addAll(attachButton1, attachClassLabel);

                box.setUserData(imgV.getUserData());
                attachFlowPane.setHgap(5);
                attachFlowPane.setPadding(new Insets(5, 0, 5,0));
                attachFlowPane.getChildren().add(box);
            }
        }

        WebEngine engine = mailContentWebView.getEngine();
        String url = "http://java2s.com/";
        engine.load(url);
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

    @FXML
    public void handleOnFavoriteButton() {
        isFav = !isFav;
        setFav();
    }

    public void setFav() {
        if (isFav) {
            favLabel.setGraphic(ViewFactory.defaultFactory.resolveIconWithName("images/fav_yes.png"));
        } else {
            favLabel.setGraphic(ViewFactory.defaultFactory.resolveIconWithName("images/fav_no.png"));
        }
    }
}
