package hr.algebra.networking;

import hr.algebra.model.Message;
import hr.algebra.utils.ByteUtils;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Properties;

public class ServerThread extends Thread {

    public static int SERVER_PORT_SEND;
    public static int SERVER_PORT_RECEIVE;
    public static int CLIENT_PORT;
    public static String GROUP;

    static {
        try {
            Properties properties = new Properties();
            properties.load(new FileInputStream("socket.properties"));
            SERVER_PORT_SEND = Integer.parseInt(properties.getProperty("SERVER_PORT_SEND"));
            SERVER_PORT_RECEIVE = Integer.parseInt(properties.getProperty("SERVER_PORT_RECEIVE"));
            CLIENT_PORT = Integer.parseInt(properties.getProperty("CLIENT_PORT"));
            GROUP = properties.getProperty("GROUP");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(Message message) {
        try (DatagramSocket socket = new DatagramSocket(SERVER_PORT_SEND)) {
            InetAddress groupAddress = InetAddress.getByName(GROUP);
            byte[] messageBytes;
            try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
                 ObjectOutputStream oos = new ObjectOutputStream(baos)) {
                oos.writeObject(message);
                oos.flush();
                messageBytes = baos.toByteArray();
            }
            byte[] numberOfMessageBytes = ByteUtils.intToByteArray(messageBytes.length);
            DatagramPacket packet = new DatagramPacket(numberOfMessageBytes, numberOfMessageBytes.length, groupAddress, CLIENT_PORT);
            socket.send(packet);
            packet = new DatagramPacket(messageBytes, messageBytes.length, groupAddress, CLIENT_PORT);
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try (MulticastSocket socket = new MulticastSocket(SERVER_PORT_RECEIVE)) {
            InetAddress address = InetAddress.getByName(GROUP);
            System.err.println("Server joined group");
            socket.joinGroup(address);

            while (true) {
                byte[] numberOfMessageBytes = new byte[4];
                DatagramPacket packet = new DatagramPacket(numberOfMessageBytes, numberOfMessageBytes.length);
                socket.receive(packet);
                int length = ByteUtils.byteArrayToInt(numberOfMessageBytes);

                byte[] messageBytes = new byte[length];
                packet = new DatagramPacket(messageBytes, messageBytes.length);
                socket.receive(packet);
                try (ByteArrayInputStream bais = new ByteArrayInputStream(messageBytes);
                     ObjectInputStream ois = new ObjectInputStream(bais)) {
                    Message message = (Message) ois.readObject();
                    //send message to database later
                    //now send message to client directly
                    if(message != null)
                        sendMessage(message);
                }
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
