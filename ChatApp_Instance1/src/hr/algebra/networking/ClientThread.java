package hr.algebra.networking;

import hr.algebra.controllers.Controller;
import hr.algebra.model.Message;
import hr.algebra.utils.ByteUtils;
import javafx.application.Platform;

import java.io.*;
import java.net.*;
import java.util.Properties;

public class ClientThread extends Thread {

    private final Controller controller;
    public static int SERVER_PORT;
    public static int CLIENT_PORT_SEND;
    public static int CLIENT_PORT_RECEIVE;
    public static String GROUP;

    static {
        try {
            Properties properties = new Properties();
            properties.load(new FileInputStream("socket.properties"));
            SERVER_PORT = Integer.parseInt(properties.getProperty("SERVER_PORT"));
            CLIENT_PORT_SEND = Integer.parseInt(properties.getProperty("CLIENT_PORT_SEND"));
            CLIENT_PORT_RECEIVE = Integer.parseInt(properties.getProperty("CLIENT_PORT_RECEIVE"));
            GROUP = properties.getProperty("GROUP");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ClientThread(Controller controller) {
        this.controller = controller;
    }

    public void sendMessage(Message message) {
        try (DatagramSocket socket = new DatagramSocket(CLIENT_PORT_SEND)) {
            InetAddress groupAddress = InetAddress.getByName(GROUP);
            byte[] messageBytes;
            try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
                 ObjectOutputStream oos = new ObjectOutputStream(baos)) {
                oos.writeObject(message);
                oos.flush();
                messageBytes = baos.toByteArray();
            }
            byte[] numberOfMessageBytes = ByteUtils.intToByteArray(messageBytes.length);
            DatagramPacket packet = new DatagramPacket(numberOfMessageBytes, numberOfMessageBytes.length, groupAddress, SERVER_PORT);
            socket.send(packet);
            packet = new DatagramPacket(messageBytes, messageBytes.length, groupAddress, SERVER_PORT);
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try (MulticastSocket client = new MulticastSocket(CLIENT_PORT_RECEIVE)) {
            InetAddress groupAddress = InetAddress.getByName(GROUP);
            System.err.println("Client connected!");
            client.joinGroup(groupAddress);

            while (true) {
                byte[] numberOfMessageBytes = new byte[4];
                DatagramPacket packet = new DatagramPacket(numberOfMessageBytes, numberOfMessageBytes.length);
                client.receive(packet);
                int length = ByteUtils.byteArrayToInt(numberOfMessageBytes);

                byte[] messageBytes = new byte[length];
                packet = new DatagramPacket(messageBytes, messageBytes.length);
                client.receive(packet);
                try (ByteArrayInputStream bais = new ByteArrayInputStream(messageBytes);
                     ObjectInputStream ois = new ObjectInputStream(bais)) {
                    Message message = (Message) ois.readObject();
                    if (message != null)
                        Platform.runLater(() -> controller.showMessage(message));
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
