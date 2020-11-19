package hr.algebra.model;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.sql.Timestamp;

public class Message implements Externalizable {
    private static final long serialVersionUID = 1L;

    private int idMessage;
    private String messageContent;
    private int fromId;
    private int toId;
    private Timestamp time;

    public Message(){
    }

    public Message(int idMessage, String messageContent, int fromId, int toId, Timestamp time) {
        this.idMessage = idMessage;
        this.messageContent = messageContent;
        this.fromId = fromId;
        this.toId = toId;
        this.time = time;
    }

    public Message(String messageContent, int fromId, int toId) {
        this.messageContent = messageContent;
        this.fromId = fromId;
        this.toId = toId;
        this.time = new Timestamp(System.currentTimeMillis());
    }

    public int getIdMessage() {
        return idMessage;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public int getFromId() {
        return fromId;
    }

    public int getToId() {
        return toId;
    }

    public Timestamp getTime() {
        return time;
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeInt(idMessage);
        out.writeUTF(messageContent);
        out.writeInt(fromId);
        out.writeInt(toId);
        out.writeUTF(time.toString());
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException {
        idMessage = in.readInt();
        messageContent = in.readUTF();
        fromId = in.readInt();
        toId = in.readInt();
        time = Timestamp.valueOf(in.readUTF());
    }
}
