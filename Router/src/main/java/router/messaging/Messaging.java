package messaging;

import java.io.*;
import java.net.*;
import java.util.concurrent.*;
import java.util.ArrayList;
import router.broker.BrokerThread;
import router.market.MarketThread;

public class Messaging {
    public static ArrayList<BrokerThread> brokers = new ArrayList<>();
    public static MarketThread market;
    public static ExecutorService pool = Executors.newFixedThreadPool(100);

    public static void sendToBroker(String message) {
        String id = message.split("\\s+")[0];
        for (BrokerThread broker : brokers) {
            if (broker.getBrokerID().equalsIgnoreCase(id)) {
                broker.sendToBroker(message);
                break;
            }
        }
    }

    public static void sendToMarket(String message) {
        String id = message.split("\\s+")[0];
        market.sendToMarket(message);
    }
}
