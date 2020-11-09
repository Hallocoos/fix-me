package router.market;

import java.io.*;
import java.net.*;
import java.util.Random;
import messaging.Messaging;

public class MarketThread implements Runnable {
    private BufferedReader marketInput;
    private PrintWriter marketOutput;
    private Socket marketSocket;
    private String marketID;

    public MarketThread(Socket marketSocket) throws IOException {
        marketInput = new BufferedReader(new InputStreamReader(marketSocket.getInputStream()));
        marketOutput = new PrintWriter(marketSocket.getOutputStream(), true);
        Random r = new Random();
        int i = r.nextInt((99999 - 10000) + 1) + 99999;
        marketID = Integer.toString(i);
        this.marketSocket = marketSocket;
    }

    @Override
    public void run() {
        try {
            while (true) {
                String reponse = null;
                String input = marketInput.readLine();
                if (input != null) {
                    reponse = input + "doSomething()";
                    Messaging.sendToBroker(reponse + " " + marketID);
                } else {
                    System.out.println("Connection to market has been closed.");
                }
            }
        } catch (IOException e) {
            System.out.println("Connection to market has been lost.");
        } finally {
            try {
                marketOutput.close();
                marketInput.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getMarketID() {
        return this.marketID;
    }

    public Socket getMarketSocket() {
        return this.marketSocket;
    }

    public void sendToMarket(String message) {
        marketOutput.println(message);
    }
}