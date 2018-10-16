package com.jinyuan.controller;

import com.jinyuan.controller.persistence.ValidAccount;
import com.jinyuan.controller.services.CreateAndRegisterEmailAccountService;
import com.jinyuan.model.AddressBookItem;
import com.jinyuan.model.EmailConstants;
import com.jinyuan.model.GlobalVariables.GlobalVariables;
import com.jinyuan.model.MailSecurity;
import com.jinyuan.model.http.Apis;
import com.jinyuan.view.ViewFactory;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.util.Callback;

import javafx.print.PrinterJob;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import javax.mail.*;

public class PrototypeController extends AbstractController implements Initializable {

    //toolbar buttons
    @FXML
    private SplitMenuButton newMailMenuButton;

    @FXML
    private Button printButton;

    @FXML
    private Button moveToFolderButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Button replyButton;

    @FXML
    private Button replyToAllButton;

    @FXML
    private Button forwardButton;

    @FXML
    private Button categoriseButton;

    @FXML
    private Button followUpButton;

    @FXML
    private MenuButton sendReceiveButton;

    @FXML
    private Button oneNoteButton;

    @FXML
    private Button addressBookButton;

    @FXML
    private ComboBox addressBookComboBox;

    @FXML
    private Button helpButton;

    //main split
    @FXML
    private SplitPane mainSplitePane;

    @FXML
    private AnchorPane leftAnchorPane;

    @FXML
    private AnchorPane centerAnchorPane;

    @FXML
    private AnchorPane rightAnchorPane;

    @FXML
    private Label categoryNameLabel;

    //collapse
    @FXML
    private Button leftPanCollapseButton;

    @FXML
    private Button leftPanExpandButton;

    @FXML
    private HBox categoryListHBox;

    //expand
    @FXML
    private AnchorPane expandAnchorPane;

    @FXML
    private ListView categoryListView;

    @FXML
    private Label itemsLabel;

    @FXML
    private ListView categoryItemListView;

    @FXML
    private TableView mailItemTableView;

    @FXML
    private WebView mailContentWebView;

    @FXML
    private ToggleButton mailCatButton;

    @FXML
    private ToggleButton addressBookCatButton;

    //search box for mail category
    @FXML
    private ComboBox searchComboBox;

    @FXML
    private Button exSearchButton;

    @FXML
    private FlowPane exSearchFlowPane;

    @FXML
    private MenuButton addCriteriaButton;

    @FXML
    private VBox searchVBox;

    @FXML
    private Label categoryNameLabel1;

    @FXML
    private Label receivedDateLabel;

    @FXML
    private Button replyInContentButton;

    @FXML
    private Button replyAllInContentButton;

    @FXML
    private Button forwardInContentButton;

    @FXML
    private Label fromLabel;

    @FXML
    private Label subjectLabel;

    private int currentSelectedCategoryItem = CAT_MAIL_INBOX;

    private int currentSelectedMailBoxItem = -1;

    //divider postions of the mailSplitPane;
    double[] mDivPosOfMailExp;
    double[] mDivPosOfAddressBookExp;

    List<String> mAryCategory = new ArrayList<>();
    List<String> mAryMailItems = new ArrayList<>();
    List<String> mAryAddressBookItems = new ArrayList<>();

    //show/hide preview flag
    boolean isShowingMode = false;

    public final static int CAT_MAIL = 0;
    public final static int CAT_ADB = CAT_MAIL + 1;

    public final static int CAT_MAIL_DRAFTS = 0;
    public final static int CAT_MAIL_OUTBOX = CAT_MAIL_DRAFTS + 1;
    public final static int CAT_MAIL_JUNK = CAT_MAIL_OUTBOX + 1;
    public final static int CAT_MAIL_INBOX = CAT_MAIL_JUNK + 1;
    public final static int CAT_MAIL_SENT = CAT_MAIL_INBOX + 1;
    public final static int CAT_MAIL_DELETE = CAT_MAIL_SENT + 1;
    public final static int CAT_MAIL_SEARCH = CAT_MAIL_DELETE + 1;

    final static int CAT_ADB_PHONE = 0;
    final static int CAT_ADB_CAT = CAT_ADB_PHONE + 1;
    final static int CAT_ADB_COMPANY = CAT_ADB_CAT + 1;
    final static int CAT_ADB_LOCATION = CAT_ADB_COMPANY + 1;

