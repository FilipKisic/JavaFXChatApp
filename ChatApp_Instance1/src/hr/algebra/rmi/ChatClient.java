package hr.algebra.rmi;

import com.sun.jndi.rmi.registry.RegistryContextFactory;
import hr.algebra.controllers.Controller;
import hr.algebra.jndi.InitialDirContextCloseable;
import hr.algebra.model.Message;

import javax.naming.Context;
import javax.naming.NamingException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Hashtable;

public class ChatClient {

    private static final String RMI_CLIENT = "client2";
    private static final String RMI_SERVER = "server";
    private static final String RMI_URL = "rmi://localhost:1099";
    private static final int RMI_PORT = 1099;

    private final Controller controller;
    private ChatProtocol chatProtocol;
    private ChatProtocol server;

    public ChatClient(Controller controller) {
        this.controller = controller;
        System.out.println("CLIENT: " + RMI_CLIENT);
        publishClient();
        fetchServer();
        try {
            server.registerClient(2);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void publishClient() {
        chatProtocol = new ChatProtocol() {
            @Override
            public void registerClient(int i) {

            }

            @Override
            public void sendMessage(Message message) {
                controller.displayMessage(message);
            }
        };
        try {
            Registry registry = LocateRegistry.getRegistry(RMI_PORT);
            ChatProtocol stub = (ChatProtocol) UnicastRemoteObject.exportObject(chatProtocol, 0);
            registry.rebind(RMI_CLIENT, stub);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void fetchServer() {
        final Hashtable<String, String> properties = new Hashtable<>();
        properties.put(Context.INITIAL_CONTEXT_FACTORY, RegistryContextFactory.class.getName());
        properties.put(Context.PROVIDER_URL, RMI_URL);

        try (InitialDirContextCloseable ctx = new InitialDirContextCloseable(properties)) {
            server = (ChatProtocol) ctx.lookup(RMI_SERVER);
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    public void send(Message message) {
        try {
            server.sendMessage(message);
        } catch (RemoteException ignored) {
        }
    }
}
