package org.example.serverlogic;

import java.io.*;
import java.util.*;

public class MaxCategoryCalc implements Serializable {

    // Для хранения списка категорий и соответствующих им товаров удобно использовать коллекцию HashSet
    // для быстрого поиска в ней соответствий, ведь в реальности данный список может быть гораздо больше 8 строк
    // При этом порядок элементов не важен абсолютно
    protected static Set<CategoriesOfGoods> categoriesOfGoods = new HashSet<>();
    // Для хранения сумм по категориям удобно использовать хешмап, в качестве ключа - категория,
    // в качестве значения - сумма покупок по ней
    protected Map<String, Integer> categoriesAndSpends = new HashMap<>();

    public void readFromTsv(File tsvFile) {
        try (BufferedReader loadCatFromFile = new BufferedReader(new FileReader(tsvFile))) {
            // заводим переменную для чтения из файла, т.к. при упоминании метода в скобках
            // цикла / ифа он выполняется,
            // итого без переменной у нас каждая вторая итерация цикла останется без данных
            String s;
            while (true) {
                if ((s = loadCatFromFile.readLine()) == null) break;
                String[] split = s.split(" |\t");
                CategoriesOfGoods c = new CategoriesOfGoods();
                c.setCategory(split[2]);
                c.setGoods(split[1]);
                categoriesOfGoods.add(c);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    // Этим методом присваиваем категории входящим покупкам, далее передаем эти категории вместе с
    // суммой покупки в следующий метод, производящий основной рассчет наибольшей категории
    public String assignCategory(IncomingPurchase purchase) {
        for (CategoriesOfGoods c : categoriesOfGoods) {
            if (c.getGoods().equals(purchase.getTitle())) {
                return c.getCategory();
            }
        }
        return "другое";
    }

    // для отправки клиенту нам тоже как-то без надобности использовать именно JSON,
    // отправим String вида JSON
    public String categoriesCount(String category, int spends) {
        String maxCategory = "";
        int maxValue = -1;
        if (!categoriesAndSpends.containsKey(category)) {
            categoriesAndSpends.put(category, spends);
        } else {
            int value = categoriesAndSpends.get((category));
            value += spends;
            categoriesAndSpends.put(category, value);
        }
        for (String key : categoriesAndSpends.keySet()) {
            int value = categoriesAndSpends.get(key);
            if (value > maxValue) {
                maxValue = value;
                maxCategory = key;
            }
        }
        return "{" +
                "\"maxCategory\": {" +
                "\"category\": \"" + maxCategory + "\"," +
                "\"sum\": " + maxValue +
                "}" +
                "}";
    }
}