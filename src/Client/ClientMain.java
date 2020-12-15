package Client;

import java.io.*;
import java.net.Socket;

public class ClientMain {
    private static BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] args) {
        try {
            while (true) {
                System.out.println("-------------------------------------------");
                System.out.println("1. GET");
                System.out.println("2. PUT");
                System.out.println("3. DELETE");
                String commandIndex = consoleReader.readLine();
                switch (commandIndex) {
                    case "1" -> commandGet();
                    case "2" -> commandPut();
                    case "3" -> commandDelete();
                }
            }
        }
        catch (IOException e) {
            System.out.println("Error: "+ e.getMessage());
        }
    }

    private static void commandGet() {
        try {
            System.out.print("Enter key: ");
            String key = consoleReader.readLine();

            Socket socket = new Socket("127.0.0.1", 58399);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            writer.write("GET");
            writer.newLine();
            writer.write(key);
            writer.newLine();

            writer.newLine();
            writer.flush();

            String header = reader.readLine();
            if (header.equals("Ok")) {
                String value = reader.readLine();
                System.out.println("Value: " + value);
            }
            else if (header.equals("Error")) {
                String errorMessage = reader.readLine();
                System.out.println("Error: " + errorMessage);
            }
        }
        catch (IOException e) {
            System.out.println("Error on getCommand: " + e.getMessage());
        }
    }

    private static void commandPut() {
        try {
            System.out.print("Enter key: ");
            String key = consoleReader.readLine();
            System.out.print("Enter value: ");
            String value = consoleReader.readLine();

            Socket socket = new Socket("127.0.0.1", 58399);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            writer.write("PUT");
            writer.newLine();
            writer.write(key);
            writer.newLine();
            writer.write(value);
            writer.newLine();

            writer.newLine();
            writer.flush();

            String header = reader.readLine();
            if (header.equals("Ok")) {
                System.out.println("Added successfully");
            }
            else if (header.equals("Error")) {
                String errorMessage = reader.readLine();
                System.out.println("Error: " + errorMessage);
            }
        }
        catch (IOException e) {
            System.out.println("Error on putCommand: " + e.getMessage());
        }
    }

    private static void commandDelete() {
        try {
            System.out.print("Enter key: ");
            String key = consoleReader.readLine();

            Socket socket = new Socket("127.0.0.1", 58399);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            writer.write("DELETE");
            writer.newLine();
            writer.write(key);
            writer.newLine();

            writer.newLine();
            writer.flush();

            String header = reader.readLine();
            if (header.equals("Ok")) {
                System.out.println("Removed successfully");
            }
            else if (header.equals("Error")) {
                String errorMessage = reader.readLine();
                System.out.println("Error: " + errorMessage);
            }
        }
        catch (IOException e) {
            System.out.println("Error on putCommand: " + e.getMessage());
        }
    }
}
