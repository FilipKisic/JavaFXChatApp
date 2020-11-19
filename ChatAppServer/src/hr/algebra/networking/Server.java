package hr.algebra.networking;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;

public class Server {
    private static int SERVER_PORT;
    private static int CLIENT_PORT;
    private static String GROUP;

    static {
        try{
            Properties properties = new Properties();
            properties.load(new FileInputStream("socket.properties"));
            SERVER_PORT = Integer.parseInt(properties.getProperty("SERVER_PORT"));
            CLIENT_PORT = Integer.parseInt(properties.getProperty("CLIENT_PORT"));
            GROUP = properties.getProperty("GROUP");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startServer(){
        try(ServerSocket serverSocket = new ServerSocket(SERVER_PORT)){
            System.out.println("Server listening on port: " + SERVER_PORT);
            while(true){
                Socket client = serverSocket.accept();
                System.err.println("New client connected on port: " + client.getPort());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
