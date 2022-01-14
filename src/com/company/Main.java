package com.company;

import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {
   Server server = new Server(30120,4);
   server.run();

    }
}
