package com.jinyuan.controller;

import com.jinyuan.controller.persistence.ValidAddressBook;
import com.jinyuan.view.ViewFactory;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
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

    public PrototypeController(ModelAccess modelAccess) {
        super(modelAccess);
    }

    double mWidthOfLeftAnchorPane;
    List<String> mAryCategory = new ArrayList<>();
    List<String> mAryMailItems = new ArrayList<>();
    List<String> mAryAddressBookItems = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        initToolbarButtons();

        //init values
        mWidthOfLeftAnchorPane = leftAnchorPane.getMinWidth();

        //init list for test
        mAryCategory.add("Mail");
        mAryCategory.add("AddressBook");
        categoryListView.getItems().addAll(mAryCategory);
        categoryListView.getSelectionModel().select(0);

        mAryMailItems.add("RSS");
        mAryMailItems.add("Drafts");
        mAryMailItems.add("Outbox");
        mAryMailItems.add("Inbox");
        mAryMailItems.add("Sent Items");
        mAryMailItems.add("Delete Items");
        mAryMailItems.add("Search Folders");

        mAryAddressBookItems.add("Test1@outlook.com");
        mAryAddressBookItems.add("Test2@outlook.com");
        mAryAddressBookItems.add("Test3@outlook.com");
        mAryAddressBookItems.add("Test4@outlook.com");
        mAryAddressBookItems.add("Test5@outlook.com");
        mAryAddressBookItems.add("Test6@outlook.com");

        categoryItemListView.getItems().addAll(mAryMailItems);

        categoryListView.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {

            @Override
            public ListCell<String> call(ListView<String> param) {
                // TODO Auto-generated method stub
                return new MailListCell();
            }
        });
    }

    //for test
    private static class MailListCell extends ListCell<String> {
        @Override
        public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(ViewFactory.defaultFactory.resoveMailBoxIcon(item));
                setText(item);
            }
        }
    }

    @FXML
    public void handleCategoryClick() {

        categoryNameLabel.setText(categoryListView.getSelectionModel().getSelectedItem().toString());
        System.out.println("OnClicked: Category list index = " + categoryListView.getSelectionModel().getSelectedItem());

        categoryItemListView.getItems().clear();
        if (categoryListView.getSelectionModel().getSelectedItem().toString().equalsIgnoreCase("Mail"))
            categoryItemListView.getItems().addAll(mAryMailItems);
        else
            categoryItemListView.getItems().addAll(mAryAddressBookItems);
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
