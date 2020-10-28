package hr.algebra.controllers;

import hr.algebra.dal.UserHolderSingleton;
import hr.algebra.utils.FxmlLoader;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        UserHolderSingleton userHolder = UserHolderSingleton.getInstance();
        //AppUser user = userHolder.getUser();
        tfTextContent.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        initializeBottomMenu();
        initializeContactTitle();
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
        Label message = new Label();
        message.setText(tfTextContent.getText());
        message.getStyleClass().add("myMessage");
        message.setWrapText(true);
        vbChat.getChildren().add(message);
        tfTextContent.clear();
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
}

/* TODO:
 *   - contact onClick load chat
 *   - set chat for selected contact
 */