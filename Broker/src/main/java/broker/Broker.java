package broker;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

public class Broker {
    private static final String ROUTER_IP = "127.0.0.1";
    private static final int ROUTER_PORT = 5000;
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
        System.out.println("Commands are `buy` and `sell`:");
        System.out.println("buy <stock> <amount>:");
        System.out.println("    -User can attempt to buy <amount> of <stock/s>");
        System.out.println("buy:");
        System.out.println("    -Will show user what stocks are available");
        System.out.println("sell <stock> <amount>:");
        System.out.println("    -User can attempt to sell <amount> of <stock/s>");

        PrintWriter routerOutput = new PrintWriter(routerSocket.getOutputStream(), true);
        BufferedReader routerInput = new BufferedReader(new InputStreamReader(routerSocket.getInputStream()));
        BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            System.out.print("> ");
            String input = (stdin.readLine()).trim().replaceAll("\\s+", " ");
            if (input.equals("quit")) {
                routerOutput.println(input);
                break;
            } else if (validateInput(input)) {
                input += "|" + calculateChecksum(input);
                routerOutput.println(input.trim().replaceAll("\\s+", "|"));
                String marketMessage = routerInput.readLine();
                System.out.println(marketMessage);
            }
        }
        routerSocket.close();
        System.exit(0);
    }

    private static boolean validateInput(String input) {
        if (input == null || input.equalsIgnoreCase(""))
            return false;
        if (input.equalsIgnoreCase("buy") || input.equalsIgnoreCase("sell"))
            return true;
        String[] validArray = input.split("\\s+");
        if (validArray.length != 3)
            return false;
        if (!validArray[0].equalsIgnoreCase("buy") && !validArray[0].equalsIgnoreCase("sell"))
            return false;
        boolean isNumeric = validArray[2].chars().allMatch(x -> Character.isDigit(x));
        if (!isNumeric)
            return false;
        return true;
    }

    public static long calculateChecksum(String request){
        byte[] bytes = request.getBytes();
        Checksum crc32 = new CRC32();
        crc32.update(bytes, 0, bytes.length);
        return crc32.getValue();
    }
}
