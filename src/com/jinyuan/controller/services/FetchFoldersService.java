package com.jinyuan.controller.services;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.event.MessageCountAdapter;
import javax.mail.event.MessageCountEvent;

import com.jinyuan.controller.ModelAccess;
import com.jinyuan.model.EmailAccountBean;
import com.jinyuan.model.folder.EmailFolderBean;
import com.jinyuan.view.ViewFactory;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class FetchFoldersService extends Service<Void>{
	
	private static int  NUMBER_OF_FETCHFOLDERSERVICES_ACTIVE = 0;
	private EmailFolderBean<String> foldersRoot;
	private EmailAccountBean emailAccountBean;
	private ModelAccess modelAccess;
	
	public FetchFoldersService(EmailFolderBean<String> foldersRoot, EmailAccountBean emailAccountBean, ModelAccess modelAccess) {
		this.foldersRoot = foldersRoot;
		this.emailAccountBean = emailAccountBean;
		this.modelAccess = modelAccess;
		
		this.setOnSucceeded(e->{
			NUMBER_OF_FETCHFOLDERSERVICES_ACTIVE--;
		});
	}

	@Override
	protected Task<Void> createTask() {
		return new Task<Void>(){
			@Override
			protected Void call() throws Exception {
				NUMBER_OF_FETCHFOLDERSERVICES_ACTIVE++;
				if (ViewFactory.defaultFactory.wasLogout()) {
					System.out.print("main scene was not initialized!!!");
					return null;
				}
				if(emailAccountBean != null){
					Folder[] folders = emailAccountBean.getStore().getDefaultFolder().list();
					for(Folder folder: folders){
						
						if (ViewFactory.defaultFactory.wasLogout()) {
							System.out.print("main scene was not initialized!!!");
							return null;
						}
						
						EmailFolderBean<String> item = new EmailFolderBean<String>(folder.getName(), folder.getFullName());
						foldersRoot.getChildren().add(item);
						item.setExpanded(true);
						modelAccess.addFolder(folder);
						addMessageListenerToFolder(folder, item);
						System.out.println("added " +  folder.getName());

						Folder[] subFolders = folder.list();
						FetchMessagesOnFolderService fetchMessagesOnFolderService = new FetchMessagesOnFolderService(item, folder);
						fetchMessagesOnFolderService.restart();
						for(Folder subFolder: subFolders){
							
							if (ViewFactory.defaultFactory.wasLogout()) {
								System.out.print("main scene was not initialized!!!");
								return null;
							}

							EmailFolderBean<String> subItem = new EmailFolderBean<String>(subFolder.getName(), subFolder.getFullName());
							item.getChildren().add(subItem);
							modelAccess.addFolder(subFolder);
							addMessageListenerToFolder(subFolder, subItem);
							System.out.println("added " +  subFolder.getName());
							FetchMessagesOnFolderService fetchMessagesOnSubFolderService = new FetchMessagesOnFolderService(subItem, subFolder);
							fetchMessagesOnSubFolderService.restart();
						}
					}
				}
				return null;
			}
			
		};
	}
	
	
	
	
	private void addMessageListenerToFolder(Folder folder, EmailFolderBean<String> item){
		folder.addMessageCountListener(new MessageCountAdapter() {
			@Override
			public void messagesAdded(MessageCountEvent e) {
				for (int i = 0; i < e.getMessages().length; i++) {
					try {
						Message currentMessage = folder.getMessage(folder.getMessageCount() - i);
						item.addEmail(currentMessage, 0);
						
					} catch (MessagingException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
	}
	
	public static boolean noServicesActive(){
		System.out.println("number of services active: " + NUMBER_OF_FETCHFOLDERSERVICES_ACTIVE);
		return NUMBER_OF_FETCHFOLDERSERVICES_ACTIVE==0;
	}

}
