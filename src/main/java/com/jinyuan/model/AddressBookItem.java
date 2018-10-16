package com.jinyuan.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class AddressBookItem {
    private StringProperty userID;
    private StringProperty userDepartment;
    private StringProperty userPath;
    private StringProperty userName;
    private StringProperty mailAddress;
    private StringProperty userLevel;
    private StringProperty userSecurityLevel;

    public AddressBookItem( String userID, String userDepartment, String userPath, String userName, String mailAddress, String userLevel, String userSecurityLevel) {

        this.userID = new SimpleStringProperty(userID);
        this.userDepartment = new SimpleStringProperty(userDepartment);
        this.userPath = new SimpleStringProperty(userPath);
        this.userName = new SimpleStringProperty(userName);
        this.mailAddress = new SimpleStringProperty(mailAddress);
        this.userLevel = new SimpleStringProperty(userLevel);
        this.userSecurityLevel = new SimpleStringProperty(userSecurityLevel);
    }

    public StringProperty userIDProperty() { return userID; }
    public StringProperty userDepartmentProperty() { return userDepartment; }
    public StringProperty userPathProperty() { return userPath; }
    public StringProperty userNameProperty() { return userName; }
    public StringProperty mailAddressProperty() { return mailAddress; }
    public StringProperty userLevelProperty() { return userLevel; }
    public StringProperty userSecurityLevelProperty() { return userSecurityLevel; }

    @Override
    public String toString() {
        return "User ID : <" +
                userID.getValue() + ">, User Department : <" +
                userDepartment.getValue() + ">, User Path : " +
                userPath.getValue() + ", User Name : " +
                userName.getValue() + ", Mail Address : " +
                mailAddress.getValue() + ", User Level : " +
                userLevel.getValue() + ", User Security Level : " +
                userSecurityLevel.getValue();
    }
}