package org.issackareno.utils;

import org.issackareno.utils.Constants.*;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;

public class SendHandler implements Runnable {

    DataOutputStream dataOutputStream;
    DataInputStream dataInputStream;
    BufferedReader bufferedReader;

    @Override
    public void run() {
        while (true) {
            try {
                String message = bufferedReader.readLine();
                if(message.startsWith("[print-receiver]:")) {
                    dataOutputStream.writeUTF(Commands.PRINT_RECEIVER);
                    message = message.replace("[print-receiver]:", "");
                }
                dataOutputStream.writeUTF(message);
                dataOutputStream.flush();
                if (message.equals(Commands.EXIT)) {
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public SendHandler(DataOutputStream dataOutputStream, DataInputStream dataInputStream, BufferedReader bufferedReader) {
        this.dataOutputStream = dataOutputStream;
        this.dataInputStream = dataInputStream;
        this.bufferedReader = bufferedReader;
    }
}
