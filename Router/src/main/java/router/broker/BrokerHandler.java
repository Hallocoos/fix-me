package router.broker;

import java.io.*;
import java.net.*;
import messaging.Messaging;

public class BrokerHandler implements Runnable {
    private static final int BROKER_PORT = 5000;

    @Override
    public void run() {
        try {
            ServerSocket brokerServerSocket = new ServerSocket(BROKER_PORT);
    
            while (true) {
                System.out.println("[ROUTER] Router waiting for broker connections.");
                Socket brokerSocket = brokerServerSocket.accept();
                System.out.println("[ROUTER] Connected to broker!");
                BrokerThread brokerThread = new BrokerThread(brokerSocket);
                Messaging.brokers.add(brokerThread);

                Messaging.pool.execute(brokerThread);
            }
        } catch (IOException e) {
            System.out.println("IOException in BrokerHandler's, run() function.");
        }
    }
}
