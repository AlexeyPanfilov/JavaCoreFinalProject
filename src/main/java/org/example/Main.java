package org.example;

import org.example.serverlogic.IncomingPurchase;
import org.example.serverlogic.MaxCategoryCalc;
import org.example.serverlogic.DataSaveAndLoad;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;

public class Main {

    private static final int PORT = 8989;

    public static void main(String[] args) {
        MaxCategoryCalc max = new MaxCategoryCalc();
        File binServerStateFile = new File("data.bin");
        if (binServerStateFile.exists()) {
            max = DataSaveAndLoad.loadServerStateFromBin(binServerStateFile);
        } else {
            System.out.println("Файл покупок не найден, будет формироваться новая статистика");
        }
        File tsvCategoriesListFile = new File("categories.tsv");
        max.readFromTsv(tsvCategoriesListFile);
        try (ServerSocket serverSocket = new ServerSocket(PORT);) { // стартуем сервер один(!) раз
            while (true) { // в цикле(!) принимаем подключения
                try (
                        Socket socket = serverSocket.accept();
                        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        PrintWriter out = new PrintWriter(socket.getOutputStream());
                ) {
                    // Получаем данные от клиента в виде JSON-строки
                    String incomingData = in.readLine().toLowerCase();
                    // Создаем объект класса нашей покупки для ее парсинга
                    IncomingPurchase incomingPurchase = new IncomingPurchase();
                    incomingPurchase.splitJson(incomingData);
                    // Присваиваем категории каждой входящей покупке
                    String category = max.assignCategory(incomingPurchase);
                    out.println(max.statisticsForPeriod(category, incomingPurchase));
                    DataSaveAndLoad saveServer = new DataSaveAndLoad();
                    saveServer.saveServerStatToBin(binServerStateFile, max);
                }
            }
        } catch (IOException e) {
            System.out.println("Не могу стартовать сервер");
            e.printStackTrace();
        }

    }
}