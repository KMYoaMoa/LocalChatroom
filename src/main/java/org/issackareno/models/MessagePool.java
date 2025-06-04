package org.issackareno.models;

import java.util.ArrayList;
import java.util.LinkedList;

public class MessagePool {
    static boolean enableHistory = false;
    static String historyText = null;
    static int messageCount = 0;
    ArrayList<Message> historyMessages = new ArrayList<>();
    LinkedList<Message> suspendedMessages = new LinkedList<>();
    public MessagePool(boolean enableHistory, String historyText) {
        MessagePool.enableHistory = enableHistory;
        MessagePool.historyText = historyText;
    }
    public void addMessage(Message m) {
        m.setId(messageCount++);
        suspendedMessages.add(m);
    }
    public boolean isEmpty() {
        return suspendedMessages.isEmpty();
    }
    public Message getLastMessage() {
        Message m = suspendedMessages.getLast();
        suspendedMessages.removeLast();
        historyMessages.add(m);
        return m;
    }
    public void addReceiver(int id, String receiver) {
        for (Message m : historyMessages) {
            if (m.getMessageId() == id) {
                m.addReceiver(receiver);
            }
        }
    }
    public ArrayList<String> getReceivers(int id) {
        for (Message m : historyMessages) {
            if (m.getMessageId() == id) {
                return m.getReceivers();
            }
        }
        return new ArrayList<>();
    }
    public String saveHistory() {

        StringBuilder response = new StringBuilder();
        for (Message m : historyMessages) {
            response.append(m.toString()).append("\n");
        }
        return response.toString();
    }
    public boolean isEnableHistory() {
        return enableHistory;
    }
    public String getHistoryFileText() {
        return historyText;
    }
}