    public PrototypeController(ModelAccess modelAccess) {
        super(modelAccess);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        getMailBoxInfo();
        getAddressList();
        getSecurityLevels();

        initCategoryButtons();
        initToolbarButtons();
        initSearchCategory();

        //init list for test
        initTestValues();
        //<-- test end

        categoryListView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                handleClickedOnCategory(event);
            }
        });

        handleClickedOnMailCatButton();

        categoryItemListView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                handleClickedOnCategoryItem();
            }
        });

        categoryListView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                handleClickedOnCategory(null);
            }
        });

        mailItemTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                handleClickedOnTableView();
            }
        });

        //hide right pane
        mainSplitePane.getItems().remove(rightAnchorPane);

        //hide expand pan and button
        categoryListHBox.getChildren().remove(expandAnchorPane);

        categoryListView.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {

            @Override
            public ListCell<String> call(ListView<String> param) {
                // TODO Auto-generated method stub
                return new CategoryListCell();
            }
        });

        categoryItemListView.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {

            @Override
            public ListCell<String> call(ListView<String> param) {
                // TODO Auto-generated method stub
                return new CategoryListItemCell();
            }
        });

        mDivPosOfMailExp = mainSplitePane.getDividerPositions();

        mailItemTableView.setRowFactory( tv -> {
            TableRow row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    GlobalVariables.messageIndex = row.getIndex();
                    Object rowData = row.getItem();
                    if (rowData instanceof MailItem) {
                        System.out.println("Double Clicked On MailItem -> {" + rowData.toString() + "}");
                        showMailDetailScene((MailItem) rowData);
                    } else {
                        System.out.println("Double Clicked On AddressBookItem -> {" + rowData.toString() + "}");
                    }
                }
            });
            return row ;
        });
    }

    /**
     * show the mail detail scene for the clicked item
     */
    public void showMailDetailScene(MailItem aItem) {
        Scene scene = ViewFactory.defaultFactory.getEmailDetailScene(aItem);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Detailed Email");
        stage.show();
    }

    @FXML
    public void actionOnShowPreviewMenuItem() {
        System.out.println("OnClicked actionOnShowPreviewMenuItem");
        mainSplitePane.getItems().add(rightAnchorPane);
        isShowingMode = true;
    }

    @FXML
    public void actionOnHidePreviewMenuItem() {
        System.out.println("OnClicked actionOnHidePreviewMenuItem");
        mainSplitePane.getItems().remove(rightAnchorPane);
        isShowingMode = false;
    }

    @FXML
    public void handeClickedOnNewMailMenuButton() {
        GlobalVariables.replyType = "";
        Scene scene = ViewFactory.defaultFactory.getDraftMailScene();
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("New Mail");
        stage.show();
    }

    @FXML
    public void handeClickedOnReplyMenuButton() {
        int categoryIndex = categoryListView.getSelectionModel().getSelectedIndex();
        int index = mailItemTableView.getSelectionModel().getSelectedIndex();
        if (index != -1 && categoryIndex == 0) {
            GlobalVariables.replyType = "Reply";
            GlobalVariables.replyIndex = index;
            Scene scene = ViewFactory.defaultFactory.getDraftMailScene();
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Reply Mail");
            stage.show();
        }
    }

    @FXML
    public void handeClickedOnReplyAllMenuButton() {
        int categoryIndex = categoryListView.getSelectionModel().getSelectedIndex();
        int index = mailItemTableView.getSelectionModel().getSelectedIndex();
        if (index != -1 && categoryIndex == 0) {
            GlobalVariables.replyType = "Reply To All";
            GlobalVariables.replyIndex = index;
            Scene scene = ViewFactory.defaultFactory.getDraftMailScene();
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Reply Mail");
            stage.show();
        }
    }

    @FXML
    public void handeClickedOnForwardMenuButton() {
        int categoryIndex = categoryListView.getSelectionModel().getSelectedIndex();
        int index = mailItemTableView.getSelectionModel().getSelectedIndex();
        if (index != -1 && categoryIndex == 0) {
            GlobalVariables.replyType = "Forward";
            GlobalVariables.replyIndex = index;
            Scene scene = ViewFactory.defaultFactory.getDraftMailScene();
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Forward Mail");
            stage.show();
        }
    }

    @FXML
    public void handeClickedOnDeleteMenuButton() {
        int categoryIndex = categoryListView.getSelectionModel().getSelectedIndex();
        int index = mailItemTableView.getSelectionModel().getSelectedIndex();
        if (index != -1 && categoryIndex == 0) {
            try {
                Message message = GlobalVariables.messageList.get(index);
                message.setFlag(Flags.Flag.DELETED, true);
                message.getFolder().expunge();
            } catch (Exception e) {
                e.printStackTrace();
                itemsLabel.setText(e.getMessage());
            }
        }
    }

    @FXML
    public void handeClickedOnFollowMenuButton() {

        int categoryIndex = categoryListView.getSelectionModel().getSelectedIndex();
        int index = mailItemTableView.getSelectionModel().getSelectedIndex();
        if (index != -1 && categoryIndex == 0) {
            try {
                Message message = GlobalVariables.messageList.get(index);
                Flags flags = message.getFlags();
                if (flags.contains(Flags.Flag.FLAGGED)) {
                    message.setFlag(Flags.Flag.FLAGGED, false);
                } else {
                    message.setFlag(Flags.Flag.FLAGGED, true);
                }
                message.saveChanges();
            } catch (Exception e){
                e.printStackTrace();
                itemsLabel.setText(e.getMessage());
            }
        }
    }

    @FXML
    public void handeClickedOnPrintMenuButton() {
        int categoryIndex = categoryListView.getSelectionModel().getSelectedIndex();
        int index = mailItemTableView.getSelectionModel().getSelectedIndex();
        if (index != -1 && categoryIndex == 0) {
            try {
                String content = "";
                Message message = GlobalVariables.messageList.get(index);
                Multipart multipart = (Multipart) message.getContent();
                for (int i = 0; i < multipart.getCount(); i++) {
                    BodyPart bodyPart = multipart.getBodyPart(i);
                    if (bodyPart.getContentType().contains("text/html")) {
                        content = bodyPart.getContent().toString();
                    }
                }
                PrinterJob job = PrinterJob.createPrinterJob();
                WebEngine engine = mailContentWebView.getEngine();
                engine.loadContent(content);
                engine.print(job);
            } catch (Exception e) {
                e.printStackTrace();
                itemsLabel.setText(e.getMessage());
            }
        }
    }

    void initSearchCategory() {
        searchVBox.getChildren().remove(searchComboBox);
        searchVBox.getChildren().remove(exSearchButton);
        searchVBox.getChildren().remove(exSearchFlowPane);
        searchVBox.getChildren().remove(addCriteriaButton);

        searchComboBox.setEditable(true);
        exSearchButton.setGraphic(ViewFactory.defaultFactory.resolveIconWithName("images/arrows_down.png"));
        exSearchButton.setText("");
    }

    final ObservableList<MailItem> mailData = FXCollections.observableArrayList();

    final ObservableList<AddressBookItem> addressBookData = FXCollections.observableArrayList();

    void initTestValues() {
        mAryCategory.add(get("Mail"));
        mAryCategory.add(get("AddressBook"));
        categoryListView.getItems().addAll(mAryCategory);
        categoryListView.getSelectionModel().select(0);

        mAryMailItems.add(get("Drafts"));
        mAryMailItems.add(get("Outbox"));
        mAryMailItems.add(get("Junk"));
        mAryMailItems.add(get("Inbox"));
        mAryMailItems.add(get("Deleted"));
        mAryMailItems.add(get("Search"));

        mAryAddressBookItems.add(get("By_UserName"));
        mAryAddressBookItems.add(get("By_Department"));
        mAryAddressBookItems.add(get("By_UserLevel"));
        mAryAddressBookItems.add(get("By_UserSecurityLevel"));

        categoryItemListView.getItems().addAll(mAryMailItems);

        WebEngine engine = mailContentWebView.getEngine();
        String url = "http://java2s.com/";
        engine.load(url);

        categoryItemListView.getSelectionModel().select(CAT_MAIL_INBOX);
    }

    public void initMailBoxTable(int aBoxIndex) {

        TableColumn importantCol = (TableColumn) mailItemTableView.getColumns().get(0);
        TableColumn attachCol = (TableColumn) mailItemTableView.getColumns().get(1);
        TableColumn categoryCol = (TableColumn) mailItemTableView.getColumns().get(2);
        TableColumn fromCol = (TableColumn) mailItemTableView.getColumns().get(3);
        TableColumn subjectCol = (TableColumn) mailItemTableView.getColumns().get(4);
        TableColumn receivedDateCol = (TableColumn) mailItemTableView.getColumns().get(5);
        TableColumn sizeCol = (TableColumn) mailItemTableView.getColumns().get(6);
        TableColumn flagCol = (TableColumn) mailItemTableView.getColumns().get(7);

        importantCol.setVisible(true);
        attachCol.setVisible(true);
        categoryCol.setVisible(true);
        flagCol.setVisible(true);

        categoryCol.setPrefWidth(120);
        fromCol.setPrefWidth(206);
        subjectCol.setPrefWidth(350);
        receivedDateCol.setPrefWidth(184);
        sizeCol.setPrefWidth(64);
        flagCol.setPrefWidth(20);

        ((TableColumn) mailItemTableView.getColumns().get(8)).setVisible(false);
        ((TableColumn) mailItemTableView.getColumns().get(9)).setVisible(false);
        ((TableColumn) mailItemTableView.getColumns().get(10)).setVisible(false);
        ((TableColumn) mailItemTableView.getColumns().get(11)).setVisible(false);

        categoryCol.setText(get("Security_Level"));
        subjectCol.setText(get("Subject"));
        sizeCol.setText(get("Size"));
        flagCol.setText("");

        switch (aBoxIndex) {
            case CAT_MAIL_DRAFTS:
            case CAT_MAIL_SENT:
            case CAT_MAIL_OUTBOX:
                fromCol.setText(get("To"));
                receivedDateCol.setText(get("Sent"));
                fromCol.setCellValueFactory(new PropertyValueFactory("to"));
                receivedDateCol.setCellValueFactory(new PropertyValueFactory("sentDate"));
                break;
            case CAT_MAIL_JUNK:
            case CAT_MAIL_INBOX:
            case CAT_MAIL_DELETE:
                fromCol.setText(get("From"));
                receivedDateCol.setText(get("Received"));
                fromCol.setCellValueFactory(new PropertyValueFactory("from"));
                receivedDateCol.setCellValueFactory(new PropertyValueFactory("receivedDate"));
                break;
            case CAT_MAIL_SEARCH:
                break;
        }

        importantCol.setCellValueFactory(new PropertyValueFactory("important"));
        importantCol.setCellFactory(new Callback<TableColumn<MailItem, MailItem>, TableCell<MailItem, MailItem>>() {
            @Override
            public TableCell<MailItem, MailItem> call(TableColumn<MailItem, MailItem> param) {
                TableCell cell = new TableCell() {
                    @Override
                    protected void updateItem(Object item, boolean empty) {
                        if (isSelectedMailCategory()) {
                            if (item != null) {
                                ImageView imageview = (ImageView) ViewFactory.defaultFactory.resolveMailImportantIcon(item.toString());
                                setGraphic(imageview);
                            } else {
                                setGraphic(null);
                            }
                        } else {
                            setGraphic(null);
                        }
                    }
                };

                return cell;
            }
        });

        attachCol.setGraphic(ViewFactory.defaultFactory.resolveIconWithName("images/attach.png"));
        attachCol.setCellValueFactory(new PropertyValueFactory("attach"));
        attachCol.setCellFactory(new Callback<TableColumn<MailItem, MailItem>, TableCell<MailItem, MailItem>>() {
            @Override
            public TableCell<MailItem, MailItem> call(TableColumn<MailItem, MailItem> param) {
                TableCell cell = new TableCell() {
                    @Override
                    protected void updateItem(Object item, boolean empty) {
                        if (isSelectedMailCategory()) {
                            if (item != null) {
                                if ((Boolean) item) {
                                    setGraphic(ViewFactory.defaultFactory.resolveIconWithName("images/attach.png"));
                                } else {
                                    setGraphic(null);
                                }
                            } else {
                                setGraphic(null);
                            }
                        } else {
                            setGraphic(null);
                        }
                    }
                };

                return cell;
            }
        });

        categoryCol.setCellValueFactory(new PropertyValueFactory("category"));
        categoryCol.setCellFactory(new Callback<TableColumn<MailItem, MailItem>, TableCell<MailItem, MailItem>>() {
            @Override
            public TableCell<MailItem, MailItem> call(TableColumn<MailItem, MailItem> param) {
                TableCell cell = new TableCell() {
                    @Override
                    protected void updateItem(Object item, boolean empty) {
                        if (isSelectedMailCategory()) {
                            if (item != null) {
                                HBox box= new HBox();
                                box.setSpacing(10) ;
                                VBox vbox = new VBox();

                                if(!item.toString().isEmpty())
                                    vbox.getChildren().add(new Label(item.toString() + ""));
                                else
                                    vbox.getChildren().add(new Label(""));

                                Node imageview = ViewFactory.defaultFactory.resolveMailCategoryIcon(item.toString());

                                box.getChildren().addAll(imageview,vbox);
                                setGraphic(box);
                            } else {
                                setGraphic(null);
                            }
                        } else {
                            setGraphic(null);
                        }
                    }
                };

                return cell;
            }
        });

        flagCol.setGraphic(ViewFactory.defaultFactory.resolveIconWithName("images/follow_down_small.png"));
        flagCol.setCellValueFactory(new PropertyValueFactory("follow"));
        flagCol.setCellFactory(new Callback<TableColumn<MailItem, MailItem>, TableCell<MailItem, MailItem>>() {
            @Override
            public TableCell<MailItem, MailItem> call(TableColumn<MailItem, MailItem> param) {
                TableCell cell = new TableCell() {
                    @Override
                    protected void updateItem(Object item, boolean empty) {
                        if (isSelectedMailCategory()) {
                            if (item != null) {
                                if ((Boolean) item) {
                                    setGraphic(ViewFactory.defaultFactory.resolveIconWithName("images/follow_up_small.png"));
                                } else {
                                    setGraphic(ViewFactory.defaultFactory.resolveIconWithName("images/follow_down_small.png"));
                                }
                            } else {
                                setGraphic(null);
                            }
                        } else {
                            if (item != null) {
                                setText(item.toString());
                            } else {
                                setText("");
                            }
                            setGraphic(null);
                        }
                    }
                };

                return cell;
            }
        });

        subjectCol.setCellValueFactory(new PropertyValueFactory("subject"));
        sizeCol.setCellValueFactory(new PropertyValueFactory("size"));

        searchComboBox.getEditor().setText("");
        FilteredList<MailItem> filteredData = new FilteredList<>(mailData, p -> true);
        searchComboBox.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {

            System.out.println("textfield changed from " + oldValue + " to " + newValue);

            filteredData.setPredicate(mailItem -> {

                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Compare first name and last name of every person with filter text.
                String lowerCaseFilter = newValue.toLowerCase();

                if (mailItem.fromProperty().getValue().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches last name.
                }
                if (mailItem.subjectProperty().getValue().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches last name.
                }
                if (mailItem.categoryProperty().getValue().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches last name.
                }
                if (mailItem.toProperty().get().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches last name.
                }
                if (mailItem.sizeProperty().getValue().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches last name.
                }
                if (mailItem.receivedDateProperty().getValue().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches last name.
                }
                if (mailItem.sentDateProperty().getValue().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches last name.
                }

                return false;

            });
        });

        // 3. Wrap the FilteredList in a SortedList.
        SortedList<MailItem> sortedData = new SortedList<>(filteredData);

        // 4. Bind the SortedList comparator to the TableView comparator.
        sortedData.comparatorProperty().bind(mailItemTableView.comparatorProperty());

        // 5. Add sorted (and filtered) data to the table.
        mailItemTableView.setItems(sortedData);

