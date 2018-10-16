package com.jinyuan.controller;

import com.jinyuan.controller.services.EmailSenderService;
import com.jinyuan.model.EmailConstants;
import com.jinyuan.model.GlobalVariables.GlobalVariables;
import com.jinyuan.model.MailSecurity;
import com.jinyuan.model.http.Apis;
import com.jinyuan.view.ViewFactory;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.web.HTMLEditor;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class DraftMailController extends AbstractController implements Initializable {

    @FXML
    Label errorLabel;

    @FXML
    TextField toTextField;

    @FXML
    TextField ccTextField;

    @FXML
    TextField bccTextField;

    @FXML
    private HTMLEditor ComposeArea;

    @FXML
    private TextField SubjectField;

    @FXML
    FlowPane attachFlowPane;

    @FXML
    RadioButton mailSecurityClassRadioButton1;

    @FXML
    RadioButton mailSecurityClassRadioButton2;

    @FXML
    RadioButton mailSecurityClassRadioButton3;

    @FXML
    RadioButton mailSecurityClassRadioButton4;

    @FXML
    RadioButton mailSecurityClassRadioButton5;

    @FXML
    RadioButton mailSecurityClassRadioButton6;

    @FXML
    RadioButton mailSecurityClassRadioButton7;

    ArrayList<String> mAttachFileSecurityClass;

    private List<File> attachments = new ArrayList<File>();

    final ToggleGroup group = new ToggleGroup();

    public DraftMailController(ModelAccess modelAccess) {
        super(modelAccess);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        mailSecurityClassRadioButton1.setToggleGroup(group);
        mailSecurityClassRadioButton2.setToggleGroup(group);
        mailSecurityClassRadioButton3.setToggleGroup(group);
        mailSecurityClassRadioButton4.setToggleGroup(group);
        mailSecurityClassRadioButton5.setToggleGroup(group);
        mailSecurityClassRadioButton6.setToggleGroup(group);

        mAttachFileSecurityClass = new ArrayList<String>();
        for (int i = 0; i < GlobalVariables.mailSecurityList.size(); i++) {
            MailSecurity security = GlobalVariables.mailSecurityList.get(i);
            mAttachFileSecurityClass.add(security.levelName);
            switch (i) {
                case 0:
                    mailSecurityClassRadioButton1.setVisible(true);
                    mailSecurityClassRadioButton1.setText(security.levelName);
                    break;
                case 1:
                    mailSecurityClassRadioButton2.setVisible(true);
                    mailSecurityClassRadioButton2.setText(security.levelName);
                    break;
                case 2:
                    mailSecurityClassRadioButton3.setVisible(true);
                    mailSecurityClassRadioButton3.setText(security.levelName);
                    break;
                case 3:
                    mailSecurityClassRadioButton4.setVisible(true);
                    mailSecurityClassRadioButton4.setText(security.levelName);
                    break;
                case 4:
                    mailSecurityClassRadioButton5.setVisible(true);
                    mailSecurityClassRadioButton5.setText(security.levelName);
                    break;
                case 5:
                    mailSecurityClassRadioButton6.setVisible(true);
                    mailSecurityClassRadioButton6.setText(security.levelName);
                    break;
                case 6:
                    mailSecurityClassRadioButton7.setVisible(true);
                    mailSecurityClassRadioButton7.setText(security.levelName);
                    break;
            }
        }

        if (GlobalVariables.replyType.equals("Reply")) {
            Message message = GlobalVariables.messageList.get(GlobalVariables.replyIndex);
            try {
                String from = message.getFrom()[0].toString();
                toTextField.setText(from);
                String subject = message.getSubject().toString();
                SubjectField.setText("Re : " + subject);

                String content = "";
                Multipart multipart = (Multipart) message.getContent();
                for (int i = 0; i < multipart.getCount(); i++) {
                    BodyPart bodyPart = multipart.getBodyPart(i);
                    if (bodyPart.getContentType().contains("text/html")) {
                        content = bodyPart.getContent().toString();
                    }
                }

                Document doc = Jsoup.parse(content);
                String body = doc.body().text();
                String newContent = "<html dir=\"ltr\"><head></head><body contenteditable=\"true\"> <br> <br> <br> <div style ='background-color:#EEE; margin: 3px; padding:20px;'>" + body + "</div></body></html>";
                ComposeArea.setHtmlText(newContent);
            }catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (GlobalVariables.replyType.equals("Reply To All")) {
            Message message = GlobalVariables.messageList.get(GlobalVariables.replyIndex);
            try {
                String from = message.getFrom()[0].toString();
                toTextField.setText(from);
                Address[] addresslist = message.getRecipients(Message.RecipientType.CC);
                if (addresslist != null) {
                    String cc = "";
                    for (int i = 0; i < addresslist.length; i++) {
                        if (i == 0) {
                            cc = addresslist[i].toString();
                        } else {
                            cc += ", " + addresslist[i].toString();
                        }
                    }
                    ccTextField.setText(cc);
                }

                addresslist = message.getRecipients(Message.RecipientType.BCC);
                if (addresslist != null) {
                    String bcc = "";
                    for (int i = 0; i < addresslist.length; i++) {
                        if (i == 0) {
                            bcc = addresslist[i].toString();
                        } else {
                            bcc += ", " + addresslist[i].toString();
                        }
                    }
                    bccTextField.setText(bcc);
                }
                String subject = message.getSubject().toString();
                SubjectField.setText("Re : " + subject);

                String content = "";
                Multipart multipart = (Multipart) message.getContent();
                for (int i = 0; i < multipart.getCount(); i++) {
                    BodyPart bodyPart = multipart.getBodyPart(i);
                    if (bodyPart.getContentType().contains("text/html")) {
                        content = bodyPart.getContent().toString();
                    }
                }

                Document doc = Jsoup.parse(content);
                String body = doc.body().text();
                String newContent = "<html dir=\"ltr\"><head></head><body contenteditable=\"true\"> <br> <br> <br> <div style ='background-color:#EEE; margin: 3px; padding:20px;'>" + body + "</div></body></html>";
                ComposeArea.setHtmlText(newContent);
            }catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (GlobalVariables.replyType.equals("Forward")) {
            Message message = GlobalVariables.messageList.get(GlobalVariables.replyIndex);
            try {
                String subject = message.getSubject().toString();
                SubjectField.setText("FWD : " + subject);

                String content = "";
                Multipart multipart = (Multipart) message.getContent();
                for (int i = 0; i < multipart.getCount(); i++) {
                    BodyPart bodyPart = multipart.getBodyPart(i);
                    if (bodyPart.getContentType().contains("text/html")) {
                        content = bodyPart.getContent().toString();
                    }
                }

                Document doc = Jsoup.parse(content);
                String body = doc.body().text();
                String newContent = "<html dir=\"ltr\"><head></head><body contenteditable=\"true\"> <br> <br> <br> <div style ='background-color:#EEE; margin: 3px; padding:20px;'>" + body + "</div></body></html>";
                ComposeArea.setHtmlText(newContent);
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void fetchRecipient(Scene scene) {
        FXMLLoader loader = (FXMLLoader) scene.getUserData();
        ChooseUserController chooseUserController = loader.getController();
        setEmailToField(toTextField, chooseUserController.getToValues());
        setEmailToField(ccTextField, chooseUserController.getCcValues());
        setEmailToField(bccTextField, chooseUserController.getBccValues());
    }

    public void setRecipient(Scene scene) {
        FXMLLoader loader = (FXMLLoader) scene.getUserData();
        ChooseUserController chooseUserController = loader.getController();
        chooseUserController.setToValues(toTextField.getText());
        chooseUserController.setCcValues(ccTextField.getText());
        chooseUserController.setBccValues(bccTextField.getText());
    }

    @FXML
    public void actionOnToButton() {
        Scene scene = ViewFactory.defaultFactory.getChooseUserScene();
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.setTitle("Address Book");
        setRecipient(scene);
        stage.showAndWait();
        fetchRecipient(scene);
    }

    @FXML
    public void actionOnCcButton() {
        Scene scene = ViewFactory.defaultFactory.getChooseUserScene();
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.setTitle("Address Book");
        setRecipient(scene);
        stage.showAndWait();
        fetchRecipient(scene);
    }

    @FXML
    public void actionOnBccButton() {
        Scene scene = ViewFactory.defaultFactory.getChooseUserScene();
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.setTitle("Address Book");
        setRecipient(scene);
        stage.showAndWait();
        fetchRecipient(scene);
    }


    @FXML
    public void actionOnAttachButton() {
        System.out.println("Clicked on actionOnAttachButton");

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        File selected = fileChooser.showOpenDialog(mailSecurityClassRadioButton1.getScene().getWindow());

        if (selected != null) {
            System.out.println("selected file = " + selected.getAbsolutePath());

            attachFlowPane.setHgap(5);
            attachFlowPane.setVgap(5);
            attachFlowPane.setPadding(new Insets(5, 0, 5,0));
            attachFlowPane.getChildren().add(getAttachFile(selected));
        }
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

    public Node getAttachFile(File aFile) {
        SplitMenuButton attachButton = new SplitMenuButton();

        String fileName = aFile.getName();
        attachButton.setGraphic(getFileIcon(aFile));

        attachButton.getStyleClass().add("attach-menu-button");

        attachButton.setText(fileName);

        MenuItem actionItemView = new MenuItem();
        MenuItem actionItemOpenFolder = new MenuItem();
        MenuItem actionItemRemove = new MenuItem();

        actionItemRemove.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                System.out.println("Delete action on the [" + aFile.getName() + "] file");

                remoteAttachFile(aFile);
            }
        });

        actionItemView.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                System.out.println("Delete action on the [" + aFile.getName() + "] file");
                String path = aFile.getAbsolutePath();
                try {
                    Desktop.getDesktop().open(aFile);
                } catch ( Exception e) {
                    e.printStackTrace();
                }
            }
        });

        actionItemOpenFolder.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                System.out.println("Delete action on the [" + aFile.getName() + "] file");
                String path = aFile.getAbsolutePath();
                File folder = new File(path.substring(0, path.lastIndexOf(File.separator)));
                try {
                    Desktop.getDesktop().open(folder);
                } catch ( Exception e) {
                    e.printStackTrace();
                }
            }
        });

        actionItemView.setText(get("Open"));
        actionItemOpenFolder.setText(get("Open_Folder"));
        actionItemRemove.setText(get("Remove"));

        attachButton.getItems().add(actionItemView);
        attachButton.getItems().add(actionItemOpenFolder);
        attachButton.getItems().add(actionItemRemove);

        HBox box= new HBox();
        box.setSpacing(5) ;
        box.setAlignment(Pos.CENTER_LEFT);

        ComboBox attachClassLabel = new ComboBox();
        attachClassLabel.getItems().addAll(mAttachFileSecurityClass);
        attachClassLabel.getSelectionModel().select(0);
        attachClassLabel.getStyleClass().add("mail_attach_security_class_1");
        attachClassLabel.getStyleClass().add("attach-menu-button");

        System.out.println("combox class = " + attachClassLabel.getStyleClass().toString());

        attachClassLabel.valueProperty().addListener((obs, oldItem, newItem) -> {
            attachClassLabel.getEditor().getStyleClass().add("mail_attach_security_class_" + attachClassLabel.getSelectionModel().getSelectedIndex());
        });

        box.setUserData(aFile);
        box.getChildren().addAll(attachButton, attachClassLabel);

        return box;
    }

    @FXML
    public void actionOnSendButton() {

//        if (checkSend() == false) {
//
//            return;
//        }

        errorLabel.setText("sending message ...");

        String mailSecurityLevel = "";
        if (mailSecurityClassRadioButton1.isSelected()) {
            mailSecurityLevel = "<meta name='mailSecurityLevel' content='" + mAttachFileSecurityClass.get(0) + "'>";
        } else if (mailSecurityClassRadioButton2.isSelected()) {
            mailSecurityLevel = "<meta name='mailSecurityLevel' content='" + mAttachFileSecurityClass.get(1) + "'>";
        } else if (mailSecurityClassRadioButton3.isSelected()) {
            mailSecurityLevel = "<meta name='mailSecurityLevel' content='" + mAttachFileSecurityClass.get(2) + "'>";
        } else if (mailSecurityClassRadioButton4.isSelected()) {
            mailSecurityLevel = "<meta name='mailSecurityLevel' content='" + mAttachFileSecurityClass.get(3) + "'>";
        } else if (mailSecurityClassRadioButton5.isSelected()) {
            mailSecurityLevel = "<meta name='mailSecurityLevel' content='" + mAttachFileSecurityClass.get(4) + "'>";
        } else if (mailSecurityClassRadioButton6.isSelected()) {
            mailSecurityLevel = "<meta name='mailSecurityLevel' content='" + mAttachFileSecurityClass.get(5) + "'>";
        } else if (mailSecurityClassRadioButton7.isSelected()) {
            mailSecurityLevel = "<meta name='mailSecurityLevel' content='" + mAttachFileSecurityClass.get(6) + "'>";
        }

        String attachSecurityLevels = "";
        for (int i = 0; i < attachFlowPane.getChildren().size(); i ++) {
            HBox item = (HBox)attachFlowPane.getChildren().get(i);
            File attach  = (File) item.getUserData();
            attachments.add(attach);
            ComboBox attachClassLabel = (ComboBox) item.getChildren().get(1);
            int securityIndex = attachClassLabel.getSelectionModel().getSelectedIndex();
            attachSecurityLevels += "<meta name='attach_" + i + "' content='" + mAttachFileSecurityClass.get(securityIndex) + "'>";
        }

        String content = ComposeArea.getHtmlText();

        String head = "<head><meta charset='UTF-8'>" + mailSecurityLevel + attachSecurityLevels + "</head>";
        content = content.replace("<head></head>", head);
        EmailSenderService emailSenderService =
                new EmailSenderService(getModelAccess().getEmailAccountByName(GlobalVariables.account.getAddress()),
                        SubjectField.getText(),
                        toTextField.getText(),
                        ccTextField.getText(),
                        bccTextField.getText(),
                        content,
                        attachments);
        emailSenderService.restart();
        emailSenderService.setOnSucceeded(e-> {
            if(emailSenderService.getValue() != EmailConstants.MESSAGE_SENT_OK){
                errorLabel.setText("Error while sending message");
            } else {
                errorLabel.setText("Message have been sent successfully!");
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        try {
                            Thread.sleep(2000);
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    Stage stage = (Stage)toTextField.getScene().getWindow();
                                    stage.close();
                                }
                            });
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
    }

    public boolean checkSend() {
        boolean canSend = false;
        try {

            String to = toTextField.getText();
            to = to.replace(",", ";");
            String security = "";
            if (mailSecurityClassRadioButton1.isSelected()) {
                security = mAttachFileSecurityClass.get(0);
            } else if (mailSecurityClassRadioButton2.isSelected()) {
                security = mAttachFileSecurityClass.get(1);
            } else if (mailSecurityClassRadioButton3.isSelected()) {
                security = mAttachFileSecurityClass.get(2);
            } else if (mailSecurityClassRadioButton4.isSelected()) {
                security = mAttachFileSecurityClass.get(3);
            } else if (mailSecurityClassRadioButton5.isSelected()) {
                security = mAttachFileSecurityClass.get(4);
            } else if (mailSecurityClassRadioButton6.isSelected()) {
                security = mAttachFileSecurityClass.get(5);
            } else if (mailSecurityClassRadioButton7.isSelected()) {
                security = mAttachFileSecurityClass.get(6);
            }

            String url = String.format(Apis.CHECK_SEND, GlobalVariables.account.getAddress(), to, security);
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            int responseCode = con.getResponseCode();

            InputStreamReader inputStreamReader = new InputStreamReader(con.getInputStream(), Charset.forName("GB18030"));
            BufferedReader in = new BufferedReader( inputStreamReader);

            String inputLine;
            StringBuffer response = new StringBuffer();
            while((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
        } catch (Exception e) {

        }
        return canSend;
    }
}