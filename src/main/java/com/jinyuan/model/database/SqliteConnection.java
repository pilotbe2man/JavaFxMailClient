package com.jinyuan.model.database;

import com.jinyuan.controller.PrototypeController;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class SqliteConnection {

    public static Connection Connector() {
        try {
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection("jdbc:sqlite:mail.db");
            return conn;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    public void insertMail(PrototypeController.MailItem aItem) {

    }
}
