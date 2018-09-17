package com.jinyuan.controller;

import com.jinyuan.view.ViewFactory;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
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

    @FXML
    AnchorPane leftAnchorPane;

    @FXML
    AnchorPane centerAnchorPane;

    @FXML
    AnchorPane rightAnchorPane;

    @FXML
    Label categoryNameLabel;

    @FXML
    Button leftPanCollapseButton;

    @FXML
    ListView categoryListView;

    @FXML
    Label itemsLabel;

    @FXML
    ListView categoryItemListView;

    @FXML
    ListView mailItemListView;

    @FXML
    WebView mailContentWebView;

    public PrototypeController(ModelAccess modelAccess) {
        super(modelAccess);
    }

    double mWidthOfLeftAnchorPane;
    List<String> mAryCategory = new ArrayList<>();
    List<String> mAryMailItems = new ArrayList<>();
    List<String> mAryAddressBookItems = new ArrayList<>();
    List<String> mAryMailListItems = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        initToolbarButtons();

        //init values
        mWidthOfLeftAnchorPane = leftAnchorPane.getMinWidth();

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

        mAryMailListItems.add("From Test1@outlook.com");
        mAryMailListItems.add("From Test2@outlook.com");
        mAryMailListItems.add("From Test3@outlook.com");
        mAryMailListItems.add("From Test4@outlook.com");
        mAryMailListItems.add("From Test5@outlook.com");
        mAryMailListItems.add("From Test6@outlook.com");
        mAryMailListItems.add("From Test7@outlook.com");
        mAryMailListItems.add("From Test8@outlook.com");

        mailItemListView.getItems().addAll(mAryMailListItems);

        WebEngine engine = mailContentWebView.getEngine();
        String url = "http://java2s.com/";
        engine.load(url);

        categoryItemListView.getSelectionModel().select("Inbox");
        mailItemListView.getSelectionModel().select(5);
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
                setGraphic(ViewFactory.defaultFactory.resoveCategoryIcon(item));
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
                    setGraphic(ViewFactory.defaultFactory.resoveMailBoxIcon(item));
                } else {
                    setGraphic(ViewFactory.defaultFactory.resoveIconWithName("images/user.png"));
                }
            }
        }
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

    /**
     * @autbor Pilot
     * Initialization for the toolbar button icons and titles
     */
    public void initToolbarButtons() {

        newMailMenuButton.setGraphic(ViewFactory.defaultFactory.resoveIconWithName("/com/jinyuan/view/images/mail_new.png"));

        printButton.setGraphic(ViewFactory.defaultFactory.resoveIconWithName("/com/jinyuan/view/images/print.png"));
        printButton.setText("");

        moveToFolderButton.setGraphic(ViewFactory.defaultFactory.resoveIconWithName("/com/jinyuan/view/images/move_to_folder.png"));
        moveToFolderButton.setText("");

        deleteButton.setGraphic(ViewFactory.defaultFactory.resoveIconWithName("/com/jinyuan/view/images/delete.png"));
        deleteButton.setText("");

        replyButton.setGraphic(ViewFactory.defaultFactory.resoveIconWithName("/com/jinyuan/view/images/mail_reply.png"));

        replyToAllButton.setGraphic(ViewFactory.defaultFactory.resoveIconWithName("/com/jinyuan/view/images/reply_to_all.png"));

        forwardButton.setGraphic(ViewFactory.defaultFactory.resoveIconWithName("/com/jinyuan/view/images/forward.png"));

        categoriseButton.setGraphic(ViewFactory.defaultFactory.resoveIconWithName("/com/jinyuan/view/images/categorize.png"));
        categoriseButton.setText("");

        followUpButton.setGraphic(ViewFactory.defaultFactory.resoveIconWithName("/com/jinyuan/view/images/follow_up.png"));
        followUpButton.setText("");

        sendReceiveButton.setGraphic(ViewFactory.defaultFactory.resoveIconWithName("/com/jinyuan/view/images/sendReceive.png"));

        oneNoteButton.setGraphic(ViewFactory.defaultFactory.resoveIconWithName("/com/jinyuan/view/images/one_note.png"));
        oneNoteButton.setText("");

        addressBookButton.setGraphic(ViewFactory.defaultFactory.resoveIconWithName("/com/jinyuan/view/images/address_book.png"));
        addressBookButton.setText("");

        addressBookComboBox.setEditable(true);
        addressBookComboBox.getEditor().textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable,
                                String oldValue, String newValue) {
                System.out.println("Text changed");
            }
        });

        helpButton.setGraphic(ViewFactory.defaultFactory.resoveIconWithName("/com/jinyuan/view/images/help.png"));
        helpButton.setText("");
    }

    @FXML
    public void handleClickedOnCollapseButton() {
        System.out.println("OnClicked : Collapse");
    }
}
