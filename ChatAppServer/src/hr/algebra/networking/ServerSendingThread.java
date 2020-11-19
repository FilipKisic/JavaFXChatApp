package hr.algebra.networking;

import hr.algebra.model.Message;
import hr.algebra.utils.ByteUtils;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Properties;
import java.util.concurrent.LinkedBlockingDeque;

public class ServerSendingThread extends Thread {

    public static int SERVER_PORT;
    public static int CLIENT_PORT;
    public static String GROUP;

    static {
        try {
            Properties properties = new Properties();
            properties.load(new FileInputStream("socket.properties"));
            SERVER_PORT = Integer.parseInt("SERVER_PORT");
            CLIENT_PORT = Integer.parseInt("CLIENT_PORT");
            GROUP = properties.getProperty("GROUP");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private final LinkedBlockingDeque<Message> messages = new LinkedBlockingDeque<>();

    public void trigger(Message message) {
        messages.add(message);
    }

    @Override
    public void run() {
        try (DatagramSocket server = new DatagramSocket(SERVER_PORT)) {
            System.err.println("Server multicasting on port: " + server.getLocalPort());
            while (true) {
                if (!messages.isEmpty()) {
                    byte[] messageBytes;
                    try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
                         ObjectOutputStream oos = new ObjectOutputStream(baos)) {
                        oos.writeObject(messages.getFirst());
                        messages.clear();
                        oos.flush();
                        messageBytes = baos.toByteArray();
                    }
                    byte[] numberOfMessageBytes = ByteUtils.intToByteArray(messageBytes.length);
                    InetAddress groupAddress = InetAddress.getByName(GROUP);
                    DatagramPacket packet = new DatagramPacket(numberOfMessageBytes, numberOfMessageBytes.length, groupAddress, CLIENT_PORT);
                    server.send(packet);
                    packet = new DatagramPacket(messageBytes, messageBytes.length, groupAddress, CLIENT_PORT);
                    server.send(packet);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
