package market;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

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
                String data[] = input.split("|");
                int endIndex = input.lastIndexOf("|");
                if (endIndex != -1) {
                    String request = input.substring(0, endIndex);
                    String checksum = input.substring(endIndex + 1, endIndex + 10);
                    if (validateChecksum(request, Long.parseLong(checksum))) {
                        System.out.println("Checksum passed.");
                        String response = "bought or sold a certain amount something";
                        routerOutput.println(input + "|" + response);
                    } else
                        routerOutput.println(input + "|" + "Checksum failed.");
                }
            } else
                break;
        }
        routerSocket.close();
        System.exit(0);
    }

    public static boolean validateChecksum(String request, long checksum){
        byte[] bytes = request.getBytes();
        Checksum crc32 = new CRC32();
        crc32.update(bytes, 0, bytes.length);
        if (crc32.getValue() == checksum)
            return false;
        return true;
    }
}