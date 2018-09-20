package com.jinyuan.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;

import com.jinyuan.model.table.AbstractTableItem;
import com.jinyuan.model.table.FormatableInteger;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class EmailMessageBean extends AbstractTableItem{
	
	private SimpleStringProperty sender;
	private SimpleStringProperty subject;
	private SimpleStringProperty recipient;
	private SimpleObjectProperty<FormatableInteger> size;
	private SimpleObjectProperty<Date> date;
	private Message messageRefference;
	private List<MimeBodyPart> listOfAttachments = new ArrayList<MimeBodyPart>();
	private StringBuffer attachmentsNames = new StringBuffer();
	
	/**
	 * only use when forvarding or repling to a message!
	 * memory consuming!!
	 */
	private String contentForForvarding;

	public EmailMessageBean(String Subject, String Sender, String Recipient, int size, boolean isRead, Date date, Message MessageRefference){
		super(isRead);
		this.subject = new SimpleStringProperty(Subject);
		this.sender = new SimpleStringProperty(Sender);
		this.recipient = new SimpleStringProperty(Recipient);
		this.size = new SimpleObjectProperty<FormatableInteger>(new FormatableInteger(size));
		this.date = new SimpleObjectProperty<Date>(date);
		this.messageRefference = MessageRefference;
	}
	
	public String getSender(){
		return sender.get();
	}
	public String getSubject(){
		return subject.get();
	}
	public String getRecipient(){
		return recipient.get();
	}
	public FormatableInteger getSize(){
		return size.get();
	}
	public Date getDate(){
		return date.get();
	}
	public Message getMessageRefference() {
		return messageRefference;
	}
	public List<MimeBodyPart> getListOfAttachments() {
		return listOfAttachments;
	}
	public void addAttachement(MimeBodyPart mbp){
		listOfAttachments.add(mbp);
	}
	public void clearAttachmentsList(){
		listOfAttachments.clear();
	}
	
	/**
	 * Returns a new object containing an exact copy of the EmailMessageBean object.
	 */
	public EmailMessageBean copy(){
		return new EmailMessageBean(this.subject.get(), 
				this.sender.get(), 
				this.recipient.get(), 
				this.size.get().getSize(),
				this.isRead(), 
				this.date.get(), 
				this.getMessageRefference());
	}
	
	public boolean hasAttachments(){
		return listOfAttachments.size() > 0;
	}
	
	public String getAttachmentsNames(){
		attachmentsNames.setLength(0);
		if(hasAttachments()){
			for(MimeBodyPart mbp : listOfAttachments){
				try {
					attachmentsNames.append(mbp.getFileName() + "; ");
				} catch (MessagingException e) {
					e.printStackTrace();
				}
			}
		}
		return attachmentsNames.toString();
	}

	public String getContentForForvarding() {
		return contentForForvarding;
	}

	public void setContentForForvarding(String contentForForvarding) {
		this.contentForForvarding = contentForForvarding;
	}
	
}