//        mailItemTableView.setItems(mailData);

        mailItemTableView.getSelectionModel().select(0);
        handleClickedOnTableView();

        GlobalVariables.mailData = mailData;
    }

    public void initAddressBookTable(String type) {

        ((TableColumn)mailItemTableView.getColumns().get(0)).setVisible(false);
        ((TableColumn)mailItemTableView.getColumns().get(1)).setVisible(false);
        ((TableColumn)mailItemTableView.getColumns().get(2)).setVisible(false);
        ((TableColumn)mailItemTableView.getColumns().get(7)).setVisible(false);

        TableColumn userIDCol = (TableColumn) mailItemTableView.getColumns().get(3);
        TableColumn userDepartmentCol = (TableColumn) mailItemTableView.getColumns().get(4);
        TableColumn userPathCol = (TableColumn) mailItemTableView.getColumns().get(5);
        TableColumn userNameCol = (TableColumn) mailItemTableView.getColumns().get(6);
        TableColumn mailAddressCol = (TableColumn) mailItemTableView.getColumns().get(8);
        TableColumn userLevelCol = (TableColumn) mailItemTableView.getColumns().get(9);
        TableColumn userSecurityLevelCol = (TableColumn) mailItemTableView.getColumns().get(10);

        userIDCol.setPrefWidth(150);
        userDepartmentCol.setPrefWidth(150);
        userPathCol.setPrefWidth(150);
        userNameCol.setPrefWidth(100);
        mailAddressCol.setPrefWidth(150);
        userLevelCol.setPrefWidth(150);
        userSecurityLevelCol.setPrefWidth(150);

        userIDCol.setText(get("User_ID"));
        userDepartmentCol.setText(get("User_Department"));
        userPathCol.setText(get("User_Path"));
        userNameCol.setText(get("User_Name"));
        mailAddressCol.setText(get("Mail_Address"));
        userLevelCol.setText(get("User_Level"));
        userSecurityLevelCol.setText(get("User_Security_Level"));

        userIDCol.setVisible(true);
        userDepartmentCol.setVisible(true);
        userPathCol.setVisible(true);
        userNameCol.setVisible(true);
        mailAddressCol.setVisible(true);
        userLevelCol.setVisible(true);
        userSecurityLevelCol.setVisible(true);

        userIDCol.setCellValueFactory(new PropertyValueFactory("userID"));
        userDepartmentCol.setCellValueFactory(new PropertyValueFactory("userDepartment"));
        userPathCol.setCellValueFactory(new PropertyValueFactory("userPath"));
        userNameCol.setCellValueFactory(new PropertyValueFactory("userName"));
        mailAddressCol.setCellValueFactory(new PropertyValueFactory("mailAddress"));
        userLevelCol.setCellValueFactory(new PropertyValueFactory("userLevel"));
        userSecurityLevelCol.setCellValueFactory(new PropertyValueFactory("userSecurityLevel"));

        searchComboBox.getEditor().setText("");
        FilteredList<AddressBookItem> filteredData = new FilteredList<>(addressBookData, p -> true);
        searchComboBox.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("textfield changed from " + oldValue + " to " + newValue);
            filteredData.setPredicate(addressBookItem -> {

                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                // Compare first name and last name of every person with filter text.
                String lowerCaseFilter = newValue.toLowerCase();
                if (addressBookItem.userIDProperty().getValue().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches last name.
                }
                if (addressBookItem.userDepartmentProperty().getValue().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches last name.
                }
                if (addressBookItem.userPathProperty().getValue().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches last name.
                }
                if (addressBookItem.userNameProperty().getValue().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches last name.
                }
                if (addressBookItem.mailAddressProperty().getValue().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches last name.
                }
                if (addressBookItem.userLevelProperty().getValue().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches last name.
                }
                if (addressBookItem.userSecurityLevelProperty().getValue().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches last name.
                }
                return false;

            });
        });

        // 3. Wrap the FilteredList in a SortedList.
        SortedList<AddressBookItem> sortedData = new SortedList<>(filteredData);

        // 4. Bind the SortedList comparator to the TableView comparator.
        sortedData.comparatorProperty().bind(mailItemTableView.comparatorProperty());

        // 5. Add sorted (and filtered) data to the table.
        mailItemTableView.setItems(sortedData);
    }

    public static class MailItem {
        private StringProperty important;
        private StringProperty category;
        private StringProperty from;
        private StringProperty to;
        private StringProperty subject;
        private StringProperty receivedDate;
        private StringProperty sentDate;
        private StringProperty size;
        private BooleanProperty attach;
        private BooleanProperty follow;
        private boolean isCC = true;

        public int index = 0;

        private MailItem(String important, String category, String from, String subject, String receivedDate, String size, Boolean attach, Boolean follow) {
            this.important = new SimpleStringProperty(important);
            this.category = new SimpleStringProperty(category);
            this.from = new SimpleStringProperty(from);
            this.to = new SimpleStringProperty(from);
            this.subject = new SimpleStringProperty(subject);
            this.receivedDate = new SimpleStringProperty(receivedDate);
            this.sentDate = new SimpleStringProperty(receivedDate);
            this.size = new SimpleStringProperty(size);
            this.attach = new SimpleBooleanProperty(attach);
            this.follow = new SimpleBooleanProperty(follow);
        }

        public StringProperty importantProperty() { return important; }
        public StringProperty categoryProperty() { return category; }
        public StringProperty fromProperty() { return from; }
        public StringProperty toProperty() { return to; }
        public StringProperty subjectProperty() { return subject; }
        public StringProperty receivedDateProperty() { return receivedDate; }
        public StringProperty sentDateProperty() { return sentDate; }
        public StringProperty sizeProperty() { return size; }
        public BooleanProperty attachProperty() { return attach; }
        public BooleanProperty followProperty() { return follow; }

        @Override
        public String toString() {
            return "from : <" +
                    from.getValue() + ">, to : <" +
                    to.getValue() + ">, subject : " +
                    subject.getValue() + ", received date : " +
                    receivedDate.getValue() + ", sent date : " +
                    sentDate.getValue() + ", category : " +
                    category.getValue();
        }

        public boolean isCC() {
            return isCC;
        }
    }

    @FXML
    public void handleClickedOnTableView() {
        if (isSelectedMailCategory()) {
            MailItem item = (MailItem) mailItemTableView.getSelectionModel().getSelectedItem();
            if (item != null) {
                fromLabel.setText("<" + item.fromProperty().getValue() + ">");
                subjectLabel.setText(item.subjectProperty().getValue());
                receivedDateLabel.setText(item.receivedDateProperty().getValue());
            }
        }
    }

    //for test
    private class CategoryListCell extends ListCell<String> {
        @Override
        public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(ViewFactory.defaultFactory.resolveCategoryIcon(getIndex()));
                setText(item);
            }
        }
    }

    //for test
    private class CategoryListItemCell extends ListCell<String> {
        @Override
        public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                setGraphic(null);
                setText(null);
            } else {
                setText(item);
                if (isSelectedMailCategory()) {
                    setGraphic(ViewFactory.defaultFactory.resolveMailBoxListItemIcon(getIndex()));
                } else {
                    setGraphic(ViewFactory.defaultFactory.resolveIconWithName("images/user.png"));
                }
            }
        }
    }

    @FXML
    void handleClickedOnAdressBookCatButton() {
        categoryListView.getSelectionModel().select(1);
        handleClickedOnCategory(null);
    }

    @FXML
    void handleClickedOnMailCatButton() {
        categoryListView.getSelectionModel().select(0);
        handleClickedOnCategory(null);
    }

    @FXML
    public void handleClickedOnCategoryItem() {
        System.out.println("OnClicked: handleClickedOnCategoryItem");
        String catTitle = "";
        if (isSelectedMailCategory()) {
            catTitle = categoryItemListView.getSelectionModel().getSelectedItem().toString();
            currentSelectedMailBoxItem = categoryItemListView.getSelectionModel().getSelectedIndex();
            categoryNameLabel1.setGraphic(ViewFactory.defaultFactory.resolveMailBoxListItemIcon(currentSelectedMailBoxItem));

            initMailBoxTable(currentSelectedMailBoxItem);

            ObservableList<Node> nodes = mainSplitePane.getItems();
            boolean isExist = false;
            for (Node eachNode: nodes) {
                if (eachNode.getId().equalsIgnoreCase("rightAnchorPane")) {
                    isExist = true;
                    break;
                }
            }

            if (isShowingMode) {
                if (!isExist)
                    mainSplitePane.getItems().add(rightAnchorPane);
            }

            if (isExpandedOfLeftPane()) {
                mainSplitePane.setDividerPositions(mDivPosOfMailExp);
                mDivPosOfMailExp = mainSplitePane.getDividerPositions();
            }

            addressBookCatButton.setSelected(false);
            mailCatButton.setSelected(true);
        } else {
            catTitle = get("AddressBook");
            categoryNameLabel1.setGraphic(ViewFactory.defaultFactory.resolveIconWithName("/com/jinyuan/view/images/contacts.png"));

            initAddressBookTable("");

            if (!isShowingMode)
                mainSplitePane.getItems().remove(rightAnchorPane);

            if (isExpandedOfLeftPane())
                mDivPosOfAddressBookExp = mainSplitePane.getDividerPositions();

            addressBookCatButton.setSelected(true);
            mailCatButton.setSelected(false);
        }

        categoryNameLabel1.setText(catTitle);
    }

    public void handleClickedOnCategory(MouseEvent event) {

        String catTitle = categoryListView.getSelectionModel().getSelectedItem().toString();
        System.out.println("OnClicked: Category list index = " + catTitle);

        categoryNameLabel.setText(catTitle);

        if (categoryListView.getSelectionModel().getSelectedIndex() == currentSelectedCategoryItem)
            return;
        currentSelectedCategoryItem = categoryListView.getSelectionModel().getSelectedIndex();

        categoryItemListView.getItems().clear();
        if (isSelectedMailCategory()) {
            categoryItemListView.getItems().addAll(mAryMailItems);

            if (currentSelectedMailBoxItem == -1)
                categoryItemListView.getSelectionModel().select(CAT_MAIL_INBOX);
            else
                categoryItemListView.getSelectionModel().select(currentSelectedMailBoxItem);
        } else {
            categoryItemListView.getItems().addAll(mAryAddressBookItems);
        }

        handleClickedOnCategoryItem();
    }

    public boolean isSelectedMailCategory() {
        return categoryListView.getSelectionModel().getSelectedIndex() == CAT_MAIL;
    }

    void initCategoryButtons() {
        ImageView img = (ImageView) ViewFactory.defaultFactory.resolveCategoryIcon(CAT_MAIL);
        mailCatButton.setGraphic(img);
        mailCatButton.setText("");

        img = (ImageView) ViewFactory.defaultFactory.resolveCategoryIcon(CAT_ADB);
        addressBookCatButton.setGraphic(img);
        addressBookCatButton.setText("");
    }

    /**
     * @autbor Pilot
     * Initialization for the toolbar button icons and titles
     */
    void initToolbarButtons() {

        leftPanCollapseButton.setGraphic(ViewFactory.defaultFactory.resolveIconWithName("images/arrows_left.png"));
        leftPanCollapseButton.setText("");

        leftPanExpandButton.setGraphic(ViewFactory.defaultFactory.resolveIconWithName("images/arrows_right.png"));
        leftPanExpandButton.setText("");

        newMailMenuButton.setGraphic(ViewFactory.defaultFactory.resolveIconWithName("/com/jinyuan/view/images/mail_new.png"));

        printButton.setGraphic(ViewFactory.defaultFactory.resolveIconWithName("/com/jinyuan/view/images/print.png"));
        printButton.setText("");

        moveToFolderButton.setGraphic(ViewFactory.defaultFactory.resolveIconWithName("/com/jinyuan/view/images/move_to_folder.png"));
        moveToFolderButton.setText("");

        deleteButton.setGraphic(ViewFactory.defaultFactory.resolveIconWithName("/com/jinyuan/view/images/delete.png"));
        deleteButton.setText("");

        replyInContentButton.setGraphic(ViewFactory.defaultFactory.resolveIconWithName("/com/jinyuan/view/images/mail_reply.png"));
        replyButton.setGraphic(ViewFactory.defaultFactory.resolveIconWithName("/com/jinyuan/view/images/mail_reply.png"));

        replyAllInContentButton.setGraphic(ViewFactory.defaultFactory.resolveIconWithName("/com/jinyuan/view/images/reply_to_all.png"));
        replyToAllButton.setGraphic(ViewFactory.defaultFactory.resolveIconWithName("/com/jinyuan/view/images/reply_to_all.png"));

        forwardInContentButton.setGraphic(ViewFactory.defaultFactory.resolveIconWithName("/com/jinyuan/view/images/forward.png"));
        forwardButton.setGraphic(ViewFactory.defaultFactory.resolveIconWithName("/com/jinyuan/view/images/forward.png"));

        categoriseButton.setGraphic(ViewFactory.defaultFactory.resolveIconWithName("/com/jinyuan/view/images/categorize.png"));
        categoriseButton.setText("");

        followUpButton.setGraphic(ViewFactory.defaultFactory.resolveIconWithName("/com/jinyuan/view/images/follow_up.png"));
        followUpButton.setText("");

        sendReceiveButton.setGraphic(ViewFactory.defaultFactory.resolveIconWithName("/com/jinyuan/view/images/sendReceive.png"));

        oneNoteButton.setGraphic(ViewFactory.defaultFactory.resolveIconWithName("/com/jinyuan/view/images/one_note.png"));
        oneNoteButton.setText("");

        addressBookButton.setGraphic(ViewFactory.defaultFactory.resolveIconWithName("/com/jinyuan/view/images/contacts.png"));
        addressBookButton.setText("");

        addressBookComboBox.setEditable(true);
        addressBookComboBox.getEditor().textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable,
                                String oldValue, String newValue) {
                System.out.println("Text changed");
            }
        });

        helpButton.setGraphic(ViewFactory.defaultFactory.resolveIconWithName("/com/jinyuan/view/images/help.png"));
        helpButton.setText("");
    }

    @FXML
    public void handleClickedOnLeftPanCollapseButton() {
        System.out.println("OnClicked : Collapse index = " + mainSplitePane.getItems());

        Node child = mainSplitePane.lookup("#leftAnchorPane");

        if (child != null) {
            //before collapse
            if (isSelectedMailCategory())
                mDivPosOfMailExp = mainSplitePane.getDividerPositions();
            else
                mDivPosOfAddressBookExp = mainSplitePane.getDividerPositions();

            mainSplitePane.getItems().remove(leftAnchorPane);
            categoryListHBox.getChildren().add(0, expandAnchorPane);

            if (isSelectedMailCategory())
                mainSplitePane.setDividerPositions(mDivPosOfMailExp[1]);

        } else {
            //before expand
            mainSplitePane.getItems().add(0, leftAnchorPane);
            categoryListHBox.getChildren().remove(expandAnchorPane);
            if (isSelectedMailCategory())
                mainSplitePane.setDividerPositions(mDivPosOfMailExp);
            else
                mainSplitePane.setDividerPositions(mDivPosOfAddressBookExp);
        }

    }

    public boolean isExpandedOfLeftPane() {
        return mainSplitePane.lookup("#leftAnchorPane") != null;
    }


    public String getPCName() {
        String hostname = System.getenv().get("COMPUTERNAME");
        return hostname.toLowerCase();
//        return "user4";
    }

    public void getMailBoxInfo() {
        try {
            String url = String.format(Apis.GET_MAILBOX_INFO, getPCName());
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            int responseCode = con.getResponseCode();

            InputStreamReader inputStreamReader = new InputStreamReader(con.getInputStream(), Charset.forName("GB18030"));
            BufferedReader in = new BufferedReader( inputStreamReader);

            String inputLine;
            StringBuffer response = new StringBuffer();
            while((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            JSONParser parser = new JSONParser();
            JSONObject jsonObj = (JSONObject)parser.parse(response.toString());
            GlobalVariables.account = new ValidAccount(jsonObj.get("mailbox").toString(), jsonObj.get("mailboxPwd").toString(), "");

            getModelAccess().setMailType("other");

            GlobalVariables.messageList = new ArrayList<>();

            CreateAndRegisterEmailAccountService createAndRegisterEmailAccountService = new CreateAndRegisterEmailAccountService( jsonObj.get("mailbox").toString(), jsonObj.get("mailboxPwd").toString(), getModelAccess());
            createAndRegisterEmailAccountService.parent = this;
            createAndRegisterEmailAccountService.start();
            createAndRegisterEmailAccountService.setOnSucceeded(e->{

                if(createAndRegisterEmailAccountService.getValue() != EmailConstants.LOGIN_STATE_SUCCEDED){
                    System.out.println("login error");
                }else{
                }
            });

        } catch (Exception e) {
            System.out.println("getMailBoxInfo() error");
        }
    }

    public void getAddressList() {

        try {
            String url = Apis.GET_ADDRESSBOOK;
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            int responseCode = con.getResponseCode();

            InputStreamReader inputStreamReader = new InputStreamReader(con.getInputStream(), Charset.forName("GB18030"));
            BufferedReader in = new BufferedReader( inputStreamReader);

            String inputLine;
            StringBuffer response = new StringBuffer();
            while((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            JSONParser parser = new JSONParser();
            JSONArray array = (JSONArray)parser.parse(response.toString());
            GlobalVariables.addresslist = new ArrayList<AddressBookItem>();
            for (int i = 0; i < array.size(); i++) {
                JSONObject jsonObj = (JSONObject)array.get(i);
                AddressBookItem item = new AddressBookItem(jsonObj.get("u").toString(), jsonObj.get("d").toString(), jsonObj.get("dp").toString(), jsonObj.get("n").toString(), jsonObj.get("m").toString(), jsonObj.get("ul").toString(), jsonObj.get("usl").toString());
                addressBookData.add(item);
                GlobalVariables.addresslist.add(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getSecurityLevels() {
        try {
            String url = Apis.GET_MAIL_SECURITY_LEVEL;
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            int responseCode = con.getResponseCode();

            InputStreamReader inputStreamReader = new InputStreamReader(con.getInputStream(), Charset.forName("GB18030"));
            BufferedReader in = new BufferedReader( inputStreamReader);

            String inputLine;
            StringBuffer response = new StringBuffer();
            while((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            JSONParser parser = new JSONParser();
            JSONArray array = (JSONArray)parser.parse(response.toString());
            GlobalVariables.mailSecurityList = new ArrayList<MailSecurity>();
            for (int i = 0; i < array.size(); i++) {
                JSONObject jsonObj = (JSONObject)array.get(i);
                String level = jsonObj.get("sysSecurityLevel").toString();
                String name = jsonObj.get("sysSecurityLevelName").toString();
                String color = jsonObj.get("sysSecurityLevelColor").toString();
                MailSecurity security = new MailSecurity(Integer.parseInt(level), name, color);
                GlobalVariables.mailSecurityList.add(security);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateMailItem(Message message) {
        System.out.println(message.toString());

        String from = "";
        String subject = "";
        String sentDate = "";
        String mailSize = "";
        boolean hasAttach = false;
        String content = "";
        String category = "";
        boolean follow = false;

        try {
            from = message.getFrom()[0].toString();
            subject = message.getSubject().toString();
            if (message.getSentDate() != null) {
                sentDate = message.getSentDate().toString();
            }
            int size = message.getSize();
            if (size < 1024) {
                mailSize = "1Kb";
            } else if (size < 1024 * 1024) {
                mailSize = String.format("%dKb", size / 1024);
            } else {
                mailSize = String.format("%dMb", size / 1024 / 1024);
            }
            Multipart multipart = (Multipart) message.getContent();
            for (int i = 0; i < multipart.getCount(); i++) {
                BodyPart bodyPart = multipart.getBodyPart(i);
                if (bodyPart.getFileName() != null && bodyPart.getFileName().length() > 0) {
                    hasAttach = true;
                } else if (bodyPart.getContentType().contains("text/html")) {
                    content = bodyPart.getContent().toString();
                }
            }

            Document doc = Jsoup.parse(content);
            if (content.contains("mailSecurityLevel")) {
                category = doc.select("meta[name=mailSecurityLevel]").get(0).attr("content");
            }

            Flags flags = message.getFlags();
            if (flags.contains(Flags.Flag.FLAGGED)) {
                follow = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        MailItem mailItem = new MailItem(category, category, from, subject, sentDate, mailSize, hasAttach, false);
        mailData.add(mailItem);
    }
}