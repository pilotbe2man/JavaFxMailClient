package com.jinyuan.view;

import javax.naming.OperationNotSupportedException;

import com.jinyuan.controller.*;
import com.jinyuan.controller.persistence.PersistenceAcess;
import com.jinyuan.model.EmailMessageBean;

import com.jinyuan.model.GlobalVariables.GlobalVariables;
import com.jinyuan.model.MailSecurity;
import com.sun.javafx.iio.gif.GIFImageLoader2;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.Locale;
import java.util.ResourceBundle;

public class ViewFactory {

	public static ViewFactory defaultFactory = new ViewFactory();

	public static boolean mainViewInitialized = false;
	public static boolean isLogout = false;

	private final String DEFAULT_CSS = "style.css";
	private final String EMAIL_DETAILS_FXML = "EmailDetailsLayout.fxml";
	private final String MAIN_SCREEN_FXML = "MainLayout.fxml";
	private final String COMPOSE_EMAIL_FXML = "ComposeEmailLayout.fxml";
	private final String ADD_ACCOUNT_EMAIL_FXML = "AddAccountLayout.fxml";
	private final String ADD_USER_EMAIL_FXML = "AddUserLayout.fxml";
	private final String MAIL_TYPE_SELECTION_FXML = "MailTypeSelectionLayout.fxml";
	private final String PROTOTYPE_FXML = "PrototypeLayout.fxml";
	private final String DRAFT_MAIL_FXML = "DraftMailLayout.fxml";
	private final String CHOOSE_USER_FXML = "ChooseUserLayout.fxml";
	private final String EMAIL_DETAIL_FXML = "EmailDetailLayout.fxml";

	private ModelAccess modelAccess = new ModelAccess();
	private PersistenceAcess persistenceAcess = new PersistenceAcess(modelAccess);
	Scene currentMailScene;

	public Scene getMainScene() throws OperationNotSupportedException {
		if (!mainViewInitialized) {
			AbstractController mainController = new MainController(modelAccess);
			mainViewInitialized = true;
			return initializeScene(MAIN_SCREEN_FXML, mainController);
		} else {
			throw new OperationNotSupportedException("Main Scene allready initialized!!!!");
		}
	}

	public Scene getChooseUserScene() {
		AbstractController chooseUserController = new ChooseUserController(modelAccess);
		return initializeScene(CHOOSE_USER_FXML, chooseUserController);
	}

	public Scene getDraftMailScene() {
		AbstractController draftMailController = new DraftMailController(modelAccess);
		return initializeScene(DRAFT_MAIL_FXML, draftMailController);
	}

	public Scene getPrototypeScene() {
		AbstractController prototypeController = new PrototypeController(modelAccess);
		return initializeScene(PROTOTYPE_FXML, prototypeController);
	}

	public Scene getEmailDetailsScene() {
		AbstractController emailDetailsController = new EmailDetailsController(modelAccess);
		return initializeScene(EMAIL_DETAILS_FXML, emailDetailsController);
	}

	public Scene getEmailDetailScene(PrototypeController.MailItem aItem) {
		AbstractController emailDetailController = new EmailDetailController(modelAccess, aItem);
		return initializeScene(EMAIL_DETAIL_FXML, emailDetailController);
	}

	public Scene getComposeEmailScene() {
		AbstractController composeEmailController = new ComposeEmailController(modelAccess);
		return initializeScene(COMPOSE_EMAIL_FXML, composeEmailController);
	}
	
	public Scene getAddAccountScene() {
		return currentMailScene;
	}
	
	public Scene getAddAccountScene(String mailType) {
		modelAccess.setMailType(mailType);
		AbstractController addAccountController = new AddAccountController(modelAccess);
		currentMailScene = initializeScene(ADD_ACCOUNT_EMAIL_FXML, addAccountController);
		return currentMailScene;
	}
	
	public Scene getMailTypeSelectionScene(){
		AbstractController addAccountController = new MailTypeSelectionController(modelAccess);
		return initializeScene(MAIL_TYPE_SELECTION_FXML, addAccountController);
	}
	
