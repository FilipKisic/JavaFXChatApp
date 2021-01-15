package hr.algebra.rmi;

import hr.algebra.model.Message;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ChatProtocol extends Remote {
    void registerClient(int clientID) throws RemoteException;
    void sendMessage(Message message) throws RemoteException;
}
