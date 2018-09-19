package com.jinyuan.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class AddressBookItem {
    private StringProperty fullName;
    private StringProperty company;
    private StringProperty fileAs;
    private StringProperty businessPhone;
    private StringProperty businessFax;
    private StringProperty homePhone;
    private StringProperty mobilePhone;
    private StringProperty journal;
    private StringProperty categories;
    private StringProperty mail;

    public AddressBookItem(
            String fullName,
            String mail,
            String company,
            String fileAs,
            String businessPhone,
            String businessFax,
            String homePhone,
            String mobilePhone,
            String journal,
            String categories) {

        this.fullName = new SimpleStringProperty(fullName);
        this.mail = new SimpleStringProperty(mail);
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
    public StringProperty mailProperty() { return mail; }
    public StringProperty companyProperty() { return company; }
    public StringProperty fileAsProperty() { return fileAs; }
    public StringProperty businessPhoneProperty() { return businessPhone; }
    public StringProperty businessFaxProperty() { return businessFax; }
    public StringProperty homePhoneProperty() { return homePhone; }
    public StringProperty mobilePhoneProperty() { return mobilePhone; }
    public StringProperty journalProperty() { return journal; }
    public StringProperty categoriesProperty() { return categories; }
}