	public Scene getAddUserScene(){
		AbstractController addUserController = new AddUserController(modelAccess);
		return initializeScene(ADD_USER_EMAIL_FXML, addUserController);
	}

	public Scene getComposeEmailScene(EmailMessageBean initialMessage, int type) {
		AbstractController composeEmailController;
		try {
			composeEmailController = new ComposeEmailController(modelAccess, initialMessage, type);
		} catch (Exception e) {
			composeEmailController = new ComposeEmailController(modelAccess);
			e.printStackTrace();
		}
		return initializeScene(COMPOSE_EMAIL_FXML, composeEmailController);
	}

	public Node resolveIcon(String treeItemValue) {
		String lowerCaseTreeItemValue = treeItemValue.toLowerCase();
		ImageView returnIcon;
		try {
			if (lowerCaseTreeItemValue.contains("inbox")) {
				returnIcon = new ImageView(new Image(getClass().getResourceAsStream("images/inbox.png")));
			} else if (lowerCaseTreeItemValue.contains("sent")) {
				returnIcon = new ImageView(new Image(getClass().getResourceAsStream("images/sent2.png")));
			} else if (lowerCaseTreeItemValue.contains("spam")) {
				returnIcon = new ImageView(new Image(getClass().getResourceAsStream("images/spam.png")));
			} else if (lowerCaseTreeItemValue.contains("@")) {
				returnIcon = new ImageView(new Image(getClass().getResourceAsStream("images/email.png")));
			} else if (lowerCaseTreeItemValue.isEmpty()) {
				return null;
			} else {
				returnIcon = new ImageView(new Image(getClass().getResourceAsStream("images/folder.png")));
			}
		} catch (NullPointerException e) {
			System.out.println("Invalid image location!!!");
			e.printStackTrace();
			returnIcon = new ImageView();
		}

		returnIcon.setFitHeight(16);
		returnIcon.setFitWidth(16);

		return returnIcon;
	}

	public Node resolveIconWithName(String name) {
		try {
			ImageView iv = new ImageView(new Image(getClass().getResourceAsStream(name)));
			iv.setFitWidth(16);
			iv.setFitHeight(16);
			return iv;
		} catch (Exception e) {
			e.printStackTrace();
			return new ImageView();
		}
	}

	public Node resolveCategoryIcon(int aIndex) {
		try {
			String iconName = "";
			switch (aIndex) {
				case PrototypeController.CAT_MAIL:
					iconName = "images/mail.png";
					break;
				case PrototypeController.CAT_ADB:
					iconName = "images/contacts.png";
					break;
			}
			ImageView iv = new ImageView(new Image(getClass().getResourceAsStream(iconName)));
			iv.setFitWidth(16);
			iv.setFitHeight(16);
			return iv;
		} catch (Exception e) {
			e.printStackTrace();
			return new ImageView();
		}
	}

	public Node resolveMailCategoryIcon(String name) {
		String color = "";
		for (int i = 0; i < GlobalVariables.mailSecurityList.size(); i++) {
			MailSecurity security = GlobalVariables.mailSecurityList.get(i);
			if (security.levelName.equalsIgnoreCase(name)) {
				color = security.levelColor;
				break;
			}
		}

		try {
			String iconName = name.toLowerCase();
			Rectangle rect = new Rectangle(0, 0, 10, 15);
			if (color.length() > 0) {
				rect.setFill(Color.valueOf(color));
			} else {
				rect.setFill(Color.TRANSPARENT);
			}
			return rect;
		} catch (Exception e) {
			e.printStackTrace();
			return new ImageView();
		}
	}

	public Node resolveMailImportantIcon(String name) {

		int level = 0;
		for (int i = 0; i < GlobalVariables.mailSecurityList.size(); i++) {
			MailSecurity security = GlobalVariables.mailSecurityList.get(i);
			if (security.levelName.equalsIgnoreCase(name)) {
				level = security.level;
				break;
			}
		}


		try {
			ImageView iv;
			if (level >= 20) {
				iv = new ImageView(new Image(getClass().getResourceAsStream("images/important_high.png")));
			} else if (level <= 15){
				iv = new ImageView(new Image(getClass().getResourceAsStream("images/important_row.png")));
			} else {
				iv = new ImageView();
			}
			iv.setFitWidth(16);
			iv.setFitHeight(16);
			return iv;
		} catch (Exception e) {
			e.printStackTrace();
			return new ImageView();
		}
	}

