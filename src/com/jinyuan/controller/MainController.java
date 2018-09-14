package com.jinyuan.controller;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

import javax.mail.Flags;

import com.jinyuan.controller.services.FolderUpdaterService;
import com.jinyuan.controller.services.MessageRendererService;
import com.jinyuan.controller.services.SaveAttachmentsService;
import com.jinyuan.model.EmailConstants;
import com.jinyuan.model.EmailMessageBean;
import com.jinyuan.model.folder.EmailFolderBean;
import com.jinyuan.model.table.BoldableRowFactory;
import com.jinyuan.model.table.FormatableInteger;
import com.jinyuan.view.ViewFactory;

import javafx.concurrent.Worker.State;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class MainController extends AbstractController implements Initializable{
	
    public MainController(ModelAccess modelAccess) {
		super(modelAccess);
	}

	@FXML
    private TreeView<String> emailFoldersTreeView;
	@FXML
    private TreeView<String> addressbookTreeView;
	
    private MenuItem showDetails = new MenuItem("show details");
    private MenuItem markUnread = new MenuItem("mark as unread");
    private MenuItem deleteMessage = new MenuItem("delete message");
    private MenuItem reply = new MenuItem("reply ");

    @FXML
    private Label downAttachLabel;
    @FXML
    private ProgressIndicator attachProgress;
    private SaveAttachmentsService saveAttachmentsService;
	
    @FXML
    private Label attachementsLabel;
    @FXML
    private TableView<EmailMessageBean> emailTableView;	
    @FXML
    private TableColumn<EmailMessageBean, String> subjectCol;
    @FXML
    private TableColumn<EmailMessageBean, String> senderCol;
    @FXML
    private TableColumn<EmailMessageBean, String> recipientCol;
    @FXML
    private TableColumn<EmailMessageBean, FormatableInteger> sizeCol;    
    @FXML
    private TableColumn<EmailMessageBean, Date> dateCol;	
    @FXML
    private WebView messageRenderer;	
    @FXML
    private Button downloadAttachBtn;
    @FXML
    void composeBtnAction() {
    	Scene scene = ViewFactory.defaultFactory.getComposeEmailScene();
    	Stage stage = new Stage();
    	stage.setScene(scene);
    	stage.show();

    }
    @FXML
    void downloadAttachBtnAction() {
    	saveAttachmentsService.setEmailMessage(getModelAccess().getSelectedMessage());
    	if(getModelAccess().getSelectedMessage().getListOfAttachments().size() > 0 ){
    		saveAttachmentsService.restart();
    	}    	
    }
    
    @FXML
    void addAccountBtnAction(){
    	Scene scene = ViewFactory.defaultFactory.getAddAccountScene();
    	Stage stage = new Stage();
    	stage.setScene(scene);
    	stage.show();
    }
    
    @FXML
    void logutBtnAction(){
    }
    
    private MessageRendererService messageRendererService;
    private FolderUpdaterService folderUpdaterService;
    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		downloadAttachBtn.setDisable(true);
		attachementsLabel.setText("");
		saveAttachmentsService = new SaveAttachmentsService(attachProgress, downAttachLabel);
		attachProgress.progressProperty().bind(saveAttachmentsService.progressProperty());
		folderUpdaterService = new FolderUpdaterService(getModelAccess());
		folderUpdaterService.start();
		messageRendererService = new MessageRendererService(messageRenderer.getEngine());
		
		emailTableView.setRowFactory(e-> new BoldableRowFactory<>());
		subjectCol.setCellValueFactory(new PropertyValueFactory<EmailMessageBean, String>("subject"));
		senderCol.setCellValueFactory(new PropertyValueFactory<EmailMessageBean, String>("sender"));
		recipientCol.setCellValueFactory(new PropertyValueFactory<EmailMessageBean, String>("recipient"));
		sizeCol.setCellValueFactory(new PropertyValueFactory<EmailMessageBean, FormatableInteger>("size"));	
		dateCol.setCellValueFactory(new PropertyValueFactory<EmailMessageBean, Date>("date"));
		
		//BUG: sizeCol doesn't get it's default comparator overridden, have to do this manually!!!
		sizeCol.setComparator(new FormatableInteger(0));	
		

		emailFoldersTreeView.setRoot(getModelAccess().getRoot());		
		emailFoldersTreeView.setShowRoot(false);
		
		emailTableView.setContextMenu(new ContextMenu(showDetails, markUnread, deleteMessage, reply));
		
		emailFoldersTreeView.setOnMouseClicked(e ->{
			EmailFolderBean<String> item = (EmailFolderBean<String>)emailFoldersTreeView.getSelectionModel().getSelectedItem();
			if(item != null && !item.isTopElement()){
				emailTableView.setItems(item.getData());
				getModelAccess().setSelectedFolder(item);
				//clear selected message:
				getModelAccess().setSelectedMessage(null);
			}
		});
		emailTableView.setOnMouseClicked(e->{
			EmailMessageBean message = emailTableView.getSelectionModel().getSelectedItem();
			if(message != null){
				if(!message.isRead()){
					message.setRead(true);
					try {
						message.getMessageRefference().setFlag(Flags.Flag.SEEN, true);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					getModelAccess().getSelectedFolder().decrementUreadMessagesCount();
				}
			if(message.hasAttachments()){
				downloadAttachBtn.setDisable(false);
				attachementsLabel.setText(message.getAttachmentsNames());				
			}else{
				downloadAttachBtn.setDisable(true);
				attachementsLabel.setText("");
			}	
				getModelAccess().setSelectedMessage(message);
				messageRendererService.setMessageToRender(message);
				messageRendererService.restart();
			}
		});
		showDetails.setOnAction(e->{			
			Scene scene = ViewFactory.defaultFactory.getEmailDetailsScene();
			Stage stage = new Stage();
			stage.setScene(scene);
			stage.show();
		});
		markUnread.setOnAction(e->{
			EmailMessageBean message = emailTableView.getSelectionModel().getSelectedItem();
			getModelAccess().getSelectedFolder().incrementUnreadMessageCount(1);
			message.setRead(false);
			try {
				message.getMessageRefference().setFlag(Flags.Flag.SEEN, false);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});
		deleteMessage.setOnAction(e->{
			EmailMessageBean message = emailTableView.getSelectionModel().getSelectedItem();
			try {
				message.getMessageRefference().setFlag(Flags.Flag.DELETED, true);
			} catch (Exception e1) {
				e1.printStackTrace();
				return;
			}
			getModelAccess().getSelectedFolder().getData().remove(message);
		});
		reply.setOnAction(e->{
			if (messageRendererService.getState() != State.RUNNING) {
				EmailMessageBean message = emailTableView.getSelectionModel().getSelectedItem();
				message.setContentForForvarding(messageRendererService.getContent());
				Scene scene = ViewFactory.defaultFactory.getComposeEmailScene(message, EmailConstants.REPLY_MESSAGE);
				Stage stage = new Stage();
				stage.setScene(scene);
				stage.show();
			}
		});
		
		
		
	}
	

}
