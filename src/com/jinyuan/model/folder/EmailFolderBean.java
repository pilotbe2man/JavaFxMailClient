package com.jinyuan.model.folder;

import javax.mail.Flags.Flag;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage.RecipientType;

import com.jinyuan.model.EmailMessageBean;
import com.jinyuan.view.ViewFactory;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.TreeItem;

public class EmailFolderBean<T> extends TreeItem<String>{
	
	private boolean topElement = false;
	private int unreadMessagesCount;
	private String name;
	@SuppressWarnings("unused")
	private String completeName;
	private ObservableList<EmailMessageBean> data = FXCollections.observableArrayList();
	
	/**
	 * Constructor for the top element
	 * @param value
	 */
	public EmailFolderBean(String value){
		super(value, ViewFactory.defaultFactory.resolveIcon(value));
		this.name = value;
		this.completeName = value;
		data = null;
		topElement = true;
		this.setExpanded(true);
	}
	
	/**
	 * Constructor for the root element element
	 * @param value
	 */
	public EmailFolderBean(String value, Node graphic){
		super(value, graphic);
		this.name = value;
		this.completeName = value;
		data = null;
		topElement = true;
		this.setExpanded(true);
	}
	
	/**
	 * Constructor for email folders
	 * @param value
	 * @param completeName
	 */
	public EmailFolderBean(String value, String completeName){
		super(value, ViewFactory.defaultFactory.resolveIcon(value));
		this.name = value;
		this.completeName = completeName;
	}
	
	
	public void addEmail(Message message) throws MessagingException{
		boolean messageIsRead = message.getFlags().contains(Flag.SEEN);
		EmailMessageBean emailMessageBean= new EmailMessageBean(message.getSubject(), 
				message.getFrom()[0].toString(),
				message.getRecipients(RecipientType.TO)[0].toString(),
				message.getSize(), 
				messageIsRead, 
				message.getSentDate(),
				message);
		data.add(emailMessageBean);
		if(!messageIsRead){
			incrementUnreadMessageCount(1);
		}		
	}
	
	public void addEmail(Message message, int index) throws MessagingException{
		boolean messageIsRead = message.getFlags().contains(Flag.SEEN);
		EmailMessageBean emailMessageBean= new EmailMessageBean(message.getSubject(), 
				message.getFrom()[0].toString(),
				message.getRecipients(RecipientType.TO)[0].toString(),
				message.getSize(), 
				messageIsRead, 
				message.getSentDate(),
				message);
		data.add(index, emailMessageBean);
		if(!messageIsRead){
			incrementUnreadMessageCount(1);
		}		
	}

	public void incrementUnreadMessageCount(int newMessages){
		unreadMessagesCount = unreadMessagesCount + newMessages;
		updateValue();
	}
	public void decrementUreadMessagesCount(){
		unreadMessagesCount--;
		updateValue();
	}
	
	public ObservableList<EmailMessageBean> getData(){
		return data;
	}
	
	private void updateValue(){
		if(unreadMessagesCount>0){
			this.setValue((String) (name + "(" + unreadMessagesCount + ")"));
		}else{
			this.setValue(name);
		}		
	}
	public boolean isTopElement(){
		return topElement;
	}
}
