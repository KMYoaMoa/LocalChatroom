package org.issackareno;

import java.io.*;
import java.net.*;

import org.issackareno.utils.SendHandler;

public class Client {
    static String name;
    static String id;
    public static void main(String[] args) {

        try {
            Socket socket = new Socket("localhost", 1017);
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("[System] Successfully connected to server at " + socket.getRemoteSocketAddress());
            System.out.println("[System] Enter your username: ");
            name = bufferedReader.readLine();

            // Handshake
            dataOutputStream.writeUTF(name);
            dataOutputStream.flush();
            id = dataInputStream.readUTF();
            System.out.println("[System] Welcome " + name + "@" + id + "!");
            System.out.println("[System] type 'command::help' to see list of commands");
            Thread sendThread = new Thread(new SendHandler(dataOutputStream, dataInputStream, bufferedReader));
            sendThread.start();

            while (true) {
                String response = dataInputStream.readUTF();
                System.out.println(response);
                if (response.equals("[Server] Connection closed")) {
                    break;
                }
            }
            socket.close();
            dataOutputStream.close();
            bufferedReader.close();
            dataInputStream.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }
}

