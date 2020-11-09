package router.market;

import java.io.*;
import java.net.*;
import messaging.Messaging;

public class MarketHandler implements Runnable {
    private static final int MARKET_PORT = 5001;

    @Override
    public void run() {
        try {
            ServerSocket marketServerSocket = new ServerSocket(MARKET_PORT);

            System.out.println("[ROUTER] Router waiting for market connections.");
            Socket marketSocket = marketServerSocket.accept();
            System.out.println("[ROUTER] Connected to market.");
            MarketThread marketThread = new MarketThread(marketSocket);
            Messaging.market = marketThread;

            Messaging.pool.execute(marketThread);
        } catch (IOException e) {
            System.out.println("IOException in MarketHandler's, run() function.");
        }
    }
}