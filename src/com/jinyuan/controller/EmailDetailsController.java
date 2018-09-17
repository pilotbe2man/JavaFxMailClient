package com.jinyuan.controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.jinyuan.controller.services.MessageRendererService;
import com.jinyuan.model.EmailMessageBean;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.web.WebView;

public class EmailDetailsController extends AbstractController implements Initializable {
	
	public EmailDetailsController(ModelAccess modelAccess) {
		super(modelAccess);
	}

    @FXML
    private WebView webView;
    @FXML
    private Label subjectLabel;
    @FXML
    private Label SenderLabel;
    
    private EmailMessageBean selectedMessage;
    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//TODO: implement delete function

		selectedMessage = getModelAccess().getSelectedMessage().copy();

		
		subjectLabel.setText("Subject: " + selectedMessage.getSubject());
		SenderLabel.setText("Sender: " + selectedMessage.getSender());
		
		MessageRendererService messageRendererService = new MessageRendererService(webView.getEngine());
		messageRendererService.setMessageToRender(selectedMessage);
		messageRendererService.restart();

	}

}
