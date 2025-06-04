package org.issackareno.utils;

import org.issackareno.Server;
import org.issackareno.models.Message;
import org.issackareno.models.MessagePool;

import java.util.ArrayList;

public class BroadcastHandler implements Runnable {

    static final int UPS_SET = 15;
    int update = 0;
    long previousTime = 0;
    long currentTime = 0;

    @Override
    public void run() {

        System.out.println("[Info] BroadcastHandler started");

        double timePerUpdate = 1000000000.0 / UPS_SET;
        long lastCheck = System.currentTimeMillis();
        double deltaU = 0;
        previousTime = System.nanoTime();

        while(!Thread.interrupted()) {

            // update current time
            currentTime = System.nanoTime();

            // absorb the time loss
            deltaU += (currentTime - previousTime) / timePerUpdate;

            // update previous time
            previousTime = currentTime;

            // update broadcast tick
            if(deltaU >= 1) {
                broadcast();
                deltaU -= 1;
                update++;
            }

            if(System.currentTimeMillis() - lastCheck >= 1000) {
                lastCheck = System.currentTimeMillis();
                // System.out.println("[Info] UPS: " + update + " | dU: " + deltaU);
                update = 0;
            }

        }
    }

    public static void broadcast() {

        ArrayList<ClientHandler> runningClients = Server.getRunningClients();
        MessagePool pool = Server.getMessagePool();

        while(!pool.isEmpty()) {

            Message msg = pool.getLastMessage();
            String msgStr = msg.toString();

            for(ClientHandler client : runningClients) {
                if(client.running) { //  && !msg.isFrom(client.getClientInfo())

                    client.broadcast(msgStr);
                    pool.addReceiver(msg.getMessageId(), client.getClientInfo());

                }

            }

        }

    }

}