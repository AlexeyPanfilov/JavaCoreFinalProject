package org.example.user;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class UserSpends {

    private static final int PORT = 8989;
    private static final String HOST = "localHost";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            StringBuilder date = new StringBuilder();
            System.out.println("Введите покупку, дату совершения и сумму через запятые." +
                    "\nДля завершения программы введите end");
            String input = scanner.nextLine();
            if (input.equals("end")) break;
            if (input.isEmpty()) {
                System.out.println("Вы ввели пустой запрос");
                continue;
            }

            String[] parts = input.split(", |,");
            if (parts.length != 3) {
                System.out.println("Неверный ввод");
                continue;
            }
            String title = parts[0];
            String[] dateFromInput = parts[1].split("\\D", 3);
            if (dateFromInput.length != 3) {
                System.out.println("Неверный ввод даты покупки");
                continue;
            }
            int[] intArrDate = new int[3];
            try {
                for (int i = 0; i < 3; i++) {
                    intArrDate[i] = Integer.parseInt(dateFromInput[i]);
                    date.append(intArrDate[i]).append(".");
                }
                // При попытке ввести отрицательный год или месяц такая дата криво парсится и кидает исключение,
                // при попытке ввести отрицательный день, парсинг проходит нормально и исключение не кидается,
                // вызовем его сами
                if (intArrDate[2] < 0) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException e) {
                System.out.println("Ошибка при вводе даты");
                continue;
            }
//  Бессмысленно передавать дату как дату через JSON
            // нумерация месяцев в Calendar начинается с 0
//            date.set(Calendar.YEAR, intArrDate[0]);
//            date.set(Calendar.MONTH, intArrDate[1] - 1);
//            date.set(Calendar.DAY_OF_MONTH, intArrDate[2]);
            int purchaseSum;
            try {
                purchaseSum = Integer.parseInt(parts[2]);
            } catch (NumberFormatException e) {
                System.out.println("Ошибка при вводе суммы покупки");
                continue;
            }
            if (purchaseSum < 0) {
                System.out.println("Стоимость товара не может быть отрицательной");
                continue;
            }
            Purchase purchase = new Purchase(title, date.toString(), purchaseSum);
            try (Socket clientSocket = new Socket(HOST, PORT);
                 PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                 BufferedReader in = new BufferedReader(
                         new InputStreamReader(clientSocket.getInputStream()))) {
                out.println(purchase.convertToJson());
                String replyFromServer;
                while ((replyFromServer = in.readLine()) != null) {
                    System.out.println(replyFromServer);
                    Thread.sleep(300);
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
