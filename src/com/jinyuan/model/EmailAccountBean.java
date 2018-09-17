package com.jinyuan.model;

import java.util.Properties;

import javax.mail.AuthenticationFailedException;
import javax.mail.Authenticator;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.URLName;

import com.sun.mail.pop3.POP3SSLStore;

public class EmailAccountBean {
	
	private String emailAdress;
	private String password;
	private Properties properties;
	private Store store;
	private Session session;
	private int loginState = EmailConstants.LOGIN_STATE_NOT_READY;
	
	public String getEmailAdress() {
		return emailAdress;
	}
	public String getPassword(){
		return password;
	}
	public Properties getProperties(){
		return properties;
	}
	public Store getStore() {
		return store;
	}
	public Session getSession() {
		return session;
	}
	public int getLoginState(){
		return loginState;
	}

	public EmailAccountBean(String emailAdress, String password, String type) {
		this.emailAdress = emailAdress;
		this.password = password;
		properties = new Properties();
		properties.put("mail.store.protocol", "imaps");
		properties.put("mail.transport.protocol", "smtps");
		
		String smtpsHost;
		String inComingHost;
		String outGoingHost;
		switch (type) {
		
		case "qq":
			properties.put("mail.smtp.port", "465");
			smtpsHost = "smtp.qq.com";
			inComingHost = "imap.qq.com";
			outGoingHost = "smtp.qq.com";
			break;
			
		case "163":
			properties.put("mail.smtp.port", "465");
			smtpsHost = "smtp.163.com";
			inComingHost = "imap.163.com";
			outGoingHost = "smtp.163.com";
			break;
			
		case "126":
			properties.put("mail.smtp.port", "465");
			smtpsHost = "smtp.126.com";
			inComingHost = "imap.126.com";
			outGoingHost = "smtp.126.com";
			break;
			
		case "sina":
			properties.put("mail.pop3.port", "995");
			properties.put("mail.smtp.port", "465");
			properties.put("mail.store.protocol", "pop3");
			smtpsHost = "smtp.sina.com";
			inComingHost = "pop.sina.com";
			outGoingHost = "smtp.sina.com";
			break;
			
		case "hotmail":
		case "outlook":
			smtpsHost = "smtp-mail.outlook.com";
			inComingHost = "imap-mail.outlook.com";
			outGoingHost = "smtp-mail.outlook.com";
			
			break;
			
		case "gmail":
		{
			try {
				gmailConnect();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			return;
		}
			
//			smtpsHost = "smtp.gmail.com";
//			inComingHost = "pop.gmail.com";
//			outGoingHost = "smtp.gmail.com";
//			break;
			
		case "fox":
			smtpsHost = "smtp.qq.com";
			inComingHost = "imap.qq.com";
			outGoingHost = "smtp.qq.com";
			break;
			
		case "139":
			properties.put("mail.smtp.port", "465");
			smtpsHost = "smtp.139.com";
			inComingHost = "imap.139.com";
			outGoingHost = "smtp.139.com";
			break;
			
		case "sohu":
			properties.put("mail.pop3.port", "110");
			properties.put("mail.smtp.port", "25");
			properties.put("mail.store.protocol", "pop3");
			smtpsHost = "smtp.sohu.com";
			inComingHost = "pop3.sohu.com";
			outGoingHost = "smtp.sohu.com";
			break;
			
		default:
			smtpsHost = "";
			inComingHost = "";
			outGoingHost = "";
			break;
			
		}
		
//		properties.put("mail.smtps.host", smtpsHost);
//		properties.put("mail.smtps.auth", "true");
		properties.put("incomingHost", inComingHost);
		properties.put("outgoingHost", outGoingHost);

		Authenticator auth = new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(emailAdress, password);
			}
		};

		// Connecting:
		session = Session.getInstance(properties, auth);
		
		try{
			this.store = session.getStore();
			store.connect(properties.getProperty("incomingHost"), emailAdress, password);
			System.out.println("EmailAccount constructed successfully: " + this);
			loginState = EmailConstants.LOGIN_STATE_SUCCEDED;
		}catch(AuthenticationFailedException ae){
			ae.printStackTrace();
			loginState = EmailConstants.LOGIN_STATE_FAILED_BY_CREDENTIALS;
		}catch(Exception e){
			e.printStackTrace();
			loginState = EmailConstants.LOGIN_STATE_FAILED_BY_NETWORK;
		}
		
	}
	
	public void gmailConnect() {

		String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";

		Properties pop3Props = new Properties();

		pop3Props.setProperty("mail.pop3.socketFactory.class", SSL_FACTORY);
		pop3Props.setProperty("mail.pop3.socketFactory.fallback", "false");
		pop3Props.setProperty("mail.pop3.port",  "995");
		pop3Props.setProperty("mail.pop3.socketFactory.port", "995");

		URLName url = new URLName("pop3", "pop.gmail.com", 995, "", emailAdress, password);

		session = Session.getInstance(pop3Props, null);
		store = new POP3SSLStore(session, url);
		try {
			store.connect();
			loginState = EmailConstants.LOGIN_STATE_SUCCEDED;
		} catch (AuthenticationFailedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			loginState = EmailConstants.LOGIN_STATE_FAILED_BY_CREDENTIALS;
		} catch(Exception e){
			e.printStackTrace();
			loginState = EmailConstants.LOGIN_STATE_FAILED_BY_NETWORK;
		}

	}
	 
	public Transport transportConnect() throws MessagingException{
		Transport transport = session.getTransport();
		transport.connect(getProperties().getProperty("outgoingHost"),
				// 465,
				getEmailAdress(), password); // account
		return transport;
																					
	}
	
	


}
