package com.jinyuan.controller;

import java.net.*;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.mail.Flags;

import com.jinyuan.controller.persistence.ValidAddressBook;
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
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.util.Callback;

public class MainController extends AbstractController implements Initializable{
	
    public MainController(ModelAccess modelAccess) {
		super(modelAccess);
	}

	@FXML
    private TreeView<String> emailFoldersTreeView;
	@FXML
    private ListView<String> categoryListView;
	@FXML
	private ListView<ValidAddressBook> addressbookListView;
	
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
    private Button addAccountBtn;
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
    void addAccountBtnAction() {
    	
    	String selectedItem = categoryListView.getSelectionModel().getSelectedItem();
    	System.out.println("clicked on " + selectedItem);
    	
    	Scene scene = null;
    	switch (selectedItem) {
    	
		case "Mail":
			
			scene = ViewFactory.defaultFactory.getMailTypeSelectionScene();
			
			break;
			
		case "AddressBook":
			
			scene = ViewFactory.defaultFactory.getAddUserScene();
			
			break;

		default:
			break;
		}
    	
    	
    	Stage stage = new Stage();
    	stage.setScene(scene);
    	stage.show();
    }
    
    @FXML
    void handleMouseClick(MouseEvent arg0) {
    	
    	String selectedItem = categoryListView.getSelectionModel().getSelectedItem();
    	System.out.println("clicked on " + selectedItem);
    	
    	switch (selectedItem) {
    	
		case "Mail":
			addAccountBtn.setText("Add Account");
			addressbookListView.setVisible(false);
			emailFoldersTreeView.setVisible(true);
			break;
			
		case "AddressBook":
			addAccountBtn.setText("Add User");
			addressbookListView.setVisible(true);
			emailFoldersTreeView.setVisible(false);
			break;

		default:
			break;
		}
    	
    }
    
    @FXML
    void logutBtnAction(){
    	System.out.println("clicked on logout");
    	Stage stage = (Stage)categoryListView.getScene().getWindow();
    	stage.close();
    	
    	folderUpdaterService.cancel();
    	ViewFactory.defaultFactory.getPersistenceAcess().clear();
    	ViewFactory.defaultFactory.logout();
    	stage = new Stage();
    	stage.setScene(ViewFactory.defaultFactory.getMailTypeSelectionScene());
    	stage.show();
    }
    
    
    private MessageRendererService messageRendererService;
    private FolderUpdaterService folderUpdaterService;
    
    void loadAddressBook() {

		addressbookListView.setItems(getModelAccess().getValidAddressBookList());

		addressbookListView.setCellFactory(new Callback<ListView<ValidAddressBook>, ListCell<ValidAddressBook>>() {
			
			@Override
			public ListCell<ValidAddressBook> call(ListView<ValidAddressBook> param) {
				// TODO Auto-generated method stub
				return new AddressBookListCell();
			}
		});
		
    	if (ViewFactory.defaultFactory.getPersistenceAcess().validAddressBookfound()) {
			
			List<ValidAddressBook> adbList = ViewFactory.defaultFactory.getPersistenceAcess().getAddressbook();
			for (ValidAddressBook validAddressBook : adbList) {
				getModelAccess().addUser(validAddressBook);
			}
			
		}
    }
    
    private static class AddressBookListCell extends ListCell<ValidAddressBook> {
        @Override
        public void updateItem(ValidAddressBook item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(ViewFactory.defaultFactory.resolveIcon("@"));
                setText(item.toString());
            }
        }
    }
    
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		System.out.println("Document Module initialized.");

		//add list view category
		categoryListView.getItems().add("Mail");
		categoryListView.getItems().add("AddressBook");
		categoryListView.getSelectionModel().select(0);
		
		addressbookListView.setVisible(false);
		//load addressbook
		loadAddressBook();
		
		addAccountBtn.setText("Add Account");
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
		deleteMessage.setOnAction(e-> {
			EmailMessageBean message = emailTableView.getSelectionModel().getSelectedItem();
			try {
				message.getMessageRefference().setFlag(Flags.Flag.DELETED, true);
			} catch (Exception e1) {
				e1.printStackTrace();
				return;
			}
			getModelAccess().getSelectedFolder().getData().remove(message);
		});
		reply.setOnAction(e-> {
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