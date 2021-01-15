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
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

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
    private TextField tfUsername;
    @FXML
    private PasswordField pfPassword;

    private Repository repository;

    public void btnLoginClicked(ActionEvent actionEvent) {
        if(!formIsValid())
            DialogUtils.showErrorDialog("Error", "Login failed", "One or more fields are empty");
        else {
            try {
                Optional<Contact> user = repository.authenticateContact(tfUsername.getText(), pfPassword.getText());
                if(!user.isPresent()){
                    DialogUtils.showErrorDialog("Error", "User does not exist.");
                }
                else{
                    UserProvider userHolder = UserProvider.getInstance();
                    userHolder.setUser(user.get());
                    FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getClassLoader().getResource("view/main.fxml")));
                    Parent root = loader.load();
                    Controller controller = loader.getController();
                    Stage stage = new Stage();
                    stage.setScene(new Scene(root, 1200, 675));
                    stage.setTitle("Chat App");
                    stage.setResizable(false);
                    stage.sizeToScene();
                    stage.setOnHidden(e -> {
                        controller.stopApplication();
                        Platform.exit();
                    });
                    stage.show();
                    ((Node)(actionEvent.getSource())).getScene().getWindow().hide();
                }
            } catch (SQLException | IOException ex) {
                DialogUtils.showErrorDialog("Error", "Exception", ex.getMessage());
                ex.printStackTrace();
            }
        }
    }

    public void btnRegisterClicked() {
        if (!formIsValid())
            DialogUtils.showErrorDialog("Error", "Login failed", "One or more fields are empty");
        else {
            try {
                Optional<Contact> user = repository.authenticateContact(tfUsername.getText(), pfPassword.getText());
                if(!user.isPresent()) {
                    repository.createContact(new Contact(tfUsername.getText(), pfPassword.getText(), "Enter Enter", null));
                    btnLogin.fire();
                }
                else
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        repository = RepositoryFactory.getRepository();
    }

    public void loginUser() {
        btnLogin.fire();
    }

}
