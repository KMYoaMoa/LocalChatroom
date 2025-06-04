package org.issackareno.models;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class Message {
    final String from;
    final long timestamp;
    int id;
    final String text;
    ArrayList<String> receivers = new ArrayList<>();
    public Message(String from, String text, long timestamp) {
        this.from = from;
        this.timestamp = timestamp;
        this.text = text;
    }
    public String getFromInfo() {
        return from;
    }
    public long getTimestamp() {
        return timestamp;
    }
    public String getText() {
        return text;
    }
    public int getMessageId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public ArrayList<String> getReceivers() {
        return receivers;
    }
    public void addReceiver(String receiver) {
        receivers.add(receiver);
    }
    public String toString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr = dateFormat.format(timestamp);
        return dateStr + "\n" + from + " : " + text;
    }
    public String toString(String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr = dateFormat.format(timestamp);
        return String.format(format, dateStr, from, text);
    }
    public boolean isFrom(String clientInfo) {
        return clientInfo.equals(getFromInfo());
    }
}
