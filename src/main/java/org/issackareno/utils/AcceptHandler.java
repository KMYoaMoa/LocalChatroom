package org.issackareno.utils;

import org.issackareno.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class AcceptHandler implements Runnable{
    ServerSocket serverSocket;
    @Override
    public void run() {
        try{
            while(!serverSocket.isClosed() && !Thread.currentThread().isInterrupted()) {
                Socket socket = serverSocket.accept();
                Server.addClient(socket);
            }
        } catch (IOException e) {
            // e.printStackTrace();
        }
    }
    public AcceptHandler(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }
}
