package com.jinyuan.controller;

import com.jinyuan.model.GlobalVariables.GlobalVariables;
import com.jinyuan.view.ViewFactory;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import javax.mail.*;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import java.awt.*;
import java.io.File;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ResourceBundle;

public class EmailDetailController extends AbstractController implements Initializable {

    @FXML
    VBox rootVBox;

    @FXML
    Button replyInContentButton;

    @FXML
    Button replyAllInContentButton;

    @FXML
    Button forwardInContentButton;

    @FXML
    WebView mailContentWebView;

    @FXML
    Label fromLabel;

    @FXML
    Label subjectLabel;

    @FXML
    Label dateLabel;

    @FXML
    FlowPane toFollowPane;

    @FXML
    FlowPane ccFlowPane;

    @FXML
    FlowPane bccFlowPane;

    @FXML
    Label ccLabel;

    @FXML
    Label mailSecurityClassLabel;

    @FXML
    FlowPane attachFlowPane;

    @FXML
    Label statusLabel;

    @FXML
    Label favLabel;

    boolean isFav;

    PrototypeController.MailItem mMailItem;

    Message message;

    public EmailDetailController(ModelAccess modelAccess, PrototypeController.MailItem aItem) {
        super(modelAccess);
        mMailItem = aItem;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        message = GlobalVariables.messageList.get(GlobalVariables.messageIndex);

        replyInContentButton.setGraphic(ViewFactory.defaultFactory.resolveIconWithName("/com/jinyuan/view/images/mail_reply.png"));
        replyAllInContentButton.setGraphic(ViewFactory.defaultFactory.resolveIconWithName("/com/jinyuan/view/images/reply_to_all.png"));
        forwardInContentButton.setGraphic(ViewFactory.defaultFactory.resolveIconWithName("/com/jinyuan/view/images/forward.png"));

        fromLabel.setText("From :  " + mMailItem.fromProperty().getValue() + "");
        subjectLabel.setText(mMailItem.subjectProperty().getValue());
        dateLabel.setText(mMailItem.receivedDateProperty().getValue());

        try {
            Address[] addresslist = message.getRecipients(Message.RecipientType.TO);
            for (int i = 0; i < addresslist.length; i++) {
                Label to1 = new Label(addresslist[i].toString());
                to1.setFont(new Font(15));
                if (i  > 0) {
                    to1.setText(",  " + addresslist[i].toString());
                }
                toFollowPane.getChildren().add(to1);
            }

            addresslist = message.getRecipients(Message.RecipientType.CC);
            if (addresslist != null) {
                Label header = new Label("Cc  :  ");
                header.setFont( new Font(15));
                ccFlowPane.getChildren().add(header);
                for (int i = 0; i < addresslist.length; i++) {
                    Label to1 = new Label(addresslist[i].toString());
                    to1.setFont( new Font(15));
                    if (i  > 0) {
                        to1.setText(",  " + addresslist[i].toString());
                    }
                    ccFlowPane.getChildren().add(to1);
                }
            }

            addresslist = message.getRecipients(Message.RecipientType.BCC);
            if (addresslist != null) {
                Label header = new Label("Bcc :  ");
                bccFlowPane.getChildren().add(header);
                for (int i = 0; i < addresslist.length; i++) {
                    Label to1 = new Label(addresslist[i].toString());
                    to1.setFont( new Font(15));
                    if (i  > 0) {
                        to1.setText(",  " + addresslist[i].toString());
                    }
                    bccFlowPane.getChildren().add(to1);
                }
            }
            String content = "";
            Multipart multipart = (Multipart) message.getContent();
            for (int i = 0; i < multipart.getCount(); i++) {
                BodyPart bodyPart = multipart.getBodyPart(i);
                System.out.println(bodyPart.getContentType());
                if (bodyPart.getContentType().contains("text/html")) {
                    content = bodyPart.getContent().toString();
                }
            }

            Document doc = Jsoup.parse(content);
            if (content.contains("mailSecurityLevel")) {
                String security = doc.select("meta[name=mailSecurityLevel]").get(0).attr("content");
                mailSecurityClassLabel.setText("[" + security + "]");
            } else {
                mailSecurityClassLabel.setText("[" + get("Class_1") + "]");
            }

            WebEngine engine = mailContentWebView.getEngine();
            engine.loadContent(content);

            int attachIndex = -1;

            for (int i = 0; i < multipart.getCount(); i++) {
                BodyPart bodyPart = multipart.getBodyPart(i);
                System.out.println(bodyPart.getContentType());
                if (bodyPart.getContentType().contains("text/html")) {
                    content = bodyPart.getContent().toString();
                } else if(bodyPart.getFileName() != null && bodyPart.getFileName().length() > 0){
                    attachIndex ++;

                    String fileName = bodyPart.getFileName();
                    System.out.println(fileName);

                    SplitMenuButton attachButton1 = new SplitMenuButton();
                    ImageView imgV = getFileIcon(new File(fileName));
                    attachButton1.setGraphic(imgV);

                    attachButton1.getStyleClass().add("attach-menu-button1");
                    attachButton1.setText(fileName);

                    MenuItem actionItemView = new MenuItem();
                    MenuItem actionItemSaveAs = new MenuItem();

                    actionItemView.setText(get("Open"));
                    actionItemSaveAs.setText(get("Save_as"));

                    actionItemView.setOnAction(new EventHandler<ActionEvent>() {
                        public void handle(ActionEvent t) {
                            try {

                                String fileExtension = fileName.substring(fileName.lastIndexOf("."));
                                File tempFile = File.createTempFile("icon", fileExtension);
                                MimeBodyPart part  = (MimeBodyPart)bodyPart;
                                part.saveFile(tempFile);
                                Desktop.getDesktop().open(tempFile);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });

                    actionItemSaveAs.setOnAction(new EventHandler<ActionEvent>() {
                        public void handle(ActionEvent t) {
                            try {
                                File dest = new DirectoryChooser().showDialog(mailSecurityClassLabel.getScene().getWindow());
                                if (dest != null) {
                                    statusLabel.setText("downloading the file ...");
                                    String rootPath = dest.getAbsolutePath();
                                    MimeBodyPart part = (MimeBodyPart) bodyPart;
                                    part.saveFile(rootPath + "/" + fileName);
                                    statusLabel.setText("downloaded the file successfully!");
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });

                    attachButton1.getItems().add(actionItemView);
                    attachButton1.getItems().add(actionItemSaveAs);

                    HBox box= new HBox();
                    box.setSpacing(5) ;
                    box.setAlignment(Pos.CENTER_LEFT);
                    box.getStyleClass().add("attach-menu-button");
                    Label attachClassLabel = new Label();

                    String security = doc.select("meta[name=attach_" + attachIndex + "]").get(0).attr("content");

                    attachClassLabel.setPadding(new Insets(0,5,0,0));
                    attachClassLabel.setText("[" + security + "]");
                    attachClassLabel.getStyleClass().add("mail_attach_security_class_1");
                    box.getChildren().addAll(attachButton1, attachClassLabel);

                    box.setUserData(imgV.getUserData());
                    attachFlowPane.setHgap(5);
                    attachFlowPane.setVgap(5);
                    attachFlowPane.setPadding(new Insets(5, 0, 5,0));
                    attachFlowPane.getChildren().add(box);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        favLabel.setText("");
        isFav = mMailItem.followProperty().getValue();
        setFav();
    }

    @FXML
    public void handeClickedOnReplyMenuButton() {

        Stage stage = (Stage)favLabel.getScene().getWindow();
        stage.close();

        GlobalVariables.replyType = "Reply";
        GlobalVariables.replyIndex = GlobalVariables.messageIndex;
        Scene scene = ViewFactory.defaultFactory.getDraftMailScene();
        stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Reply Mail");
        stage.show();
    }

    @FXML
    public void handeClickedOnReplyAllMenuButton() {

        Stage stage = (Stage)favLabel.getScene().getWindow();
        stage.close();

        GlobalVariables.replyType = "Reply To All";
        GlobalVariables.replyIndex = GlobalVariables.messageIndex;
        Scene scene = ViewFactory.defaultFactory.getDraftMailScene();
        stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Reply Mail");
        stage.show();
    }

    @FXML
    public void handeClickedOnForwardMenuButton() {

        Stage stage = (Stage)favLabel.getScene().getWindow();
        stage.close();

        GlobalVariables.replyType = "Forward";
        GlobalVariables.replyIndex = GlobalVariables.messageIndex;
        Scene scene = ViewFactory.defaultFactory.getDraftMailScene();
        stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Forward Mail");
        stage.show();
    }

    /**
     * @author pilot
     * remove the attach file
     * @param aFile
     */
    public void remoteAttachFile(File aFile) {
        int iSize = attachFlowPane.getChildren().size();
        for (int i = 0; i < iSize; i++) {
            Node eachNode = attachFlowPane.getChildren().get(i);
            if (((File)eachNode.getUserData()).getAbsolutePath().equalsIgnoreCase(aFile.getAbsolutePath())) {
                attachFlowPane.getChildren().remove(i);
                break;
            }
        }
    }

    @FXML
    public void handleOnFavoriteButton() {
        isFav = !isFav;
        setFav();
    }

    public void setFav() {
        if (isFav) {
            favLabel.setGraphic(ViewFactory.defaultFactory.resolveIconWithName("images/fav_yes.png"));
        } else {
            favLabel.setGraphic(ViewFactory.defaultFactory.resolveIconWithName("images/fav_no.png"));
        }
    }
}
