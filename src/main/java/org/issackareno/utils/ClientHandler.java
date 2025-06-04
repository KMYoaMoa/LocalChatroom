package org.issackareno.utils;

import org.issackareno.Server;
import org.issackareno.models.Message;
import org.issackareno.models.MessagePool;
import org.issackareno.utils.Constants.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable {

    Socket socket;
    String clientName;
    String clientId;
    DataOutputStream dataOutputStream;
    DataInputStream dataInputStream;
    Boolean running = true;
    MessagePool messagePool;

    @Override
    public void run() {

        try {

            clientName = dataInputStream.readUTF();
            dataOutputStream.writeUTF(clientId);
            dataOutputStream.flush();

            listMembers();

            String history_text = "";
            if(messagePool.isEnableHistory()) {
                 history_text += messagePool.getHistoryFileText();
            }

            history_text += messagePool.saveHistory();

            dataOutputStream.writeUTF(history_text);
            dataOutputStream.flush();

            Message _message = new Message(clientName + "@" + clientId, "[Server] HAS JOINED THE CHATROOM...", System.currentTimeMillis());
            System.out.println(clientName + ": " + _message.getText());
            messagePool.addMessage(_message);

            // Start Message Service
            label:
            while (true) {

                String rawMessage = dataInputStream.readUTF();

                switch (rawMessage) {
                    case Commands.EXIT: {
                        dataOutputStream.writeUTF("[Server] Bye, " + clientName + "!");
                        dataOutputStream.flush();
                        dataOutputStream.writeUTF("[Server] Connection closed");
                        dataOutputStream.flush();

                        Message message = new Message(clientName + "@" + clientId, "[Server] HAS LEFT THE CHATROOM...", System.currentTimeMillis());
                        System.out.println(clientName + ": " + message.getText());
                        messagePool.addMessage(message);

                        running = false;
                        break label;
                    }
                    case Commands.PRINT_MEMBER: {
                        listMembers();
                        break;
                    }
                    case Commands.PRINT_RECEIVER: {
                        listReceivers();
                        break;
                    }
                    case Commands.HELP: {
                        dataOutputStream.writeUTF(Texts.HELP_TEXT);
                        dataOutputStream.flush();

                        break;
                    }
                    default: {
                        Message message = new Message(clientName + "@" + clientId, rawMessage, System.currentTimeMillis());
                        System.out.println(clientName + ": " + message.getText());
                        messagePool.addMessage(message);

                        break;
                    }
                }
            }
        } catch (Exception e) {

            //noinspection CallToPrintStackTrace
            e.printStackTrace();
            running = false;

        }
    }

    public Boolean getRunning() {
        return running;
    }

    public void broadcast(String message) {
        try{
            dataOutputStream.writeUTF(message);
            dataOutputStream.flush();
        }
        catch(Exception e){
            e.printStackTrace();
        }

    }

    public ClientHandler(Socket socket, String clientId, DataOutputStream dataOutputStream, DataInputStream dataInputStream) {
        this.socket = socket;
        this.dataOutputStream = dataOutputStream;
        this.dataInputStream = dataInputStream;
        this.clientId = clientId;
        messagePool = Server.getMessagePool();
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientName() {
        return clientName;
    }

    public String getClientInfo() {
        return clientName + "@" + clientId;
    }

    void listReceivers() {
        try{
            String _rawMessage = dataInputStream.readUTF();

            Message _message = new Message(clientName + "@" + clientId, _rawMessage, System.currentTimeMillis());
            System.out.println(clientName + ": " + _message.getText());
            messagePool.addMessage(_message);

            BroadcastHandler.broadcast();

            ArrayList<String> receivedUsers = messagePool.getReceivers(_message.getMessageId());

            StringBuilder response = new StringBuilder("[System] Users Received Your Last Message: \n");
            for (String clientInfo : receivedUsers) {
                if(!clientInfo.equals(clientName + "@" + clientId)) {
                    response.append(" * ").append(clientInfo).append("\n");
                }
            }
            response.append(" * * * * * * * * * * *");

            dataOutputStream.writeUTF(response.toString());
            dataOutputStream.flush();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    void listMembers() {
        try {
            ArrayList<String> runningClients = Server.listRunningClients();

            StringBuilder response = new StringBuilder("[System] Currently Online Users: \n");
            for (String clientInfo : runningClients) {
                response.append(" * ").append(clientInfo).append("\n");
            }
            response.append(" * * * * * * * * * * *");

            dataOutputStream.writeUTF(response.toString());
            dataOutputStream.flush();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
