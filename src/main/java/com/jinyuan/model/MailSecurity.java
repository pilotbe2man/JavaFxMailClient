package com.jinyuan.model;

public class MailSecurity {

    public int level;
    public String levelName;
    public String levelColor;

    public MailSecurity(int level, String name, String color) {
        this.level = level;
        this.levelName = name;
        this.levelColor = color;
    }
}