package com.jinyuan.controller.services;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.jinyuan.model.EmailAccountBean;
import com.jinyuan.model.EmailConstants;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class EmailSenderService extends Service<Integer> {

	private int result;

	private EmailAccountBean emailAccountBean;
	private String subject;
	private String recipient;
	private String cc;
	private String content;
	private List<File> attachments = new ArrayList<File>();

	public EmailSenderService(EmailAccountBean emailAccountBean, String subject, String recipient, String cc,
			String content, List<File> attachments) {
		this.emailAccountBean = emailAccountBean;
		this.subject = subject;
		this.recipient = recipient;
		this.cc = cc;
		this.content = content;
		this.attachments = attachments;
	}

	@Override
	protected Task<Integer> createTask() {
		return new Task<Integer>() {
			@Override
			protected Integer call() throws Exception {
				try {
					// SetUp:
					Session session = emailAccountBean.getSession();
					MimeMessage message = new MimeMessage(session);
					message.setFrom(emailAccountBean.getEmailAdress());
					message.addRecipients(Message.RecipientType.TO, recipient);
					if (cc != null) {
						message.addRecipients(Message.RecipientType.CC, cc);
					}
					message.setSubject(subject);

					// Setting the content:
					Multipart multipart = new MimeMultipart();
					BodyPart messageBodyPart = new MimeBodyPart();
					messageBodyPart.setContent(content, "text/html");
					multipart.addBodyPart(messageBodyPart);

					// adding attachments:
					if (attachments.size() > 0) {
						for (File file : attachments) {
							MimeBodyPart messageBodyPartAttach = new MimeBodyPart();
							DataSource source = new FileDataSource(file.getAbsolutePath());
							messageBodyPartAttach.setDataHandler(new DataHandler(source));
							messageBodyPartAttach.setFileName(file.getName());
							multipart.addBodyPart(messageBodyPartAttach);
						}
					}
					message.setContent(multipart);

					// Sending the message:
					Transport transport = emailAccountBean.transportConnect();
					transport.sendMessage(message, message.getAllRecipients());
					transport.close();
					result = EmailConstants.MESSAGE_SENT_OK;

				} catch (Exception e) {
					result = EmailConstants.MESSAGE_SENT_ERROR;
					e.printStackTrace();
				}
				return result;
			}
		};
	}

}
