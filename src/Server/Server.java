package Server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Server {
    private ServerSocket serverSocket;
    private Map<String, String> storage = new HashMap<>();

    public Server() {
        try {
            serverSocket = new ServerSocket(58399, 10);
        }
        catch (IOException ignored) {
            // TODO
        }
    }

    public void start() {
        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Accepted new client");
                Thread thread = new Thread(() -> onClientAccepted(clientSocket));
                thread.start();
            }
            catch (IOException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void onClientAccepted(Socket clientSocket) {
        String command;
        List<String> args = new ArrayList<>();

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

            command = reader.readLine();
            if (command == null) {
                System.out.println("Received nothing");
                return;
            }

            while (true) {
                String line = reader.readLine();
                if (line == null || line.isEmpty())
                    break;
                args.add((line));
            }

            String[] response = switch (command.toUpperCase()) {
                case "PUT" -> onPutCommand(args);
                case "GET" -> onGetCommand(args);
                case "DELETE" -> onDeleteCommand(args);
                default -> new String[] {
                        "Error",
                        "Unknown command: " + command
                };
            };

            for (String line : response) {
                writer.write(line);
                writer.newLine();
            }
            writer.newLine();
            writer.flush();
        }
        catch (IOException e) {
            System.out.println("OnClientAccepted error: " + e.getMessage());
        }
    }

    private String[] onPutCommand(List<String> args) {
        if (args.size() != 2) {
            System.out.println("Received wrong count or arguments for PUT command: " + args.size());
            return new String[] {
                    "Error",
                    "Wrong count of args"
            };
        }

        storage.put(args.get(0), args.get(1));
        return new String[] { "Ok" };
    }

    private String[] onGetCommand(List<String> args) {
        if (args.size() != 1) {
            System.out.println("Received wrong count or arguments for GET command: " + args.size());
            return new String[] {
                    "Error",
                    "Wrong count of args"
            };
        }

        String value = storage.getOrDefault(args.get(0), null);
        if (value == null)
            return new String[] {
                    "Error",
                    "Wrong key"
            };
        else
            return new String[] {
                    "Ok",
                    value
            };
    }

    private String[] onDeleteCommand(List<String> args) {
        if (args.size() != 1) {
            System.out.println("Received wrong count or arguments for GET command: " + args.size());
            return new String[] {
                    "Error",
                    "Wrong count of args"
            };
        }

        String key = args.get(0);
        if (storage.containsKey(key)) {
            storage.remove(key);
            return new String[] {"Ok"};
        }
        else {
            return new String[] {
                    "Error",
                    "Wrong key."
            };
        }
    }


}
