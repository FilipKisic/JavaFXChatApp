package hr.algebra.controllers;

import hr.algebra.dal.Repository;
import hr.algebra.dal.RepositoryFactory;
import hr.algebra.model.ContactProvider;
import hr.algebra.model.ControllerProvider;
import hr.algebra.model.Message;
import hr.algebra.model.UserProvider;
import hr.algebra.networking.ClientThread;
import hr.algebra.rmi.ChatClient;
import hr.algebra.utils.DOMUtils;
import hr.algebra.utils.FileUtils;
import hr.algebra.utils.FxmlLoader;
import hr.algebra.utils.MessageUtils;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.sql.SQLException;
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
    public Button btnExport;
    @FXML
    public Button btnImport;
    @FXML
    public GridPane mainPane;
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
    private List<Message> allMessages;
    private UserProvider user;
    private ContactProvider contact;
    private Repository repository;
    private ClientThread thread;
    private ChatClient chatClient;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tfTextContent.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        initializeBottomMenu();
        initializeObjects();
        initializeClientThread();
        ControllerProvider controllerProvider = ControllerProvider.getInstance();
        controllerProvider.setController(this);
        //loadData();
    }

    private void initializeObjects() {
        mainPane.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
        user = UserProvider.getInstance();
        repository = RepositoryFactory.getRepository();
        contact = ContactProvider.getInstance();
        chatClient = new ChatClient(this);
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
        scrlpChatScrollPane.setVvalue(1.0);
        tfTextContent.clear();
    }

    public void sendMessage() {
        btnSend.fire();
    }

    public void showMessage(Message message) {
        MessageUtils.createMessage(message, vbChat, user);
        scrlpChatScrollPane.applyCss();
        scrlpChatScrollPane.layout();
        scrlpChatScrollPane.setVvalue(1.0);
    }

    public void loadMessages() {
        vbChat.getChildren().clear();
        allMessages = repository.selectMessages(user.getUser().getIdContact(), contact.getContact().getIdContact());
        allMessages.forEach(this::showMessage);
    }

    public void stopApplication() {
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

    public void displayMessage(Message message) {
        Platform.runLater(() -> showMessage(message));
    }

    public void btnExportPressed() {
        DOMUtils.saveChat(FXCollections.observableArrayList(allMessages));
        vbChat.getChildren().clear();
    }

    public void btnImportPressed() {
        allMessages = DOMUtils.loadMessages();
        vbChat.getChildren().clear();
        allMessages.forEach(this::showMessage);
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