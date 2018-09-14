package com.jinyuan.controller.services;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;

import com.jinyuan.model.EmailMessageBean;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.web.WebEngine;

public class MessageRendererService extends Service<Void>{
	
	private EmailMessageBean messageToRender;
	private WebEngine messageRendererEngine;
	private StringBuffer sb = new StringBuffer();

	public MessageRendererService(WebEngine messageRendererEngine) {
		this.messageRendererEngine = messageRendererEngine;
		this.setOnSucceeded(e->{showMessage();});
	}
	
	public void setMessageToRender(EmailMessageBean messageToRender){
		this.messageToRender = messageToRender;
	}
	
	public String getContent(){
		return sb.toString();
	}

	@Override
	protected Task<Void> createTask() {
		return new Task<Void>(){
			@Override
			protected Void call() throws Exception {
				loadMessage();
				return null;
			}
			
		};
	}

	/**
	 * load in different thread, so that UI doesn't block
	 */
	private void loadMessage(){
		sb.setLength(0);

		Message message = messageToRender.getMessageRefference();
		messageToRender.clearAttachmentsList();
		try {
			String messageType = message.getContentType();
			if(messageType.contains("TEXT/HTML") || 
					messageType.contains("TEXT/PLAIN") ||
					messageType.contains("text")){
				sb.append(message.getContent().toString());

			} else if(messageType.contains("multipart")){
				Multipart mp = (Multipart)message.getContent();

				for (int i = mp.getCount()-1; i >= 0; i--) {
					BodyPart bp = mp.getBodyPart(i);
					String contentType = bp.getContentType();
					if(contentType.contains("TEXT/HTML") ||
							contentType.contains("TEXT/PLAIN") ||
							contentType.contains("mixed")||
							contentType.contains("text")){
						//Here the risk of incomplete messages are shown, for messages that contain both 
						//html and text content, but these messages are very rare;
						if (sb.length()==0) {
							sb.append(bp.getContent().toString());
						}
						
					//here the attachments are handled
					}else if(contentType.toLowerCase().contains("application")){
							MimeBodyPart mbp = (MimeBodyPart)bp;
							messageToRender.addAttachement(mbp);
							
					//Sometimes the text content of the message is encapsulated in another multipart,
					//so we have to iterate again through it.		
					}else if(bp.getContentType().contains("multipart")){
						Multipart mp2 = (Multipart)bp.getContent();
						for (int j = mp2.getCount()-1; j >= 0; j--) {
							BodyPart bp2 = mp2.getBodyPart(i);
							if((bp2.getContentType().contains("TEXT/HTML") ||
									bp2.getContentType().contains("TEXT/PLAIN") ) ){
								sb.append(bp2.getContent().toString());
						    }
					    }
				   }  
			}
				

			}
		} catch (Exception e) {
			System.out.println("Exception while vizualizing message: ");
			e.printStackTrace();		
		}	
	}
	
	/**
	 * only call when message is loaded
	 * using the state listener
	 */
	private void showMessage(){
		messageRendererEngine.loadContent(sb.toString());
		//TODO: inform UI about attachments.
	}


	

}
