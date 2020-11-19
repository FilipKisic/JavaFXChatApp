package hr.algebra.controllers;

import hr.algebra.networking.ServerThread;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    public Button btnStart;
    private ServerThread serverThread;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    private void initServerThread() {
        serverThread = new ServerThread();
        serverThread.setDaemon(true);
        serverThread.start();
    }

    public void startServer() {
        initServerThread();
    }
}
