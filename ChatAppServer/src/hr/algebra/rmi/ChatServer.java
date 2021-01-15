package hr.algebra.rmi;

import hr.algebra.dal.Repository;
import hr.algebra.model.Message;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class ChatServer {

    private static final int REMOTE_PORT = 1099;
    private static final String RMI_SERVER = "server";
    private static String RMI_CLIENT = "client";

    private ChatProtocol chatProtocol;
    private List<Integer> clientIDs;
    private HashSet<ChatProtocol> clients;
    private final Repository repository;
    private Registry registry;

    public ChatServer(Repository repository) {
        this.repository = repository;
        clientIDs = new ArrayList<>();
        clients = new HashSet<>();
        publishServer();
        waitForClient();
    }

    private void publishServer() {
        chatProtocol = new ChatProtocol() {
            @Override
            public void registerClient(int clientID) throws RemoteException {
                clientIDs.add(clientID);
            }

            @Override
            public void sendMessage(Message message) throws RemoteException {
                try {
                    repository.createMessage(message);
                    send(message);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        };
        try {
            registry = LocateRegistry.createRegistry(REMOTE_PORT);
            ChatProtocol stub = (ChatProtocol) UnicastRemoteObject.exportObject(chatProtocol, 0);
            registry.rebind(RMI_SERVER, stub);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void send(Message message) {
        clients.forEach(c -> {
            try {
                c.sendMessage(message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
        //client.sendMessage(message);
    }

    private synchronized void waitForClient() {
        Thread thread = new Thread(() -> {
            while (true) {
                for (int clientID : clientIDs) {
                    try {
                        clients.add((ChatProtocol) registry.lookup(RMI_CLIENT + clientID));
                    } catch (RemoteException | NotBoundException ignored) {
                        //stavi log
                    }
                }
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.setDaemon(true);
        thread.start();
    }
}
