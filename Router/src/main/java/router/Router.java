package router;

import java.io.*;
import router.market.MarketHandler;
import router.broker.BrokerHandler;

public class Router {
    public static void main(String[] args) throws IOException {
        Thread marketHandler = new Thread(new MarketHandler());
        marketHandler.start();
        Thread brokerHandler = new Thread(new BrokerHandler());
        brokerHandler.start();
    }
}
