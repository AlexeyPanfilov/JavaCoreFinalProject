package org.example;

import org.example.serverlogic.IncomingPurchase;
import org.example.serverlogic.MaxCategoryCalc;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {

    private static final int PORT = 8989;

    public static void main(String[] args) {
        File tsvFile = new File("categories.tsv");
        MaxCategoryCalc max = new MaxCategoryCalc();
        max.readFromTsv(tsvFile);
        try (ServerSocket serverSocket = new ServerSocket(PORT);) { // стартуем сервер один(!) раз
            while (true) { // в цикле(!) принимаем подключения
                try (
                        Socket socket = serverSocket.accept();
                        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        PrintWriter out = new PrintWriter(socket.getOutputStream());
                ) {
                    String incomingData = in.readLine().toLowerCase();
                    IncomingPurchase incomingPurchase = new IncomingPurchase();
                    incomingPurchase.splitJson(incomingData);
                    String category = max.assignCategory(incomingPurchase);
                    out.println(max.categoriesCount(category, incomingPurchase.getSum()));

                    // обработка одного подключения
                }
            }
        } catch (IOException e) {
            System.out.println("Не могу стартовать сервер");
            e.printStackTrace();
        }

    }
}