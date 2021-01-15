package hr.algebra.controllers;

import hr.algebra.dal.Repository;
import hr.algebra.dal.RepositoryFactory;
import hr.algebra.networking.ServerThread;
import hr.algebra.rmi.ChatServer;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    public Button btnStart;
    private ServerThread serverThread;
    public ChatServer chatServer;
    private Repository repository;

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
        repository = RepositoryFactory.getRepository();
        chatServer = new ChatServer(repository);
    }
}
