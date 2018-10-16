package com.jinyuan.controller;

import com.jinyuan.model.AddressBookItem;
import com.jinyuan.model.GlobalVariables.GlobalVariables;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Predicate;

public class ChooseUserController extends AbstractController implements Initializable {

    final ObservableList<AddressBookItem> addressBookData = FXCollections.observableArrayList();
    
    @FXML
    TableView userTableView;

    @FXML
    TextField searchNameTextField;

    @FXML
    TextField toTextField;

    @FXML
    TextField ccTextField;

    @FXML
    TextField bccTextField;

    @FXML
    RadioButton nameOnlyRadioButton;

    @FXML
    RadioButton moreColumnsRadioButton;

    final ToggleGroup group = new ToggleGroup();

    public ChooseUserController(ModelAccess modelAccess) {
        super(modelAccess);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        initAddressBookTable();

        nameOnlyRadioButton.setToggleGroup(group);
        moreColumnsRadioButton.setToggleGroup(group);

        nameOnlyRadioButton.setUserData("Name Only");
        moreColumnsRadioButton.setUserData("More");

        group.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
            public void changed(ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) {
                if (group.getSelectedToggle() != null) {
                    System.out.println(group.getSelectedToggle().getUserData().toString());
                    String key = searchNameTextField.getText();
                    searchNameTextField.setText("");
                    searchNameTextField.setText(key);
                }
            }
        });

        FilteredList<AddressBookItem> filteredData = new FilteredList<>(addressBookData, p -> true);
        searchNameTextField.textProperty().addListener((observable, oldValue, newValue) -> {

            System.out.println("textfield changed from " + oldValue + " to " + newValue);

            filteredData.setPredicate(addressBookItem -> {

                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Compare first name and last name of every person with filter text.
                String lowerCaseFilter = newValue.toLowerCase();

                String condition = group.getSelectedToggle().getUserData().toString();
                if (condition.equalsIgnoreCase("Name Only")) {
                    if (addressBookItem.userIDProperty().toString().toLowerCase().contains(lowerCaseFilter))
                        return true; // Filter matches first name.
                } else {
                    if (addressBookItem.userDepartmentProperty().toString().toLowerCase().contains(lowerCaseFilter)) {
                        return true; // Filter matches last name.
                    }
                    if (addressBookItem.userPathProperty().toString().toLowerCase().contains(lowerCaseFilter)) {
                        return true; // Filter matches last name.
                    }
                    if (addressBookItem.userNameProperty().toString().toLowerCase().contains(lowerCaseFilter)) {
                        return true; // Filter matches last name.
                    }
                    if (addressBookItem.mailAddressProperty().toString().toLowerCase().contains(lowerCaseFilter)) {
                        return true; // Filter matches last name.
                    }
                    if (addressBookItem.userLevelProperty().toString().toLowerCase().contains(lowerCaseFilter)) {
                        return true; // Filter matches last name.
                    }
                    if (addressBookItem.userSecurityLevelProperty().toString().toLowerCase().contains(lowerCaseFilter)) {
                        return true; // Filter matches last name.
                    }
                }
                return false;

            });
        });

        // 3. Wrap the FilteredList in a SortedList.
        SortedList<AddressBookItem> sortedData = new SortedList<>(filteredData);

        // 4. Bind the SortedList comparator to the TableView comparator.
        sortedData.comparatorProperty().bind(userTableView.comparatorProperty());

        // 5. Add sorted (and filtered) data to the table.
        userTableView.setItems(sortedData);
    }

    public void initAddressBookTable() {

        TableColumn userIDCol = (TableColumn) userTableView.getColumns().get(0);
        TableColumn userDepartmentCol = (TableColumn) userTableView.getColumns().get(1);
        TableColumn userPathCol = (TableColumn) userTableView.getColumns().get(2);
        TableColumn userNameCol = (TableColumn) userTableView.getColumns().get(3);
        TableColumn mailAddressCol = (TableColumn) userTableView.getColumns().get(4);
        TableColumn userLevelCol = (TableColumn) userTableView.getColumns().get(5);
        TableColumn userSecurityLevelCol = (TableColumn) userTableView.getColumns().get(6);

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

        userIDCol.setCellValueFactory(new PropertyValueFactory("userID"));
        userDepartmentCol.setCellValueFactory(new PropertyValueFactory("userDepartment"));
        userPathCol.setCellValueFactory(new PropertyValueFactory("userPath"));
        userNameCol.setCellValueFactory(new PropertyValueFactory("userName"));
        mailAddressCol.setCellValueFactory(new PropertyValueFactory("mailAddress"));
        userLevelCol.setCellValueFactory(new PropertyValueFactory("userLevel"));
        userSecurityLevelCol.setCellValueFactory(new PropertyValueFactory("userSecurityLevel"));

        addressBookData.addAll(GlobalVariables.addresslist);



    }
    
    @FXML
    public void actionOnToButton() {
        AddressBookItem person = (AddressBookItem) userTableView.getSelectionModel().getSelectedItem();
        String val = person.mailAddressProperty().getValue();
        System.out.println(val);

        setEmailToField(toTextField, val);
    }

    @FXML
    public void actionOnCcButton() {
        AddressBookItem person = (AddressBookItem) userTableView.getSelectionModel().getSelectedItem();
        String val = person.mailAddressProperty().getValue();
        System.out.println(val);

        setEmailToField(ccTextField, val);
    }

    @FXML
    public void actionOnBccButton() {
        AddressBookItem person = (AddressBookItem) userTableView.getSelectionModel().getSelectedItem();
        String val = person.mailAddressProperty().getValue();
        System.out.println(val);

        setEmailToField(bccTextField, val);
    }

    @FXML
    public void actionOnOkButton() {
        Stage stage = (Stage) userTableView.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void actionOnCancelButton() {
        toTextField.setText("");
        ccTextField.setText("");
        bccTextField.setText("");
        Stage stage = (Stage) userTableView.getScene().getWindow();
        stage.close();
    }

    public void setToValues(String val) {
        setEmailToOverwriteField(toTextField, val);
    }

    public void setCcValues(String val) {
        setEmailToOverwriteField(ccTextField, val);
    }

    public void setBccValues(String val) {
        setEmailToOverwriteField(bccTextField, val);
    }

    public String getToValues() {
        return toTextField.getText();
    }

    public String getCcValues() {
        return ccTextField.getText();
    }

    public String getBccValues() {
        return bccTextField.getText();
    }
}
