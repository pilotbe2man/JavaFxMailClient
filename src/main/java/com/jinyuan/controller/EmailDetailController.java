package com.jinyuan.controller;

import com.jinyuan.view.ViewFactory;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.net.URL;
import java.util.ResourceBundle;

public class EmailDetailController extends AbstractController implements Initializable {

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
    Label receivedDateLabel;

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
        receivedDateLabel.setText(mMailItem.receivedDateProperty().getValue());

        WebEngine engine = mailContentWebView.getEngine();
        String url = "http://java2s.com/";
        engine.load(url);
    }

}
