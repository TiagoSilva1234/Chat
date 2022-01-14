package com.company;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server implements Runnable {
    private final ServerSocket serverSocket;
    private final ExecutorService pool;

    public Server(int port, int poolSize) throws IOException {
        this.serverSocket = new ServerSocket(port);
        this.pool = Executors.newFixedThreadPool(poolSize);
    }

    @Override
    public void run() {
        try {
            while (true) {
                pool.execute(new ClientHandler(serverSocket.accept()));
            }
        } catch (Exception e) {

        }
    }

    private static class ClientHandler implements Runnable {
        Socket clientsocket;
        private final PrintStream out;
        private final Scanner input;
        private static Set<ClientHandler> allClients = new HashSet<ClientHandler>();
        private String name;

        @Override
        public void run() {
            try {
                out.println("hello to my private chat");
                out.println("whats your name :3");
                name = input.nextLine();
                out.println("current name: " + name);
                out.println("Thank you very much please enjoy your discussion.");
                String message;
                do {
                    message = input.nextLine();
                    for (ClientHandler now : allClients) {
                        if (!now.equals(this)) {
                            now.sendMessage(message, name);
                        }
                    }
                } while (message.indexOf("quit") != 0);
                out.println("Quitting...");

                out.close();

                allClients.remove(this);
            } catch (Exception e) {
                System.out.println("thread error");
            }

        }

        public ClientHandler(Socket socket) throws IOException {
            this.clientsocket = socket;
            out = new PrintStream(socket.getOutputStream());
            InputStream is = socket.getInputStream();
            input = new Scanner(is);
            out.flush();
            allClients.add(this);
        }

        public synchronized void sendMessage(String message, String name) {
            out.println(name + ": " + message);
        }
    }
}
