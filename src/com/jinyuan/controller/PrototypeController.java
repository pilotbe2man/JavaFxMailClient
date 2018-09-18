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
import javafx.scene.layout.HBox;
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
    Button mailCatButton;

    @FXML
    Button adressBookCatButton;

    //divider postions of the mailSplitPane;
    double[] mDivPosOfMainSplitPane;
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

        //hide expand pan and button
        categoryListHBox.getChildren().remove(expandAnchorPane);

        //init list for test
        initTestValues();
        //<-- test end

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
    }

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

        mAryAddressBookItems.add("Test1@outlook.com");
        mAryAddressBookItems.add("Test2@outlook.com");
        mAryAddressBookItems.add("Test3@outlook.com");
        mAryAddressBookItems.add("Test4@outlook.com");
        mAryAddressBookItems.add("Test5@outlook.com");
        mAryAddressBookItems.add("Test6@outlook.com");

        categoryItemListView.getItems().addAll(mAryMailItems);

        final ObservableList<MailItem> data = FXCollections.observableArrayList(
                new MailItem("Important", "jacob.smith@example.com", "Receiving the packages", "2018-09-18 09:30:34", "34KB"),
                new MailItem("Normal",  "isabella.johnson@example.com", "Lost things", "2018-09-18 10:30:34", "23KB"),
                new MailItem("Emergency",  "ethan.williams@example.com", "New gallery", "2018-09-18 11:30:34", "20KB"),
                new MailItem("Standard",  "emma.jones@example.com", "New market launched", "2018-09-18 12:30:34", "30KB"),
                new MailItem("SoSo", "michael.brown@example.com", "Indeed job growing", "2018-09-18 12:30:34", "40KB")
        );

        TableColumn categoryCol = (TableColumn) mailItemTableView.getColumns().get(0);
        TableColumn fromCol = (TableColumn) mailItemTableView.getColumns().get(1);
        TableColumn subjectCol = (TableColumn) mailItemTableView.getColumns().get(2);
        TableColumn receivedDateCol = (TableColumn) mailItemTableView.getColumns().get(3);
        TableColumn sizeCol = (TableColumn) mailItemTableView.getColumns().get(4);

        categoryCol.setCellValueFactory(new PropertyValueFactory("category"));
        fromCol.setCellValueFactory(new PropertyValueFactory("from"));
        subjectCol.setCellValueFactory(new PropertyValueFactory("subject"));
        receivedDateCol.setCellValueFactory(new PropertyValueFactory("receivedDate"));
        sizeCol.setCellValueFactory(new PropertyValueFactory("size"));

        mailItemTableView.setItems(data);

        WebEngine engine = mailContentWebView.getEngine();
        String url = "http://java2s.com/";
        engine.load(url);

        categoryItemListView.getSelectionModel().select("Inbox");
    }

    public static class MailItem {
        private StringProperty category;
        private StringProperty from;
        private StringProperty subject;
        private StringProperty receivedDate;
        private StringProperty size;

        private MailItem(String category, String from, String subject, String receivedDate, String size) {
            this.category = new SimpleStringProperty(category);
            this.from = new SimpleStringProperty(from);
            this.subject = new SimpleStringProperty(subject);
            this.receivedDate = new SimpleStringProperty(receivedDate);
            this.size = new SimpleStringProperty(size);
        }

        public StringProperty categoryProperty() { return category; }
        public StringProperty fromProperty() { return from; }
        public StringProperty subjectProperty() { return subject; }
        public StringProperty receivedDateProperty() { return receivedDate; }
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
                    setGraphic(ViewFactory.defaultFactory.resolveMailBoxIcon(item));
                } else {
                    setGraphic(ViewFactory.defaultFactory.resolveIconWithName("images/user.png"));
                }
            }
        }
    }

    @FXML
    void handleClickedOnAdressBookItem() {
        categoryListView.getSelectionModel().select(1);
        handleCategoryClick();
    }

    @FXML
    void handleClickedOnMailItem() {
        categoryListView.getSelectionModel().select(0);
        handleCategoryClick();
    }

    @FXML
    public void handleCategoryClick() {

        String catTitle = categoryListView.getSelectionModel().getSelectedItem().toString();
        System.out.println("OnClicked: Category list index = " + catTitle);

        categoryNameLabel.setText(catTitle);

        categoryItemListView.getItems().clear();
        if (isSelectedCategoryMail())
            categoryItemListView.getItems().addAll(mAryMailItems);
        else
            categoryItemListView.getItems().addAll(mAryAddressBookItems);
    }

    public boolean isSelectedCategoryMail() {
        return categoryListView.getSelectionModel().getSelectedItem().toString().equalsIgnoreCase("Mail");
    }

    void initCategoryButtons() {
        ImageView img = (ImageView) ViewFactory.defaultFactory.resolveCategoryIcon("Mail");
        mailCatButton.setGraphic(img);
        mailCatButton.setText("");

        img = (ImageView) ViewFactory.defaultFactory.resolveCategoryIcon("AddressBook");
        adressBookCatButton.setGraphic(img);
        adressBookCatButton.setText("");

        System.out.println("adb bound = " + leftPanCollapseButton.getBoundsInLocal());
    }

    /**
     * @autbor Pilot
     * Initialization for the toolbar button icons and titles
     */
    void initToolbarButtons() {

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

        addressBookButton.setGraphic(ViewFactory.defaultFactory.resolveIconWithName("/com/jinyuan/view/images/address_book.png"));
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
    public void handleClickedOnCollapseButton() {
        System.out.println("OnClicked : Collapse index = " + mainSplitePane.getItems());

        Node child = mainSplitePane.lookup("#leftAnchorPane");

        if (child != null) {
            mDivPosOfMainSplitPane = mainSplitePane.getDividerPositions();
            mainSplitePane.getItems().remove(leftAnchorPane);
            categoryListHBox.getChildren().add(0, expandAnchorPane);
        } else {
            mainSplitePane.getItems().add(0, leftAnchorPane);
            categoryListHBox.getChildren().remove(expandAnchorPane);
            mainSplitePane.setDividerPositions(mDivPosOfMainSplitPane);
        }
    }
}
