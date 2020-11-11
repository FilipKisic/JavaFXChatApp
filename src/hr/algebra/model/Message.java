package hr.algebra.model;

import java.sql.Time;

public class Message {
    private int idMessage;
    private final String messageContent;
    private final int fromId;
    private final int toId;
    private final Time time;

    public Message(int idMessage, String messageContent, int fromId, int toId, Time time) {
        this.idMessage = idMessage;
        this.messageContent = messageContent;
        this.fromId = fromId;
        this.toId = toId;
        this.time = time;
    }

    public Message(String messageContent, int fromId, int toId, Time time) {
        this.messageContent = messageContent;
        this.fromId = fromId;
        this.toId = toId;
        this.time = time;
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

    public Time getTime() {
        return time;
    }

    /*TODO
     *  - differentiate between image and string as messageContent
     */
}
