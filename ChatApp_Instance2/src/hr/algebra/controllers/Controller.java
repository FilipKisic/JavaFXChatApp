package hr.algebra.controllers;

import hr.algebra.dal.Repository;
import hr.algebra.dal.RepositoryFactory;
import hr.algebra.model.ContactProvider;
import hr.algebra.model.ControllerProvider;
import hr.algebra.model.Message;
import hr.algebra.model.UserProvider;
import hr.algebra.networking.ClientThread;
import hr.algebra.rmi.ChatClient;
import hr.algebra.utils.FileUtils;
import hr.algebra.utils.FxmlLoader;
import hr.algebra.utils.MessageUtils;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    private static final String FILE_NAME = "data/userMessages.dat";
    @FXML
    public Button btnSend;
    @FXML
    public BorderPane bpMenu;
    @FXML
    public ImageView ivContactImage;
    @FXML
    public Label lbContactName;
    @FXML
    public Button btnUploadImage;
    @FXML
    public ScrollPane scrlpChatScrollPane;
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
    private List<Message> userMessages;
    private UserProvider user;
    private ContactProvider contact;
    private Repository repository;
    private ClientThread thread;
    private ChatClient chatClient;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tfTextContent.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        initializeBottomMenu();
        user = UserProvider.getInstance();
        repository = RepositoryFactory.getRepository();
        contact = ContactProvider.getInstance();
        userMessages = new ArrayList<>();
        chatClient = new ChatClient(this);
        initializeClientThread();
        ControllerProvider controllerProvider = ControllerProvider.getInstance();
        controllerProvider.setController(this);
        //loadData();
    }

    private void initializeClientThread() {
        thread = new ClientThread(this);
        thread.setDaemon(true);
        thread.start();
    }

    private void initializeBottomMenu() {
        ToggleGroup optionSelection = new ToggleGroup();
        tglbtnContacts.setToggleGroup(optionSelection);
        tglbtnSettings.setToggleGroup(optionSelection);
        hboxBottom.setSpacing(100.0);
        tglbtnSettings.fire();
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

    public void setCurrentContact() {
        ivContactImage.setImage(contact.getContact().getProfileImage());
        lbContactName.setText(contact.getContact().getFullName());
    }

    public void btnSendClicked() {
        contact = ContactProvider.getInstance();
        Message message = new Message(tfTextContent.getText().getBytes(), user.getUser().getIdContact(), contact.getContact().getIdContact(), false);
        processMessage(message);
    }

    private void processMessage(Message message) {
        thread.sendMessage(message);
        try {
            repository.createMessage(message);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        userMessages.add(message);
        tfTextContent.clear();
    }

    public void sendMessage() {
        btnSend.fire();
    }

    public void showMessage(Message message){
        MessageUtils.createMessage(message, vbChat, user);
        scrlpChatScrollPane.applyCss();
        scrlpChatScrollPane.layout();
        scrlpChatScrollPane.setVvalue(1.0);
    }

    public void loadMessages() {
        vbChat.getChildren().clear();
        List<Message> messages = repository.selectMessages(user.getUser().getIdContact(), contact.getContact().getIdContact());
        messages.forEach(this::showMessage);
    }

    private void saveData() {
        try (FileOutputStream serializationStream = new FileOutputStream(FILE_NAME);
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(serializationStream)) {
            objectOutputStream.writeObject(userMessages);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void stopApplication() {
        saveData();
        Platform.exit();
    }

    public void btnUploadImageClicked() {
        File file = FileUtils.uploadFileDialog(btnUploadImage.getScene().getWindow(), "jpg", "png");
        if (file != null) {
            try {
                byte[] messageContent = Files.readAllBytes(file.toPath());
                Message message = new Message(messageContent, user.getUser().getIdContact(), contact.getContact().getIdContact(), true);
                chatClient.send(message);
                Platform.runLater(() -> scrlpChatScrollPane.setVvalue(1.0));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void displayMessage(Message message){
        Platform.runLater(() -> Controller.this.showMessage(message));
    }

    /* SERIALIZATION READ */
    /*private void loadData(){
        File file = new File(FILE_NAME);
        if(file.exists() && !file.isDirectory()) {
            try (FileInputStream serializationStream = new FileInputStream(FILE_NAME);
                 ObjectInputStream objectInputStream = new ObjectInputStream(serializationStream)) {
                userMessages.clear();
                userMessages = (List<String>) objectInputStream.readObject();
                for (String message : userMessages) {
                    createMessage(message);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }*/
}
