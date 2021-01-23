package hr.algebra.controllers;

import hr.algebra.dal.Repository;
import hr.algebra.dal.RepositoryFactory;
import hr.algebra.model.Contact;
import hr.algebra.model.UserProvider;
import hr.algebra.utils.DialogUtils;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    @FXML
    public Button btnLogin;
    @FXML
    public Button btnRegister;
    @FXML
    public AnchorPane mainPane;
    @FXML
    private TextField tfUsername;
    @FXML
    private PasswordField pfPassword;

    private Repository repository;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        repository = RepositoryFactory.getRepository();
        mainPane.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
    }

    public void btnLoginClicked(ActionEvent actionEvent) {
        if (!formIsValid())
            DialogUtils.showErrorDialog("Error", "Login failed", "One or more fields are empty");
        else {
            try {
                Optional<Contact> user = repository.authenticateContact(tfUsername.getText(), pfPassword.getText());
                if (!user.isPresent()) {
                    DialogUtils.showErrorDialog("Error", "User does not exist.");
                } else {
                    UserProvider userHolder = UserProvider.getInstance();
                    userHolder.setUser(user.get());
                    FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getClassLoader().getResource("view/main.fxml")));
                    Parent root = loader.load();
                    Controller controller = loader.getController();
                    Stage stage = setupStage(root);
                    closeLoginWindow(actionEvent, controller, stage);
                }
            } catch (SQLException | IOException ex) {
                DialogUtils.showErrorDialog("Error", "Exception", ex.getMessage());
                ex.printStackTrace();
            }
        }
    }

    private Stage setupStage(Parent root) {
        Stage stage = new Stage();
        Scene scene = new Scene(root, 1200, 675);
        stage.initStyle(StageStyle.TRANSPARENT);
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.sizeToScene();
        return stage;
    }

    private void closeLoginWindow(ActionEvent actionEvent, Controller controller, Stage stage) {
        stage.setOnHidden(e -> {
            controller.stopApplication();
            Platform.exit();
        });
        stage.show();
        ((Node) (actionEvent.getSource())).getScene().getWindow().hide();
    }

    public void btnRegisterClicked() {
        if (!formIsValid())
            DialogUtils.showErrorDialog("Error", "Login failed", "One or more fields are empty");
        else {
            try {
                Optional<Contact> user = repository.authenticateContact(tfUsername.getText(), pfPassword.getText());
                if (!user.isPresent()) {
                    repository.createContact(new Contact(tfUsername.getText(), pfPassword.getText(), "Enter Enter", null));
                    btnLogin.fire();
                } else
                    DialogUtils.showWarningDialog("Warning", "User exists", "Cannot create user because the user already exists...");
            } catch (SQLException ex) {
                DialogUtils.showErrorDialog("Error", "SqlException", ex.getMessage());
                ex.printStackTrace();
            }
        }
    }

    private boolean formIsValid() {
        return !tfUsername.getText().trim().isEmpty() && !pfPassword.getText().trim().isEmpty();
    }

    public void loginUser() {
        btnLogin.fire();
    }

    public void btnExitPressed() {
        System.exit(0);
    }

    public void btnMinimizePressed(ActionEvent actionEvent) {
        ((Stage) ((Button) actionEvent.getSource()).getScene().getWindow()).setIconified(true);
    }
}
