package com.jinyuan.controller.services;

import javax.mail.internet.MimeBodyPart;

import com.jinyuan.model.EmailMessageBean;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;

public class SaveAttachmentsService extends Service<Void>{
	
	private String LOCATION_OF_DOWNLOADS = System.getProperty("user.home") + "/Downloads/";
	private EmailMessageBean message;

	private ProgressIndicator progress;
	private Label label;
	
	public void setEmailMessage(EmailMessageBean message){
		this.message = message;
	}
	
	public SaveAttachmentsService(ProgressIndicator progress, Label label) {
		this.progress = progress;
		this.label = label;
		showVisuals(false);
		this.setOnRunning(e->{showVisuals(true);});
		this.setOnSucceeded(e->{showVisuals(false);});
	}
	
	@Override
	protected Task<Void> createTask() {
		return new Task<Void>(){
			@Override
			protected Void call() throws Exception {
				updateProgress(0, 1);
				for(MimeBodyPart mbp:message.getListOfAttachments()){
					try {
						updateProgress(message.getListOfAttachments().indexOf(mbp),
								message.getListOfAttachments().size());
						mbp.saveFile(LOCATION_OF_DOWNLOADS + mbp.getFileName());
						
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				updateProgress(1, 1);
				return null;
			}			
		};
	}
	
	private void showVisuals(boolean show){
		label.setVisible(show);
		progress.setVisible(show);
	}
	

	
}