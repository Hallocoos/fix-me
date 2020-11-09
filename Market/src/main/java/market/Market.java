package market;

import java.io.*;
import java.net.*;
import java.util.*;

public class Market {
    private static final String ROUTER_IP = "127.0.0.1";
    private static final int ROUTER_PORT = 5001;
    public static Socket routerSocket = null;


    public static void main(String[] args) throws IOException {
        while (routerSocket == null) {
            try {
                routerSocket = new Socket(ROUTER_IP, ROUTER_PORT);
            } catch (ConnectException e) {
                System.out.println("Waiting to establish connection to router.");
            }
        }
        System.out.println("Connected to router!");
        PrintWriter routerOutput = new PrintWriter(routerSocket.getOutputStream(), true);
        BufferedReader routerInput = new BufferedReader(new InputStreamReader(routerSocket.getInputStream()));
        BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            System.out.print("> ");
            String input = routerInput.readLine();
            if (input != null) {
                System.out.println(input);
                String data[] = input.split(" ");
                routerOutput.println(input);
            } else
                break;
        }
        routerSocket.close();
        System.exit(0);
    }
}