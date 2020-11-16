package router.broker;

import java.io.*;
import java.net.*;
import java.util.Random;
import messaging.Messaging;

public class BrokerThread implements Runnable {
    private BufferedReader brokerInput;
    private PrintWriter brokerOutput;
    private Socket brokerSocket;
    private String brokerID;

    public BrokerThread(Socket brokerSocket) throws IOException {
        brokerInput = new BufferedReader(new InputStreamReader(brokerSocket.getInputStream()));
        brokerOutput = new PrintWriter(brokerSocket.getOutputStream(), true);
        Random r = new Random();
        int i = r.nextInt((99999 - 10000) + 1) + 99999;
        brokerID = Integer.toString(i);
        this.brokerSocket = brokerSocket;
    }

    @Override
    public void run() {
        try {
            while (true) {
                String request = brokerInput.readLine();
                if (request != null && !request.equals("quit")) {
                    System.out.println(brokerID + "|" + request);
                    Messaging.sendToMarket(brokerID + "|" + request);
                } else {
                    System.out.println("Connection to broker has been closed.");
                }
            }
        } catch (IOException e) {
            System.out.println("Connection to broker has been lost.");
        } finally {
            try {
                brokerOutput.close();
                brokerInput.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getBrokerID() {
        return this.brokerID;
    }

    public Socket getBrokerSocket() {
        return this.brokerSocket;
    }

    public void sendToBroker(String message) {
        brokerOutput.println(message);
    }
}