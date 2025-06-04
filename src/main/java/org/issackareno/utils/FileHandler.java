package org.issackareno.utils;

import javax.swing.*;
import java.io.*;

public class FileHandler {
    public static String chooseFile() {
        JFileChooser fileChooser = new JFileChooser("C:\\");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int returnVal = fileChooser.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            String filePath = fileChooser.getSelectedFile().getAbsolutePath();
            System.out.println("[INFO] File Chosen: " + filePath);
            return filePath;
        }
        return "NULL";
    }
    public static String chooseDirectory() {
        JFileChooser fileChooser = new JFileChooser("C:\\");
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int returnVal = fileChooser.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            String filePath = fileChooser.getSelectedFile().getAbsolutePath();
            System.out.println("[INFO] Directory Chosen: " + filePath);
            return filePath;
        }
        return "NULL";
    }
    public static boolean saveFile(String directory, String fileName, String fileText) {
        File file = new File(directory, fileName);
        try {
            boolean result = file.createNewFile();
            if (result) {
                FileWriter fileWriter = new FileWriter(file);
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                bufferedWriter.write(fileText);
                bufferedWriter.close();
                fileWriter.close();
                return true;
            }
            else {
                return false;
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static boolean saveFile(String directory, String fileText) {
        File file = new File(directory);
        try {
            boolean result = file.createNewFile();
            if (result) {
                FileWriter fileWriter = new FileWriter(file);
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                bufferedWriter.write(fileText);
                bufferedWriter.close();
                fileWriter.close();
                return true;
            }
            else {
                return false;
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static String readFile(String directory) {
        File file = new File(directory);
        try {
            FileReader fileReader = new FileReader(file);
            StringBuilder stringBuilder = new StringBuilder();
            int data;
            while ((data = fileReader.read()) != -1){
                System.out.print((char) data);
                stringBuilder.append((char) data);
            }
            return stringBuilder.toString();
        }
        catch (IOException e) {
            e.printStackTrace();
            return "null";
        }
    }

    public static String readFile(String directory, String fileName) {
        File file = new File(directory, fileName);
        try {
            FileReader fileReader = new FileReader(file);
            StringBuilder stringBuilder = new StringBuilder();
            int data;
            while ((data = fileReader.read()) != -1){
                System.out.print((char) data);
                stringBuilder.append((char) data);
            }
            return stringBuilder.toString();
        }
        catch (IOException e) {
            e.printStackTrace();
            return "null";
        }
    }
}
