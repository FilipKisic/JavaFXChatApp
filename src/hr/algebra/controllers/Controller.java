package hr.algebra.controllers;

import hr.algebra.dal.UserHolderSingleton;
import hr.algebra.utils.FxmlLoader;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.awt.event.ActionEvent;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    private static final String FILE_NAME = "userMessages.dat";
    @FXML
    public Button btnEmoticon;
    @FXML
    public Button btnSend;
    @FXML
    public BorderPane bpMenu;
    @FXML
    public BorderPane bpContact;
    @FXML
    private ToggleButton tglbtnContacts;
    @FXML
    private ToggleButton tglbtnSettings;
    @FXML
    private HBox hboxBottom;
    @FXML
    private TextField tfTextContent;
    @FXML
    private VBox vbChat;
    private List<String> userMessages;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        UserHolderSingleton userHolder = UserHolderSingleton.getInstance();
        //AppUser user = userHolder.getUser();
        tfTextContent.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        initializeBottomMenu();
        initializeContactTitle();
        userMessages = new ArrayList<>();
        loadData();
    }

    private void initializeBottomMenu() {
        ToggleGroup optionSelection = new ToggleGroup();
        tglbtnContacts.setToggleGroup(optionSelection);
        tglbtnSettings.setToggleGroup(optionSelection);
        hboxBottom.setSpacing(100.0);
        tglbtnSettings.fire();
    }

    public void initializeContactTitle() {
        Pane pane = new FxmlLoader().getScene("contactTitle");
        bpContact.setCenter(pane);
    }

    public void btnSendClicked() {
        userMessages.add(tfTextContent.getText());
        createMessage(tfTextContent.getText());
        tfTextContent.clear();
    }

    private void createMessage(String messageText) {
        Label message = new Label();
        message.setText(messageText);
        message.getStyleClass().add("myMessage");
        message.setWrapText(true);
        vbChat.getChildren().add(message);
    }

    public void tglbtnContactsIsToggled() {
        if (tglbtnContacts.isSelected()) {
            Pane pane = new FxmlLoader().getScene("contactList");
            bpMenu.setCenter(pane);
        }
    }


    public void tglbtnSettingsIsToggled() {
        if (tglbtnSettings.isSelected()) {
            Pane pane = new FxmlLoader().getScene("settings");
            bpMenu.setCenter(pane);
        }
    }

    public void sendMessage() {
        btnSend.fire();
    }

    @FXML
    public void exitApplication(ActionEvent event){
        Platform.exit();
    }

    public void stopApplication() {
        saveData();
        Platform.exit();
    }

    private void saveData() {
        try(FileOutputStream serializationStream = new FileOutputStream(FILE_NAME);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(serializationStream)){
            objectOutputStream.writeObject(userMessages);
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void loadData(){
        try(FileInputStream serializationStream = new FileInputStream(FILE_NAME);
            ObjectInputStream objectInputStream = new ObjectInputStream(serializationStream)){
            userMessages.clear();
            userMessages = (List<String>) objectInputStream.readObject();
            for (String message : userMessages){
                createMessage(message);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}

/* TODO:
 *   - contact onClick load chat
 *   - set chat for selected contact
 */