	public Node resolveMailBoxListItemIcon(int aIndex) {
		try {
			String iconName = "";
			switch (aIndex) {
				case PrototypeController.CAT_MAIL_DRAFTS:
					iconName = "images/drafts.png";
					break;
				case PrototypeController.CAT_MAIL_OUTBOX:
					iconName = "images/outbox.png";
					break;
				case PrototypeController.CAT_MAIL_JUNK:
					iconName = "images/junk_mail.png";
					break;
				case PrototypeController.CAT_MAIL_INBOX:
					iconName = "images/inbox.png";
					break;
				case PrototypeController.CAT_MAIL_SENT:
					iconName = "images/sent.png";
					break;
				case PrototypeController.CAT_MAIL_DELETE:
					iconName = "images/trash.png";
					break;
				case PrototypeController.CAT_MAIL_SEARCH:
					iconName = "images/search_folder.png";
					break;
			}
			ImageView iv = new ImageView(new Image(getClass().getResourceAsStream(iconName)));
			iv.setFitWidth(16);
			iv.setFitHeight(16);
			return iv;
		} catch (Exception e) {
			e.printStackTrace();
			return new ImageView();
		}
	}

	public Image emailIcon(String value) {
		
		Image returnIcon;
		
		try {
			switch (value) {
			
			case "qq":
				returnIcon = new Image(getClass().getResourceAsStream("images/qq.jpeg"));
				break;
				
			case "163":
				returnIcon = new Image(getClass().getResourceAsStream("images/163.png"));
				break;
				
			case "126":
				returnIcon = new Image(getClass().getResourceAsStream("images/126.png"));
				break;
				
			case "sina":
				returnIcon = new Image(getClass().getResourceAsStream("images/sina.jpeg"));
				break;
				
			case "hotmail":
				returnIcon = new Image(getClass().getResourceAsStream("images/hotmail.jpeg"));
				break;
				
			case "gmail":
				returnIcon = new Image(getClass().getResourceAsStream("images/gmail.jpeg"));
				break;
				
			case "fox":
				returnIcon = new Image(getClass().getResourceAsStream("images/fox.jpeg"));
				break;
				
			case "139":
				returnIcon = new Image(getClass().getResourceAsStream("images/139.jpeg"));
				break;
				
			case "sohu":
				returnIcon = new Image(getClass().getResourceAsStream("images/sohu.jpeg"));
				break;
				
			case "outlook":
				returnIcon = new Image(getClass().getResourceAsStream("images/outlook.jpeg"));
				break;
				
			default:
				returnIcon = null;
				break;
				
			}
			
		} catch (NullPointerException e) {
			System.out.println("Invalid image location!!!");
			e.printStackTrace();
			returnIcon = null;
		}


		return returnIcon;
	}

	private Scene initializeScene(String fxmlPath, AbstractController controller) {
		FXMLLoader loader;
		Parent parent;
		Scene scene = null;
		try {
			ResourceBundle bundle = ResourceBundle.getBundle("com/jinyuan/resources/lang", Locale.getDefault());
			loader = new FXMLLoader(getClass().getResource(fxmlPath), bundle);
			loader.setController(controller);
			parent = loader.load();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		scene = new Scene(parent);
		scene.setUserData(loader);
		scene.getStylesheets().add(getClass().getResource(DEFAULT_CSS).toExternalForm());
		return scene;
	}

	public PersistenceAcess getPersistenceAcess() {
		return persistenceAcess;
	}
	
	public void didLogin() {
		isLogout = false;
	}
	
	public boolean wasLogout() {
		return isLogout;
	}
	
	public void logout() {
		isLogout = true;
	}

}
