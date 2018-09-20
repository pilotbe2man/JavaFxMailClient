package com.jinyuan.controller;

import com.jinyuan.model.AddressBookItem;
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

    final ObservableList<AddressBookItem> addressBookData = FXCollections.observableArrayList(
            new AddressBookItem(
                    "Jhone Smith",
                    "JhoneSmith@gmail.com",
                    "361 Degrees",
                    "China Baowu Steel Group",
                    "123123123123",
                    "123123123123",
                    "123123123123",
                    "123123123123",
                    "Beijing Hualian Group",
                    "Bolisi"),
            new AddressBookItem(
                    "Zhong Jingyi",
                    "ZhongJingyi@gmail.com",
                    "Anta Sports",
                    "Bosideng",
                    "123123123123",
                    "123123123123",
                    "123123123123",
                    "123123123123",
                    "Brilliance Auto",
                    "BYD Auto"),
            new AddressBookItem(
                    "Wen Cai",
                    "WenCai@gmail.com",
                    "Baidu",
                    "Changan Automobile",
                    "123123123123",
                    "123123123123",
                    "123123123123",
                    "123123123123",
                    "Changhe",
                    "Changhong"),
            new AddressBookItem(
                    "Kong Ru",
                    "KongRu@gmail.com",
                    "Bank of China",
                    "Changhong Technology",
                    "123123123123",
                    "123123123123",
                    "123123123123",
                    "123123123123",
                    "Chery Automobile",
                    "China Clean Energy"),
            new AddressBookItem(
                    "Wei Jing",
                    "WeiJing@gmail.com",
                    "China Dongxiang",
                    "China Eastern Airlines",
                    "123123123123",
                    "123123123123",
                    "123123123123",
                    "123123123123",
                    "China Housing and Land Development",
                    "China International Marine Containers"),
            new AddressBookItem(
                    "Cao Ming",
                    "CaoMing@gmail.com",
                    "China Life Insurance Company",
                    "China Medical Technologies",
                    "123123123123",
                    "123123123123",
                    "123123123123",
                    "123123123123",
                    "China Merchants Bank",
                    "China Merchants Energy Shipping"),
            new AddressBookItem(
                    "Qian Zhou",
                    "QianZhou@gmail.com",
                    "China Metal Recycling",
                    "China Mobile",
                    "123123123123",
                    "123123123123",
                    "123123123123",
                    "123123123123",
                    "China National Erzhong Group",
                    "China National Offshore Oil Corporation"),
            new AddressBookItem(
                    "Duan Huang",
                    "DuanHuang@gmail.com",
                    "China National Petroleum Corporation",
                    "China Natural Gas",
                    "123123123123",
                    "123123123123",
                    "123123123123",
                    "123123123123",
                    "China Nepstar",
                    "China Netcom"),
            new AddressBookItem(
                    "Zhu Wen",
                    "ZhuWen@gmail.com",
                    "China Pabst Blue Ribbon",
                    "China Post",
                    "123123123123",
                    "123123123123",
                    "123123123123",
                    "123123123123",
                    "China Shipping Group",
                    "China Southern Airlines"),
            new AddressBookItem(
                    "Liao Heng",
                    "LiaoHeng@gmail.com",
                    "Bank of Communications",
                    "China Communications Construction",
                    "123123123123",
                    "123123123123",
                    "123123123123",
                    "123123123123",
                    "China Construction Bank",
                    "China COSCO Shipping")
    );
    
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
                    if (addressBookItem.fullNameProperty().toString().toLowerCase().contains(lowerCaseFilter))
                        return true; // Filter matches first name.
                } else {
                    if (addressBookItem.fullNameProperty().toString().toLowerCase().contains(lowerCaseFilter)) {
                        return true; // Filter matches last name.
                    }
                    if (addressBookItem.companyProperty().toString().toLowerCase().contains(lowerCaseFilter)) {
                        return true; // Filter matches last name.
                    }
                    if (addressBookItem.fileAsProperty().toString().toLowerCase().contains(lowerCaseFilter)) {
                        return true; // Filter matches last name.
                    }
                    if (addressBookItem.businessPhoneProperty().toString().toLowerCase().contains(lowerCaseFilter)) {
                        return true; // Filter matches last name.
                    }
                    if (addressBookItem.businessFaxProperty().toString().toLowerCase().contains(lowerCaseFilter)) {
                        return true; // Filter matches last name.
                    }
                    if (addressBookItem.homePhoneProperty().toString().toLowerCase().contains(lowerCaseFilter)) {
                        return true; // Filter matches last name.
                    }
                    if (addressBookItem.mobilePhoneProperty().toString().toLowerCase().contains(lowerCaseFilter)) {
                        return true; // Filter matches last name.
                    }
                    if (addressBookItem.journalProperty().toString().toLowerCase().contains(lowerCaseFilter)) {
                        return true; // Filter matches last name.
                    }
                    if (addressBookItem.categoriesProperty().toString().toLowerCase().contains(lowerCaseFilter)) {
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

        TableColumn fullNameCol = (TableColumn) userTableView.getColumns().get(0);
        TableColumn companyCol = (TableColumn) userTableView.getColumns().get(1);
        TableColumn fileAsCol = (TableColumn) userTableView.getColumns().get(2);
        TableColumn businessPhoneCol = (TableColumn) userTableView.getColumns().get(3);
        TableColumn businessFaxCol = (TableColumn) userTableView.getColumns().get(4);
        TableColumn homePhoneCol = (TableColumn) userTableView.getColumns().get(5);
        TableColumn mobilePhoneCol = (TableColumn) userTableView.getColumns().get(6);
        TableColumn journalCol = (TableColumn) userTableView.getColumns().get(7);
        TableColumn categoriesCol = (TableColumn) userTableView.getColumns().get(8);

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
        homePhoneCol.setText("Home Phone");
        mobilePhoneCol.setText("Mobile Phone");
        journalCol.setText("Journal");
        categoriesCol.setText("Categories");

        fullNameCol.setCellValueFactory(new PropertyValueFactory("fullName"));
        companyCol.setCellValueFactory(new PropertyValueFactory("company"));
        fileAsCol.setCellValueFactory(new PropertyValueFactory("fileAs"));
        businessPhoneCol.setCellValueFactory(new PropertyValueFactory("businessPhone"));
        businessFaxCol.setCellValueFactory(new PropertyValueFactory("businessFax"));
        homePhoneCol.setCellValueFactory(new PropertyValueFactory("homePhone"));
        mobilePhoneCol.setCellValueFactory(new PropertyValueFactory("mobilePhone"));
        journalCol.setCellValueFactory(new PropertyValueFactory("journal"));
        categoriesCol.setCellValueFactory(new PropertyValueFactory("categories"));

//        userTableView.setItems(addressBookData);
    }
    
    @FXML
    public void actionOnToButton() {
        AddressBookItem person = (AddressBookItem) userTableView.getSelectionModel().getSelectedItem();
        String val = person.mailProperty().getValue();
        System.out.println(val);

        setEmailToField(toTextField, val);
    }

    @FXML
    public void actionOnCcButton() {
        AddressBookItem person = (AddressBookItem) userTableView.getSelectionModel().getSelectedItem();
        String val = person.mailProperty().getValue();
        System.out.println(val);

        setEmailToField(ccTextField, val);
    }

    @FXML
    public void actionOnBccButton() {
        AddressBookItem person = (AddressBookItem) userTableView.getSelectionModel().getSelectedItem();
        String val = person.mailProperty().getValue();
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
