package com.jinyuan.model.http;

public class Apis {

    public static String HTTP_PROTOCOL = "http://";
    public static String HTTP_HOST = "101.231.201.56";
    public static String HTTP_PORT = ":6080";
    public static String HTTP_CLIENT_APP = "/kmclient";

    public static String GET_MAILBOX_INFO = HTTP_PROTOCOL + HTTP_HOST + HTTP_PORT + HTTP_CLIENT_APP + "?action=describeMailboxInfo&domainAccount=%s";

    public static String GET_USER_INFO = HTTP_PROTOCOL + HTTP_HOST + HTTP_PORT + HTTP_CLIENT_APP + "?action=describeUserInfo&domainAccount=%s";

    public static String GET_SYS_SECURITY_LEVEL = HTTP_PROTOCOL + HTTP_HOST + HTTP_PORT + HTTP_CLIENT_APP + "?action=describeSysSecurityLevel";

    public static String GET_MAIL_SECURITY_LEVEL = HTTP_PROTOCOL + HTTP_HOST + HTTP_PORT + HTTP_CLIENT_APP + "?action=describeSecurityLevels";

    public static String GET_USER_LEVEL = HTTP_PROTOCOL + HTTP_HOST + HTTP_PORT + HTTP_CLIENT_APP + "?action=describeUserLevels";

    public static String GET_ADDRESSBOOK = HTTP_PROTOCOL + HTTP_HOST + HTTP_PORT + HTTP_CLIENT_APP + "?action=describeAddressList";

    public static String CHECK_SEND = HTTP_PROTOCOL + HTTP_HOST + HTTP_PORT + HTTP_CLIENT_APP + "?action=checkSend&from=%s&to=%s&mailSecurityLevel=%s";

    public static String APPROVE_PENDING_LIST = HTTP_PROTOCOL + HTTP_HOST + HTTP_PORT + HTTP_CLIENT_APP + "?menumark=1&frame=1&domainAccount=%s";
}
