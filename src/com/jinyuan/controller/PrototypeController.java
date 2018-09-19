package com.jinyuan.controller;

import com.jinyuan.view.ViewFactory;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.util.Callback;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class PrototypeController extends AbstractController implements Initializable {

    //toolbar buttons
    @FXML
    MenuButton newMailMenuButton;

    @FXML
    Button printButton;

    @FXML
    Button moveToFolderButton;

    @FXML
    Button deleteButton;

    @FXML
    Button replyButton;

    @FXML
    Button replyToAllButton;

    @FXML
    Button forwardButton;

    @FXML
    Button categoriseButton;

    @FXML
    Button followUpButton;

    @FXML
    MenuButton sendReceiveButton;

    @FXML
    Button oneNoteButton;

    @FXML
    Button addressBookButton;

    @FXML
    ComboBox addressBookComboBox;

    @FXML
    Button helpButton;

    //main split
    @FXML
    SplitPane mainSplitePane;

    @FXML
    AnchorPane leftAnchorPane;

    @FXML
    AnchorPane centerAnchorPane;

    @FXML
    AnchorPane rightAnchorPane;

    @FXML
    Label categoryNameLabel;

    //collapse
    @FXML
    Button leftPanCollapseButton;

    @FXML
    Button leftPanExpandButton;

    @FXML
    HBox categoryListHBox;

    //expand
    @FXML
    AnchorPane expandAnchorPane;

    @FXML
    ListView categoryListView;

    @FXML
    Label itemsLabel;

    @FXML
    ListView categoryItemListView;

    @FXML
    TableView mailItemTableView;

    @FXML
    WebView mailContentWebView;

    @FXML
    ToggleButton mailCatButton;

    @FXML
    ToggleButton addressBookCatButton;

    //search box for mail category
    @FXML
    ComboBox searchComboBox;

    @FXML
    Button exSearchButton;

    @FXML
    FlowPane exSearchFlowPane;

    @FXML
    MenuButton addCriteriaButton;

    @FXML
    VBox searchVBox;

    @FXML
    Label categoryNameLabel1;

    String currentSelectedMailBoxItem = "";

    //divider postions of the mailSplitPane;
    double[] mDivPosOfMainSplitPane;
    double[] mDivPosOfInit;
    List<String> mAryCategory = new ArrayList<>();
    List<String> mAryMailItems = new ArrayList<>();
    List<String> mAryAddressBookItems = new ArrayList<>();

    public PrototypeController(ModelAccess modelAccess) {
        super(modelAccess);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        initCategoryButtons();
        initToolbarButtons();
        initSearchCategory();

        //init list for test
        initTestValues();
        //<-- test end

        handleClickedOnMailCatButton();

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

        mDivPosOfInit = mainSplitePane.getDividerPositions();
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

    final ObservableList<MailItem> mailData = FXCollections.observableArrayList(
            new MailItem("Important", "jacob.smith@example.com", "Receiving the packages", "2018-09-18 09:30:34", "34KB"),
            new MailItem("Normal",  "isabella.johnson@example.com", "Lost things", "2018-09-18 10:30:34", "23KB"),
            new MailItem("Emergency",  "ethan.williams@example.com", "New gallery", "2018-09-18 11:30:34", "20KB"),
            new MailItem("Standard",  "emma.jones@example.com", "New market launched", "2018-09-18 12:30:34", "30KB"),
            new MailItem("SoSo", "michael.brown@example.com", "Indeed job growing", "2018-09-18 12:30:34", "40KB")
    );

    final ObservableList<AddressBookItem> addressBookData = FXCollections.observableArrayList(
            new AddressBookItem(
                    "Jhone Smith",
                    "361 Degrees",
                    "Receiving the packages",
                    "123123123123",
                    "123123123123",
                    "123123123123",
                    "123123123123",
                    "Aluminum Corporation of China Limited",
                    "Amoi"),
            new AddressBookItem(
                    "Zhong Jingyi",
                    "Anta Sports",
                    "Receiving the packages",
                    "123123123123",
                    "123123123123",
                    "123123123123",
                    "123123123123",
                    "Aluminum Corporation of China Limited",
                    "Amoi"),
            new AddressBookItem(
                    "Wen Cai",
                    "Baidu",
                    "Receiving the packages",
                    "123123123123",
                    "123123123123",
                    "123123123123",
                    "123123123123",
                    "Aluminum Corporation of China Limited",
                    "Amoi"),
            new AddressBookItem(
                    "Kong Ru",
                    "Bank of China",
                    "Receiving the packages",
                    "123123123123",
                    "123123123123",
                    "123123123123",
                    "123123123123",
                    "Aluminum Corporation of China Limited",
                    "Amoi"),
            new AddressBookItem(
                    "Liao Heng",
                    "Bank of Communications",
                    "Receiving the packages",
                    "123123123123",
                    "123123123123",
                    "123123123123",
                    "123123123123",
                    "Aluminum Corporation of China Limited",
                    "Amoi")
    );

    void initTestValues() {
        mAryCategory.add("Mail");
        mAryCategory.add("AddressBook");
        categoryListView.getItems().addAll(mAryCategory);
        categoryListView.getSelectionModel().select(0);

        mAryMailItems.add("RSS Feeds");
        mAryMailItems.add("Drafts");
        mAryMailItems.add("Outbox");
        mAryMailItems.add("Junk E-mail");
        mAryMailItems.add("Inbox");
        mAryMailItems.add("Sent Items");
        mAryMailItems.add("Deleted Items");
        mAryMailItems.add("Search Folders");

        mAryAddressBookItems.add("Phone List");
        mAryAddressBookItems.add("By Category");
        mAryAddressBookItems.add("By Company");
        mAryAddressBookItems.add("By Location");
        mAryAddressBookItems.add("Outlook Data Files");

        categoryItemListView.getItems().addAll(mAryMailItems);

        WebEngine engine = mailContentWebView.getEngine();
        String url = "http://java2s.com/";
        engine.load(url);

        categoryItemListView.getSelectionModel().select("Inbox");
    }

    public void initMailBoxTable(String boxName) {

        TableColumn categoryCol = (TableColumn) mailItemTableView.getColumns().get(0);
        TableColumn fromCol = (TableColumn) mailItemTableView.getColumns().get(1);
        TableColumn subjectCol = (TableColumn) mailItemTableView.getColumns().get(2);
        TableColumn receivedDateCol = (TableColumn) mailItemTableView.getColumns().get(3);
        TableColumn sizeCol = (TableColumn) mailItemTableView.getColumns().get(4);

        categoryCol.setPrefWidth(139);
        fromCol.setPrefWidth(206);
        subjectCol.setPrefWidth(350);
        receivedDateCol.setPrefWidth(126);
        sizeCol.setPrefWidth(64);

        ((TableColumn) mailItemTableView.getColumns().get(5)).setVisible(false);
        ((TableColumn) mailItemTableView.getColumns().get(6)).setVisible(false);
        ((TableColumn) mailItemTableView.getColumns().get(7)).setVisible(false);
        ((TableColumn) mailItemTableView.getColumns().get(8)).setVisible(false);

        categoryCol.setText("Category");
        subjectCol.setText("Subject");
        sizeCol.setText("Size");

        switch (boxName) {
            case "RSS Feeds":
                break;
            case "Drafts":
            case "Sent Items":
            case "Outbox":
                fromCol.setText("To");
                receivedDateCol.setText("Sent");
                fromCol.setCellValueFactory(new PropertyValueFactory("to"));
                receivedDateCol.setCellValueFactory(new PropertyValueFactory("sentDate"));
                break;
            case "Junk E-mail":
            case "Inbox":
            case "Deleted Items":
                fromCol.setText("From");
                receivedDateCol.setText("Received");
                fromCol.setCellValueFactory(new PropertyValueFactory("from"));
                receivedDateCol.setCellValueFactory(new PropertyValueFactory("receivedDate"));
                break;
            case "Search Folders":
                break;
        }

        categoryCol.setCellValueFactory(new PropertyValueFactory("category"));

        categoryCol.setCellFactory(new Callback<TableColumn<MailItem, String>, TableCell<MailItem, String>>() {
            @Override
            public TableCell<MailItem, String> call(TableColumn<MailItem, String> param) {
                TableCell cell = new TableCell() {
                    @Override
                    protected void updateItem(Object item, boolean empty) {
                        if(item!=null){
                            HBox box= new HBox();
                            box.setSpacing(10) ;
                            VBox vbox = new VBox();
                            vbox.getChildren().add(new Label(item.toString()));

                            ImageView imageview = (ImageView) ViewFactory.defaultFactory.resolveMailCategoryIcon(item.toString());
                            imageview.setFitHeight(16);
                            imageview.setFitWidth(16);

                            box.getChildren().addAll(imageview,vbox);
                            setGraphic(box);
                        }
                    }
                };

                return cell;
            }
        });

        subjectCol.setCellValueFactory(new PropertyValueFactory("subject"));
        sizeCol.setCellValueFactory(new PropertyValueFactory("size"));

        mailItemTableView.setItems(mailData);
    }

    public void initAddressBookTable(String type) {

        TableColumn fullNameCol = (TableColumn) mailItemTableView.getColumns().get(0);
        TableColumn companyCol = (TableColumn) mailItemTableView.getColumns().get(1);
        TableColumn fileAsCol = (TableColumn) mailItemTableView.getColumns().get(2);
        TableColumn businessPhoneCol = (TableColumn) mailItemTableView.getColumns().get(3);
        TableColumn businessFaxCol = (TableColumn) mailItemTableView.getColumns().get(4);

        TableColumn homePhoneCol = (TableColumn) mailItemTableView.getColumns().get(5);
        TableColumn mobilePhoneCol = (TableColumn) mailItemTableView.getColumns().get(6);
        TableColumn journalCol = (TableColumn) mailItemTableView.getColumns().get(7);
        TableColumn categoriesCol = (TableColumn) mailItemTableView.getColumns().get(8);

        fullNameCol.setPrefWidth(140);
        companyCol.setPrefWidth(140);
        fileAsCol.setPrefWidth(140);
        businessPhoneCol.setPrefWidth(140);
        businessFaxCol.setPrefWidth(140);
        homePhoneCol.setPrefWidth(140);
        mobilePhoneCol.setPrefWidth(140);
        journalCol.setPrefWidth(140);
        categoriesCol.setPrefWidth(140);

        fullNameCol.setText("Full Name");
        companyCol.setText("Company");
        fileAsCol.setText("File As");
        businessPhoneCol.setText("Business Phone");
        businessFaxCol.setText("Business Fax");

        homePhoneCol.setVisible(true);
        mobilePhoneCol.setVisible(true);
        journalCol.setVisible(true);
        categoriesCol.setVisible(true);

        switch (type) {
            case "Business Cards":
                break;
            case "Address Cards":
                break;
            case "Detailed Address Cards":
                break;
            case "Phone List":
                break;
            case "By Category":
                break;
            case "By Company":
                break;
            case "By Location":
                break;
            case "Outlook Data Files":
                break;
        }

        fullNameCol.setCellValueFactory(new PropertyValueFactory("fullName"));
        companyCol.setCellValueFactory(new PropertyValueFactory("company"));
        fileAsCol.setCellValueFactory(new PropertyValueFactory("fileAs"));
        businessPhoneCol.setCellValueFactory(new PropertyValueFactory("businessPhone"));
        businessFaxCol.setCellValueFactory(new PropertyValueFactory("businessFax"));
        homePhoneCol.setCellValueFactory(new PropertyValueFactory("homePhone"));
        mobilePhoneCol.setCellValueFactory(new PropertyValueFactory("mobilePhone"));
        journalCol.setCellValueFactory(new PropertyValueFactory("journal"));
        categoriesCol.setCellValueFactory(new PropertyValueFactory("categories"));

        mailItemTableView.setItems(addressBookData);
    }

    public static class AddressBookItem {
        private StringProperty fullName;
        private StringProperty company;
        private StringProperty fileAs;
        private StringProperty businessPhone;
        private StringProperty businessFax;
        private StringProperty homePhone;
        private StringProperty mobilePhone;
        private StringProperty journal;
        private StringProperty categories;

        private AddressBookItem(String fullName, String company, String fileAs, String businessPhone, String businessFax, String homePhone, String mobilePhone, String journal, String categories) {
            this.fullName = new SimpleStringProperty(fullName);
            this.company = new SimpleStringProperty(company);
            this.fileAs = new SimpleStringProperty(fileAs);
            this.businessPhone = new SimpleStringProperty(businessPhone);
            this.businessFax = new SimpleStringProperty(businessFax);
            this.homePhone = new SimpleStringProperty(homePhone);
            this.mobilePhone = new SimpleStringProperty(mobilePhone);
            this.journal = new SimpleStringProperty(journal);
            this.categories = new SimpleStringProperty(categories);
        }

        public StringProperty fullNameProperty() { return fullName; }
        public StringProperty companyProperty() { return company; }
        public StringProperty fileAsProperty() { return fileAs; }
        public StringProperty businessPhoneProperty() { return businessPhone; }
        public StringProperty businessFaxProperty() { return businessFax; }
        public StringProperty homePhoneProperty() { return homePhone; }
        public StringProperty mobilePhoneProperty() { return mobilePhone; }
        public StringProperty journalProperty() { return journal; }
        public StringProperty categoriesProperty() { return categories; }
    }

    public static class MailItem {
        private StringProperty category;
        private StringProperty from;
        private StringProperty to;
        private StringProperty subject;
        private StringProperty receivedDate;
        private StringProperty sentDate;
        private StringProperty size;

        private MailItem(String category, String from, String subject, String receivedDate, String size) {
            this.category = new SimpleStringProperty(category);
            this.from = new SimpleStringProperty(from);
            this.to = new SimpleStringProperty(from);
            this.subject = new SimpleStringProperty(subject);
            this.receivedDate = new SimpleStringProperty(receivedDate);
            this.sentDate = new SimpleStringProperty(receivedDate);
            this.size = new SimpleStringProperty(size);
        }

        public StringProperty categoryProperty() { return category; }
        public StringProperty fromProperty() { return from; }
        public StringProperty toProperty() { return to; }
        public StringProperty subjectProperty() { return subject; }
        public StringProperty receivedDateProperty() { return receivedDate; }
        public StringProperty sentDateProperty() { return sentDate; }
        public StringProperty sizeProperty() { return size; }
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
                setGraphic(ViewFactory.defaultFactory.resolveCategoryIcon(item));
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
                if (isSelectedCategoryMail()) {
                    setGraphic(ViewFactory.defaultFactory.resolveMailBoxListItemIcon(item));
                } else {
                    setGraphic(ViewFactory.defaultFactory.resolveIconWithName("images/user.png"));
                }
            }
        }
    }

    @FXML
    void handleClickedOnAdressBookCatButton() {
        categoryListView.getSelectionModel().select(1);
        handleCategoryClick();
    }

    @FXML
    void handleClickedOnMailCatButton() {
        categoryListView.getSelectionModel().select(0);
        handleCategoryClick();
    }

    @FXML
    public void handleClickedOnCategoryItem() {
        System.out.println("OnClicked: handleClickedOnCategoryItem");
        String catTitle = "";
        if (isSelectedCategoryMail()) {
            catTitle = categoryItemListView.getSelectionModel().getSelectedItem().toString();
            currentSelectedMailBoxItem = catTitle;
            categoryNameLabel1.setGraphic(ViewFactory.defaultFactory.resolveMailBoxListItemIcon(catTitle));

            initMailBoxTable(currentSelectedMailBoxItem);

            ObservableList<Node> nodes = mainSplitePane.getItems();
            boolean isExist = false;
            for (Node eachNode: nodes) {
                if (eachNode.getId().equalsIgnoreCase("rightAnchorPane")) {
                    isExist = true;
                    break;
                }
            }

            if (!isExist)
                mainSplitePane.getItems().add(rightAnchorPane);

            if (mDivPosOfInit != null)
                mainSplitePane.setDividerPositions(mDivPosOfInit);

            addressBookCatButton.setSelected(false);
            mailCatButton.setSelected(true);
        } else {
            catTitle = "Address Book";
            categoryNameLabel1.setGraphic(ViewFactory.defaultFactory.resolveIconWithName("/com/jinyuan/view/images/contacts.png"));

            initAddressBookTable("");

            mainSplitePane.getItems().remove(rightAnchorPane);

            addressBookCatButton.setSelected(true);
            mailCatButton.setSelected(false);
        }
        categoryNameLabel1.setText(catTitle);
    }

    @FXML
    public void handleCategoryClick() {

        String catTitle = categoryListView.getSelectionModel().getSelectedItem().toString();
        System.out.println("OnClicked: Category list index = " + catTitle);

        categoryNameLabel.setText(catTitle);

        categoryItemListView.getItems().clear();
        if (isSelectedCategoryMail()) {
            categoryItemListView.getItems().addAll(mAryMailItems);

            if (currentSelectedMailBoxItem.isEmpty())
                categoryItemListView.getSelectionModel().select("Inbox");
            else
                categoryItemListView.getSelectionModel().select(currentSelectedMailBoxItem);
        } else {
            categoryItemListView.getItems().addAll(mAryAddressBookItems);
        }
        handleClickedOnCategoryItem();
    }

    public boolean isSelectedCategoryMail() {
        return categoryListView.getSelectionModel().getSelectedItem().toString().equalsIgnoreCase("Mail");
    }

    void initCategoryButtons() {
        ImageView img = (ImageView) ViewFactory.defaultFactory.resolveCategoryIcon("Mail");
        mailCatButton.setGraphic(img);
        mailCatButton.setText("");

        img = (ImageView) ViewFactory.defaultFactory.resolveCategoryIcon("AddressBook");
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

        replyButton.setGraphic(ViewFactory.defaultFactory.resolveIconWithName("/com/jinyuan/view/images/mail_reply.png"));

        replyToAllButton.setGraphic(ViewFactory.defaultFactory.resolveIconWithName("/com/jinyuan/view/images/reply_to_all.png"));

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
            if (isSelectedCategoryMail())
                mDivPosOfMainSplitPane = mainSplitePane.getDividerPositions();
            mainSplitePane.getItems().remove(leftAnchorPane);
            categoryListHBox.getChildren().add(0, expandAnchorPane);
        } else {
            mainSplitePane.getItems().add(0, leftAnchorPane);
            categoryListHBox.getChildren().remove(expandAnchorPane);
            if (isSelectedCategoryMail())
                mainSplitePane.setDividerPositions(mDivPosOfMainSplitPane);
        }
    }
}
