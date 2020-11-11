package hr.algebra.model;

import java.io.Serializable;
import java.sql.Timestamp;

public class Message implements Serializable {
    private int idMessage;
    private final String messageContent;
    private final int fromId;
    private final int toId;
    private final Timestamp time;

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

    /*TODO
     *  - differentiate between image and string as messageContent
     */
}
