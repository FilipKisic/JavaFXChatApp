package hr.algebra.model;

import java.io.*;
import java.sql.Timestamp;

public class Message implements Externalizable {
    private static final long serialVersionUID = 1L;
    private int idMessage;
    private byte[] messageContent;
    private int fromId;
    private int toId;
    private Timestamp time;
    private boolean isImage;

    public Message() {
    }

    public Message(int idMessage, byte[] messageContent, int fromId, int toId, Timestamp time, boolean isImage) {
        this.idMessage = idMessage;
        this.messageContent = messageContent;
        this.fromId = fromId;
        this.toId = toId;
        this.time = time;
        this.isImage = isImage;
    }

    public Message(byte[] messageContent, int fromId, int toId, boolean isImage) {
        this.messageContent = messageContent;
        this.fromId = fromId;
        this.toId = toId;
        this.time = new Timestamp(System.currentTimeMillis());
        this.isImage = isImage;
    }

    public int getIdMessage() {
        return idMessage;
    }

    public byte[] getMessageContent() {
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

    public boolean isImage() {
        return isImage;
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeInt(idMessage);
        out.writeInt(fromId);
        out.writeInt(toId);
        out.writeUTF(time.toString());
        out.writeBoolean(isImage);
        out.writeInt(messageContent.length);
        out.write(messageContent);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException {
        idMessage = in.readInt();
        fromId = in.readInt();
        toId = in.readInt();
        time = Timestamp.valueOf(in.readUTF());
        isImage = in.readBoolean();
        messageContent = new byte[in.readInt()];
        in.readFully(messageContent);
    }
}
