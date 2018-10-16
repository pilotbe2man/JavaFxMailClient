package com.jinyuan.model.GlobalVariables;

import com.jinyuan.controller.PrototypeController;
import com.jinyuan.controller.persistence.ValidAccount;
import com.jinyuan.model.AddressBookItem;
import com.jinyuan.model.MailSecurity;
import javafx.collections.ObservableList;

import javax.mail.Message;
import java.util.ArrayList;

public class GlobalVariables {
    public static ArrayList<AddressBookItem> addresslist;
    public static ValidAccount account;
    public static ObservableList<PrototypeController.MailItem> mailData;
    public static ArrayList<MailSecurity> mailSecurityList;
    public static ArrayList<Message> messageList;
    public static int messageIndex;
    public static String replyType;
    public static int replyIndex;
}
