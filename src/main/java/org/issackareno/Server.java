package org.issackareno;

import java.net.*;
import java.io.*;
import java.util.ArrayList;

import org.issackareno.utils.*;
import org.issackareno.models.MessagePool;

public class Server {

    static int clientCount = 0;

    static MessagePool messagePool;

    static ArrayList<ClientHandler> clients = new ArrayList<>();

    static BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

    static boolean enableHistory = false;
    static String historyText = "";

    public static void main(String[] args) {

        try{

            System.out.println("Read history?(Y/n)");
            String read_history = bufferedReader.readLine();
            if(!(read_history.equals("N") || read_history.equals("n"))) {
                String history_path = FileHandler.chooseFile();
                historyText = FileHandler.readFile(history_path);
                messagePool = new MessagePool(true, historyText);
                enableHistory = true;
            }
            else {
                messagePool = new MessagePool(false, "");
            }

            ServerSocket serverSocket = new ServerSocket(1017);

            AcceptHandler acceptHandler = new AcceptHandler(serverSocket);
            Thread acceptThread = new Thread(acceptHandler);
            acceptThread.start();

            BroadcastHandler broadcastHandler = new BroadcastHandler();
            Thread broadcastThread = new Thread(broadcastHandler);
            broadcastThread.start();
            boolean running = true;
            while (running) {
                try {
                    String message = bufferedReader.readLine();
                    if (message.equals("stop")) {
                        broadcastThread.interrupt();
                        acceptThread.interrupt();
                        running = false;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            System.out.println("Save history?(Y/n)");
            String save_history = bufferedReader.readLine();
            if(!(save_history.equals("N") || save_history.equals("n"))) {
                String history_path = FileHandler.chooseDirectory();
                String history_text = "";
                history_text += messagePool.getHistoryFileText();
                history_text += messagePool.saveHistory();
                System.out.println(history_text);
                if(FileHandler.saveFile(history_path, "history_" + Constants.generateDate(System.currentTimeMillis()) + ".txt", history_text)) {
                    System.out.println("Saved history to " + history_path);
                }
                else {
                    System.out.println("Failed to save history to " + history_path);
                    System.out.println(history_text);
                }
            }
            bufferedReader.close();
            serverSocket.close();

        }
        catch(Exception e){
            e.printStackTrace();
        }

    }

    public static void addClient(Socket socket){
        try {
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            ClientHandler clientHandler = new ClientHandler(socket, String.format("%05d", clientCount++), dataOutputStream, dataInputStream);
            clients.add(clientHandler);
            Thread thread = new Thread(clientHandler);
            thread.start();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public static ArrayList<String> listRunningClients() {
        ArrayList<String> runningClients = new ArrayList<>();
        for (ClientHandler clientHandler : clients) {
            if(clientHandler.getRunning()) {
                runningClients.add(clientHandler.getClientName() + "@" + clientHandler.getClientId());
            }
        }
        return runningClients;
    }

    public static ArrayList<ClientHandler> getRunningClients() {
        ArrayList<ClientHandler> runningClients = new ArrayList<>();
        for (ClientHandler clientHandler : clients) {
            if(clientHandler.getRunning()) {
                runningClients.add(clientHandler);
            }
        }
        return runningClients;
    }

    public static MessagePool getMessagePool() {
        return messagePool;
    }
}